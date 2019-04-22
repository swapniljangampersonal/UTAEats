package utaeats.uta.mav.utaeats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginUser extends AppCompatActivity {

    private EditText login_uid, login_password;
    private Button login_btn;
    String user_uid,user_password;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Html.fromHtml("<font color='#35838F'>Login</font>"));
        setSupportActionBar(toolbar);

        //session management
        final SessionManagement session = new SessionManagement(getApplicationContext());
        final String id = session.getKeyId();

        FirebaseApp.initializeApp(LoginUser.this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(id);

        //values from user
        login_uid= findViewById(R.id.uid);
        login_password= findViewById(R.id.password);
        login_btn= findViewById(R.id.button_login);
        final RadioButton r_seller = findViewById(R.id.radio_seller);
        RadioButton r_buyer = findViewById(R.id.radio_buyer);

        //string values
        user_uid = login_uid.getText().toString();
        user_password = login_password.getText().toString();


        //On clicking the login button

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

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

                            Toast.makeText(LoginUser.this, "Login successful", Toast.LENGTH_SHORT).show();
                            session.createUserLoginSession(user_uid);
                            if(r_seller.isChecked())
                            {
                                session.setRole("seller");
                                Users users = new Users(id,user_uid,user_password,"seller");
                                databaseReference.setValue(users);

                                //calling the seller home activity
                                Intent i_seller = new Intent(LoginUser.this, HomeDrawerSeller.class);
                                i_seller.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i_seller.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i_seller.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                LoginUser.this.startActivity(i_seller);
                            }
                            else{

                                //calling the seller home activity
                                Intent i_buyer = new Intent(LoginUser.this, FoodActivity.class);
                                i_buyer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i_buyer.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i_buyer.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                LoginUser.this.startActivity(i_buyer);
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

                        Toast.makeText(LoginUser.this, "Error occurred while reading from the database", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
