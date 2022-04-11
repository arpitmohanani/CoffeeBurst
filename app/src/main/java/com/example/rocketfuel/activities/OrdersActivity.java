package com.example.rocketfuel.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rocketfuel.R;
import com.example.rocketfuel.databinding.ActivityMainBinding;
import com.example.rocketfuel.databinding.OrderLayoutBinding;

import java.text.DecimalFormat;

public class OrdersActivity extends AppCompatActivity {

    TextView tvProductName;
    TextView tvAmount;
    //OrderLayoutBinding binding;
    final String TAG = "Rocket Fuel";
    Button btnCheckout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        String prod_name;
        String orderTotal = "";

        try {
            Bundle thisBundle = getIntent().getExtras();
            prod_name = getIntent().getExtras().getString("PRODUCTNAME", "Error");
            orderTotal = getIntent().getExtras().getString("TOTAL", "Error");


            tvProductName = findViewById(R.id.txtViewProdName2);
            tvProductName.setText(prod_name);
            tvAmount = findViewById(R.id.txtViewAmount2);
            tvAmount.setText(orderTotal);
            //tvAmount.setText((int) orderTotal);

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, orderTotal + "");
        }


        String finalOrderTotal = orderTotal;

        btnCheckout = findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener((View view) -> {

            Intent chkIntent = new Intent(OrdersActivity.this, CheckoutActivity.class);

            Bundle chkBundle = new Bundle();
            chkBundle.putString("FINALTOTAL", finalOrderTotal);

            chkIntent.putExtras(chkBundle);
            startActivity(chkIntent);

        });

    }
}