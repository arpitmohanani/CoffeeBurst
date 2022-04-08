package com.example.rocketfuel.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import com.example.rocketfuel.databases.ProductDatabase;
import com.example.rocketfuel.databinding.ActivityCatalogBinding;
import com.example.rocketfuel.interfaces.ProductDao;
import com.example.rocketfuel.model.Product;
import com.example.rocketfuel.R;
import com.example.coffeeapp.supplements.RecyclerItemClickListener;
import com.example.rocketfuel.adapters.ProductAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class CatalogActivity extends AppCompatActivity {

    private RecyclerView coffeeRecyclerView;
    private RecyclerView teaRecyclerView;

    private ArrayList<Product> coffeeModelArrayList;
    private ArrayList<Product> teaModelArrayList;

    //    ActivityCatalogBinding binding;
    ProductDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        coffeeRecyclerView = findViewById(R.id.idRVCoffee);
        teaRecyclerView = findViewById(R.id.idRVTea);

        db = Room.databaseBuilder(getApplicationContext(),
                ProductDatabase.class,"Products.db").build();

        coffeeModelArrayList = new ArrayList<>();
        teaModelArrayList = new ArrayList<>();

        AtomicReference<ProductAdapter> coffeeAdapter = new AtomicReference<>(new ProductAdapter(this, coffeeModelArrayList));
        AtomicReference<ProductAdapter> teaAdapter = new AtomicReference<>(new ProductAdapter(this, teaModelArrayList));

        List<Product> AllProducts = ReadProductsCSV();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        coffeeRecyclerView.setLayoutManager(linearLayoutManager);
        //coffeeRecyclerView.setAdapter(coffeeAdapter);

        teaRecyclerView.setLayoutManager(linearLayoutManager2);
        teaRecyclerView.setAdapter(teaAdapter.get());
        coffeeRecyclerView.setAdapter(coffeeAdapter.get());
        //teaRecyclerView.setAdapter(teaAdapter);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                ProductDao productDao = db.productDao();
                productDao.insertProductsFromList(AllProducts);
                coffeeModelArrayList = (ArrayList<Product>) db.productDao().GetProductsOfCategory("Coffee");
                teaModelArrayList = (ArrayList<Product>) db.productDao().GetProductsOfCategory("Tea");
                Log.d("Size of the list", String.valueOf(teaModelArrayList.size()));

                //instead of setting the data directly from file
                //we are setting it after getting it from Database
                runOnUiThread(() ->{
                    coffeeAdapter.get().updateList(coffeeModelArrayList);
                    teaAdapter.get().updateList(teaModelArrayList);
                });
            } catch (Exception ex){
                Log.d("CoffeeBurst",ex.getMessage());
            }
        });

        coffeeRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, coffeeRecyclerView,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(CatalogActivity.this, ProductCardActivity.class);
                        ProductAdapter.Viewholder mod = (ProductAdapter.Viewholder) coffeeRecyclerView.findViewHolderForAdapterPosition(position);
                        String name = (String) Objects.requireNonNull(mod).productNameTV.getText();
                        String price = (String) Objects.requireNonNull(mod).productPriceIV.getText();
                        //Double priceDouble = Double.parseDouble(price);
                        intent.putExtra("name", name);
                        intent.putExtra("price", price);
                        //int imageId = Objects.requireNonNull(mod).productIV.AppCompatResources.getDrawable(this,this.getResources().getIdentifier(IMAGE_NAME.split("\\.")[0] , "drawable", this.getPackageName())));
                        //intent.putExtra("imgId", imageId);
                        System.out.println(name);
                        startActivity(intent);
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        System.out.println("@");
                    }
                })
        );
    }

    private List<Product> ReadProductsCSV(){
        List<Product> productList = new ArrayList<>();

        InputStream inputStream = getResources().openRawResource(R.raw.catalog);
        BufferedReader reader
                = new BufferedReader(new InputStreamReader(inputStream));
        try {
            // This is header, it's not included
            String productLine;
            if ((productLine = reader.readLine()) != null){

            }
            while ((productLine = reader.readLine()) != null){
                String[] eachProductFields = productLine.split(",");
                Product eachProduct = new Product(eachProductFields[1],
                        eachProductFields[0], eachProductFields[2],
                        Double.parseDouble(eachProductFields[3]), eachProductFields[4]);
                productList.add(eachProduct);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error reading file " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return productList;
    }
}
