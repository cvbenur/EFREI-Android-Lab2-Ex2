package fr.rnabet.flickrapp.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;

import java.util.Vector;

import fr.rnabet.flickrapp.R;
import fr.rnabet.flickrapp.singleton.Singleton;

public class MyAdapter extends BaseAdapter {

    // Attributes
    private Activity _context;
    private Vector<String> _vector;


    // Ctor
    public MyAdapter (Activity context) {
        this._vector = new Vector<>();
        this._context = context;
    }


    // Add a String to the Vector
    public void add (String url) {
        this._vector.add(url);
    }


    // Class methods to implement
    @Override
    public int getCount() {
        return this._vector.size();
    }

    @Override
    public Object getItem(int position) {
        return this._vector.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Getting RequestQueue singleton instance
        RequestQueue queue = Singleton.getInstance(parent.getContext()).getRequestQueue();

        // Checking if the View already exists
        if (convertView == null) {
            // If not, inflate


            //  convertView = this._context
            //        .getLayoutInflater()
            //        .inflate(R.layout.text_view_layout, parent, false);

            convertView = this._context
                    .getLayoutInflater()
                    .inflate(R.layout.bitmap_layout, parent, false);
        }

        // Retrieve TextView from Activity and set it to the URL at the current position
        // TextView imgUrl = (TextView) convertView.findViewById(R.id.list_text_item);
        // imgUrl.setText(this._vector.get(position));

        // Retrieve ImageView from Activity
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);

        // Instantiate new ImageRequest with the current URL
        ImageRequest imageRequest = new ImageRequest(
                this._vector.get(position),
                response -> imageView.setImageBitmap(response),
                0, 0,
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.RGB_565,
                error -> error.printStackTrace()
        );

        // Add created ImageRequest to the RequestQueue
        queue.add(imageRequest);

        // Return the updated View
        return convertView;
    }
}
