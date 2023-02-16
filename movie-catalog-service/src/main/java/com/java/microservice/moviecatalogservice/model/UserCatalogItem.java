package com.java.microservice.moviecatalogservice.model;

import java.util.List;

public class UserCatalogItem {
    private String userID;
    private List<CatalogItem> userCatalogItem;

    public UserCatalogItem() {
    }

    public UserCatalogItem(String userID, List<CatalogItem> userCatalogItem) {
        this.userID = userID;
        this.userCatalogItem = userCatalogItem;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<CatalogItem> getUserCatalogItem() {
        return userCatalogItem;
    }

    public void setUserCatalogItem(List<CatalogItem> userCatalogItem) {
        this.userCatalogItem = userCatalogItem;
    }
}
