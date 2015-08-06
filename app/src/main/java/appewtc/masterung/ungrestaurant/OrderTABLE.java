package appewtc.masterung.ungrestaurant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Chaiwoot on 8/6/2015.
 */
public class OrderTABLE {
    private MySQLiteOpenHelper objMySQLiteOpenHelper;
    private SQLiteDatabase writeSqLiteDatabase, readSqLiteDatabase;

    public static final String ORDER_TABLE = "orderTABLE";
    public static final String COLUMN_ID_ORDER = "_id";
    public static final String COLUMN_OFFICER = "Officer";
    public static final String COLUMN_DESK = "Desk";
    public static final String COLUMN_FOOD = "Food";

    public OrderTABLE(Context context) {
        objMySQLiteOpenHelper = new MySQLiteOpenHelper(context);
        writeSqLiteDatabase = objMySQLiteOpenHelper.getWritableDatabase();
        readSqLiteDatabase = objMySQLiteOpenHelper.getReadableDatabase();
    }   // Constructor
}   // Main Class
