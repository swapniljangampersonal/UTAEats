package utaeats.uta.mav.utaeats;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.support.v7.app.AppCompatActivity;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void submitComment(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

}
