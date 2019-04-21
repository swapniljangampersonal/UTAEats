/* connected  with Database, retrieves  data from database*/

package UTAEats.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retreive);


        food=new Food();
        listView = (ListView) findViewById(R.id.listview);
          database = FirebaseDatabase.getInstance();
        ref = database.getReference("Food");
        list= new ArrayList<>();
        adapter= new ArrayAdapter<String>(this, R.layout.user_info, R.id.editText1, list);
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                     for (DataSnapshot ds: dataSnapshot.getChildren())
                       {
                                food=ds.getValue(Food.class);
                              list.add(food.getName().toString()+ " "+ food.getCost().toString());
                         /*  list.add(food.getServings().toString()+ " "+ food.getPickup_Location().toString());*/

                       }
                       listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }

        );
    }
}