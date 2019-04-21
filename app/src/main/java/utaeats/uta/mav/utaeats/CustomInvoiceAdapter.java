package utaeats.uta.mav.utaeats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import utaeats.uta.mav.models.Items;

public class CustomInvoiceAdapter extends ArrayAdapter<Items> {
    private Context mContext;
    private ArrayList<Items> items = new ArrayList<>();
    private float totalCost = 0.0f;

    public CustomInvoiceAdapter(Context context, ArrayList<Items> items) {
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
        Items item = items.get(position);

        TextView itemName = listItem.findViewById(R.id.itemNameInvoice);
        itemName.setText(item.getItemName());

        TextView itemQuantity = listItem.findViewById(R.id.itemQuantityInvoice);
        itemQuantity.setText(""+item.getNo_of_servings());

        TextView itemCost = listItem.findViewById(R.id.itemCostInvoice);
        itemCost.setText("$"+Float.parseFloat(item.getCost())*Integer.parseInt(item.getNo_of_servings()));

        return listItem;
    }
}
