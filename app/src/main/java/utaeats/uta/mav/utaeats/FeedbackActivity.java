package utaeats.uta.mav.utaeats;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import utaeats.uta.mav.controller.DBMgr;
import utaeats.uta.mav.models.Cart;
import utaeats.uta.mav.models.Feedback;
import utaeats.uta.mav.models.Items;

public class FeedbackActivity extends AppCompatActivity {
    EditText comment;
    ArrayList<Items> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Html.fromHtml("<font color='#35838F'>Feedback</font>"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference orderRef = database.getReference().child("order");

        // Get this item from order
        itemList = new ArrayList<>();
        System.out.println("Size feedback: "+itemList.size());
        Items item = Cart.cartItems.get(0);

        TextView itemName = findViewById(R.id.itemNameFeedback);
        itemName.setText(item.getItemName());

        ImageView iv = findViewById(R.id.itemImageFeedback);
        Glide.with(this).load(item.getImage()).into(iv);
    }

    public void submitComment(View view) {
        comment  = findViewById(R.id.feedbackEditText);
        String feedback_text = comment.getText().toString();
        DBMgr dbMgr = new DBMgr();
        dbMgr.saveComment(feedback_text);
        Intent i = new Intent(this, FoodActivity.class);
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
