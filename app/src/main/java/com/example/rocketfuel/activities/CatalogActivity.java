package com.example.rocketfuel.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
// Activity to display the catalog
public class CatalogActivity extends AppCompatActivity {
    // recyclerview for the categories
    private RecyclerView coffeeRecyclerView;
    private RecyclerView teaRecyclerView;

    // lists for data extracted from DB
    private ArrayList<Product> coffeeModelArrayList;
    private ArrayList<Product> teaModelArrayList;

    ActivityCatalogBinding binding;
    ProductDatabase db;

    // boolean flag to determine on what recyclerview item the user clicked
    boolean isTea = false;
    boolean isCoffee = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        coffeeRecyclerView = findViewById(R.id.idRVCoffee);
        teaRecyclerView = findViewById(R.id.idRVTea);

        db = Room.databaseBuilder(getApplicationContext(),
                ProductDatabase.class, "Products.db").build();

        coffeeModelArrayList = new ArrayList<>();
        teaModelArrayList = new ArrayList<>();


        // Read products from csv
        List<Product> AllProducts = ReadProductsCSV();

        // set up Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        coffeeRecyclerView.setLayoutManager(linearLayoutManager);
        teaRecyclerView.setLayoutManager(linearLayoutManager2);


        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                // Get items from the db according to a category
                ProductDao productDao = db.productDao();
                productDao.insertProductsFromList(AllProducts);
                coffeeModelArrayList = (ArrayList<Product>) db.productDao().GetProductsOfCategory("Coffee");
                teaModelArrayList = (ArrayList<Product>) db.productDao().GetProductsOfCategory("Tea");


                runOnUiThread(() ->{
                    // set up the adapter
                    ProductAdapter coffeeAdapter = new ProductAdapter(this, coffeeModelArrayList);
                    ProductAdapter teaAdapter = new ProductAdapter(this, teaModelArrayList);
                    teaRecyclerView.setAdapter(teaAdapter);
                    coffeeRecyclerView.setAdapter(coffeeAdapter);
                });
            } catch (Exception ex) {
                Log.d("CoffeeBurst", ex.getMessage());
            }
        });

       // Click listeners for both categories
        coffeeRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, coffeeRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        isCoffee = true;
                        click(view, position);
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {
                        click(view, position);
                    }
                })
        );
        teaRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, teaRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        isTea = true;
                        click(view, position);
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {
                        click(view, position);
                    }
                })
        );
    }

    // On click get data from the card view and put to a bundle
    private void click(View view, int position) {
        Intent intent = new Intent(CatalogActivity.this, ProductCardActivity.class);
        ProductAdapter.Viewholder mod = null;
        if(isTea){
            mod = (ProductAdapter.Viewholder) teaRecyclerView.findViewHolderForAdapterPosition(position);
        }
        else if(isCoffee){
            mod = (ProductAdapter.Viewholder) coffeeRecyclerView.findViewHolderForAdapterPosition(position);
        }
        String name = (String) Objects.requireNonNull(mod).productNameTV.getText();
        String price = (String) Objects.requireNonNull(mod).productPriceIV.getText();
        Objects.requireNonNull(mod).productIV.buildDrawingCache();
        Bitmap im = Objects.requireNonNull(mod).productIV.getDrawingCache();
        intent.putExtra("name", name);
        intent.putExtra("price", price);
        Bundle extras = new Bundle();
        extras.putParcelable("im", im);
        intent.putExtras(extras);
        System.out.println(name);
        startActivity(intent);
    }

    // Read the products csv file and write it into a list of products
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
