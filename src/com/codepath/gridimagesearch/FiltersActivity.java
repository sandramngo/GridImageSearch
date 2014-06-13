package com.codepath.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class FiltersActivity extends Activity {
    Spinner colorSpinner;
    Spinner sizeSpinner;
    Spinner typeSpinner;
    EditText etSiteFilter;
    Button btnSaveFilters;
    
    FilterSettings filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        filters = (FilterSettings) getIntent().getSerializableExtra("filters");
        setupViews();
    }
    
    public void setupViews() {
        colorSpinner = (Spinner) findViewById(R.id.spinnerColorFilter);
        ArrayAdapter colorAdapter = (ArrayAdapter) colorSpinner.getAdapter();
        int spinnerPosition = colorAdapter.getPosition(filters.colorFilter);
        colorSpinner.setSelection(spinnerPosition);
        
        sizeSpinner = (Spinner) findViewById(R.id.spinnerImgSize);
        ArrayAdapter sizeAdapter = (ArrayAdapter) sizeSpinner.getAdapter();
        spinnerPosition = sizeAdapter.getPosition(filters.sizeFilter);
        sizeSpinner.setSelection(spinnerPosition);
        
        typeSpinner = (Spinner) findViewById(R.id.spinnerImageType);
        ArrayAdapter typeAdapter = (ArrayAdapter) typeSpinner.getAdapter();
        spinnerPosition = typeAdapter.getPosition(filters.typeFilter);
        typeSpinner.setSelection(spinnerPosition);
        
        etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
        etSiteFilter.setText(filters.siteFilter);
        
        btnSaveFilters = (Button) findViewById(R.id.btnSaveFilters);
    }
    
    public void onSaveFilters(View v) {
        String colorFilter = colorSpinner.getSelectedItem().toString();
        String typeFilter = typeSpinner.getSelectedItem().toString();
        String siteFilter = etSiteFilter.getText().toString();
        String sizeFilter = sizeSpinner.getSelectedItem().toString();
        
        filters.colorFilter = colorFilter;
        filters.typeFilter = typeFilter;
        filters.siteFilter = siteFilter;
        filters.sizeFilter = sizeFilter;
        
        Intent i = new Intent();
        i.putExtra("filters", filters);
        setResult(RESULT_OK, i);
        finish();
    }
}
