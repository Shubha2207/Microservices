package com.example.moviecatalogservice.models;

import java.util.ArrayList;
import java.util.List;

public class Catalog {

    private List<CatalogItem> catalogItems = new ArrayList<CatalogItem>();

    public Catalog(){

    }

    public Catalog(List<CatalogItem> catalogItems) {
        this.catalogItems = catalogItems;
    }

    public List<CatalogItem> getCatalogItems() {
        return catalogItems;
    }

    public void setCatalogItems(List<CatalogItem> catalogItems) {
        this.catalogItems = catalogItems;
    }

    public void setSingleCatalogItem(CatalogItem catalogItem){
        this.catalogItems.add(catalogItem);
    }

}
