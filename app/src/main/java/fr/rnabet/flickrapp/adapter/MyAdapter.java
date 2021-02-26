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

    private Activity _context;
    private Vector<String> _vector;


    public MyAdapter (Activity context) {
        this._vector = new Vector<>();
        this._context = context;
    }


    public void add (String url) {
        this._vector.add(url);
    }


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

        RequestQueue queue = Singleton.getInstance(parent.getContext()).getRequestQueue();

        if (convertView == null) {
            //  convertView = this._context
            //        .getLayoutInflater()
            //        .inflate(R.layout.text_view_layout, parent, false);

            convertView = this._context
                    .getLayoutInflater()
                    .inflate(R.layout.bitmap_layout, parent, false);
        }

        // TextView imgUrl = (TextView) convertView.findViewById(R.id.list_text_item);
        // imgUrl.setText(this._vector.get(position));

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        ImageRequest imageRequest = new ImageRequest(
                this._vector.get(position),
                response -> imageView.setImageBitmap(response),
                0, 0,
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.RGB_565,
                error -> error.printStackTrace()
        );


        queue.add(imageRequest);


        return convertView;
    }
}
