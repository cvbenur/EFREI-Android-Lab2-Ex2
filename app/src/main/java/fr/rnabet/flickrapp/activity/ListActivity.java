package fr.rnabet.flickrapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import fr.rnabet.flickrapp.adapter.MyAdapter;
import fr.rnabet.flickrapp.R;
import fr.rnabet.flickrapp.async.AsyncFlickrJSONDataForList;

public class ListActivity extends AppCompatActivity {

    private MyAdapter _adapter = new MyAdapter(this);
    private ListView _listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        this._listView = (ListView) findViewById(R.id.list_list_view);
        _listView.setAdapter(this._adapter);


        AsyncFlickrJSONDataForList listLoader = new AsyncFlickrJSONDataForList(this._adapter);
        listLoader.execute(MainActivity.getUrl());
    }
}