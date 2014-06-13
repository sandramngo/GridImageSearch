package com.codepath.gridimagesearch;

import java.io.Serializable;

public class FilterSettings  implements Serializable {
    public String sizeFilter;
    public String colorFilter;
    public String typeFilter;
    public String siteFilter;
    
    public FilterSettings() {
        sizeFilter = "";
        colorFilter = "";
        typeFilter = "";
        siteFilter = "";
    }
    
    public String getSizeFilter() {
        return sizeFilter;
    }


    public void setSizeFilter(String sizeFilter) {
        this.sizeFilter = sizeFilter;
    }


    public String getColorFilter() {
        return colorFilter;
    }


    public void setColorFilter(String colorFilter) {
        this.colorFilter = colorFilter;
    }


    public String getTypeFilter() {
        return typeFilter;
    }


    public void setTypeFilter(String typeFilter) {
        this.typeFilter = typeFilter;
    }


    public String getSiteFilter() {
        return siteFilter;
    }


    public void setSiteFilter(String siteFilter) {
        this.siteFilter = siteFilter;
    }


    public String toParams() {
        return "&as_sitesearch=" + this.siteFilter
                + "&imgcolor=" + this.colorFilter
                + "&imgsz=" + this.siteFilter
                + "&imgtype=" + this.typeFilter;
    }
}