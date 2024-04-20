package edu.uga.cs.ridesharing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.firebase.ui.auth.data.model.User;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String USERS_TABLE = "USERS";
    public static final String REQUESTS_TABLE = "REQUESTS";
    public static final String OFFERS_TABLE = "OFFERS";
    public static final String COLUMN_ID = "user_id";
    public static final String COLUMN_REQUESTS_ID = "requests_id";
    public static final String COLUMN_OFFERS_ID = "offers_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_POINTS = "points";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DESTINATION = "destination";
    public static final String COLUMN_USER_ID = "user_id";

    public DBHelper(@Nullable Context context) {
        super(context, "ridesharing.db", null, 1);
    }

    //this is called the first time you try to access a database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTableStatement = "CREATE TABLE " + USERS_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " + COLUMN_EMAIL + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, " + COLUMN_POINTS + " INTEGER )";
        String createRequestsTableStatement = "CREATE TABLE " + REQUESTS_TABLE + " (" + COLUMN_REQUESTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DATE + " TEXT, "
                + COLUMN_DESTINATION + " TEXT, " + COLUMN_USER_ID + " INTEGER )";

        String createOffersTableStatement = "CREATE TABLE " + OFFERS_TABLE + " (" + COLUMN_OFFERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DATE + " TEXT, "
                + COLUMN_DESTINATION + " TEXT, " + COLUMN_USER_ID + " INTEGER )";

        db.execSQL(createUserTableStatement);
        db.execSQL(createRequestsTableStatement);
        db.execSQL(createOffersTableStatement);
    }

    // this is called everytime the version of the DB changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOneUser(UserModel userModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, userModel.getName());
        cv.put(COLUMN_EMAIL, userModel.getEmail());
        cv.put(COLUMN_PASSWORD, userModel.getPassword());
        cv.put(COLUMN_POINTS, userModel.getPoints());

        long insert = db.insert(USERS_TABLE, null, cv);
        if(insert == -1){
            return false;
        } else {
            return true;
        }
    }//addOne

    public List<UserModel> getUserList(){
        List<UserModel> returnList = new ArrayList<>();

        //get data from database
        String queryString = "SELECT * FROM " + USERS_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            //loop throughout the cursor (result set) and create new CountryModel object for each row
            do {
                int userID = cursor.getInt(0);
                String userName = cursor.getString(1);
                String userEmail = cursor.getString(2);
                String userPassword = cursor.getString(3);
                int userPoints = cursor.getInt(4);

                UserModel newUser = new UserModel(userID, userName, userEmail, userPassword, userPoints);
                returnList.add(newUser);
            } while (cursor.moveToNext());
        } else {
            // failure. Did not add anything to the list.
        }

        cursor.close();
        db.close();
        return returnList;
    }//getList
}

