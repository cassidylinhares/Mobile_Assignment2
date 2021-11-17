package com.sofe4640.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LocationActivity extends AppCompatActivity {
    LocationModel location;
    EditText longEdit, latEdit, addrEdit;
    Button backBtn, saveBtn, deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        getSelectedLocation();
        setView(location);

        saveBtnHandler();
        deleteBtnHandler();
        backBtnHandler();
    }

    //get the values from the location selected from list and turn it into a location type
    private void getSelectedLocation() {
        Intent selectedLoc = getIntent();

        int id = selectedLoc.getIntExtra("id", -1);
        String address = selectedLoc.getStringExtra("address");
        double longitude = selectedLoc.getDoubleExtra("longitude", 0);
        double latitude = selectedLoc.getDoubleExtra("latitude", 0);

        location = new LocationModel(id, address, latitude, longitude);
    }

    //initialize the ui views and set the text to be the location selected from list
    private void setView(LocationModel loc) {
        addrEdit = findViewById(R.id.loc_addressEdit);
        longEdit = findViewById(R.id.loc_longitudeEdit);
        latEdit = findViewById(R.id.loc_latitudeEdit);

        addrEdit.setText(loc.getAddress());
        longEdit.setText(String.valueOf(loc.getLongitude()));
        latEdit.setText(String.valueOf(loc.getLatitude()));
    }

    //back button to go back to main activity and destroy this activity
    private void backBtnHandler() {
        backBtn = findViewById(R.id.loc_backButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LocationActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    //button to update any changes made
    private void saveBtnHandler() {
        saveBtn = findViewById(R.id.loc_saveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DAO dbHandler = new DAO(LocationActivity.this);

                //get form values
                double longitude = Double.parseDouble(longEdit.getText().toString());
                double latitude = Double.parseDouble(latEdit.getText().toString());
                String address = addrEdit.getText().toString();

                //check if either address has been changed or coordinates have been changed
                if (!address.equalsIgnoreCase(location.getAddress())) {
                    location.setAddress(address);
                    location.getCoordinates(LocationActivity.this); //update the address and coordinates to match
                    location.getAddressFromCoord(LocationActivity.this);
                } else {
                    location.setLatitude(latitude);
                    location.setLongitude(longitude);
                    location.getAddressFromCoord(LocationActivity.this); //update the address and coordinates to match
                    location.getCoordinates(LocationActivity.this);
                }

                //update the db, ui, and tell user it updated
                LocationModel updatedLoc = dbHandler.update(location);
                if(updatedLoc != null) {
                    setView(updatedLoc);
                    Toast.makeText(LocationActivity.this, "Updated & Saved", Toast.LENGTH_SHORT).show();

                    //go back to main activity and destroy this one
                    startActivity(new Intent(LocationActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LocationActivity.this, "Error updating", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //button to delete location
    private void deleteBtnHandler() {
        deleteBtn = findViewById(R.id.loc_deleteButton);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete
                DAO dbHandler = new DAO(LocationActivity.this);
                boolean success = dbHandler.delete(location);

                //check if error deleting
                if(success) {
                    Toast.makeText(LocationActivity.this, "Deleted.", Toast.LENGTH_SHORT).show();

                    //go back to main activity and destroy this one if there's no delete error
                    startActivity(new Intent(LocationActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LocationActivity.this, "Error deleting", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}