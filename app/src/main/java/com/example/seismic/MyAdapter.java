package com.example.seismic;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.graphics.drawable.GradientDrawable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyAdapter extends ArrayAdapter<EarthquakeData> {

    public MyAdapter(Activity context, List<EarthquakeData> arrayList){
        super(context,0,arrayList);
    }
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }
    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        //You can call ContextCompat getColor() to convert the color resource ID into an actual integer color value, and return the result as the return value of the getMagnitudeColor() helper method.
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView =convertView;
        //check if the existing view is reused,otherwise inflate the view
        if(listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.my_list_layout,parent,false);
        }
        //get the EarthquakeData object located at this position in the list
        EarthquakeData eData=getItem(position);

        TextView $magView = (TextView)listItemView.findViewById(R.id.magnitudeView);
        String hehe=Double.toString(eData.getMagnitude());
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable)$magView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(eData.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        $magView.setText(hehe);
        TextView $offsetView=(TextView)listItemView.findViewById(R.id.offSetView);
        $offsetView.setText(eData.getLocationOffSet());
        TextView $placeView=(TextView)listItemView.findViewById(R.id.placeView);
        $placeView.setText(eData.getPrimaryLocation());
        TextView $dateView =(TextView)listItemView.findViewById(R.id.dateView);
        Date date = new Date(eData.getDate());
        String dateData=formatDate(date);
        $dateView.setText(dateData);
        TextView $timeView = (TextView)listItemView.findViewById(R.id.timeViewer);
        String timeData=formatTime(date);
        $timeView.setText(timeData);
        return listItemView;
    }

}
