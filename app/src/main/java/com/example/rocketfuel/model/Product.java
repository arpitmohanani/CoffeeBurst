package com.example.rocketfuel.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Class to hold product title, image, and price
@Entity(tableName = "products")
public class Product {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "product_code")
    private String product_code;

    @ColumnInfo(name = "product_name")
    private String product_name;

    @ColumnInfo(name = "product_price")
    private double product_price;

    @ColumnInfo(name = "product_image")
    private String product_image;

    @ColumnInfo(name = "product_category")
    private String product_category;


    // Constructor
    public Product(@NonNull String product_code, String product_name, String product_category, double product_price,String product_image) {
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_price = product_price;
        this.product_code = product_code;
        this.product_category = product_category;
    }

    @NonNull
    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(@NonNull String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }
}

