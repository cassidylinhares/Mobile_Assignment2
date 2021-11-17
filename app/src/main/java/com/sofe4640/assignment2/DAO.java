package com.sofe4640.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DAO extends SQLiteOpenHelper {
    private static final String LOCATION_TABLE = "LOCATION_TABLE";
    private static final String ADDRESS_COL = "ADDRESS";
    private static final String LATITUDE_COL = "LATITUDE";
    private static final String LONGITUDE_COL = "LONGITUDE";
    private static final String ID_COL = "ID";

    public DAO(@Nullable Context context) {
        super(context, "location.db", null, 1);
    }

    //runs once to create the db
    @Override
    public void onCreate(SQLiteDatabase db) {
        String creatStmt = "CREATE TABLE " + LOCATION_TABLE + " (" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ADDRESS_COL + " TEXT, " + LATITUDE_COL + " REAL, " + LONGITUDE_COL + " REAL)";

        db.execSQL(creatStmt);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //lol no
    }

    //inserts a location into the db
    public boolean insert(LocationModel locModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ADDRESS_COL, locModel.getAddress());
        cv.put(LONGITUDE_COL, locModel.getLongitude());
        cv.put(LATITUDE_COL, locModel.getLatitude());

        long success = db.insert(LOCATION_TABLE, null, cv);
        db.close();
        return success != -1 ? true : false;
    }

    //gets a list of search results from the db when user searches by address
    public List<LocationModel> searchAddress(String address) {
        List<LocationModel> allLocs = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + LOCATION_TABLE + " WHERE " + ADDRESS_COL + " LIKE '" + "%" + address + "%'";

        Cursor cur = db.rawQuery(query, null);

        if(cur.moveToFirst()) {
            do{
                int id = cur.getInt(0);
                String addr = cur.getString(1);
                double lat = cur.getDouble(2);
                double lng = cur.getDouble(3);

                LocationModel locModel = new LocationModel(id, addr, lat, lng);

                allLocs.add(locModel);
            } while(cur.moveToNext());
        }
        db.close();
        return allLocs;
    }

    //get all the locations
    public List<LocationModel> getAll() {
        List<LocationModel> allLocs = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + LOCATION_TABLE;

        Cursor cur = db.rawQuery(query, null);

        if(cur.moveToFirst()) {
            do{
                int id = cur.getInt(0);
                String addr = cur.getString(1);
                double lat = cur.getDouble(2);
                double lng = cur.getDouble(3);

                LocationModel locModel = new LocationModel(id, addr, lat, lng);

                allLocs.add(locModel);
            } while(cur.moveToNext());
        }

        db.close();
        return allLocs;
    }

    //update a given location
    public LocationModel update(LocationModel locModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ADDRESS_COL, locModel.getAddress());
        cv.put(LONGITUDE_COL, locModel.getLongitude());
        cv.put(LATITUDE_COL, locModel.getLatitude());

        db.update(LOCATION_TABLE, cv, ID_COL+"=?", new String[]{String.valueOf(locModel.getId())});

        db.close();
        return locModel;
    }

    //delete a location
    public boolean delete(LocationModel locModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM " + LOCATION_TABLE + " WHERE " + ID_COL + " = " + locModel.getId();

        Cursor cur = db.rawQuery(query, null);

        boolean success = cur.moveToFirst() ? true : false;
        db.close();
        return success;
    }
}
