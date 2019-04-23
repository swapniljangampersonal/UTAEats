package utaeats.uta.mav.utaeats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import utaeats.uta.mav.models.Feedback;
import utaeats.uta.mav.models.Items;

public class FeedbackActivity extends AppCompatActivity {
    EditText comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Html.fromHtml("<font color='#35838F'>Feedback</font>"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        comment  = findViewById(R.id.feedbackEditText);
        String feedback_text = comment.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("feedback");
        Feedback f= new Feedback(feedback_text);
        myRef.child("feedback").child("comment").setValue(f);

        // Get this item from order
        Items item = new Items("someid","panipuri","2","13.5","South Campus","Image/panipuri.jpg");

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
