package appewtc.masterung.ungrestaurant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
        objBuilder.setTitle("What do you wont ?");
        objBuilder.setNegativeButton("Add Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
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
