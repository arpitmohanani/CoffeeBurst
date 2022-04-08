
package com.example.rocketfuel.activities;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.rocketfuel.R;

import java.util.Objects;

public class ProductCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_card_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.cappuchino));
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String imgId = intent.getStringExtra("imgId");



        TextView drinkName = findViewById(R.id.drink_name);
        ImageView drinkImage = findViewById(R.id.imgViewProductCardImage);
        drinkName.setText(name);

    }

    public void onRadioButtonClicked(View view) {

    }
}