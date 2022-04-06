package com.example.rocketfuel.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.rocketfuel.model.Product;

import java.util.List;
@Dao
public interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertProducts(Product... products);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProductsFromList(List<Product> products);

    @Query("SELECT * from products")
    List<Product> GetAllProducts();

    @Query("SELECT * FROM products WHERE product_category =:Category")
    List<Product> GetProductsOfCategory(String Category);

}
