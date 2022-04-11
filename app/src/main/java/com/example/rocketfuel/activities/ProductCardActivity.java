
package com.example.rocketfuel.activities;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rocketfuel.R;

public class ProductCardActivity extends AppCompatActivity {
    final String TAG = "Rocket Fuel";
    String productName ="";
    String productPrice ="";
    TextView drinkName;
    ImageView drinkImage;
    TextView drinkPrice;
    RadioGroup radioGroupSizes;
    String productPriceStr;
    Button btnAddToCart;
    EditText editTextQuantity;
    TextView total;
    double productSizedPrice = 1;
    RadioButton radioBtnSmall;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_card_layout);
        drinkName = findViewById(R.id.drink_name);
        drinkImage = findViewById(R.id.imgViewProductCardImage);
        drinkPrice = findViewById(R.id.txtViewPrice);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        radioBtnSmall = findViewById(R.id.radioBtnSmall);
        radioBtnSmall.setChecked(true);

//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
 //       getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        productName = intent.getStringExtra("name");
        productPrice = intent.getStringExtra("price");

        drinkName.setText(productName);
        drinkPrice.setText(productPrice);
        drinkImage.setImageBitmap(intent.getParcelableExtra("im"));






            btnAddToCart.setOnClickListener((View view) -> {
            editTextQuantity = findViewById(R.id.txtViewQuantity);
            if (editTextQuantity.getText().toString().isEmpty()) {
                Log.d(TAG, "Quantity is empty");
                Toast.makeText(this, "Please enter item quantity", Toast.LENGTH_SHORT).show();
            } else {

                try {
                    int qty = Integer.parseInt(editTextQuantity.getText().toString());
                    double priceDouble = Double.parseDouble(productPrice.replaceAll("[$]", ""));
                    double total = qty * priceDouble* productSizedPrice;


                    Intent myIntent = new Intent(ProductCardActivity.this, OrdersActivity.class);
                    Bundle myBundle = new Bundle();
                    myBundle.putString("PRODUCTNAME", productName);
                    //myBundle.putString("PRODUCTSIZE", String.valueOf(productSizedPrice));
                    myBundle.putString("PRODUCTQTY", editTextQuantity.getText().toString());

                    myBundle.putString("TOTAL", String.valueOf(total));
                    myIntent.putExtras(myBundle);
                    startActivity(myIntent);

                } catch (Exception ex) {
                    Log.d(TAG, ex.getMessage());

                    Toast.makeText(this, "Please enter valid number of items", Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }
            }
        });

    }

    public void onRadioButtonClicked(View view) {
        radioGroupSizes = findViewById(R.id.radioGroupSizes);
        int selectedId = radioGroupSizes.getCheckedRadioButtonId();
        String selectedValue =  (String) ((RadioButton) findViewById(selectedId)).getText();
        if(selectedValue.equalsIgnoreCase("Small")){
            System.out.println(selectedValue);
            productSizedPrice = 1;
            drinkPrice.setText("$" + productPrice.replace("$",""));

        }
        else if(selectedValue.equalsIgnoreCase("Medium")){
            System.out.println(selectedValue);
            productSizedPrice = 1.5;
            drinkPrice.setText(String.valueOf("$" + Double.parseDouble(productPrice.replace("$",""))*productSizedPrice));

        }
        else if(selectedValue.equalsIgnoreCase("Large")){
            System.out.println(selectedValue);
            productSizedPrice = 2;
            drinkPrice.setText(String.valueOf("$" + Double.parseDouble(productPrice.replace("$",""))*productSizedPrice));

        }
    }
}