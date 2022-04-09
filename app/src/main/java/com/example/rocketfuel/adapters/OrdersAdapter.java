package com.example.rocketfuel.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rocketfuel.databinding.LayoutStoreBinding;
import com.example.rocketfuel.databinding.OrderLayoutBinding;
import com.example.rocketfuel.model.Orders;

import java.util.List;

public class OrdersAdapter extends BaseAdapter {

    List<Orders> adapterOrdersList;

    public OrdersAdapter(List<Orders> adapterOrdersList) {
        this.adapterOrdersList = adapterOrdersList;
    }

    @Override
    public int getCount() {
        return adapterOrdersList.size();
    }

    @Override
    public Orders getItem(int i) {
        return adapterOrdersList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        OrderLayoutBinding orderBinding =
                OrderLayoutBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        TextView txtViewProductName = orderBinding.txtViewProdName;
        TextView txtViewTotal = orderBinding.txtViewTotal;

        return orderBinding.getRoot();
    }
}
