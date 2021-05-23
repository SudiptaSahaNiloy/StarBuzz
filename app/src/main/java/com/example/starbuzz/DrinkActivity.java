package com.example.starbuzz;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkActivity extends Activity {
    public static final String EXTRA_DRINKID = "drinkId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        //Get the drink from the intent
        int drinkId = (Integer) getIntent().getExtras().get(EXTRA_DRINKID);

        //Create a cursor
        SQLiteOpenHelper sqLiteOpenHelper = new StarbuzzDatabaseHelper(this);
        try {
            SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();

            Cursor cursor = sqLiteDatabase.query("DRINK", new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"}, "_id = ?",
                    new String[]{Integer.toString(drinkId)}, null, null, null);

            if (cursor.moveToFirst()) {
//                Get the drink details from the cursor
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);

                //update the checkbox
                Boolean isFavorite = (cursor.getInt(3) == 1);
                CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);

                //populate the drink name
                TextView name = (TextView) findViewById(R.id.name);
                name.setText(nameText);

                //populate the drink description
                TextView description = (TextView) findViewById(R.id.description);
                description.setText(descriptionText);

                //populate the drink image
                ImageView photo = (ImageView) findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);
            }
            cursor.close();
            sqLiteDatabase.close();
        } catch (SQLException e) {
            Toast.makeText(this, "Database Unavailable1", Toast.LENGTH_SHORT).show();
        }
    }

    //update the database when the checkbox is clicked
    public void onFavouriteClicked(View view) {
        int drinkId = (Integer) getIntent().getExtras().get(EXTRA_DRINKID);
        new UpdateDrinkTask().execute(drinkId);
    }

    private class UpdateDrinkTask extends AsyncTask<Integer, Void, Boolean> {
        private ContentValues drinkValues;

        protected void onPreExecute() {
            CheckBox favourite = (CheckBox) findViewById(R.id.favorite);
            drinkValues = new ContentValues();
            drinkValues.put("FAVORITE", favourite.isChecked());
        }

        protected Boolean doInBackground(Integer... drinks) {
            int drinkId = drinks[0];
            SQLiteOpenHelper sqLiteOpenHelper = new StarbuzzDatabaseHelper(DrinkActivity.this);
            try {
                SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
                sqLiteDatabase.update("DRINK", drinkValues, "_id = ?", new String[]{Integer.toString(drinkId)});
                sqLiteDatabase.close();
                return true;
            } catch (SQLException e) {
                return false;
            }
        }

        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast.makeText(DrinkActivity.this, "Database Unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }
}