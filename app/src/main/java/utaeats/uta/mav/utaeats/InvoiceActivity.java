package utaeats.uta.mav.utaeats;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import utaeats.uta.mav.controller.UTAEatsController;
import utaeats.uta.mav.models.Cart;
import utaeats.uta.mav.models.Items;

public class InvoiceActivity extends AppCompatActivity {

    ArrayList<Items> items = new ArrayList<Items>();
    private ListView listView;
    private CustomInvoiceAdapter customListView;
    private RelativeLayout invoiceLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Html.fromHtml("<font color='#35838F'>Invoice</font>"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.invoiceListView);

        customListView = new CustomInvoiceAdapter(this, Cart.cartItems);
        listView.setAdapter(customListView);

        float totalCost = 0.0f;
        for (Items itemloop:Cart.cartItems
             ) {
            System.out.println("Calculation = "+Float.parseFloat(itemloop.getCost()) * Integer.parseInt(itemloop.getNo_of_servings()));
            totalCost += Float.parseFloat(itemloop.getCost()) * Integer.parseInt(itemloop.getNo_of_servings());
        }
        System.out.println(totalCost);
        TextView totalCostView = findViewById(R.id.totalInvoiceCost);
        totalCostView.setText("$"+String.valueOf(totalCost));
    }

    public void saveAsPDF(View view) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            } else {
            invoiceLayout = findViewById(R.id.invoiceLayout);
            UTAEatsController utaEatsController = new UTAEatsController();
            utaEatsController.save(invoiceLayout, getApplicationContext());
        }
    }



    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, CartActivity.class);
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

    public void goBuyer(View view) {
        Intent i = new Intent(this, FoodActivity.class);
        startActivity(i);
        finish();
    }

}
