package utaeats.uta.mav.utaeats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class CustomListView extends ArrayAdapter<Item> {

    private Context mContext;
    private ArrayList<Item> items = new ArrayList<>();

    public CustomListView(Context context, ArrayList<Item> items) {
        super(context,0 , items);
        this.mContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.listview_layout,parent,false);
        }
        Item item = items.get(position);
        ImageView imageView = listItem.findViewById(R.id.icon);
        Glide.with(this.mContext).load(item.getImage()).into(imageView);

        TextView itemName = listItem.findViewById(R.id.firstLine);
        itemName.setText(item.getItemName());

        TextView itemCost = listItem.findViewById(R.id.countlabel);
        itemCost.setText("$"+item.getCost());

        return listItem;
    }
}
