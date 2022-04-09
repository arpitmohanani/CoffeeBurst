package com.example.rocketfuel.databases;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.rocketfuel.interfaces.OrderDao;
import com.example.rocketfuel.model.Orders;
import com.example.rocketfuel.model.Product;

@Database(entities = {Orders.class},version=1,exportSchema = false)
public abstract class OrdersDatabase extends RoomDatabase {
    public abstract OrderDao orderDao();
}
