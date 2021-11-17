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

        // insert 50 locations;
        for(int i=0; i<locs.length; i++) {
            ContentValues cv = new ContentValues();

            cv.put(ADDRESS_COL, locs[i].getAddress());
            cv.put(LONGITUDE_COL, locs[i].getLongitude());
            cv.put(LATITUDE_COL, locs[i].getLatitude());

            db.insert(LOCATION_TABLE, null, cv);
        }
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

        int success = db.delete(LOCATION_TABLE, ID_COL+"=?", new String[]{String.valueOf(locModel.getId())});

        db.close();
        return success != 0 ? true : false;
    }

    private LocationModel[] locs  = {
        new LocationModel(1,"Toronto, Ontario, Canada", 43.653226, -79.3831843),
        new LocationModel(2,"Ottawa, Ontario, Canada",45.4215296,-75.69719309999999),
        new LocationModel(2,"Hamilton, Ontario, Canada",43.2557206, -79.8711024),
        new LocationModel(2,"Kitchener, Ontario, Canada",43.4516395, -80.4925337),
        new LocationModel(3,"London, Ontario, Canada",42.9849233, -81.2452768),
        new LocationModel(2,"Oshawa, Ontario, Canada",43.897092900000004, -78.86579119999999),
        new LocationModel(0,"Windsor, Ontario, Canada", 42.3149367, -83.03636329999999),
        new LocationModel(1,"St. Catherines, Ontario, Canada", 43.1593745, -79.2468626),
        new LocationModel(1,"Barrie, Ontario, Canada",44.389355599999995, -79.6903316),
        new LocationModel(1,"Guelph, Ontario, Canada",43.5448048, -80.24816659999999),
        new LocationModel(1,"Kingston, Ontario, Canada", 44.2311717, -76.4859544),
        new LocationModel(0,"Kanata, Ontario, Canada", 45.3088185, -75.89868349999999),
        new LocationModel(0,"Milton, Ontario, Canada", 43.5182991, -79.8774042),
        new LocationModel(0,"Brantford, Ontario, Canada", 43.139386699999996, -80.2644254),
        new LocationModel(0,"Thunder Bay, Ontario, Canada", 48.3808951, -89.2476823),
        new LocationModel(0,"Sudbury, Ontario, Canada", 46.491731699999995, -80.99302899999999),
        new LocationModel(0,"Peterborough, Ontario, Canada", 44.309058, -78.31974699999999),
        new LocationModel(0,"Sarnia, Ontario, Canada", 42.974536, -82.4065901),
        new LocationModel(0,"Belleville, Ontario, Canada",44.1627589, -77.3832315),
        new LocationModel(0,"Sault Ste. Marie, Ontario, Canada", 46.5136494, -84.33575259999999),
        new LocationModel(0,"Welland, Ontario, Canada", 42.992157899999995, -79.2482555),
        new LocationModel(0,"North Bay, Ontario, Canada", 46.3091152, -79.4608204),
        new LocationModel(0,"Cornwall, Ontario, Canada", 45.021276199999996, -74.730345),
        new LocationModel(0,"Chatham, Ontario, Canada", 42.4048028, -82.19103779999999),
        new LocationModel(0,"Georgetown, Ontario, Canada", 43.650204599999995, -79.9036236),
        new LocationModel(0,"St. Thomas, Ontario, Canada", 42.7777214, -81.18265269999999),
        new LocationModel(0,"Woodstock, Ontario, Canada", 43.1314966, -80.74716509999999),
        new LocationModel(0,"40 Conlin Rd, Oshawa, ON L1H 7K4, Canada", 43.9478037, -78.89907240000001),
        new LocationModel(0,"Bowmanville, Ontario, Canada", 43.9126042, -78.688019),
        new LocationModel(0,"Leamington, Ontario, Canada", 42.053163399999995, -82.5998874),
        new LocationModel(0,"Stouffville, Ontario, Canada", 43.9705861, -79.2442842),
        new LocationModel(0,"Orillia, Ontario, Canada", 44.6082465, -79.4196783),
        new LocationModel(0,"Stratford, Ontario, Canada", 43.3700007, -80.98222860000001),
        new LocationModel(0,"Orangeville, Ontario, Canada", 43.919978799999996, -80.0943113),
        new LocationModel(0,"Bradford, Ontario, Canada", 44.110985799999995, -79.5794265),
        new LocationModel(0,"Timmins, Ontario, Canada", 48.4758208, -81.3304953),
        new LocationModel(0,"Keswick, Ontario, Canada", 44.2266889, -79.453512),
        new LocationModel(0,"Bolton, Ontario, Canada", 43.874199499999996, -79.7306602),
        new LocationModel(0,"Midland, Ontario, Canada", 44.74951600000001, -79.89219229999999),
        new LocationModel(0,"Innisfil, Ontario, Canada", 44.3008813, -79.6114973),
        new LocationModel(0,"Owen Sound, Ontario, Canada", 44.5690305, -80.9405602),
        new LocationModel(0,"Brockville, Ontario, Canada", 44.5895244, -75.68428580000001),
        new LocationModel(0,"Fergus, Ontario, Canada", 43.7059522, -80.3778336),
        new LocationModel(0,"Lindsay, Ontario, Canada", 44.3565742, -78.7407542),
        new LocationModel(0,"Collingwood, Ontario, Canada", 44.500768699999995, -80.2169047),
        new LocationModel(0,"Cobourg, Ontario, Canada", 43.9593373, -78.1677363),
        new LocationModel(0,"Alliston, Ontario, Canada", 44.1539514, -79.8691544),
        new LocationModel(0,"Wasaga Beach, Ontario, Canada", 44.5207419, -80.01606679999999),
        new LocationModel(0,"2000 Simcoe St N, Oshawa, ON L1H 7K4, Canada", 43.9450912, -78.89480390000001),
        new LocationModel(0,"Pembroke, Ontario, Canada", 45.826651299999995, -77.1108826),
        new LocationModel(0,"Port Colborne, Ontario, Canada", 42.886520399999995, -79.2508558),
        new LocationModel(0,"Markham, Ontario, Canada", 43.8561002, -79.3370188),
        new LocationModel(0,"Ajax, Ontario, Canada", 43.8508553, -79.0203732)
    };

}
