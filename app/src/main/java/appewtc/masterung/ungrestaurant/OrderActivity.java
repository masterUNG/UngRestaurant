package appewtc.masterung.ungrestaurant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class OrderActivity extends AppCompatActivity {

    //Explicit
    private TextView officerTextView;
    private Spinner deskSpinner;
    private ListView foodListView;
    private String officerString, deskString, foodString, itemString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        //Bind Widget
        bindWidget();
        //Show Officer
        showOfficer();
        //Show Spinner
        showSpinner();
        //Create ListView
        createListView();
    }   // onCreate

    public void clickOrder(View view) {

    }

    private void showSpinner() {
        // Create Spinner
        final String[] strDeskSpinner = {"One", "Two", "Three", "Four", "Five"};
        ArrayAdapter<String> deskAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, strDeskSpinner);
        deskSpinner.setAdapter(deskAdapter);
        // Active onClice
        deskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                deskString = strDeskSpinner[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                deskString = strDeskSpinner[0];
            }
        });

    }   // showSpinner


    private void showOfficer() {
        officerString = getIntent().getStringExtra("Officer");
        officerTextView.setText(officerString);
    }

    private void createListView() {
        FoodTABLE objFoodTABLE = new FoodTABLE(this);
        final String[] strFood = objFoodTABLE.readAllFood(1);
        String[] strSource = objFoodTABLE.readAllFood(2);
        String[] strPrice = objFoodTABLE.readAllFood(3);
        MyAdapter objMyAdapter = new MyAdapter(OrderActivity.this,
                strFood, strSource, strPrice);
        foodListView.setAdapter(objMyAdapter);

        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                foodString = strFood[i];
                chooseItem();
            }
        });

    }   // createListView

    private void chooseItem() {
        final CharSequence[] choiceCharSequences = {"1 set", "2 set", "3 set", "4 set", "5 set"};
        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setTitle(foodString);
        objBuilder.setSingleChoiceItems(choiceCharSequences, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        itemString = "1";
                        break;
                    case 1:
                        itemString = "2";
                        break;
                    case 2:
                        itemString = "3";
                        break;
                    case 3:
                        itemString = "4";
                        break;
                    case 4:
                        itemString = "5";
                        break;
                }   // Swich
                uploadToSQLite();
                dialogInterface.dismiss();
            }   // event
        });
        objBuilder.show();
    }   //chooseItem

    private void uploadToSQLite() {
        OrderTABLE objOrderTABLE = new OrderTABLE(this);
        objOrderTABLE.addOrder(officerString, deskString, foodString, itemString);
    }   // uploadToSQLite

    private void bindWidget() {
        officerTextView = (TextView) findViewById(R.id.textView2);
        deskSpinner = (Spinner) findViewById(R.id.spinner);
        foodListView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order, menu);
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
