/* connected  with Database, retrieves  data from database*/

package utaeats.uta.mav.utaeats;

import utaeats.uta.mav.models.Items;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodActivity extends AppCompatActivity {
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

//        final SessionManagement session = new SessionManagement(getApplicationContext());
//        final String id = session.getKeyId();

        listView = (ListView) findViewById(R.id.listview);
          database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("items");
        final String id = ref.push().getKey();
        itemList= new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for (DataSnapshot ds: dataSnapshot.getChildren())
                   {
                       Items itemtest = ds.getValue(Items.class);
                          itemList.add(itemtest);
                   }
                   adapter = new CustomBuyerAdapter(getApplicationContext(), itemList);
                   listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }

        );
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
}