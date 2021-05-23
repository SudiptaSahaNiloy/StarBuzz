package com.example.starbuzz;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StarbuzzDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "starbuzz"; //the name of our database
    private static final int DB_VERSION = 2; //the version of the database

    public StarbuzzDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        updateMyDatabase(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        updateMyDatabase(sqLiteDatabase, oldVersion, newVersion);
    }

    private static void insertDrink(SQLiteDatabase sqLiteDatabase, String name, String description, int resourceId) {
        ContentValues drinkValues = new ContentValues();
        drinkValues.put("NAME", name);
        drinkValues.put("DESCRIPTION", description);
        drinkValues.put("IMAGE_RESOURCE_ID", resourceId);
        sqLiteDatabase.insert("DRINK", null, drinkValues);
    }

    //method to update the value of the table
    private static void updateDrink(SQLiteDatabase sqLiteDatabase, String name, String description){
        ContentValues drinkValues = new ContentValues();
        drinkValues.put("DESCRIPTION", description);
        sqLiteDatabase.update("DRINK", drinkValues, "NAME = ?", new String[] {name});
    }

//    method to delete a row from the table
    private static void deleteDrinkRow(SQLiteDatabase sqLiteDatabase, String name){
        sqLiteDatabase.delete("DRINK", "NAME = ?", new String[]{name});
    }

//    method to delete the whole table
    private static void deleteDrinkTable(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("DROP TABLE DRINK;");
    }

    private void updateMyDatabase(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            sqLiteDatabase.execSQL("CREATE TABLE DRINK (_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, DESCRIPTION TEXT, IMAGE_RESOURCE_ID INTEGER);");
            insertDrink(sqLiteDatabase, "Latte", "Espressso and steamed milk", R.drawable.latte);
            insertDrink(sqLiteDatabase, "Cappuccino", "Espressso, hot milk and steamed-milk foam", R.drawable.cappuccino);
            insertDrink(sqLiteDatabase, "Filter", "Our best drip coffee", R.drawable.filter);
        }
        if (oldVersion < 2) {
//            code to add extra columns
            sqLiteDatabase.execSQL("ALTER TABLE DRINK ADD COLUMN FAVORITE NUMERIC");
//            updateDrink(sqLiteDatabase, "Latte", "Tasty");
        }
    }
}
