package appewtc.masterung.ungrestaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Chaiwoot on 8/8/2015.
 */
public class OrderAdapter extends BaseAdapter{

    private Context objContext;
    private String[] foodStrings, itemStrings;

    public OrderAdapter(Context objContext, String[] foodStrings, String[] itemStrings) {
        this.objContext = objContext;
        this.foodStrings = foodStrings;
        this.itemStrings = itemStrings;
    }

    @Override
    public int getCount() {
        return foodStrings.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater objLayoutInflater = (LayoutInflater) objContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = objLayoutInflater.inflate(R.layout.order_listview, viewGroup, false);
        //Food
        TextView foodTextView = (TextView) view1.findViewById(R.id.txtFoodConfirm);
        foodTextView.setText(foodStrings[i]);
        //item
        TextView itemTextView = (TextView) view1.findViewById(R.id.txtitemConfirm);
        itemTextView.setText(itemStrings[i]);
        return view1;
    }   // getView
}   // Main Class
