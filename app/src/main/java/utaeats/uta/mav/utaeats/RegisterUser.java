package utaeats.uta.mav.utaeats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity {

    private EditText uid, password;
    private Button RegisterButton;
    private String emailId;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Html.fromHtml("<font color='#35838F'>Register</font>"));
        setSupportActionBar(toolbar);

        FirebaseApp.initializeApp(RegisterUser.this);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        findViewById(R.id.uid).requestFocus();

        //assigning the values entered in by user
        uid = findViewById(R.id.uid);
        password = findViewById(R.id.password);
        RegisterButton = findViewById(R.id.register_user_btn);

        //action to be taken on clicking the register button

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailId = uid.getText().toString().trim();
                if (!ValidateUID(emailId)){
                    Toast.makeText(getApplicationContext(), "Please enter a valid UTA email ID", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    //send the user details to the database

                    AddUsers();

                    //call the Login activity
                    Intent i = new Intent(RegisterUser.this,LoginUser.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    RegisterUser.this.startActivity(i);


                    //Database storing code.
                    //Need to check the connectivity with the Firebase database
                }

            }
        });

    }

    public boolean ValidateUID(String uid)
    {
        String email_pattern = "[a-zA-Z0-9._-]+@mavs.uta.edu";
        if (uid.matches(email_pattern)){
            //Toast.makeText(getApplicationContext(), "Valid UID entered", Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            //Toast.makeText(getApplicationContext(), "Invalid UID entered", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void AddUsers(){

        String useruid = uid.getText().toString();
        String user_password = password.getText().toString();
        String role = "buyer";

        if (!TextUtils.isEmpty(useruid) && !TextUtils.isEmpty(user_password)){

            String id = databaseReference.push().getKey();


            //setting the key to the session for later retrieval
            SessionManagement sessionTest = new SessionManagement(getApplicationContext());
            sessionTest.setKeyId(id);

            Toast.makeText(RegisterUser.this, sessionTest.getKeyId(), Toast.LENGTH_SHORT).show();

            Users users = new Users(id,useruid,user_password,role);
            databaseReference.child(id).setValue(users);
            uid.setText("");
            password.setText("");
        }
        else{
            Toast.makeText(RegisterUser.this,"Please enter the details",Toast.LENGTH_LONG).show();
        }
    }
}
