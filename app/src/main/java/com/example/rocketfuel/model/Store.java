package com.example.rocketfuel.model;

public class Store {
    private String StoreName;
    private int StoreIcon;

    public Store(String storeName, int storeIcon) {
        StoreName = storeName;
        StoreIcon = storeIcon;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public int getStoreIcon() {
        return StoreIcon;
    }

    public void setStoreIcon(int storeIcon) {
        StoreIcon = storeIcon;
    }
}
