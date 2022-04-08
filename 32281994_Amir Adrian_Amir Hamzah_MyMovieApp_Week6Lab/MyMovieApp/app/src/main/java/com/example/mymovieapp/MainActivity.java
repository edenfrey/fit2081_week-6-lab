package com.example.mymovieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    // Create ArrayList for storing movies
    ArrayAdapter<String> myListAdapter;
    ArrayList<String> appMovieList = new ArrayList<String>();
    ArrayList<MovieClass> appMovieDB = new ArrayList<MovieClass>();
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        // Setup Toolbar
        Toolbar appToolbar = findViewById(R.id.appToolbar);
        setSupportActionBar(appToolbar);

        //Setup Listview
        ListView myList;
        myList = findViewById(R.id.movieListview);
        myListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,appMovieList);
        myList.setAdapter(myListAdapter);

        // Creating toggle button for drawer
        drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, appToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Initializing SMS listener
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS,Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS},0);
        IntentFilter myIntentFilter = new IntentFilter("movieSmsBroadcast");
        registerReceiver(smsReceiver,myIntentFilter);

        // Making FAB and Snackbar
        FloatingActionButton appFAB = findViewById(R.id.mainFloatingActionButton);
        appFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkCanAdd(view) == true){
                    EditText movieTitle, movieYear;
                    movieTitle = findViewById(R.id.titleInput);
                    movieYear = findViewById(R.id.yearInput);

                    // Add movie
                    addMovie(view);


                    // Snackbar Confirmation
                    Snackbar.make(view,"Movie added! (" + movieTitle.getText().toString() + ", " + movieYear.getText().toString() + ")",Snackbar.LENGTH_LONG).setAction("UNDO",snackbarUndo).show();

                }else{
                    Snackbar.make(view, "Failed! Give all info!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        // Default add movie button
        Button appAddMovie = findViewById(R.id.addMovie);
        appAddMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkCanAdd(view)){
                    EditText movieTitle, movieYear;
                    movieTitle = findViewById(R.id.titleInput);
                    movieYear = findViewById(R.id.yearInput);

                    // Add movie
                    addMovie(view);

                    Toast.makeText(getApplicationContext(),"Movie added! (" + movieTitle.getText().toString() + ", " + movieYear.getText().toString() + ")",Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getApplicationContext(), "Failed to add! Input all fields!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Default clear fields button
        Button appClearFields = findViewById(R.id.clearFields);
        appClearFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFields(view);
            }
        });

        // Navigation drawer button listener
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationViewListener());
    }

    // Check if input is allowed
    public boolean checkCanAdd(View view){
        EditText movieTitleInput = findViewById(R.id.titleInput);
        EditText movieCostInput = findViewById(R.id.costInput);
        EditText movieYearInput = findViewById(R.id.yearInput);
        EditText movieGenreInput = findViewById(R.id.genreInput);
        EditText movieKeywordsInput = findViewById(R.id.keywordsInput);
        EditText movieCountryInput = findViewById(R.id.countryInput);

        if(movieCountryInput.getText().toString().equals("") || movieYearInput.getText().toString().equals("")|| movieTitleInput.getText().toString().equals("") || movieCostInput.getText().toString().equals("") || movieKeywordsInput.getText().toString().equals("") || movieGenreInput.getText().toString().equals("")){
            return false;
        }
        else {
            return true;
        }
    }

    public void addMovie(View view){
        EditText movieTitle, movieYear, movieCost, movieGenre, movieKeywords, movieCountry;
        movieTitle = findViewById(R.id.titleInput);
        movieYear = findViewById(R.id.yearInput);
        movieCost = findViewById(R.id.costInput);
        movieGenre = findViewById(R.id.genreInput);
        movieKeywords = findViewById(R.id.keywordsInput);
        movieCountry = findViewById(R.id.countryInput);

        MovieClass movie = new MovieClass(
                movieTitle.getText().toString(),movieYear.getText().toString(),
                movieGenre.getText().toString(),movieCost.getText().toString(),movieKeywords.getText().toString(),
                movieCountry.getText().toString());

        appMovieDB.add(movie);
        appMovieList.add(movieTitle.getText().toString() + " (" + movieYear.getText().toString()+")");
        myListAdapter.notifyDataSetChanged();

        saveInSharedPref(view);
    }

    // Method to clear all EditText fields
    public void clearFields(View view) {
        EditText title, country, genre, keywords, cost, year;
        String empty = "";
        title = findViewById(R.id.titleInput);
        country = findViewById(R.id.countryInput);
        genre = findViewById(R.id.genreInput);
        keywords = findViewById(R.id.keywordsInput);
        cost = findViewById(R.id.costInput);
        year = findViewById(R.id.yearInput);
        title.setText(empty);
        country.setText(empty);
        genre.setText(empty);
        keywords.setText(empty);
        cost.setText(empty);
        year.setText(empty);
    }

    // Method to save data into Shared Preferences
    public void saveInSharedPref(View view){
        EditText movieTitleInput = findViewById(R.id.titleInput);
        EditText movieCostInput = findViewById(R.id.costInput);
        EditText movieYearInput = findViewById(R.id.yearInput);
        EditText movieGenreInput = findViewById(R.id.genreInput);
        EditText movieKeywordsInput = findViewById(R.id.keywordsInput);
        EditText movieCountryInput = findViewById(R.id.countryInput);
        Gson gson = new Gson();
        String dbStr = gson.toJson(appMovieDB);
        String listStr = gson.toJson(appMovieList);

        SharedPreferences mySavedData = getSharedPreferences("savedData", 0);
        SharedPreferences.Editor myEditor = mySavedData.edit();
        myEditor.putString("movieTitle", movieTitleInput.getText().toString());
        myEditor.putString("movieGenre", movieGenreInput.getText().toString());
        myEditor.putString("movieYear", movieYearInput.getText().toString());
        myEditor.putString("movieCost", movieCostInput.getText().toString());
        myEditor.putString("movieKeywords", movieKeywordsInput.getText().toString());
        myEditor.putString("movieCountry", movieCountryInput.getText().toString());
        myEditor.putString("MOVIE_DB", dbStr);
        myEditor.putString("MOVIE_LST",listStr);
        myEditor.apply();
    }

    // Snackbar undo listener
    View.OnClickListener snackbarUndo = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            appMovieList.remove(appMovieList.size()-1);
            appMovieDB.remove(appMovieDB.size()-1);
            myListAdapter.notifyDataSetChanged();

            // Save into shared preferences
            saveInSharedPref(view);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        EditText movieTitleInput = findViewById(R.id.titleInput);
        EditText movieCostInput = findViewById(R.id.costInput);
        EditText movieYearInput = findViewById(R.id.yearInput);
        EditText movieGenreInput = findViewById(R.id.genreInput);
        EditText movieKeywordsInput = findViewById(R.id.keywordsInput);
        EditText movieCountryInput = findViewById(R.id.countryInput);

        SharedPreferences myData = getSharedPreferences("savedData", 0);
        movieTitleInput.setText(myData.getString("movieTitle",""));
        movieCostInput.setText(myData.getString("movieCost", ""));
        movieYearInput.setText(myData.getString("movieYear", ""));
        movieGenreInput.setText(myData.getString("movieGenre", ""));
        movieKeywordsInput.setText(myData.getString("movieKeywords", ""));
        movieCountryInput.setText(myData.getString("movieCountry", ""));

        Log.i("checker","onStart");
    }

    // Creating navigation drawer listener class
    class NavigationViewListener implements NavigationView.OnNavigationItemSelectedListener{
        public boolean onNavigationItemSelected(@NonNull MenuItem item){
            int id = item.getItemId();
            if (id == R.id.addMovieDrawer){
                EditText movieTitleInput = findViewById(R.id.titleInput);
                EditText movieCostInput = findViewById(R.id.costInput);
                EditText movieYearInput = findViewById(R.id.yearInput);
                EditText movieGenreInput = findViewById(R.id.genreInput);
                EditText movieKeywordsInput = findViewById(R.id.keywordsInput);
                EditText movieCountryInput = findViewById(R.id.countryInput);
                if(movieCountryInput.getText().toString().equals("") || movieYearInput.getText().toString().equals("")|| movieTitleInput.getText().toString().equals("") || movieCostInput.getText().toString().equals("") || movieKeywordsInput.getText().toString().equals("") || movieGenreInput.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Failed! Give all info!",Toast.LENGTH_SHORT).show();
                }
                else {
                    movieTitleInput = findViewById(R.id.titleInput);
                    movieYearInput = findViewById(R.id.yearInput);
                    movieCostInput = findViewById(R.id.costInput);
                    movieGenreInput = findViewById(R.id.genreInput);
                    movieKeywordsInput = findViewById(R.id.keywordsInput);
                    movieCountryInput = findViewById(R.id.countryInput);

                    MovieClass movie = new MovieClass(
                            movieTitleInput.getText().toString(),movieYearInput.getText().toString(),
                            movieGenreInput.getText().toString(),movieCostInput.getText().toString(),movieKeywordsInput.getText().toString(),
                            movieCountryInput.getText().toString());

                    appMovieDB.add(movie);
                    appMovieList.add(movieTitleInput.getText().toString() + " (" + movieYearInput.getText().toString()+")");
                    myListAdapter.notifyDataSetChanged();

                    Gson gson = new Gson();
                    String dbStr = gson.toJson(appMovieDB);
                    String listStr = gson.toJson(appMovieList);

                    SharedPreferences mySavedData = getSharedPreferences("savedData", 0);
                    SharedPreferences.Editor myEditor = mySavedData.edit();
                    myEditor.putString("movieTitle", movieTitleInput.getText().toString());
                    myEditor.putString("movieGenre", movieGenreInput.getText().toString());
                    myEditor.putString("movieYear", movieYearInput.getText().toString());
                    myEditor.putString("movieCost", movieCostInput.getText().toString());
                    myEditor.putString("movieKeywords", movieKeywordsInput.getText().toString());
                    myEditor.putString("movieCountry", movieCountryInput.getText().toString());
                    myEditor.putString("MOVIE_DB", dbStr);
                    myEditor.putString("MOVIE_LST",listStr);
                    myEditor.apply();
                    // Toast Confirmation
                    Toast.makeText(getApplicationContext(),"Movie added! (" + movieTitleInput.getText().toString() + ", " + movieYearInput.getText().toString() + ")",Toast.LENGTH_LONG).show();
                }
            }
            else if(id == R.id.removeMovieDrawer){
                if(!(appMovieList.size()==0)){
                    appMovieList.remove(appMovieList.size()-1);
                    appMovieDB.remove(appMovieDB.size()-1);
                    myListAdapter.notifyDataSetChanged();

                    // Save into shared preferences
                    SharedPreferences mySavedData = getSharedPreferences("savedData", 0);
                    SharedPreferences.Editor myEditor = mySavedData.edit();
                    Gson gson = new Gson();
                    String dbStr = gson.toJson(appMovieDB);
                    String listStr = gson.toJson(appMovieList);
                    myEditor.putString("MOVIE_DB", dbStr);
                    myEditor.putString("MOVIE_LST",listStr);
                    myEditor.apply();
                }else{
                    Toast.makeText(getApplicationContext(),"No Movie In List",Toast.LENGTH_SHORT).show();
                }
            }
            else if(id == R.id.removeAllMovieDrawer){
                appMovieList.clear();
                appMovieDB.clear();
                myListAdapter.notifyDataSetChanged();

                // Save into shared preferences
                SharedPreferences mySavedData = getSharedPreferences("savedData", 0);
                SharedPreferences.Editor myEditor = mySavedData.edit();
                Gson gson = new Gson();
                String dbStr = gson.toJson(appMovieDB);
                String listStr = gson.toJson(appMovieList);
                myEditor.putString("MOVIE_DB", dbStr);
                myEditor.putString("MOVIE_LST",listStr);
                myEditor.apply();

                Toast.makeText(getApplicationContext(),"All Movies Removed",Toast.LENGTH_LONG).show();

            }else if(id == R.id.closeAppDrawer){
                finish();
            }else if (id == R.id.listAllMovies){
                goToDatabase();
            }

            drawer.closeDrawers();

            return true;
        };
    }

    // Creating click listeners for options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.clearFieldsOption){
            EditText title, country, genre, keywords, cost, year;
            String empty = "";
            title = findViewById(R.id.titleInput);
            country = findViewById(R.id.countryInput);
            genre = findViewById(R.id.genreInput);
            keywords = findViewById(R.id.keywordsInput);
            cost = findViewById(R.id.costInput);
            year = findViewById(R.id.yearInput);
            title.setText(empty);
            country.setText(empty);
            genre.setText(empty);
            keywords.setText(empty);
            cost.setText(empty);
            year.setText(empty);
            Toast.makeText(getApplicationContext(),"Fields Cleared", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.totalMoviesOption){
            Toast.makeText(this,"Total Movies Added: "+appMovieList.size(),Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    // Move to Database Activity
    public void goToDatabase(){
        Intent i = new Intent(this, MovieDatabase.class);
        startActivity(i);
    }



































    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        EditText movieGenreInput = findViewById(R.id.genreInput);
        movieGenreInput.setText(movieGenreInput.getText().toString().toLowerCase());
        super.onSaveInstanceState(outState);
        Log.i("checker","SaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        EditText movieTitleInput = findViewById(R.id.titleInput);
        movieTitleInput.setText(movieTitleInput.getText().toString().toUpperCase());
        Log.i("checker","RestoreInstanceState");
    }


    // SMS listener for movie information
    BroadcastReceiver smsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("msgKey");
            String[] messageArray = message.split(";");
            EditText movieTitleInput = findViewById(R.id.titleInput);
            EditText movieCostInput = findViewById(R.id.costInput);
            EditText movieYearInput = findViewById(R.id.yearInput);
            EditText movieGenreInput = findViewById(R.id.genreInput);
            EditText movieKeywordsInput = findViewById(R.id.keywordsInput);
            EditText movieCountryInput = findViewById(R.id.countryInput);
            if(messageArray.length == 6){
                movieTitleInput.setText(messageArray[0]);
                movieYearInput.setText(messageArray[1]);
                movieCountryInput.setText(messageArray[2]);
                movieGenreInput.setText(messageArray[3]);
                movieCostInput.setText(messageArray[4]);
                movieKeywordsInput.setText(messageArray[5]);
            }
            else if(messageArray.length == 7){
                movieTitleInput.setText(messageArray[0]);
                movieYearInput.setText(messageArray[1]);
                movieCountryInput.setText(messageArray[2]);
                movieGenreInput.setText(messageArray[3]);
                movieCostInput.setText((Double.parseDouble(messageArray[4])+Double.parseDouble(messageArray[6]))+"");
                movieKeywordsInput.setText(messageArray[5]);
            }}};
}