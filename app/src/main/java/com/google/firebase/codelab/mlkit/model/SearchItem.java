package com.google.firebase.codelab.mlkit.model;

import java.util.Date;

public class SearchItem
{
    private Date searchDate;
    private String searchId;
    private String catType;
    private String check;

    public void SearchItem(Date date , String searchId, String catType, String check)
    {
        searchDate = date;
        this.searchId = searchId;
        this.catType = catType;
        this.check = check;
    }

    public Date getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(Date searchDate) {
        this.searchDate = searchDate;
    }


    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getCatType() {
        return catType;
    }

    public void setCatType(String catType) {
        this.catType = catType;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }
}
