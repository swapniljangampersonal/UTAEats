package utaeats.uta.mav.controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

import utaeats.uta.mav.models.Cart;
import utaeats.uta.mav.models.Feedback;
import utaeats.uta.mav.models.Items;
import utaeats.uta.mav.models.Users;
import utaeats.uta.mav.utaeats.CustomBuyerAdapter;
import utaeats.uta.mav.utaeats.FoodActivity;
import utaeats.uta.mav.utaeats.HomeDrawerSeller;
import utaeats.uta.mav.utaeats.LoginUser;
import utaeats.uta.mav.utaeats.RegisterUser;
import utaeats.uta.mav.utaeats.SessionManagement;

public class DBMgr {

    private DatabaseReference databaseReference;

    private StorageReference mStorageRef;

    ArrayList<Items> itemList = new ArrayList<>();

    public DBMgr(){
        signIn();
    }

    public static final String FB_STORAGE_PATH = "Image/";
    public void uploadImage(final String item_name, final String item_cost, final String item_address,final String item_serves, Uri imageUri, final Context itemContext) {
        if(imageUri != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(itemContext);
            System.out.println(UUID.randomUUID().toString()+"."+getImageExt(imageUri, itemContext)+" sadfgh");
            FirebaseOptions opts = FirebaseApp.getInstance().getOptions();
            System.out.println("Bucket= "+opts.getStorageBucket());
            FirebaseStorage storage = FirebaseStorage.getInstance();
            storage.setMaxDownloadRetryTimeMillis(100000);
            mStorageRef = storage.getReferenceFromUrl("gs://utaeats-7e5ed.appspot.com/");
            final String uploadFileName = UUID.randomUUID().toString()+"."+getImageExt(imageUri, itemContext);
            final StorageReference ref = mStorageRef.child(FB_STORAGE_PATH+ uploadFileName);
            ref.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            AddItem(uploadFileName,item_name, item_cost, item_address, item_serves);
                            Toast.makeText(itemContext, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(itemContext, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(itemContext)
                    .setTitle("Please select an image")
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            alertDialog.show();
        }
    }

    public void AddItem(String imagePath, final String item_name, final String item_cost, final String item_address, final String item_serves){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storage.setMaxDownloadRetryTimeMillis(100000);
        databaseReference = FirebaseDatabase.getInstance().getReference("items");
        mStorageRef = storage.getReferenceFromUrl("gs://utaeats-7e5ed.appspot.com/");
        final StorageReference ref = mStorageRef.child(FB_STORAGE_PATH+ imagePath);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String item_id = databaseReference.push().getKey();

                Items items = new Items(item_id,item_name,item_serves,item_cost,item_address, uri.toString());
                Cart.addItemSeller.add(items);
                databaseReference.child(item_id).setValue(items);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
            }
        });

    }
    public String getImageExt(Uri uri, Context itemContext) {

        ContentResolver contentResolver = itemContext.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void addOrder() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("order");

        for(Items item: Cart.cartItems) {
            myRef.child(item.getItemID()).setValue(item);
        }
    }

    public void saveComment(String feedback_text) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference feedbackRef = database.getReference("feedback");
        Feedback f= new Feedback(feedback_text);
        feedbackRef.setValue(f);
    }

    public void getItems(final Context applicationContext,final ListView listView) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("items");
        final String id = ref.push().getKey();
        ref.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  for (DataSnapshot ds: dataSnapshot.getChildren())
                  {
                      Items itemtest = ds.getValue(Items.class);
                      itemList.add(itemtest);
                  }
                  CustomBuyerAdapter adapter = new CustomBuyerAdapter(applicationContext, itemList);
                  listView.setAdapter(adapter);
              }
              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {
              }
          }
        );
    }

    public void removeOrder() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("order");
        System.out.println("Cart items: "+Cart.cartItems);
        for(Items cartitem: Cart.cartItems) {
            System.out.println("Cart item id:  -"+cartitem.getItemID());
            myRef.child(cartitem.getItemID()).removeValue();
        }
    }

    public void checkUser(final String user_uid, final String user_password, final String id, final Context applicationContext, final SessionManagement session, RadioButton r_buyer, final RadioButton r_seller) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String DB_uid = dataSnapshot.child("uid").getValue().toString();
                //Toast.makeText(LoginUser.this, DB_uid, Toast.LENGTH_SHORT).show();
                String DB_password = dataSnapshot.child("password").getValue().toString();
                //Toast.makeText(LoginUser.this, DB_password, Toast.LENGTH_SHORT).show();

                //verify the credentials entered by the user

                //(user_uid.equals(DB_uid)) && !(user_password.equals(DB_password)

                if (!(user_uid.equalsIgnoreCase(DB_uid)) && !(user_password.equalsIgnoreCase(DB_password)))
                {
                    //if (!TextUtils.equals(user_uid,DB_uid) && !TextUtils.equals(user_password,DB_password)){

                    Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_SHORT).show();
                    session.createUserLoginSession(user_uid);
                    if(r_seller.isChecked())
                    {
                        session.setRole("seller");
                        Users users = new Users(id,DB_uid,DB_password,"seller");
                        databaseReference.setValue(users);

                        //calling the seller home activity
                        Intent i_seller = new Intent(applicationContext, HomeDrawerSeller.class);
                        i_seller.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i_seller.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i_seller.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        applicationContext.startActivity(i_seller);
                    }
                    else{

                        //calling the seller home activity
                        Intent i_buyer = new Intent(applicationContext, FoodActivity.class);
                        i_buyer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i_buyer.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i_buyer.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        applicationContext.startActivity(i_buyer);
                    }

                }
                else
                {
                    //Toast.makeText(LoginUser.this, "Please enter the correct credentials you registered with", Toast.LENGTH_SHORT).show();
                }
                //If matched - create session

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(applicationContext, "Error occurred while reading from the database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addUser(String useruid, String user_password, String role, Context applicationContext) {
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        String id = databaseReference.push().getKey();


        //setting the key to the session for later retrieval
        SessionManagement sessionTest = new SessionManagement(applicationContext);
        sessionTest.setKeyId(id);

        Toast.makeText(applicationContext, sessionTest.getKeyId(), Toast.LENGTH_SHORT).show();

        Users users = new Users(id,useruid,user_password,role);
        databaseReference.child(id).setValue(users);
    }

    private void signIn() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously(mAuth);
        }
    }

    private void signInAnonymously(FirebaseAuth mAuth) {
        mAuth.signInAnonymously();
    }
}
