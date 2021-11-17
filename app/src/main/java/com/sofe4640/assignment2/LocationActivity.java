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

    private void getSelectedLocation() {
        Intent selectedLoc = getIntent();

        int id = selectedLoc.getIntExtra("id", -1);
        String address = selectedLoc.getStringExtra("address");
        double longitude = selectedLoc.getDoubleExtra("longitude", 0);
        double latitude = selectedLoc.getDoubleExtra("latitude", 0);

        location = new LocationModel(id, address, latitude, longitude);
    }

    private void setView(LocationModel loc) {
        addrEdit = findViewById(R.id.loc_addressEdit);
        longEdit = findViewById(R.id.loc_longitudeEdit);
        latEdit = findViewById(R.id.loc_latitudeEdit);

        addrEdit.setText(loc.getAddress());
        longEdit.setText(String.valueOf(loc.getLongitude()));
        latEdit.setText(String.valueOf(loc.getLatitude()));
    }

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

    private void saveBtnHandler() {
        saveBtn = findViewById(R.id.loc_saveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DAO dbHandler = new DAO(LocationActivity.this);

                double longitude = Double.parseDouble(longEdit.getText().toString());
                double latitude = Double.parseDouble(latEdit.getText().toString());
                String address = addrEdit.getText().toString();

                if (!address.equalsIgnoreCase(location.getAddress())) {
                    location.setAddress(address);
                    location.getCoordinates(LocationActivity.this);
                    location.getAddressFromCoord(LocationActivity.this);
                } else {
                    location.setLatitude(latitude);
                    location.setLongitude(longitude);
                    location.getAddressFromCoord(LocationActivity.this);
                    location.getCoordinates(LocationActivity.this);
                }

                LocationModel updatedLoc = dbHandler.update(location);
                setView(updatedLoc);
                Toast.makeText(LocationActivity.this, "Updated & Saved", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(LocationActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void deleteBtnHandler() {
        deleteBtn = findViewById(R.id.loc_deleteButton);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DAO dbHandler = new DAO(LocationActivity.this);
                dbHandler.delete(location);

                Toast.makeText(LocationActivity.this, "Deleted.", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(LocationActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}