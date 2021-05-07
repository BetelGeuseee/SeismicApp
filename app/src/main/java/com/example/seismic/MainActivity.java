package com.example.seismic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private MyAdapter myAdapter;
    private ProgressBar spinner;
    private final  static String USGS_REQUEST_URL="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=3&limit=10";
    private class EarthquakeAsyncTask extends AsyncTask<String,View, List<EarthquakeData>>{
        @Override
        protected List<EarthquakeData> doInBackground(String... urls) {
            /*
             * This method runs on a background thread and performs the network request.
             * We should not update the UI from a background thread, so we return a list of
             * {@link Earthquake}s as the result.
             */
           if(urls.length<1 || urls[0]==null){
               return null;
           }
           List<EarthquakeData> result =QueryUtils.fetchEarthquakeData(urls[0]);
           return result;
        }
        /*
         * This method runs on the main UI thread after the background work has been
         * completed. This method receives as input, the return value from the doInBackground()
         * method. First we clear out the adapter, to get rid of earthquake data from a previous
         * query to USGS. Then we update the adapter with the new list of earthquakes,
         * which will trigger the ListView to re-populate its list items.
         */
        @Override
        protected void onPostExecute(List<EarthquakeData> earthquakeData) {
            //clear all the previous data from the adapter
            myAdapter.clear();
            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
                // data set. This will trigger the ListView to update.
                if (earthquakeData!= null && !earthquakeData.isEmpty()) {
                myAdapter.addAll(earthquakeData);
            }
            spinner.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(View... values) {
            spinner.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.listerr);
        spinner=(ProgressBar)findViewById(R.id.progressBar1);
       myAdapter = new MyAdapter(this,new ArrayList<EarthquakeData>());
        listView.setAdapter(myAdapter);
        EarthquakeAsyncTask asyncTask = new EarthquakeAsyncTask();
        asyncTask.execute(USGS_REQUEST_URL);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthquakeData currentEarthquake = myAdapter.getItem(position);
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrls());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,earthquakeUri);
                startActivity(websiteIntent);
            }
        });
    }
}