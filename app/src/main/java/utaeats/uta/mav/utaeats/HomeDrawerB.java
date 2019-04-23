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

//    public void openCart(View v) {
//        Intent i = new Intent(this, CartActivity.class);
//        startActivity(i);
//        finish();
//    }
//
//    public void openFeedback(View v) {
//        Intent i = new Intent(this, FeedbackActivity.class);
//        startActivity(i);
//        finish();
//    }
//
//    public void openSettings(View view) {
//        Intent i = new Intent(this, SettingsActivity.class);
//        startActivity(i);
//        finish();
//    }
//
//    public void cancelOrder(View view) {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
//                .setTitle("Are you sure you want to cancel the order?")
//                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast t = Toast.makeText(getApplicationContext(), "Cancelled Order",Toast.LENGTH_LONG);
//                        t.show();
//                    }
//                })
//                .setNegativeButton("GO BACK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast t = Toast.makeText(getApplicationContext(), "Back Button Pressed",Toast.LENGTH_LONG);
//                        t.show();
//                    }
//                });
//
//        alertDialog.show();
//    }

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