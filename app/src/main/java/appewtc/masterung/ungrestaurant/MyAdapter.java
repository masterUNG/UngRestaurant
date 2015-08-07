package appewtc.masterung.ungrestaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Chaiwoot on 8/7/2015.
 */
public class MyAdapter extends BaseAdapter{

    //Explicit
    private Context objContext;
    private String[] foodStrings, sourceStrings, priceStrings;

    public MyAdapter(Context objContext, String[] foodStrings,
                     String[] sourceStrings, String[] priceStrings) {
        this.objContext = objContext;
        this.foodStrings = foodStrings;
        this.sourceStrings = sourceStrings;
        this.priceStrings = priceStrings;
    }   // Constructor


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
        View view1 = objLayoutInflater.inflate(R.layout.my_listview, viewGroup, false);
        //Show Food
        TextView foodTextView = (TextView) view1.findViewById(R.id.txtListFood);
        foodTextView.setText(foodStrings[i]);
        //Show Price
        TextView priceTextView = (TextView) view1.findViewById(R.id.txtListPrice);
        priceTextView.setText(priceStrings[i]);
        //Show ImageFood
        ImageView foodImageView = (ImageView) view1.findViewById(R.id.imvListFood);
        Picasso.with(objContext).load(sourceStrings[i]).into(foodImageView);
        return view1;
    }   // getView
}   // Main Class
