package utaeats.uta.mav.utaeats;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import utaeats.uta.mav.models.Item;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get this item from order
        Item item = new Item("panipuri",13.5f,2,"South Campus",true,"https://dummyimage.com/400x400/0011ff/000000.png");

        TextView itemName = findViewById(R.id.itemNameFeedback);
        itemName.setText(item.getItemName());

        ImageView iv = findViewById(R.id.itemImageFeedback);
        Glide.with(this).load(item.getImage()).into(iv);
    }

    public void submitComment(View view) {
        Intent i = new Intent(this, MainActivity.class);
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
