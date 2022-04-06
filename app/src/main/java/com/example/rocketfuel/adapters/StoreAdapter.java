package com.example.rocketfuel.adapters;

import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.rocketfuel.R;
import com.example.rocketfuel.model.Store;

import java.util.List;

public class StoreAdapter extends BaseAdapter {

    List<Store> StoreData;

    public StoreAdapter(List<Store> storeData) {
        StoreData = storeData;
    }

    @Override
    public int getCount() {
        return StoreData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater layoutInflater
                    = LayoutInflater.from(viewGroup.getContext());
            view = layoutInflater
                    .inflate(R.layout.layout_store,viewGroup,false);
        }
        TextView txtViewSiteItem = view.findViewById(R.id.txtViewStoreName);
        txtViewSiteItem.setText(StoreData.get(i).getStoreName());

        Drawable img = ResourcesCompat.getDrawable(viewGroup.getResources(),
                StoreData.get(i).getStoreIcon(),viewGroup.getContext().getTheme());

        img.setBounds(0,0,80,80);
        txtViewSiteItem.setCompoundDrawables(img,null,null,null);
        txtViewSiteItem.setCompoundDrawablePadding(80);
        txtViewSiteItem.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        return view;
    }
}
