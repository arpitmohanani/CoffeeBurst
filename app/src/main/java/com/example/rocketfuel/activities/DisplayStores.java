package com.example.rocketfuel.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rocketfuel.R;
import com.example.rocketfuel.adapters.StoreAdapter;
import com.example.rocketfuel.model.Store;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DisplayStores extends AppCompatActivity {

    TextView txtLocation;
    TextView txtGreetings;
    TextView txtInstructions;

    ListView listStoreName;
    Button btnSignOut;
    FusedLocationProviderClient fusedLocationProviderClient;

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    GoogleSignInAccount googleSignInAccount;
    String facebookName;
    AccessToken facebookAccessToken;
    GraphRequest facebookRequest;
    Intent openHomeActivity;
    Bundle bundle = new Bundle();

    String location = "Hi ";

    List<String> ChicagoSiteNames = new ArrayList<>(
            Arrays.asList("Coffee Burst Cafe - Vancouver","Coffee Burst Cafe - Burnaby","Coffee Burst Cafe - Coquitlam", "Coffee Burst Cafe - Richmond", "Coffee Burst Cafe - Surrey"));
    List<Integer> ChicagoSitePics = new ArrayList<>(Arrays.asList(
            R.drawable.coffee_icon, R.drawable.coffee_icon,R.drawable.coffee_icon, R.drawable.coffee_icon, R.drawable.coffee_icon));
    List<Store> StoreList = new ArrayList<>(populateList());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_stores);

        txtLocation = findViewById(R.id.txtViewLocation);
        txtGreetings = findViewById(R.id.txtGreetings);
        txtInstructions = findViewById(R.id.txtInstructions);
        listStoreName = findViewById(R.id.listStoreNames);
        btnSignOut = findViewById(R.id.btnSignOut);

        StoreAdapter storeAdapter = new StoreAdapter(StoreList);
        listStoreName.setAdapter(storeAdapter);

        String signInType = getIntent().getExtras().getString("SIGNINTYPE","Empty");
        String fullName = getIntent().getExtras().getString("FULLNAME","Empty");

        if(signInType.toLowerCase().equals("Facebook".toLowerCase())){
            FacebookSignOutSetup();
        }else if(signInType.toLowerCase().equals("Google".toLowerCase())){
            GoogleSignOutSetup();
        }else if(signInType.toLowerCase().equals("Local".toLowerCase())){
            txtGreetings.setText("Hi " + fullName);
        }

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(signInType.toLowerCase().equals("Facebook".toLowerCase())){
                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(DisplayStores.this, SignInPage.class));
                    finish();
                }else if(signInType.toLowerCase().equals("Google".toLowerCase())){
                    googleSignInClient.signOut();
                    startActivity(new Intent(DisplayStores.this, SignInPage.class));
                    finish();
                }else if(signInType.toLowerCase().equals("Local".toLowerCase())){
                    startActivity(new Intent(DisplayStores.this, SignInPage.class));
                    finish();
                }
            }
        });

        listStoreName.setOnItemClickListener(
                (AdapterView<?> adapterView, View view, int i, long l) -> {
                    bundle.putString("SIGNINTYPE",signInType);
                    bundle.putString("FULLNAME",fullName);
                    openHomeActivity = new Intent(DisplayStores.this, CatalogActivity.class);
                    openHomeActivity.putExtras(bundle);
                    startActivity(openHomeActivity);
                });

//        txtGreetings.setText("Hi " + fullName);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if(ActivityCompat.checkSelfPermission(DisplayStores.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //getLocation();
        }else{
            ActivityCompat.requestPermissions(DisplayStores.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
            if(ActivityCompat.checkSelfPermission(DisplayStores.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            // getLocation();
            }else{
                ActivityCompat.requestPermissions(DisplayStores.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
            }
        }
        txtInstructions.setText("Please select a store nearest to you:");
    }

    public List<Store> populateList(){
        List<Store> listStores = new ArrayList<>();
        Store storeInfo;
        for(int i = 0; i < ChicagoSiteNames.size(); i++){
            storeInfo = new Store(ChicagoSiteNames.get(i),ChicagoSitePics.get(i));
            listStores.add(storeInfo);
        }
        return listStores;
    }

//   @SuppressLint("MissingPermission")
//    private void getLocation() {
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull Task<Location> task) {
//                Location  location = task.getResult();
//
//                if(location!=null){
//                    try {
//                        Geocoder geo = new Geocoder(DisplayStores.this, Locale.getDefault());
//                        List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(),1);
//
//                        txtLocation.setText("You are in "  + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea()+ ", " + addresses.get(0).getCountryName());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });}

    private void FacebookSignOutSetup(){
        facebookAccessToken = AccessToken.getCurrentAccessToken();
        facebookRequest = GraphRequest.newMeRequest(
                facebookAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            facebookName = object.getString("name");
                            txtGreetings.setText("Hi " + facebookName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        facebookRequest.setParameters(parameters);
        facebookRequest.executeAsync();
    }

    private void GoogleSignOutSetup(){
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        txtGreetings.setText(("Hi " + googleSignInAccount.getDisplayName()));
    }
}