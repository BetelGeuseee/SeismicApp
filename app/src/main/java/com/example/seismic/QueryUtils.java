package com.example.seismic;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class QueryUtils {
    /** Sample JSON response for a USGS query */
    private static final String SAMPLE_JSON_RESPONSE="";
    private static final String LOG_TAG="hello";
    private QueryUtils() {
    }
  private static URL createUrl(String $url){
        URL url=null;
        try{
            url = new URL($url);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return url;
  }
   private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse="";
        if(url==null){
            return jsonResponse;
        }
       HttpURLConnection httpURLConnection=null;
       InputStream inputStream=null;
       try{
           httpURLConnection=(HttpURLConnection)url.openConnection();
           httpURLConnection.setReadTimeout(10000); //miliseconds
           httpURLConnection.setConnectTimeout(15000);
           httpURLConnection.setRequestMethod("GET");
           httpURLConnection.connect();

           // If the request was successful (response code 200),
           // then read the input stream and parse the response.
           if(httpURLConnection.getResponseCode()==200){
               inputStream=httpURLConnection.getInputStream();
               jsonResponse=readFromStream(inputStream);
           }else{
               Log.e(LOG_TAG, "Error response code: " + httpURLConnection.getResponseCode());
           }
       }catch(IOException e){
           Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
       }finally{
           if(httpURLConnection!=null){
               httpURLConnection.disconnect();
           }
           if(inputStream!=null){
               inputStream.close();
           }
       }
       return jsonResponse;
   }
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    public static List<EarthquakeData> extractEarthquakes(String earthquakeJSON) {
        if(TextUtils.isEmpty(earthquakeJSON)){
            return null;
        }
        // Create an empty ArrayList that we can start adding earthquakes to
       // ArrayList<EarthquakeData> earthquakes = new ArrayList<>();
        // Create an empty ArrayList that we can start adding earthquakes to
        List<EarthquakeData> earthquakes = new ArrayList<>();
        EarthquakeData earthquakeData=null;
        try {
         JSONObject root = new JSONObject(earthquakeJSON);
         JSONArray dataArray = root.getJSONArray("features");
        for(int i=0;i<dataArray.length();i++) {
            // Get a single earthquake at position i within the list of earthquakes
             JSONObject obj=dataArray.getJSONObject(i);
            // For a given earthquake, extract the JSONObject associated with the
            // key called "properties", which represents a list of all properties
            // for that earthquake.
             JSONObject obj2=obj.getJSONObject("properties");
             double $magnitude=obj2.getDouble("mag");
             String $place=obj2.getString("place");
             Long $time=obj2.getLong("time");
             String url=obj2.getString("url");
             if($place.contains("of")){
                 String[] arr=$place.split("of");
                String offSetLoc = arr[0]+"of";
                 String primeLoc=arr[1];
                 earthquakeData=new EarthquakeData($magnitude,offSetLoc,primeLoc,$time,url);
                 earthquakes.add(earthquakeData);
             }else{
                 String offSetLoc2="Near the ";
                 String primeLoc2=$place;
                 earthquakeData=new EarthquakeData($magnitude,offSetLoc2,primeLoc2,$time,url);
                 earthquakes.add(earthquakeData);
             }

         }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return earthquakes;
    }
  public static List<EarthquakeData> fetchEarthquakeData(String requestUrl){
        URL url=createUrl(requestUrl);
      // Perform HTTP request to the URL and receive a JSON response back
      String jsonResponse = null;
      try{
          jsonResponse=makeHttpRequest(url);
      }catch (IOException e){
          Log.e(LOG_TAG, "Problem making the HTTP request.", e);
      }
      List<EarthquakeData> earthquakes = extractEarthquakes(jsonResponse);

      return earthquakes;
  }
}
