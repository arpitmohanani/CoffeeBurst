package com.example.rocketfuel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
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

    @NonNull
    @Override
    public ProductAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        Product model = productArrayList.get(position);
        holder.productNameTV.setText(model.getProduct_name());
        holder.productPriceIV.setText("" + model.getProduct_price());
        holder.productIV.setImageResource(context.getResources().getIdentifier(model.getProduct_image() , "drawable", context.getPackageName()));
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
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