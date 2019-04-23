package utaeats.uta.mav.utaeats;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import java.util.ArrayList;

import utaeats.uta.mav.models.Items;

public class CustomListView extends ArrayAdapter<Items> {

    private Context mContext;
    private ArrayList<Items> items = new ArrayList<>();

    public CustomListView(Context context, ArrayList<Items> items) {
        super(context,0 , items);
        this.mContext = context;
        this.items = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.listview_layout,parent,false);
        }
        Items item = items.get(position);
        ImageView imageView = listItem.findViewById(R.id.icon);
        Glide.with(this.mContext).load(item.getImage()).into(imageView);

        TextView itemName = listItem.findViewById(R.id.firstLine);
        itemName.setText(item.getItemName());

        TextView itemCost = listItem.findViewById(R.id.countlabel);
        itemCost.setText("$"+item.getCost());

        final ElegantNumberButton servings = (ElegantNumberButton) listItem.findViewById(R.id.servings);
        servings.setNumber(item.getNo_of_servings());
        servings.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = servings.getNumber();
                items.get(position).setNo_of_servings(num);
            }
        });


        ImageButton deleteBtn = (ImageButton) listItem.findViewById(R.id.btnDelete);

        final int pos = position;

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext)
                    .setTitle("Are you sure you want to delete the item?")
                    .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            items.remove(pos); //or some other task
                            notifyDataSetChanged();
                            Toast t = Toast.makeText(mContext, "Deleted "+pos,Toast.LENGTH_LONG);
                            t.show();
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast t = Toast.makeText(mContext, "Cancelled",Toast.LENGTH_LONG);
                            t.show();
                        }
                    });

                alertDialog.show();
            }
        });

        return listItem;
    }

    public void deleteAlert(View view) {

    }
}
