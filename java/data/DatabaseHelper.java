package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "CREATION";
    private static final String TABLE_NAME = "stored_cities";
    private static final String COL1 = "ID";
    private static final String COL2 = "name";
    private static final String COL3 = "isFavorite";



    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 19);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(" + " ID INTEGER PRIMARY KEY, " +
                COL2 + " TEXT UNIQUE, " + COL3 + " TEXT)";
        //String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY, " +
        //                COL2 + " TEXT UNIQUE, " + COL3 + " TEXT, " + COL4 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String cityName, String isFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL2, cityName);
        contentValues.put(COL3, isFavorite);
//        contentValues.put(COL4, lon);

        db.insertWithOnConflict(TABLE_NAME, null, contentValues,SQLiteDatabase.CONFLICT_IGNORE);


        Log.d(TAG, "addData: Adding " + cityName + " to " + TABLE_NAME);
        //Log.d(TAG, "TABLE: " + getTableAsString(db, TABLE_NAME));


        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
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

    /**
     * Returns only the ID that matches the name passed in
     * @param name
     * @return
     */
    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT COUNT(*)" + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getIsFavorite(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL3 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    


    public void updateFavorite(int id, String isFavorite) {
        int itemID = id+1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL3, isFavorite);

        db.update(TABLE_NAME, contentValues, "ID="+itemID, null);
        Log.d(TAG, "Is favorite? " + itemID + " " + isFavorite);
        Log.d(TAG, "TABLE: " + getTableAsString(db, TABLE_NAME));

    }


    /**
     * Delete from database
     * @param id
     * @param name
     */
    public void deleteName(int id, String name){

        SQLiteDatabase db = this.getWritableDatabase();
        //Log.d(TAG, "TABLE: " + getTableAsString(db, TABLE_NAME));

        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                 + COL2 + " = '" + name + "'";
        //String query = "DELETE FROM " + TABLE_NAME + " WHERE "
        //                + COL1 + " = '" + id + "'" +
        //                " AND " + COL2 + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: ID " + id);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
        //Log.d(TAG, "TABLE: " + getTableAsString(db, TABLE_NAME));
    }



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
