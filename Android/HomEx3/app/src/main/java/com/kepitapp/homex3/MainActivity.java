package com.kepitapp.homex3;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class MainActivity extends ActionBarActivity implements ListView.OnItemSelectedListener {

    private Spinner spinner; // App cities spinner
    private ListView weatherList; // App weather list view
    private RequestQueue queue; // JSON requests queue
    private Dialog dialog; // Progress dialog.
    private HashMap<String,Integer> cityID; // Hash table, contain the cities possible names from the spinner and their ids from the website.
    private LinkedList<WeatherItem> weatherViewsList; // List of weather items for the adapter.
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialize
        this.weatherList = (ListView)findViewById(R.id.weatherList);
        spinner = (Spinner)findViewById(R.id.locationSpinner);

        //Creating cities hash tables with city name and city id
        cityID = new HashMap<>();

        weatherViewsList = new LinkedList<WeatherItem>();
        context = this;

        // Initialize the JSON requests queue and progress dialog.
        queue = Volley.newRequestQueue(this);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        // Add cities to the spinner adapter.
        ArrayList<String> cities = addingCities();

        // Fill the spinner with selected cities.
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, cities);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        weatherViewsList.removeAll(weatherViewsList);
        dialog.show();
        // Get weather info consider on city id.
        String url = "http://api.openweathermap.org/data/2.5/forecast?id="+ cityID.get(spinner.getSelectedItem().toString()) + "&units=metric&appid=2de143494c0b295cca9337e1e96b00e0";
        JsonObjectRequest request =
                new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jsonWeatherList = response.getJSONArray("list");
                                    for(int i = 0; i< jsonWeatherList.length(); i++)
                                    {
                                        // Getting the details JSON object for each hour.
                                        JSONObject detailsObj = jsonWeatherList.getJSONObject(i);

                                        // Getting date + hour
                                        String dateTime = detailsObj.getString("dt_txt");

                                        // Getting the main JSON object for Temperature.
                                        JSONObject mainObj = detailsObj.getJSONObject("main");
                                        double temperature = mainObj.getDouble("temp");

                                        // Getting the weather JSON array and object for description and icon.
                                        JSONArray weatherArray = detailsObj.getJSONArray("weather");
                                        JSONObject weatherObj = weatherArray.getJSONObject(0);
                                        String description = weatherObj.getString("description");
                                        String icon = weatherObj.getString("icon");

                                         // Splitting the date+time to separate strings.
                                        String[] separatedDateTime = dateTime.split(" ");

                                        // Round the temperature.
                                        int temp = (int)Math.round(temperature);
                                        String temperatureS = ""+temp+"c";

                                        // Change Date and Time strings to custom strings.
                                        separatedDateTime = customDateTime(separatedDateTime);

                                        // Create weather view item.
                                        WeatherItem weather_item = new WeatherItem(separatedDateTime[0], separatedDateTime[1], temperatureS, description, icon);

                                        // Add the weather view item to a list of weather items.
                                        weatherViewsList.add(weather_item);

                                    }
                                    // Close the dialog
                                    dialog.cancel();
                                    // Going to the adapter only if there are weather  items to put inside the list view.
                                    if(weatherViewsList != null) {
                                        weatherList.setAdapter(new WeatherAdapter(context, R.layout.item_weather, weatherViewsList));
                                    }
                                } catch (JSONException e) {
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_LONG).show();
                    }
                });

        queue.add(request);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Change date format to dd/mm/yyyy and hour format to hh:mm
    private String[] customDateTime(String[] original)
    {
        String[] custom = new String[2];
        //Custom Date - format dd/mm/yyyy
        String[] separateOriginalDate = original[0].split("-");
        custom[0] = separateOriginalDate[2] + "/" + separateOriginalDate[1] + "/" + separateOriginalDate[0];

        //Custom Time - format hh:mm
        String[] separateOriginalTime = original[1].split(":");
        custom[1] = separateOriginalTime[0] + ":" + separateOriginalTime[1];

        return custom;
    }

    // Adding cities to the spinner adapter and to the hash table.
    private ArrayList<String> addingCities()
    {
        ArrayList<String> cities = new ArrayList<String>();
        //cities.add("Current Location");
        cities.add("Jerusalem");
        cityID.put("Jerusalem",281184);
        cities.add("Haifa");
        cityID.put("Haifa", 294801);
        cities.add("Tel-Aviv");
        cityID.put("Tel-Aviv",293397);
        cities.add("Eilat");
        cityID.put("Eilat", 295277);
        cities.add("Ashdod");
        cityID.put("Ashdod",295629);
        cities.add("Metulla");
        cityID.put("Metulla",294247);
        cities.add("Ariel");
        cityID.put("Ariel", 8199394);
        cities.add("Rehovot");
        cityID.put("Rehovot",8199394);
        cities.add("Netanya");
        cityID.put("Netanya", 294071);
        cities.add("Modiin");
        cityID.put("Modiin",6693679);

        return cities;
    }
}
