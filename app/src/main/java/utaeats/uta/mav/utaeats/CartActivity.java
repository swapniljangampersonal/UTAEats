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
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import utaeats.uta.mav.models.Items;
import utaeats.uta.mav.models.feedback;

public class CartActivity extends AppCompatActivity {

    ArrayList<Items> items = new ArrayList<Items>();
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

//        DB method to get items in arraylist
        Items item = new Items("someid","panipuri","2","13.5","South Campus","Image/panipuri.jpg");
        items.add(item);
        items.add(item);
        items.add(item);
        items.add(item);
        items.add(item);
        items.add(item);
        items.add(item);
        items.add(item);
        items.add(item);
        items.add(item);
        items.add(item);
        items.add(item);

        customListView = new CustomListView(this, items);
        listView.setAdapter(customListView);
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

    public void deleteAlert(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setTitle("Are you sure you want to delete the item?")
                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast t = Toast.makeText(getApplicationContext(), "Deleted",Toast.LENGTH_LONG);
                        t.show();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast t = Toast.makeText(getApplicationContext(), "Cancelled",Toast.LENGTH_LONG);
                        t.show();
                    }
                });

        alertDialog.show();
    }

    public void placeOrder(View view) {
        // Insert items from cart in orders table
        Items item = new Items("Lcw3zEzV4rKIDDZabc","panipuri","2","2.99","Meadowrun","Image/panipuri.jpg");
        // Firebase code to insert in database

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("order");

        myRef.child("order").setValue(item);

        Intent i = new Intent(this, PaymentActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, HomeDrawerB.class);
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
