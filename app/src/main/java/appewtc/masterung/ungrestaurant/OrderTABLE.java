package appewtc.masterung.ungrestaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    public static final String COLUMN_ITEM = "Item";

    public OrderTABLE(Context context) {
        objMySQLiteOpenHelper = new MySQLiteOpenHelper(context);
        writeSqLiteDatabase = objMySQLiteOpenHelper.getWritableDatabase();
        readSqLiteDatabase = objMySQLiteOpenHelper.getReadableDatabase();
    }   // Constructor

    public String[] readAllOrder(int intChoose) {

        String[] strReadAll = null;
        Cursor objCursor = readSqLiteDatabase.query(ORDER_TABLE,
                new String[]{COLUMN_ID_ORDER, COLUMN_FOOD, COLUMN_ITEM},
                null, null, null, null, null);
        if (objCursor != null) {
            objCursor.moveToFirst();
            strReadAll = new String[objCursor.getCount()];
            for (int i = 0; i < objCursor.getCount(); i++) {
                if (intChoose == 1) {
                    strReadAll[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_FOOD));
                } else {
                    strReadAll[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_ITEM));
                }   // if
                objCursor.moveToNext();
            }   // for
        }   // if
        objCursor.close();
        return strReadAll;
    }   // readAllOrder

    public long addOrder(String strOfficer, String strDesk, String strFood, String strItem) {
        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_OFFICER, strOfficer);
        objContentValues.put(COLUMN_DESK, strDesk);
        objContentValues.put(COLUMN_FOOD, strFood);
        objContentValues.put(COLUMN_ITEM, strItem);
        return readSqLiteDatabase.insert(ORDER_TABLE, null, objContentValues);
    }   // addOrder

}   // Main Class
