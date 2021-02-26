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

        this._imgView = (ImageView) findViewById(R.id.image);
        this._coord = (TextView) findViewById(R.id.list_coord);



        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    1
            );


            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        Location localisation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        this.lon = localisation.getLongitude();
        this.lat = localisation.getLatitude();
        String locStr = String.format("Lat. : %s\nLong. : %s", this.lat, this.lon);


        Log.i("NABET", locStr);
        this._coord.setText(locStr);
    }


    public void getImageOnClickListener (View view) throws InterruptedException, ExecutionException, JSONException {
        AsyncFlickrJSONData json = new AsyncFlickrJSONData();
        json.execute(this._url);
        JSONObject response = json.get();

        String imgUrl = response
                .getJSONArray("items")
                .getJSONObject(0)
                .getJSONObject("media")
                .getString("m");

        Log.i("NABET", imgUrl);

        AsyncBitmapDownloader bitmap = new AsyncBitmapDownloader();
        bitmap.execute(imgUrl);
        Bitmap img = bitmap.get();

        this._imgView.setImageBitmap(img);
    }

    public void getLocalizedImage (View view) throws JSONException, ExecutionException, InterruptedException {

        String newQueryUrl = "https://api.flickr.com/services/rest/?" +
                "method=flickr.photos.search" +
                "&license=4" +
                "&api_key=" + API_KEY +
                "&has_geo=1&lat=" + this.lat +
                "&lon=" + this.lon + "&per_page=1&format=json";


        AsyncFlickrJSONData json = new AsyncFlickrJSONData();
        json.execute(newQueryUrl);
        JSONObject response = json.get();


        JSONObject imgJson = (JSONObject) response.getJSONObject("photos").getJSONArray("photo").get(0);
        String id = imgJson.getString("id");
        String secret = imgJson.getString("secret");
        String server = imgJson.getString("server");


        String imgUrl = String.format("https://live.staticflickr.com/%s/%s_%s.jpg", server, id, secret);


        AsyncBitmapDownloader bitmap = new AsyncBitmapDownloader();
        bitmap.execute(imgUrl);
        Bitmap img = bitmap.get();

        this._imgView.setImageBitmap(img);
    }

    public void goToList (View view) {
        Intent i = new Intent(this, ListActivity.class);
        startActivity(i);
    }
}