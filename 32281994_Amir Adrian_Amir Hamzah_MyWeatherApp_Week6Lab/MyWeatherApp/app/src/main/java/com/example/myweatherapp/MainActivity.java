package com.example.myweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    String URL = "https://api.weatherapi.com/v1/current.json?key=cde75afa40ce4179b2781925223003&q=";
    String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    public void search(View view){
        EditText location_in = findViewById(R.id.searchInput);
        location = location_in.getText().toString();
        if(location.equals(""))
        {
            Toast.makeText(this,"Give Input!",Toast.LENGTH_SHORT).show();
        }
        else
        {
        RequestQueue queue = Volley.newRequestQueue(this);
        String req_url = URL + location;
        try{
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, req_url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        TextView temperature, humidity, precipitation, wind_speed;
                        String temp = response.getJSONObject("current").getString("temp_c");
                        String humid= response.getJSONObject("current").getString("humidity");
                        String precip= response.getJSONObject("current").getString("precip_mm");
                        String wind= response.getJSONObject("current").getString("wind_kph");

                        temperature = findViewById(R.id.temperature);
                        humidity = findViewById(R.id.humidity);
                        precipitation = findViewById(R.id.precipitaiton);
                        wind_speed = findViewById(R.id.wind);

                        temperature.setText(temp);
                        humidity.setText(humid);
                        precipitation.setText(precip);
                        wind_speed.setText(wind);

                    }catch(Exception e){ Log.d("weather", e.getMessage());}
                }
            },(error) -> {Log.d("weather", error.getMessage()); });

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);

        }catch(Exception e){ Toast.makeText(this,"Bad Input",Toast.LENGTH_SHORT).show();}
        }
    }
}