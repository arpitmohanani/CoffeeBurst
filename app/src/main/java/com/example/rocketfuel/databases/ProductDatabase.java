package com.example.rocketfuel.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.rocketfuel.interfaces.ProductDao;
import com.example.rocketfuel.model.Product;

@Database(entities = {Product.class},version=1,exportSchema = false)
public abstract class ProductDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
}
