package fr.rnabet.flickrapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import fr.rnabet.flickrapp.R;
import fr.rnabet.flickrapp.async.AsyncBitmapDownloader;
import fr.rnabet.flickrapp.async.AsyncFlickrJSONData;

public class MainActivity extends AppCompatActivity {

    private ImageView _imgView;
    private static final String _url = "https://www.flickr.com/services/feeds/photos_public.gne?tags=trees&format=json";


    public static String getUrl() {
        return _url;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this._imgView = (ImageView) findViewById(R.id.image);
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

    public void goToList (View view) {
        Intent i = new Intent(this, ListActivity.class);
        startActivity(i);
    }
}