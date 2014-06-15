package com.codepath.gridimagesearch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class FiltersDialogFragment extends DialogFragment implements OnEditorActionListener{
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
    }
    
    public void setupViews(View view) {
        filters = (FilterSettings) 
                getArguments().getSerializable("filters");
        
        colorSpinner = (Spinner) view.findViewById(R.id.spinnerColorFilter);
        ArrayAdapter colorAdapter = (ArrayAdapter) colorSpinner.getAdapter();
        int spinnerPosition = colorAdapter.getPosition(filters.colorFilter);
        colorSpinner.setSelection(spinnerPosition);
        
        sizeSpinner = (Spinner) view.findViewById(R.id.spinnerImgSize);
        ArrayAdapter sizeAdapter = (ArrayAdapter) sizeSpinner.getAdapter();
        spinnerPosition = sizeAdapter.getPosition(filters.sizeFilter);
        sizeSpinner.setSelection(spinnerPosition);
        
        typeSpinner = (Spinner) view.findViewById(R.id.spinnerImageType);
        ArrayAdapter typeAdapter = (ArrayAdapter) typeSpinner.getAdapter();
        spinnerPosition = typeAdapter.getPosition(filters.typeFilter);
        typeSpinner.setSelection(spinnerPosition);
        
        etSiteFilter = (EditText) view.findViewById(R.id.etSiteFilter);
        etSiteFilter.setText(filters.siteFilter);
        
        etSiteFilter.requestFocus();
    }
    
    public interface FilterDialogListener {
        void onSaveFilters(FilterSettings filters);
    }
    
    // Fires whenever the textfield has an action performed
    // In this case, when the "Done" button is pressed
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            FilterDialogListener listener = (FilterDialogListener) getActivity();
         //   listener.onFinishEditDialog(mEditText.getText().toString());
            dismiss();
            return true;
        }
        return false;
    }
}