package com.example.rocketfuel.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rocketfuel.model.Product;
import com.example.rocketfuel.R;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.Viewholder> {

    private Context context;
    private ArrayList<Product> productArrayList;

    // Constructor
    public ProductAdapter(Context context, ArrayList<Product> courseModelArrayList) {
        this.context = context;
        this.productArrayList = courseModelArrayList;
    }

    // inflate the view
    @NonNull
    @Override
    public ProductAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        Product model = productArrayList.get(position);
        holder.productNameTV.setText(model.getProduct_name());
        holder.productPriceIV.setText("$" + model.getProduct_price());
        holder.productIV.setImageDrawable(AppCompatResources.getDrawable(context,context.getResources().getIdentifier(model.getProduct_image().split("\\.")[0] , "drawable", context.getPackageName())));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<Product> productArrayList){
        this.productArrayList = productArrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public ImageView productIV;
        public TextView productNameTV, productPriceIV, categoryCoffeeTV, categoryTeaTV;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            productIV = itemView.findViewById(R.id.idIVProductImage);
            productNameTV = itemView.findViewById(R.id.idTVProductName);
            productPriceIV = itemView.findViewById(R.id.idTVProductPrice);
            categoryCoffeeTV = itemView.findViewById(R.id.txtViewCoffeeCat);
            categoryTeaTV = itemView.findViewById(R.id.txtViewTeaCat);
        }
    }
}