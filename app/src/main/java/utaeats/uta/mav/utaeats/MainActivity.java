package utaeats.uta.mav.utaeats;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
        sessionManagement.clearsession();

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                final SessionManagement sessiontest =new SessionManagement(getApplicationContext());

                if(!sessiontest.checkLogin()){

                    //User is logged in - nested condition can be checked by fetching the role from session and NO DB use

                    reference = FirebaseDatabase.getInstance().getReference().child("users").child(sessiontest.getKeyId());
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String user_uid = dataSnapshot.child("uid").getValue().toString();
                            String user_role = dataSnapshot.child("role").getValue().toString();

                            //String user_role = sessiontest.getRole();

                            if(user_role.equalsIgnoreCase("Seller")){

                                //redirect to the seller add the item page or the home-page itself
                                Toast.makeText(MainActivity.this,user_uid,Toast.LENGTH_LONG).show();
                                Toast.makeText(MainActivity.this,user_role,Toast.LENGTH_LONG).show();
                                callSellerPage();

                            }
                            else if (user_role.equalsIgnoreCase("Buyer")){

                                //redirect to the buyer home-page
                                Toast.makeText(MainActivity.this,user_uid,Toast.LENGTH_LONG).show();
                                Toast.makeText(MainActivity.this,user_role,Toast.LENGTH_LONG).show();
                                callBuyerPage();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            Toast.makeText(MainActivity.this, "Error occurred while reading from the database", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //Temporary change
                //final Intent mainIntent = new Intent(MainActivity.this,RegisterUser.class);
                final Intent mainIntent = new Intent(MainActivity.this,RegisterUser.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();


                //Intent i = new Intent(MainActivity.this, RegisterUser.class);
                //startActivity(i);
            }
        }, 6500);



    }

    public void callSellerPage(){

        final Intent mainIntent = new Intent(MainActivity.this, HomeDrawerSeller.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        MainActivity.this.startActivity(mainIntent);
        MainActivity.this.finish();

    }

    public void callBuyerPage(){

        //redirect to the buyer class
        final Intent i = new Intent(MainActivity.this, HomeDrawerB.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        MainActivity.this.startActivity(i);
        MainActivity.this.finish();
    }
}

