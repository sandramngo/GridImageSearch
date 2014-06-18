package com.codepath.gridimagesearch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class FiltersDialogFragment extends DialogFragment{
    Spinner colorSpinner;
    Spinner sizeSpinner;
    Spinner typeSpinner;
    EditText etSiteFilter;
    
    FilterSettings filters = new FilterSettings();
    public FiltersDialogFragment() {
        // Empty constructor required for DialogFragment
    }
    
    public static FiltersDialogFragment newInstance(String title) {
        FiltersDialogFragment frag = new FiltersDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
         String title = getArguments().getString("Filters");
         AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
         alertDialogBuilder.setTitle(title);
         alertDialogBuilder.setPositiveButton("Search",  new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 FilterDialogListener listener = (FilterDialogListener) getActivity();
                 String colorFilter = colorSpinner.getSelectedItem().toString();
                 String typeFilter = typeSpinner.getSelectedItem().toString();
                 String siteFilter = etSiteFilter.getText().toString();
                 String sizeFilter = sizeSpinner.getSelectedItem().toString();
                 
                 filters.colorFilter = colorFilter;
                 filters.typeFilter = typeFilter;
                 filters.siteFilter = siteFilter;
                 filters.sizeFilter = sizeFilter;
                 
                 listener.onSaveFilters(filters);
             }
         });
         alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss();
             }
         });
         
         LayoutInflater inflater = getActivity().getLayoutInflater();
         View view = inflater.inflate(R.layout.filters_dialog, null);
         alertDialogBuilder.setView(view);
         setupViews(view);
         return alertDialogBuilder.create();
    }
    
    public void setupViews(View view) {
        
        Typeface aller = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Aller_Rg.ttf");
        TextView imageSize = (TextView) view.findViewById(R.id.tvImageSize);
        imageSize.setTypeface(aller);
        
        TextView tvColor = (TextView) view.findViewById(R.id.tvColorFilter);
        tvColor.setTypeface(aller);
        
        TextView tvType = (TextView) view.findViewById(R.id.tvImageType);
        tvType.setTypeface(aller);
        
        TextView tvSite = (TextView) view.findViewById(R.id.tvSiteFilter);
        tvSite.setTypeface(aller);
        
        TextView tvFiltersTitle = (TextView) view.findViewById(R.id.filtersDialogTitle);
        tvFiltersTitle.setTypeface(aller);
        
        filters = (FilterSettings) 
                getArguments().getSerializable("filters");
        
        colorSpinner = (Spinner) view.findViewById(R.id.spinnerColorFilter);
        ArrayAdapter colorAdapter = (ArrayAdapter) colorSpinner.getAdapter();
        colorAdapter.setDropDownViewResource(R.layout.custom_spinner);
        int spinnerPosition = colorAdapter.getPosition(filters.colorFilter);
        colorSpinner.setSelection(spinnerPosition);
        
        sizeSpinner = (Spinner) view.findViewById(R.id.spinnerImgSize);
        ArrayAdapter sizeAdapter = (ArrayAdapter) sizeSpinner.getAdapter();
        sizeAdapter.setDropDownViewResource(R.layout.custom_spinner);
        spinnerPosition = sizeAdapter.getPosition(filters.sizeFilter);
        sizeSpinner.setSelection(spinnerPosition);
        
        typeSpinner = (Spinner) view.findViewById(R.id.spinnerImageType);
        ArrayAdapter typeAdapter = (ArrayAdapter) typeSpinner.getAdapter();
        typeAdapter.setDropDownViewResource(R.layout.custom_spinner);
        spinnerPosition = typeAdapter.getPosition(filters.typeFilter);
        typeSpinner.setSelection(spinnerPosition);
        
        etSiteFilter = (EditText) view.findViewById(R.id.etSiteFilter);
        etSiteFilter.setText(filters.siteFilter);
        
        etSiteFilter.requestFocus();
    }
    
    public interface FilterDialogListener {
        void onSaveFilters(FilterSettings filters);
    }
}