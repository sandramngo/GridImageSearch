package com.codepath.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.codepath.gridimagesearch.FiltersDialogFragment.FilterDialogListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends FragmentActivity implements FilterDialogListener {
    GridView gvResults;
    ArrayList<ImageResult> imageResults = new ArrayList<>();
    ImageResultArrayAdapter imgResultArrayAdapter;
    FilterSettings filters = new FilterSettings();
    FrameLayout background;
    
    int requestSize = 8;
    String query = "";

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
        gvResults= (GridView) findViewById(R.id.gvResults);
        background = (FrameLayout) findViewById(R.id.backgroundImg);
    }
    
    public void search(String query) {
        this.query = query;
        search(0);
        if (!query.equals("")) {
            background.setVisibility(View.INVISIBLE);
            gvResults.setVisibility(View.VISIBLE);
        } else {
            background.setVisibility(View.VISIBLE);
            gvResults.setVisibility(View.INVISIBLE);
        }
    }
    
    public void search(final int page) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=" + requestSize + 
                "&start=" + page*requestSize + "&v=1.0" + filters.toParams() + "&q=" + Uri.encode(query), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                JSONArray imageJsonResults = null;
                try {
                    imageJsonResults = response.getJSONObject(
                            "responseData").getJSONArray("results");
                    if (page == 0) {
                        imgResultArrayAdapter.clear();
                        imgResultArrayAdapter.notifyDataSetInvalidated();
                    }
                    imgResultArrayAdapter.addAll(ImageResult
                            .fromJSONArray(imageJsonResults));
                } catch (JSONException e) {
                   e.printStackTrace(); 
                }
            }
        });   
    }
    
    public void customLoadMoreDataFromApi(int page) {
        search(page);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) { 
                search(query);
                return false; 
           }
        });
        return true;
    }
    
    public void onSettings(MenuItem mi) {
        showFiltersDialog();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 45) {
            if (resultCode == RESULT_OK) {
                filters = (FilterSettings) data.getSerializableExtra("filters");
            }
        }
    }
    
    private void showFiltersDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FiltersDialogFragment filtersDialog = FiltersDialogFragment.newInstance("Some Title");
        Bundle bundle = new Bundle();
        bundle.putSerializable("filters", filters);
        filtersDialog.setArguments(bundle);
        filtersDialog.show(fm, "fragment_filters_dialog");
    }
    
    @Override
    public void onSaveFilters(FilterSettings filters) {
      this.filters = filters;
      search(0);
    }
}
