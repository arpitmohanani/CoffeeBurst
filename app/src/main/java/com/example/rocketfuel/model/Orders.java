package com.example.rocketfuel.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class Orders {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "orderid")
    private String OrderId;

    @ColumnInfo(name="product_code")
    private String ProductCode;

    @ColumnInfo(name = "quantity")
    private int Quantity;

    @ColumnInfo(name = "totalprice")
    private double TotalPrice;

    public Orders() {

    }

    public Orders(@NonNull String orderId, String productCode, int quantity, double totalPrice) {
        OrderId = orderId;
        ProductCode = productCode;
        Quantity = quantity;
        TotalPrice = totalPrice;
    }

    @NonNull
    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(@NonNull String orderId) {
        OrderId = orderId;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }
}
