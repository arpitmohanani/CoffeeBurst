package com.example.rocketfuel.activities;

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

    //OrderLayoutBinding binding;
    final String TAG = "Rocket Fuel";
    Button btnCheckout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        String prod_name;
        double orderTotal = 0;

        try {
            Bundle thisBundle = getIntent().getExtras();
            prod_name = getIntent().getExtras().getString("PRODUCTNAME", "Error");
            orderTotal = getIntent().getExtras().getDouble("TOTAL", 0);


            TextView txtViewProdName = findViewById(R.id.txtViewProdName);
            txtViewProdName.setText(prod_name);
            TextView txtViewTotalAmount = findViewById(R.id.txtViewTotal);
            txtViewTotalAmount.setText((int) orderTotal);

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, orderTotal + "");
        }

        double finalOrderTotal = orderTotal;
        btnCheckout.setOnClickListener((View view) -> {

            Intent chkIntent = new Intent(OrdersActivity.this, CheckoutActivity.class);

            Bundle chkBundle = new Bundle();
            chkBundle.putDouble("TOTAL", finalOrderTotal);

            chkIntent.putExtras(chkBundle);
            startActivity(chkIntent);

        });

    }
}