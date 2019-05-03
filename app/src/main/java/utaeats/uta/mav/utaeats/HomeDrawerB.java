package utaeats.uta.mav.utaeats;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class HomeDrawerB extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.drawer_buyer);
    }

    public void logout(View view){
        //code here

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
                sessionManagement.logoutUser();
            }
        });
    }

}