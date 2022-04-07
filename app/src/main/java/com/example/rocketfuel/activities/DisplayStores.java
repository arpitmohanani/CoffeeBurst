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

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DisplayStores extends AppCompatActivity {

    TextView txtLocation;
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
            Arrays.asList("Rocket Fuel - Vancouver","Rocket Fuel - Burnaby","Rocket Fuel - Coquitlam", "Rocket Fuel - Richmond", "Rocket Fuel - Surrey"));
    List<Integer> ChicagoSitePics = new ArrayList<>(Arrays.asList(
            R.drawable.coffee_icon, R.drawable.coffee_icon,R.drawable.coffee_icon, R.drawable.coffee_icon, R.drawable.coffee_icon));
    List<Store> StoreList = new ArrayList<>(populateList());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_stores);

        txtLocation = findViewById(R.id.textViewLocation);
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
            //fullName.setText(("Full Name: " + getIntent().getExtras().getString("FULLNAME","Empty")));
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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if(ActivityCompat.checkSelfPermission(DisplayStores.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getLocation(fullName);
        }else{
            ActivityCompat.requestPermissions(DisplayStores.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
            if(ActivityCompat.checkSelfPermission(DisplayStores.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                getLocation(fullName);
            }else{
                ActivityCompat.requestPermissions(DisplayStores.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
            }
        }
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

    @SuppressLint("MissingPermission")
    private void getLocation(String fullName) {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if(location!=null){
                    try {
                        Geocoder geo = new Geocoder(DisplayStores.this, Locale.getDefault());
                        List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(),1);

                        txtLocation.setText("Hi " + fullName + "\nLatitude: " + addresses.get(0).getLatitude() + "\nLongitude: " + addresses.get(0).getLongitude() +
                                "\nCountry: " + addresses.get(0).getCountryName() + "\nLocality: " + addresses.get(0).getLocality() +
                                "\nAddress: " + addresses.get(0).getAddressLine(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void FacebookSignOutSetup(){
        facebookAccessToken = AccessToken.getCurrentAccessToken();
        facebookRequest = GraphRequest.newMeRequest(
                facebookAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        try {
//                            facebookName = object.getString("name");
//                            fullName.setText(facebookName);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
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
    }
}