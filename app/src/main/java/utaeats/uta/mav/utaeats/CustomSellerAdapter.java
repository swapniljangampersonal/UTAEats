package utaeats.uta.mav.utaeats;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import utaeats.uta.mav.models.Cart;
import utaeats.uta.mav.models.Items;

public class CustomSellerAdapter extends ArrayAdapter<Items> {
    private Context mContext;
    private ArrayList<Items> items = new ArrayList<>();
    private float totalCost = 0.0f;

    public CustomSellerAdapter(Context context, ArrayList<Items> items) {
        super(context,0 , items);
        this.mContext = context;
        this.items = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.seller_list,parent,false);
        }
        Items item = items.get(position);

        ImageView imageView = listItem.findViewById(R.id.sellerItemImage);
        Glide.with(this.mContext).load(item.getImage()).into(imageView);

        TextView itemName = listItem.findViewById(R.id.sellerItemName);
        itemName.setText(item.getItemName());

        TextView itemQuantity = listItem.findViewById(R.id.sellerItemQuantity);
        itemQuantity.setText(""+item.getNo_of_servings());

        TextView itemCost = listItem.findViewById(R.id.sellerItemCost);
        itemCost.setText("$"+item.getCost());

        return listItem;
    }
}
