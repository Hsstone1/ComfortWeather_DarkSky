package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WeatherDataDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "CREATION";
    private static final String TABLE_NAME = "weather_data";
    private static final String COL1 = "ID";
    private static final String COL2 = "name";




    public WeatherDataDatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 10);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(" + " ID INTEGER PRIMARY KEY, " +
                COL2 + " TEXT)";
        Log.d(TAG, "CREATE TABLE: SUCCESS");

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String weatherData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL2, weatherData);

        Log.d(TAG, "addData: Adding " + weatherData + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);
        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            Log.d(TAG, "addData: SUCCESS");
            db.close();
            return true;
        }

    }

    /**
     * Returns all the data from database
     * @return
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getItemID(String weatherData){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT COUNT(*)" + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + weatherData + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }



//    public void deleteData(int id){
//
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        String table = TABLE_NAME;
//        String whereClause = COL1 +"=?";
//        String[] whereArgs = new String[] { String.valueOf(id) };
//        db.delete(table, whereClause, whereArgs);
//    }

    public void deleteData(){

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.execSQL("VACUUM");

    }




//SQLiteDatabase db = this.getWritableDatabase();
//
//        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
//                + COL2 + " = '" + weatherData + "'";
//
//        Log.d(TAG, "deleteName: query: " + query);
//        Log.d(TAG, "deleteName: ID " + id);
//        db.execSQL(query);



    public String getTableAsString(SQLiteDatabase db, String tableName) {
        Log.d(TAG, "getTableAsString called");
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        return tableString;
    }
}
