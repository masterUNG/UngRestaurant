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
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private UserTABLE objUserTABLE;
    private FoodTABLE objFoodTABLE;
    private OrderTABLE objOrderTABLE;

    private EditText userEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initial Widget
        initialWidget();

        //Connected SQLite
        connectedSQLite();

        //Test Add Value
        //testAddValue();

        //Delete All Data
        deleteAllData();

        //Synchronize JSON to SQLite
        synJSONtoSQLite();

    }   // onCreate

    public void clickLogin(View view) {

        String strUser = userEditText.getText().toString().trim();
        String strPassword = passwordEditText.getText().toString().trim();

        //Check Zero
        if (strUser.equals("") || strPassword.equals("") ) {
            //Have Space
            errorDialog("มีช่องว่าง", "กรุณากรอกให้ครบ ทุกช่อง คะ");
        } else {
            //No Space
            checkUserPassword(strUser, strPassword);
        }

    }   // clickLogin

    private void checkUserPassword(String strUser, String strPassword) {
        try {
            String[] strMyResult = objUserTABLE.searchUserPassword(strUser);
            if (strPassword.equals(strMyResult[2])) {
                //Password True
                welcomeDialog(strMyResult[3]);
            } else {
                //Password False
                errorDialog("Password False", "Please Try Again Password False");
            }
        } catch (Exception e) {
            errorDialog("User False", "ไม่มี " + strUser + " ใน ฐานข้อมูลของเรา");
        }
    }   //checkUserPassword

    private void welcomeDialog(final String strName) {
        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setIcon(R.drawable.restaurant);
        objBuilder.setTitle("Welcome");
        objBuilder.setMessage("ยินดีต้อนรับ " + strName + "\n" + "สู่ระบบของเรา");
        objBuilder.setCancelable(false);
        objBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent objIntent = new Intent(MainActivity.this, OrderActivity.class);
                objIntent.putExtra("Officer", strName);
                startActivity(objIntent);
                finish();
            }
        });
        objBuilder.show();
    }   //welcomDialog

    private void errorDialog(String strTitle, String strMessage) {
        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setIcon(R.drawable.icon_question);
        objBuilder.setTitle(strTitle);
        objBuilder.setMessage(strMessage);
        objBuilder.setCancelable(false);
        objBuilder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        objBuilder.show();
    }   // errofDialog

    private void initialWidget() {
        userEditText = (EditText) findViewById(R.id.editText);
        passwordEditText = (EditText) findViewById(R.id.editText2);
    }   // initialWidget

    private void deleteAllData() {
        SQLiteDatabase objSqLiteDatabase = openOrCreateDatabase
                ("Restaurant.db", MODE_APPEND, null);
        objSqLiteDatabase.delete("userTABLE", null, null);
        objSqLiteDatabase.delete("foodTABLE", null, null);
        objSqLiteDatabase.delete("orderTABLE", null, null);
    }   // deleteAllData

    private void synJSONtoSQLite() {

        //Setup Policy
        StrictMode.ThreadPolicy myPolicy = new StrictMode
                .ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(myPolicy);

        //Loop 2 Times
        int intTimes = 0;
        while (intTimes <= 1) {

            //Variable & Constant
            InputStream objInputStream = null;
            String strJSON = null;
            String strUserURL = "http://swiftcodingthai.com/23jul/get_data_master.php";
            String strFoodURL = "http://swiftcodingthai.com/23jul/get_data_food.php";
            HttpPost objHttpPost;

            //1. Create InputStream
            try {
                HttpClient objHttpClient = new DefaultHttpClient();
                switch (intTimes) {
                    case 0:
                        objHttpPost = new HttpPost(strUserURL);
                        break;
                    default:
                        objHttpPost = new HttpPost(strFoodURL);
                        break;
                }
                HttpResponse objHttpResponse = objHttpClient.execute(objHttpPost);
                HttpEntity objHttpEntity = objHttpResponse.getEntity();
                objInputStream = objHttpEntity.getContent();
            } catch (Exception e) {
                Log.d("masterUNG", "InputStream ==> " + e.toString());
            }

            //2. Create strJSON
            try {
                BufferedReader objBufferedReader = new BufferedReader
                        (new InputStreamReader(objInputStream, "UTF-8"));
                StringBuilder objStringBuilder = new StringBuilder();
                String strLine = null;
                while ((strLine = objBufferedReader.readLine()) != null) {
                    objStringBuilder.append(strLine);
                }
                objInputStream.close();
                strJSON = objStringBuilder.toString();
            } catch (Exception e) {
                Log.d("masterUNG", "strJSON ==> " + e.toString());
            }

            //3. Update to SQLite
            try {
                JSONArray objJsonArray = new JSONArray(strJSON);
                for (int i = 0; i < objJsonArray.length(); i++) {
                    JSONObject jsonObject = objJsonArray.getJSONObject(i);
                    switch (intTimes) {
                        case 0:
                            //Update userTABLE
                            String strUser = jsonObject.getString("User");
                            String strPassword = jsonObject.getString("Password");
                            String strName = jsonObject.getString("Name");
                            objUserTABLE.addNewUser(strUser, strPassword, strName);
                            break;
                        default:
                            //Update foodTABLE
                            String strFood = jsonObject.getString("Food");
                            String strSource = jsonObject.getString("Source");
                            String strPrice = jsonObject.getString("Price");
                            objFoodTABLE.addNewFood(strFood, strSource, strPrice);
                            break;
                    }
                }
            } catch (Exception e) {
                Log.d("masterUNG", "Update SQLite ==> " + e.toString());
            }


            //Increase intTimes
            intTimes += 1;
        }   // while

    }   //synJSONtoSQLite

    private void testAddValue() {
        objUserTABLE.addNewUser("testUser", "testPass", "testName");
        objFoodTABLE.addNewFood("testFood", "testSource", "testPrice");
        objOrderTABLE.addOrder("testOfficer", "testDesk", "testFood", "testItem");
    }   // testAddValue

    private void connectedSQLite() {
        objUserTABLE = new UserTABLE(this);
        objFoodTABLE = new FoodTABLE(this);
        objOrderTABLE = new OrderTABLE(this);
    }   // connectedSQLite

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
