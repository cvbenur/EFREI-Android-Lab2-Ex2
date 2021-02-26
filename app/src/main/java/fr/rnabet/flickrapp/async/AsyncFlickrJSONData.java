package fr.rnabet.flickrapp.async;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class AsyncFlickrJSONData extends AsyncTask<String, Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(String... strings) {
        URL url = null;
        JSONObject json = new JSONObject();


        try {
            url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String s = readStream(in);
                Log.i("NABET", s);

                // Retrieve decoded JSON Object (remove unnecessary Strings from response)
                json = new JSONObject(s.subSequence(s.startsWith("jsonFlickrApi") ? 14 : 15, s.length()-1).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }


    // Decode input stream as String
    private String readStream(InputStream in) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = in.read();
            while(i != -1) {
                bo.write(i);
                i = in.read();
            }

            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }


    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

        // If dealing with API (as opposed to Feed) return to avoid crash
        if (jsonObject.toString().startsWith("{\"photos\"")) return;

        // Retrieve imageUrl from destructured JSONObject provided in response
        String imageUrl = null;
        try {
            imageUrl = jsonObject.getJSONArray("items")
                    .getJSONObject(0)
                    .getJSONObject("media")
                    .getString("m");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("NABET", imageUrl);
    }
}
