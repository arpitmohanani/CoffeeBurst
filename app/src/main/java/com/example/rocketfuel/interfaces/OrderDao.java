package com.example.rocketfuel.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.rocketfuel.model.Orders;
import com.example.rocketfuel.model.Product;

import java.util.List;

@Dao
public interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertOrders(Orders... order);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrdersFromList(List<Orders> orders);

/*    @Query("SELECT * from orders")
    List<Product> GetAllOrders();

    @Query("SELECT * FROM orders WHERE orderid =:OrderId")
    List<Product> GetSpecificOrder(String OrderId);*/

}
