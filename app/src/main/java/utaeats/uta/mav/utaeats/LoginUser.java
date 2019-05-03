package utaeats.uta.mav.utaeats;

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

import utaeats.uta.mav.controller.DBMgr;
import utaeats.uta.mav.models.Users;

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

        //values from user
        login_uid= findViewById(R.id.uid_login);
        login_password= findViewById(R.id.password_login);
        login_btn= findViewById(R.id.button_login);
        final RadioButton r_seller = findViewById(R.id.radio_seller);
        final RadioButton r_buyer = findViewById(R.id.radio_buyer);

        //string values
        user_uid = login_uid.getText().toString();
        user_password = login_password.getText().toString();


        //On clicking the login button

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                FirebaseApp.initializeApp(LoginUser.this);
                DBMgr dbMgr = new DBMgr();
                dbMgr.checkUser(user_uid, user_password, id, getApplicationContext(), session, r_buyer, r_seller);
            }
        });
    }

}
