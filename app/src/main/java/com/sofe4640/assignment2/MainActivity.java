package com.sofe4640.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<LocationModel> locList;
    LocationAdapter adapter;
    private ListView listView;

    SearchView searchEdit;
    Button createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchHandler();

        getData();
        configListView(locList);
        listViewOnClickListener();

        addLocation();
    }

    private void searchHandler() {
        searchEdit = findViewById(R.id.searchSearchView);
        searchEdit.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<LocationModel> filteredList;
                DAO dbHandler = new DAO(MainActivity.this);

                filteredList = dbHandler.searchAddress(s.toLowerCase());

                configListView(filteredList);
                return false;
            }
        });
    }

    private void getData() {
        DAO dbHandler = new DAO(MainActivity.this);
        locList = dbHandler.getAll();
    }

    private void configListView(List list) {
        listView = findViewById(R.id.resultsListView);
        adapter = new LocationAdapter(MainActivity.this, 0, list);
        listView.setAdapter(adapter);
    }

    private void listViewOnClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocationModel selectedLoc = (LocationModel) listView.getItemAtPosition(position);
                Intent detailActivity = new Intent(MainActivity.this, LocationActivity.class);

                detailActivity.putExtra("id", selectedLoc.getId());
                detailActivity.putExtra("address", selectedLoc.getAddress());
                detailActivity.putExtra("longitude", selectedLoc.getLongitude());
                detailActivity.putExtra("latitude", selectedLoc.getLatitude());

                startActivity(detailActivity);
                finish();
            }
        });
    }

    private void addLocation() {
        createBtn = findViewById(R.id.createButton);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateActivity.class));
                finish();
            }
        });
    }
}