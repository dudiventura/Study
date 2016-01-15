package com.kepitapp.homex3;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.zip.Inflater;

/**
 * Created by Dudi on 14/1/2016.
 */
public class WeatherAdapter extends BaseAdapter {

    private Context context;
    private LinkedList<WeatherItem> weatherItemsList;

    public WeatherAdapter(Context context, int resource, LinkedList<WeatherItem> list)
    {
        this.context = context;
        weatherItemsList = list;
    }

    @Override
    public int getCount() {
        return weatherItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Connect the adapter to the item_weather layout.
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.item_weather, parent, false);

        TextView txtDate, txtTime, txtDescription, txtTemperature;
        ImageView imgWeather;

        txtDate = (TextView)row.findViewById(R.id.txtDate);
        txtTime = (TextView)row.findViewById(R.id.txtTime);
        txtTemperature = (TextView)row.findViewById(R.id.txtTemperature);
        txtDescription = (TextView)row.findViewById(R.id.txtDescription);
        imgWeather = (ImageView)row.findViewById(R.id.imgWeather);

        // Change the date, time, temperature, description and image of a weather item consider on the position of it on the list view.
        txtDate.setText(weatherItemsList.get(position).getDate());
        txtTime.setText(weatherItemsList.get(position).getTime());
        txtDescription.setText(weatherItemsList.get(position).getDescription());
        txtTemperature.setText(weatherItemsList.get(position).getTemperature());
        Picasso.with(this.context).load("http://openweathermap.org/img/w/"+weatherItemsList.get(position).getIcon()+".png").into(imgWeather);

        return row;
    }
}
