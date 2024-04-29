package edu.uga.cs.ridesharing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.ridesharing.driverdata.OffersModel;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ridesharing.db";
    private static final int DB_VERSION = 1;
    private static DBHelper helperInstance;
    public static final String TABLE_USERS = "users";
    public static final String USER_COLUMN_ID = "user_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_POINTS = "points";

    public static final String TABLE_REQUESTS = "REQUESTS";
    public static final String COLUMN_REQUESTS_ID = "requests_id";

    public static final String TABLE_OFFERS = "OFFERS";
    public static final String COLUMN_OFFERS_ID = "offers_id";

    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DESTINATION = "destination";

    private static final String CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " ("
                    + USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_EMAIL + " TEXT, "
                    + COLUMN_PASSWORD + " TEXT, "
                    + COLUMN_POINTS + " INTEGER"
                    + ")";

    private static final String CREATE_REQUESTS =
            "CREATE TABLE " + TABLE_REQUESTS + " ("
                    + COLUMN_REQUESTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_DATE + " TEXT, "
                    + COLUMN_DESTINATION + " TEXT, "
                    + USER_COLUMN_ID + " INTEGER"
                    + ")";

    private static final String CREATE_OFFERS =
            "CREATE TABLE " + TABLE_OFFERS + " ("
                    + COLUMN_OFFERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_DATE + " TEXT, "
                    + COLUMN_DESTINATION + " TEXT, "
                    + USER_COLUMN_ID + " INTEGER"
                    + ")";


    private DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DBHelper getInstance(Context context){
        if (helperInstance == null){
            helperInstance = new DBHelper(context.getApplicationContext());
        }
        return helperInstance;
    }

    //this is called the first time you try to access a database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS);
        db.execSQL(CREATE_REQUESTS);
        db.execSQL(CREATE_OFFERS);
    }

    // this is called everytime the version of the DB changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_USERS);
        db.execSQL("drop table if exists " + TABLE_OFFERS);
        db.execSQL("drop table if exists " + TABLE_REQUESTS);
        onCreate(db);
    }

    public boolean addOneUser(UserModel userModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, userModel.getName());
        cv.put(COLUMN_EMAIL, userModel.getEmail());
        cv.put(COLUMN_PASSWORD, userModel.getPassword());
        cv.put(COLUMN_POINTS, userModel.getPoints());

        long insert = db.insert(TABLE_USERS, null, cv);
        if(insert == -1){
            return false;
        } else {
            return true;
        }
    }//addOneUser

    public boolean addOneRequest(RequestsModel requestsModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, requestsModel.getDate());
        cv.put(COLUMN_EMAIL, requestsModel.getDestination());
        cv.put(COLUMN_PASSWORD, requestsModel.getUserID());

        long insert = db.insert(TABLE_REQUESTS, null, cv);
        if(insert == -1){
            return false;
        } else {
            return true;
        }
    }//addOneRequest

    public boolean addOneOffer(OffersModel offersModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, offersModel.getDate());
        cv.put(COLUMN_EMAIL, offersModel.getDestination());
        cv.put(COLUMN_PASSWORD, offersModel.getUserID());

        long insert = db.insert(TABLE_OFFERS, null, cv);
        if(insert == -1){
            return false;
        } else {
            return true;
        }
    }//addOneOffer

    public List<UserModel> getUserList(){
        List<UserModel> returnList = new ArrayList<>();

        //get data from database
        String queryString = "SELECT * FROM " + TABLE_USERS;

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
    }//getUserList

    public List<RequestsModel> getRequestsList(){
        List<RequestsModel> returnList = new ArrayList<>();

        //get data from database
        String queryString = "SELECT * FROM " + TABLE_REQUESTS;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            //loop throughout the cursor (result set) and create new CountryModel object for each row
            do {
                int requestID = cursor.getInt(0);
                String requestDate = cursor.getString(1);
                String requestDestination = cursor.getString(2);
                int requestUserID = cursor.getInt(3);

                RequestsModel newRequest = new RequestsModel(requestID, requestDate, requestDestination, requestUserID);
                returnList.add(newRequest);
            } while (cursor.moveToNext());
        } else {
            // failure. Did not add anything to the list.
        }

        cursor.close();
        db.close();
        return returnList;
    }//getRequestsList

    public List<OffersModel> getOffersList(){
        List<OffersModel> returnList = new ArrayList<>();

        //get data from database
        String queryString = "SELECT * FROM " + TABLE_OFFERS;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            //loop throughout the cursor (result set) and create new CountryModel object for each row
            do {
                int offerID = cursor.getInt(0);
                String offerDate = cursor.getString(1);
                String offerDestination = cursor.getString(2);
                int offerUserID = cursor.getInt(3);

                OffersModel newOffer = new OffersModel(offerID, offerDate, offerDestination, offerUserID);
                returnList.add(newOffer);
            } while (cursor.moveToNext());
        } else {
            // failure. Did not add anything to the list.
        }

        cursor.close();
        db.close();
        return returnList;
    }//getList
}