package com.example.seismic;

public class EarthquakeData{
    private double magnitude;
    private String locationOffSet;
    private String primaryLocation;
    private long date;
    private String urls;
    public EarthquakeData(double mag,String offset,String primary,Long $date,String url){
        this.magnitude=mag;
        this.locationOffSet=offset;
        this.primaryLocation=primary;
        this.date=$date;
        this.urls=url;
    }
    public void setUrls(String $urls){
        this.urls=$urls;
    }
    public String getUrls(){
        return  urls;
    }
    public void setMagnitude(double mag){
        this.magnitude=mag;
    }
    public double getMagnitude() {
        return magnitude;
    }
    public void setlocationOffSet(String $ofset){
        this.locationOffSet=$ofset;
    }
    public String getLocationOffSet() {
        return locationOffSet;
    }
    public void setPrimaryLocation(String $prime){
        this.primaryLocation=$prime;
    }
    public String getPrimaryLocation(){
        return primaryLocation;
    }
    public void getDate(long $date){
        this.date=$date;
    }
    public long getDate() {
        return date;
    }
}
