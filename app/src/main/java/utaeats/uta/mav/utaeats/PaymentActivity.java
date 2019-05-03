package utaeats.uta.mav.utaeats;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import utaeats.uta.mav.models.Items;
import utaeats.uta.mav.models.Cart;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Html.fromHtml("<font color='#35838F'>Payment</font>"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        float totalCost = 0.0f;
        for(Items item: Cart.cartItems) {
            totalCost += Float.parseFloat(item.getCost()) * Integer.parseInt(item.getNo_of_servings());
        }

        TextView totalCostText = findViewById(R.id.totalCost);
        totalCostText.setText("$"+totalCost);
    }

    public void callInvoice(View view) {
        Intent i = new Intent(this, InvoiceActivity.class);
        startActivity(i);
        finish();
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

}
