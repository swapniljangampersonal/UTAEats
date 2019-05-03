package utaeats.uta.mav.utaeats;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import utaeats.uta.mav.controller.DBMgr;
import utaeats.uta.mav.models.Cart;
import utaeats.uta.mav.models.Items;
import utaeats.uta.mav.models.Feedback;

public class CartActivity extends AppCompatActivity {

    private ListView listView;
    private CustomListView customListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Html.fromHtml("<font color='#35838F'>CartActivity</font>"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.itemList);

        customListView = new CustomListView(this, Cart.cartItems);
        listView.setAdapter(customListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                ImageButton del =(ImageButton) view.findViewById(R.id.btnDelete);
                del.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                Cart.cartItems.remove(position);
                                Toast.makeText(getApplicationContext(),
                                        "Deleted ListItem Number " + position, Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                Toast.makeText(getApplicationContext(), "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void goHome(MenuItem menuItem) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }



    public void placeOrder(View view) {
        // Insert items from cart in orders table
        //Items item = new Items("Lcw3zEzV4rKIDDZabc","panipuri","2","2.99","Meadowrun","Image/panipuri.jpg");
        // Firebase code to insert in database

        DBMgr dbMgr = new DBMgr();
        dbMgr.addOrder();

        Intent i = new Intent(this, PaymentActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, FoodActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
