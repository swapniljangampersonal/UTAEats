package utaeats.uta.mav.utaeats;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import utaeats.uta.mav.models.Items;


public class AddItemSeller extends Activity {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private EditText itemname, servings, itemcost, pickupadd;
    private ImageButton item_image;
    //private ImageButton back_btn;
    private Button save_item;

    private DatabaseReference databaseReference;

    private StorageReference mStorageRef;

    private Uri imageUri;

    public static final String FB_STORAGE_PATH = "Image/";

    //public static final String FB_DATABASE_PATH = "image/";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_seller);
        Toolbar toolbar = findViewById(R.id.toolbar);

        signIn();

        FirebaseApp.initializeApp(AddItemSeller.this);

        databaseReference = FirebaseDatabase.getInstance().getReference("items");

        //mStorageRef = FirebaseStorage.getInstance().getReference();
        //mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);

        itemname = findViewById(R.id.name_of_item);
        servings = findViewById(R.id.servings);
        itemcost = findViewById(R.id.cost);
        pickupadd = findViewById(R.id.pickup_address);


        item_image = findViewById(R.id.item_picture);

        save_item = findViewById(R.id.save_item);

        //back_btn = findViewById(R.id.back_button);
        if(new SessionManagement(AddItemSeller.this).getPath()!=null)
            setProfile(new SessionManagement(AddItemSeller.this).getPath());

        item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(AddItemSeller.this, "Upload Item image", Toast.LENGTH_SHORT).show();
                selectImage();

            }
        });

        save_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadImage();
            }
        });



        /*back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddItemSeller.this, HomeDrawerSeller.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                AddItemSeller.this.startActivity(i);
            }
        });*/

        /*save_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageUri!=null){

                    final ProgressDialog dialog = new ProgressDialog(getApplicationContext());
                    dialog.setTitle("Uploading Image");
                    dialog.show();

                    //get the storage reference
                    StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "."+getImageExt(imageUri));

                    //add file to reference
                    ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //dismiss dialog on success
                            dialog.dismiss();

                            //display success msg
                            Toast.makeText(getApplicationContext(), "image uploaded", Toast.LENGTH_SHORT).show();


                            //save image info to the firebase database

                            Items items = new Items(itemname.getText().toString(),servings.getText().toString(),cost.getText().toString(),pickupadd.getText().toString(),taskSnapshot.StorageReference.taskSnapshot.getDownloadUrl().toString());

                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    //dismiss dialog on failure
                                    dialog.dismiss();

                                    //display success msg
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                    //show upload progress

                                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    dialog.setMessage("Uploaded " + (int)progress +"%");
                                }
                            });

                }
            }
        });*/


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
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
        .addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("signInAnonymously:FAILURE" + exception.getMessage());
            }
        });
    }

    private void uploadImage() {
        if(imageUri != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            System.out.println(UUID.randomUUID().toString()+"."+getImageExt(imageUri)+" sadfgh");
            FirebaseOptions opts = FirebaseApp.getInstance().getOptions();
            System.out.println("Bucket= "+opts.getStorageBucket());
            FirebaseStorage storage = FirebaseStorage.getInstance();
            storage.setMaxDownloadRetryTimeMillis(100000);
            mStorageRef = storage.getReferenceFromUrl("gs://utaeats-7e5ed.appspot.com/");
            final String uploadFileName = UUID.randomUUID().toString()+"."+getImageExt(imageUri);
            final StorageReference ref = mStorageRef.child(FB_STORAGE_PATH+ uploadFileName);
            ref.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            AddItem(uploadFileName);
                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setTitle("Please select an image")
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            alertDialog.show();
        }
    }

    public void AddItem(String imagePath){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storage.setMaxDownloadRetryTimeMillis(100000);
        mStorageRef = storage.getReferenceFromUrl("gs://utaeats-7e5ed.appspot.com/");
        final StorageReference ref = mStorageRef.child(FB_STORAGE_PATH+ imagePath);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String item_id = databaseReference.push().getKey();
                String item_name = itemname.getText().toString();
                String item_cost = itemcost.getText().toString();
                String item_address = pickupadd.getText().toString();
                String item_serves = servings.getText().toString();


                Items items = new Items(item_id,item_name,item_serves,item_cost,item_address, uri.toString());
                databaseReference.child(item_id).setValue(items);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
            }
        });

    }

    public void TakemeHome(View view) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddItemSeller.this, HomeDrawerSeller.class);
                AddItemSeller.this.startActivity(i);
            }
        });
    }

    /*public void Takemeback(View view) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddItemSeller.this, HomeDrawerSeller.class);
                AddItemSeller.this.startActivity(i);
            }
        });
    }*/

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
//
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddItemSeller.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(AddItemSeller.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /*public void selectImage() {

        String[] items = {"Choose from Gallery","Take Photo"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddItemSeller.this);
        builder.setTitle("Add Item Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(AddItemSeller.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }

                /*switch (item){
                    case 0:
                        galleryIntent();
                        break;

                    case 1:
                        cameraIntent();
                        break;
                }
            }
        });
        builder.show();
    }*/

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(AddItemSeller.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    item_image.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AddItemSeller.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            item_image.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(AddItemSeller.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }*/

    /*public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }*/

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        item_image.setImageBitmap(thumbnail);
        imageUri = data.getData();
    }

    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImage = null;
        Bitmap bm = null;
        String picturePath = "";
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(AddItemSeller.this.getContentResolver(), data.getData());
                selectedImage = data.getData();
                // Toast.makeText(getActivity().getApplicationContext(),selectedImage.toString(),Toast.LENGTH_SHORT).show();
                String[] filePath = {MediaStore.Images.Media.DATA};
                //Toast.makeText(getActivity().getApplicationContext(), "filepath"+filePath,Toast.LENGTH_SHORT).show();
                Cursor c = AddItemSeller.this.getContentResolver().query(
                        selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                SessionManagement sessionlogin = new SessionManagement(AddItemSeller.this);
                sessionlogin.setProfilePath(picturePath);
                //Toast.makeText(getActivity().getApplicationContext(), "Picture Path"+picturePath,Toast.LENGTH_SHORT).show();
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        item_image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        item_image.setImageBitmap(bm);
        imageUri = selectedImage;

    }

    public void setProfile(String path)
    {
        //ivImage=(CircleImageView)getActivity().findViewById(R.id.img_main_profile);
        item_image.setImageBitmap(BitmapFactory.decodeFile(path));
        //imgpro.setImageBitmap(bm);
    }

    public String getImageExt(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

}