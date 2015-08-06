package appewtc.masterung.ungrestaurant;

import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connected SQLite
        connectedSQLite();

        //Test Add Value
        //testAddValue();

        //Delete All Data
        deleteAllData();

        //Synchronize JSON to SQLite
        synJSONtoSQLite();

    }   // onCreate

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
