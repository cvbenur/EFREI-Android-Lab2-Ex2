package fr.rnabet.flickrapp.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

import fr.rnabet.flickrapp.R;

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

        if (convertView == null) {
            convertView = this._context.getLayoutInflater()
                    .inflate(R.layout.text_view_layout, parent, false);
        }


        TextView imgUrl = (TextView) convertView.findViewById(R.id.list_text_item);
        imgUrl.setText(this._vector.get(position));


        return convertView;
    }
}
