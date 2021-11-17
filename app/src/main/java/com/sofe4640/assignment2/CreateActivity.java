package com.sofe4640.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Activity to insert a new location. Location gets inserted by address and coordinates are auto-filled
public class CreateActivity extends AppCompatActivity {
    EditText addrEdit;
    Button backBtn, addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        backBtnHandler();
        addBtnHandler();

        addrEdit = findViewById(R.id.create_addressEditText);
    }

    //button to insert a new location
    private void addBtnHandler() {
        DAO dbHandler = new DAO(CreateActivity.this);
        addBtn = findViewById(R.id.create_AddButton);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if address was provided
                if (addrEdit.getText().toString().equals("")) {
                    addrEdit.setError("Address Required");
                } else {
                    String addr = addrEdit.getText().toString();

                    LocationModel newLoc = new LocationModel(addr);
                    newLoc.getCoordinates(CreateActivity.this); //gets the coordinates for the address provided

                    // check if it inserted properly
                    boolean success = dbHandler.insert(newLoc);
                    if (success) {
                        //tell user it inserted and go back to main activity and destroy this one
                        Toast.makeText(CreateActivity.this, "Created & Saved", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CreateActivity.this, MainActivity.class));
                        finish();
                    } else {
                        addrEdit.setError("Error creating location");
                        Toast.makeText(CreateActivity.this, "Error creating.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // goes back to main activity and destroys this activity
    private void backBtnHandler() {
        backBtn = findViewById(R.id.create_backButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}