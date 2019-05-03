/* connected  with Database, retrieves  data from database*/

package utaeats.uta.mav.utaeats;

import utaeats.uta.mav.controller.DBMgr;
import utaeats.uta.mav.models.Cart;
import utaeats.uta.mav.models.Items;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ListView listView;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<Items> itemList;
    CustomBuyerAdapter adapter;
    Items item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_buyer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Html.fromHtml("<font color='#35838F'>UTAEats</font>"));
        setSupportActionBar(toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_buyer);
        navigationView.setNavigationItemSelectedListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_buyer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        listView = (ListView) findViewById(R.id.listview);
        FirebaseApp.initializeApp(FoodActivity.this);
        new DBMgr().getItems(getApplicationContext(), listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.nav_cancel_order:
                cancelOrder(item);
                return true;
            case R.id.nav_feedback:
                openFeedback(item);
                return true;
            case R.id.nav_settings:
                openSettings(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        System.out.println("In menu selection saadffs");

        if (id == R.id.nav_cancel_order) {
            // Handle the add item
            cancelOrder(item);

        } else if (id == R.id.nav_feedback) {
            System.out.println("in feedback saadffs");
            //handle the settings page
            Intent intent = new Intent(FoodActivity.this,FeedbackActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            System.out.println("in settings saadffs");
            Intent intent = new Intent(FoodActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_buyer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public void openCart(View v) {
        Intent i = new Intent(this, CartActivity.class);
        startActivity(i);
        finish();
    }

    public boolean openFeedback(MenuItem v) {
        Intent i = new Intent(this, FeedbackActivity.class);
        startActivity(i);
        finish();
        return true;
    }

    public boolean openSettings(MenuItem view) {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
        finish();
        return true;
    }

    public boolean cancelOrder(MenuItem view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setTitle("Are you sure you want to cancel the order?")
                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBMgr dbMgr = new DBMgr();
                        dbMgr.removeOrder();
                        Toast t = Toast.makeText(getApplicationContext(), "Cancelled",Toast.LENGTH_LONG);
                        t.show();
                    }
                })
                .setNegativeButton("GO BACK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast t = Toast.makeText(getApplicationContext(), "Back Button Pressed",Toast.LENGTH_LONG);
                        t.show();
                    }
                });

        alertDialog.show();
        return true;
    }
}