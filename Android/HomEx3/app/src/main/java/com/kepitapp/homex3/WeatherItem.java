package com.kepitapp.homex3;

public class WeatherItem{

    private String date, time, temperature, description, icon;

    public WeatherItem(String date, String time, String temp, String desc, String img)
    {

        this.date = date;
        this.time = time;
        this.temperature = temp;
        this.description = desc;
        this.icon = img;

    }

    // Return the weather item date.
    public String getDate()
    {
        return date;
    }

    // Return the weather item time.
    public String getTime()
    {
        return time;
    }

    // Return the weather item temperature.
    public String getTemperature()
    {
        return temperature;
    }

    // Return the weather item description.
    public String getDescription()
    {
        return description;
    }

    // Return the weather item icon.
    public String getIcon()
    {
        return icon;
    }
}
