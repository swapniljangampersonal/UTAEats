package utaeats.uta.mav.utaeats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import utaeats.uta.mav.models.Items;

public class CustomBuyerAdapter extends ArrayAdapter<Items> {
    private Context mContext;
    private ArrayList<Items> items = new ArrayList<>();
    private float totalCost = 0.0f;

    public CustomBuyerAdapter(Context context, ArrayList<Items> items) {
        super(context,0 , items);
        this.mContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.buyer_list,parent,false);
        }
        Items item = items.get(position);

        ImageView imageView = listItem.findViewById(R.id.buyerItemImage);
        Glide.with(this.mContext).load(item.getImage()).into(imageView);

        TextView itemName = listItem.findViewById(R.id.buyerItemName);
        itemName.setText(item.getItemName());

        TextView itemQuantity = listItem.findViewById(R.id.buyerItemQuantity);
        itemQuantity.setText(""+item.getNo_of_servings());

        TextView itemCost = listItem.findViewById(R.id.buyerItemCost);
        itemCost.setText("$"+item.getCost());

        System.out.println(item + " asdfsadfasdf");

        return listItem;
    }
}
