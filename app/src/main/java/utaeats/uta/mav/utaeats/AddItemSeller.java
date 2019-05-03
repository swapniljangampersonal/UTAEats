package utaeats.uta.mav.utaeats;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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

import utaeats.uta.mav.controller.DBMgr;
import utaeats.uta.mav.models.Cart;
import utaeats.uta.mav.models.Items;


public class AddItemSeller extends Activity {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private EditText itemname, servings, itemcost, pickupadd;
    private ImageButton item_image;
    private Button save_item;


    private Uri imageUri;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_seller);
        Toolbar toolbar = findViewById(R.id.toolbar);

        FirebaseApp.initializeApp(AddItemSeller.this);

        itemname = findViewById(R.id.name_of_item);
        servings = findViewById(R.id.servings);
        itemcost = findViewById(R.id.cost);
        pickupadd = findViewById(R.id.pickup_address);


        item_image = findViewById(R.id.item_picture);

        save_item = findViewById(R.id.save_item);

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
                String item_name = itemname.getText().toString();
                String item_cost = itemcost.getText().toString();
                String item_address = pickupadd.getText().toString();
                String item_serves = servings.getText().toString();
                DBMgr dbMgr = new DBMgr();
                Context itemContext = getApplicationContext();
                dbMgr.uploadImage(item_name, item_cost, item_address, item_serves, imageUri, itemContext);
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
        item_image.setImageBitmap(BitmapFactory.decodeFile(path));
    }

}