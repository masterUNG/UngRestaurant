package appewtc.masterung.ungrestaurant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class ConfirmOrderActivity extends AppCompatActivity {

    private TextView officerTextView, deskTextView;
    private String officerString, deskString;
    private ListView orderListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        //Bind Widget
        bindWidget();
        //Show Officer & Desk
        showOfficerAndDesk();
        //Create ListView
        createListView();
    }   // onCreate

    public void clickOrderFood(View view) {
        OrderTABLE objOrderTABLE = new OrderTABLE(this);
        String[] strFood = objOrderTABLE.readAllOrder(1);
        String[] strItem = objOrderTABLE.readAllOrder(2);
        for (int i = 0; i < strFood.length; i++) {

            try {
                //Setup New Policy
                StrictMode.ThreadPolicy myPolicy = new StrictMode
                        .ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(myPolicy);

                ArrayList<NameValuePair> objNameValuePairs = new ArrayList<NameValuePair>();
                objNameValuePairs.add(new BasicNameValuePair("isAdd", "true"));
                objNameValuePairs.add(new BasicNameValuePair("Officer", officerString));
                objNameValuePairs.add(new BasicNameValuePair("Desk", deskString));
                objNameValuePairs.add(new BasicNameValuePair("Food", strFood[i]));
                objNameValuePairs.add(new BasicNameValuePair("Item", strItem[i]));

                HttpClient objHttpClient = new DefaultHttpClient();
                HttpPost objHttpPost = new HttpPost("http://swiftcodingthai.com/23jul/add_data_restaurant.php");
                objHttpPost.setEntity(new UrlEncodedFormEntity(objNameValuePairs, "UTF-8"));
                objHttpClient.execute(objHttpPost);

            } catch (Exception e) {
                Toast.makeText(ConfirmOrderActivity.this,
                        "Cannot Order Food", Toast.LENGTH_SHORT).show();
            }   //try

        }   //for

        //Delete All Order
        SQLiteDatabase objSqLiteDatabase = openOrCreateDatabase("Restaurant", MODE_PRIVATE, null);
        objSqLiteDatabase.delete("orderTABLE", null, null);

        Toast.makeText(ConfirmOrderActivity.this, "Order Finish", Toast.LENGTH_SHORT).show();
        Intent objIntent = new Intent(ConfirmOrderActivity.this, OrderActivity.class);
        objIntent.putExtra("Officer", officerString);
        startActivity(objIntent);

    }   // clickOrderFood

    private void createListView() {
        OrderTABLE objOrderTABLE = new OrderTABLE(this);
        String[] strFood = objOrderTABLE.readAllOrder(1);
        String[] strItem = objOrderTABLE.readAllOrder(2);
        OrderAdapter objOrderAdapter = new OrderAdapter(ConfirmOrderActivity.this, strFood, strItem);
        orderListView.setAdapter(objOrderAdapter);
        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editAndDeleteOrder(i);
            }
        });
    }   // createListview

    private void editAndDeleteOrder(final int intPosition) {
        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setIcon(R.drawable.restaurant);
        objBuilder.setTitle("Are you sure ?");
        objBuilder.setMessage("Delete this Order ?");
        objBuilder.setPositiveButton("Delete Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteOrder(intPosition);
                createListView();
                dialogInterface.dismiss();
            }
        });
        objBuilder.show();

    }   // editAndDeleteOrder

    private void deleteOrder(int intPosition) {
        int id = intPosition + 1;
        SQLiteDatabase objSqLiteDatabase = openOrCreateDatabase("Restaurant.db", MODE_PRIVATE, null);
        objSqLiteDatabase.delete("orderTABLE", "_id" + "=" + id, null);
    }

    private void bindWidget() {
        officerTextView = (TextView) findViewById(R.id.textView3);
        deskTextView = (TextView) findViewById(R.id.textView4);
        orderListView = (ListView) findViewById(R.id.listView2);
    }

    private void showOfficerAndDesk() {
        officerString = getIntent().getStringExtra("Officer");
        deskString = getIntent().getStringExtra("Desk");
        officerTextView.setText(officerString);
        deskTextView.setText(deskString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirm_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}   // Main Class
