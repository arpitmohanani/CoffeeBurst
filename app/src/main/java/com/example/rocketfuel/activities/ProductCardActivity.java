
package com.example.rocketfuel.activities;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;

import com.example.rocketfuel.R;

import java.util.Objects;

public class ProductCardActivity extends AppCompatActivity {
    String productName ="";
    String productPrice ="";
    String productImage ="";
    TextView drinkName;
    ImageView drinkImage;
    TextView drinkPrice;
    RadioGroup radioGroupSizes;
    String productPriceStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_card_layout);
        drinkName = findViewById(R.id.drink_name);
        drinkImage = findViewById(R.id.imgViewProductCardImage);
        drinkPrice = findViewById(R.id.txtViewPrice);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        productName = intent.getStringExtra("name");
        productImage = intent.getStringExtra("imgId");
        productPrice = intent.getStringExtra("price");
        productPrice = intent.getStringExtra("price");

        drinkName.setText(productName);
        drinkPrice.setText(productPriceStr);
        //drinkImage.setImageDrawable();

    }

    public void onRadioButtonClicked(View view) {
        radioGroupSizes = findViewById(R.id.radioGroupSizes);
        int selectedId = radioGroupSizes.getCheckedRadioButtonId();
        String selectedValue =  (String) ((RadioButton) findViewById(selectedId)).getText();
        if(selectedValue.equalsIgnoreCase("Small")){
            System.out.println(selectedValue);
            drinkPrice.setText(productPrice);
            System.out.println(productPrice);
        }


        else if(selectedValue.equalsIgnoreCase("Medium")){
            System.out.println(selectedValue);
            //drinkPrice.setText(String.valueOf(Double.parseDouble(productPrice)*1.5));
            System.out.println(Double.valueOf(productPriceStr));
        }

        else if(selectedValue.equalsIgnoreCase("Large")){
            System.out.println(selectedValue);
            //drinkPrice.setText(String.valueOf(Double.parseDouble(productPrice)*2.0));
            System.out.println(Double.valueOf(productPriceStr));
        }

    }

}