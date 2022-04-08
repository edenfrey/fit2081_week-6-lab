package com.example.mymovieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MovieDatabase extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<MovieClass> appMovieDB = new ArrayList<MovieClass>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_database_coordinator_layout);

        // Making FAB and Snackbar
        FloatingActionButton appFAB = findViewById(R.id.secondFloatingActionButton);
        appFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.movieDBRecyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        SharedPreferences myData = getSharedPreferences("savedData", 0);
        String dbStr = myData.getString("MOVIE_DB", "[]");
        Type type = new TypeToken<ArrayList<MovieClass>>() {}.getType();
        Gson gson = new Gson();
        appMovieDB = gson.fromJson(dbStr,type);

        adapter = new RecyclerViewAdapter(appMovieDB);
        recyclerView.setAdapter(adapter);

    }
}