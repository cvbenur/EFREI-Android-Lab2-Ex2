package fr.rnabet.flickrapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.BreakIterator;
import java.util.concurrent.ExecutionException;

import fr.rnabet.flickrapp.R;
import fr.rnabet.flickrapp.async.AsyncBitmapDownloader;
import fr.rnabet.flickrapp.async.AsyncFlickrJSONData;

public class MainActivity extends AppCompatActivity {


    // Attributes
    private ImageView _imgView;
    private TextView _coord;

    private Double lat = null;
    private Double lon = null;

    private static final String _url = "https://www.flickr.com/services/feeds/photos_public.gne?tags=trees&format=json";
    private final String API_KEY = "e69ac742344c7aea5ac8c126b8cdc72f";


    public static String getUrl() {
        return _url;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Retrieve item references from Activity
        this._imgView = (ImageView) findViewById(R.id.image);
        this._coord = (TextView) findViewById(R.id.list_coord);


        // Setup and use device Location
        this.useLocation();
    }


    // Location services handler
    private void useLocation () {

        // Setting up LocationManager
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        // 1st permission check
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // If permissions aren't granted, ask user
            ActivityCompat.requestPermissions(this,
                    new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    1
            );


            // 2nd permission check
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // If permissions still aren't granted, exit scope
                return;
            }
        }

        // Get last known location from device
        Location localisation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        // Update Latitude and Longitude in class
        this.lon = localisation.getLongitude();
        this.lat = localisation.getLatitude();
        String locStr = String.format("Lat. : %s\nLong. : %s", this.lat, this.lon);


        Log.i("NABET", locStr);


        // Set TextView to retrieved location data (default: "Pennding coordinates...")
        this._coord.setText(locStr);
    }


    // Click handler for "Get image" button
    public void getImageOnClickListener (View view) throws InterruptedException, ExecutionException, JSONException {

        // Launch AsyncTask to query Flickr API with URL as param
        AsyncFlickrJSONData json = new AsyncFlickrJSONData();
        json.execute(this._url);
        JSONObject response = json.get();   // Retrieve response as JSON Object

        // Build image URL from response
        String imgUrl = response
                .getJSONArray("items")
                .getJSONObject(0)
                .getJSONObject("media")
                .getString("m");

        Log.i("NABET", imgUrl);

        // Launch AsyncTask to donwload image as bitmap (from previously built URL)
        AsyncBitmapDownloader bitmap = new AsyncBitmapDownloader();
        bitmap.execute(imgUrl);
        Bitmap img = bitmap.get();  // Retrieve response as Bitmap

        // Set ImageView to the retrieved Bitmap image
        this._imgView.setImageBitmap(img);
    }


    // Click handler for "Get localized image" button
    public void getLocalizedImage (View view) throws JSONException, ExecutionException, InterruptedException {

        // Build query URL from API key and previously retrieved Latitude and Longitude
        String newQueryUrl = "https://api.flickr.com/services/rest/?" +
                "method=flickr.photos.search" +
                "&license=4" +
                "&api_key=" + API_KEY +
                "&has_geo=1&lat=" + this.lat +
                "&lon=" + this.lon + "&per_page=1&format=json";

        // Launch AsyncTask to query Flickr API with previosly built query URL
        AsyncFlickrJSONData json = new AsyncFlickrJSONData();
        json.execute(newQueryUrl);
        JSONObject response = json.get();   // Retrieve response as JSON Object


        // Desctructirung retrieved response to build new query string
        JSONObject imgJson = (JSONObject) response.getJSONObject("photos").getJSONArray("photo").get(0);
        String id = imgJson.getString("id");
        String secret = imgJson.getString("secret");
        String server = imgJson.getString("server");


        // Building new query string to actually download image
        String imgUrl = String.format("https://live.staticflickr.com/%s/%s_%s.jpg", server, id, secret);


        // Launch new AsyncTask to actually download the image as Bitmap
        AsyncBitmapDownloader bitmap = new AsyncBitmapDownloader();
        bitmap.execute(imgUrl);
        Bitmap img = bitmap.get();  // Retrieve response as Bitmap image

        // Set ImageView to retrieved Bitmap image
        this._imgView.setImageBitmap(img);
    }


    // Click handler for "Go to list" button
    public void goToList (View view) {
        Intent i = new Intent(this, ListActivity.class);
        startActivity(i);   // Launch ListActivity
    }
}