package com.example.rocketfuel.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rocketfuel.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    GoogleSignInAccount googleSignInAccount;
    String facebookName;
    AccessToken facebookAccessToken;
    GraphRequest facebookRequest;
    TextView email;
    TextView fullName;
    Button signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        email = findViewById(R.id.emailId);
        fullName = findViewById(R.id.fullName);
        signOutBtn = findViewById(R.id.signOutBtn);

        //Opening Bundle
        String signInType = getIntent().getExtras().getString("SIGNINTYPE","Empty");
        email.setText(signInType);

        if(signInType.toLowerCase().equals("Facebook".toLowerCase())){
            FacebookSignOutSetup();
        }else if(signInType.toLowerCase().equals("Google".toLowerCase())){
            GoogleSignOutSetup();
            fullName.setText(("Full Name: " + googleSignInAccount.getDisplayName()));
        }else if(signInType.toLowerCase().equals("Local".toLowerCase())){
            fullName.setText(("Full Name: " + getIntent().getExtras().getString("FULLNAME","Empty")));
        }

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(signInType.toLowerCase().equals("Facebook".toLowerCase())){
                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(HomeActivity.this, SignInPage.class));
                    finish();
                }else if(signInType.toLowerCase().equals("Google".toLowerCase())){
                    googleSignInClient.signOut();
                    startActivity(new Intent(HomeActivity.this, SignInPage.class));
                    finish();
                }else if(signInType.toLowerCase().equals("Local".toLowerCase())){
                    startActivity(new Intent(HomeActivity.this, SignInPage.class));
                    finish();
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
                        try {
                            facebookName = object.getString("name");
                            fullName.setText(facebookName);
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
    }
}