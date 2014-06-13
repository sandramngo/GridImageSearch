package com.codepath.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
    EditText etQuery;
    Button btnSearch;
    GridView gvResults;
    ArrayList<ImageResult> imageResults = new ArrayList<>();
    ImageResultArrayAdapter imgResultArrayAdapter;
    FilterSettings filters = new FilterSettings();
    
    int curOffset = 0;
    int requestSize = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        imgResultArrayAdapter = new ImageResultArrayAdapter(this, imageResults);
        gvResults.setAdapter(imgResultArrayAdapter);
        gvResults.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View parent, int position,
                    long rowId) {
                Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
                ImageResult imageResult = imageResults.get(position);
                i.putExtra("result", imageResult);
                startActivity(i);
            }
        });
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page); 
                    // or customLoadMoreDataFromApi(totalItemsCount); 
            }
            });
    }
    
    public void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        gvResults= (GridView) findViewById(R.id.gvResults);
    }
    
    public void onImageSearch(View v) {
        String query = etQuery.getText().toString();
        
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=" + requestSize + 
                "&start=" + curOffset + "&v=1.0" + filters.toParams() + "&q=" + Uri.encode(query), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                JSONArray imageJsonResults = null;
                try {
                    imageJsonResults = response.getJSONObject(
                            "responseData").getJSONArray("results");
                    imageResults.clear();
                    imgResultArrayAdapter.addAll(ImageResult
                            .fromJSONArray(imageJsonResults));
                    curOffset += requestSize;
                    Log.d("DEBUG", curOffset + "" );
                } catch (JSONException e) {
                   e.printStackTrace(); 
                }
            }
        });
    }
    
    public void customLoadMoreDataFromApi(int offset) {
        String query = etQuery.getText().toString();
        
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=" + requestSize +
                    "&start=" + curOffset + "&v=1.0" + filters.toParams() + "&q=" + Uri.encode(query), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                JSONArray imageJsonResults = null;
                try {
                    imageJsonResults = response.getJSONObject(
                            "responseData").getJSONArray("results");
                    imgResultArrayAdapter.addAll(ImageResult
                            .fromJSONArray(imageJsonResults));
                    curOffset += requestSize;
                    Log.d("DEBUG", curOffset + "");
                } catch (JSONException e) {
                   e.printStackTrace(); 
                }
            }
        });    
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    
    public void onSettings(MenuItem mi) {
        Intent i = new Intent(this, FiltersActivity.class);
        i.putExtra("filters", filters);
        startActivityForResult(i, 45);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 45) {
            if (resultCode == RESULT_OK) {
                filters = (FilterSettings) data.getSerializableExtra("filters");
            }
        }
    }
}
