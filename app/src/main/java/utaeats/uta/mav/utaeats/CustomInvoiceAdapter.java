package utaeats.uta.mav.utaeats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import utaeats.uta.mav.models.Item;

public class CustomInvoiceAdapter extends ArrayAdapter<Item> {
    private Context mContext;
    private ArrayList<Item> items = new ArrayList<>();
    private float totalCost = 0.0f;

    public CustomInvoiceAdapter(Context context, ArrayList<Item> items) {
        super(context,0 , items);
        this.mContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.invoice_list,parent,false);
        }
        Item item = items.get(position);

        TextView itemName = listItem.findViewById(R.id.itemNameInvoice);
        itemName.setText(item.getItemName());

        TextView itemQuantity = listItem.findViewById(R.id.itemQuantityInvoice);
        itemQuantity.setText(""+item.getNumberOfServings());

        TextView itemCost = listItem.findViewById(R.id.itemCostInvoice);
        itemCost.setText("$"+item.getCost()*item.getNumberOfServings());

        return listItem;
    }
}
