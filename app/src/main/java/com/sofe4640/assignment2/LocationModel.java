package com.sofe4640.assignment2;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//Model for location
public class LocationModel {
    private int id;
    private String address;
    private double latitude;
    private double longitude;

    //Constructor for existing location
    public LocationModel(int id, String address, double latitude, double longitude) {
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //Constructor for non-existing location
    public LocationModel(String address) {
        this.address = address;
    }
    public LocationModel(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    //get & set the address when given a latitude and longitude
    public void getAddressFromCoord(Context context) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List coordList = geocoder.getFromLocation(getLatitude(), getLongitude(), 1);

            if(coordList != null && coordList.size() > 0) {
                Address addr = (Address) coordList.get(0);
                setAddress(addr.getAddressLine(0));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    //get & set the coordinates when given an address
    public void getCoordinates(Context context) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List coordList = geocoder.getFromLocationName(getAddress(), 1);

            if(coordList != null && coordList.size() > 0) {
                Address addr = (Address) coordList.get(0);
                setLatitude(addr.getLatitude());
                setLongitude(addr.getLongitude());
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    //to string
    @Override
    public String toString() {
        return "LocationModel{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    //getter & setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
