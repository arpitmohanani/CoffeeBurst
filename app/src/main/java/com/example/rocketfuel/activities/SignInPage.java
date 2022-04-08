package com.example.rocketfuel.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rocketfuel.R;
import com.example.rocketfuel.databases.LoginDetailsDatabase;
import com.example.rocketfuel.interfaces.LoginDetailsDao;
import com.example.rocketfuel.model.LoginDetails;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignInPage extends AppCompatActivity {

    ImageView signBtnGoogle;
    ImageView signBtnFacebook;
    MaterialButton btnSignUp;
    MaterialButton btnLogin;
    EditText editTextUsername;
    EditText editTextPassword;

    CallbackManager callbackManager;
    AccessToken facebookAccessToken;

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    GoogleSignInAccount googleSignInAccount;
    ActivityResultLauncher<Intent> activityResultLauncher;
    Intent openDisplayStoreActivity;
    Bundle bundle = new Bundle();
    boolean isLoginSuccess = false;

    LoginDetailsDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        signBtnGoogle = findViewById(R.id.imgGoogle);
        signBtnFacebook = findViewById(R.id.imgFacebook);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        editTextUsername = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);

        db = Room.databaseBuilder(getApplicationContext(),LoginDetailsDatabase.class,"LoginDetails.db").build();
        LoginDetailsDao loginDetailsDao = db.loginDetailsDao();
        ExecutorService executorService = Executors.newSingleThreadExecutor();


        btnSignUp.setOnClickListener((View view) -> {
                startActivity(new Intent(SignInPage.this, SignUpPage.class));
                finish();
        });

        //Local SignIn
        btnLogin.setOnClickListener((View view) -> {
            if(editTextUsername.getText().toString().isEmpty()){
                Toast.makeText(SignInPage.this,"Please enter a username", Toast.LENGTH_SHORT).show();
            }else if(editTextPassword.getText().toString().isEmpty()){
                Toast.makeText(SignInPage.this,"Please enter a password", Toast.LENGTH_SHORT).show();
            }else{
                executorService.execute(() -> {
                    try {
                        List<LoginDetails> AllLoginDetails = loginDetailsDao.GetAllCredentials();
                        for(int i = 0 ; i < AllLoginDetails.size(); i++){
                            if(AllLoginDetails.get(i).getUsername().equals(editTextUsername.getText().toString()) && AllLoginDetails.get(i).getPassword().equals(editTextPassword.getText().toString())){
                                bundle.putString("SIGNINTYPE","Local");
                                bundle.putString("FULLNAME",AllLoginDetails.get(i).getFullName());
                                openDisplayStoreActivity = new Intent(SignInPage.this, DisplayStores.class);
                                openDisplayStoreActivity.putExtras(bundle);
                                isLoginSuccess = true;
                                startActivity(openDisplayStoreActivity);
                                finish();
                            }
                        }
                        if(!isLoginSuccess){
                            Handler handler = new Handler();
                            handler.post(new Runnable(){
                                public void run() {
                                    Toast.makeText(SignInPage.this,"Username and Password do not match. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }catch (Exception ex){
                        Log.d("DBDEMO", ex.getMessage());
                    }
                });
            }
        });

        //Facebook SignIn
        FacebookSignInSetup();
        signBtnFacebook.setOnClickListener((View view) -> {
                LoginManager.getInstance().logInWithReadPermissions(SignInPage.this, Arrays.asList("public_profile"));
        });

        //Google SignIn
        GoogleSignInSetup();
        signBtnGoogle.setOnClickListener((View view) -> {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(signInIntent);

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void FacebookSignInSetup(){
        callbackManager = CallbackManager.Factory.create();
        facebookAccessToken = AccessToken.getCurrentAccessToken();
        if(facebookAccessToken!=null && !facebookAccessToken.isExpired()){
            bundle.putString("SIGNINTYPE","Facebook");
//            openHomeActivity = new Intent(SignInPage.this, HomeActivity.class);
            openDisplayStoreActivity = new Intent(SignInPage.this, DisplayStores.class);
            openDisplayStoreActivity.putExtras(bundle);
            startActivity(openDisplayStoreActivity);
            finish();
        }
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        bundle.putString("SIGNINTYPE","Facebook");
//                        openHomeActivity = new Intent(SignInPage.this,HomeActivity.class);
                        openDisplayStoreActivity = new Intent(SignInPage.this, DisplayStores.class);
                        openDisplayStoreActivity.putExtras(bundle);
                        startActivity(openDisplayStoreActivity);
                        finish();
                    }
                    @Override
                    public void onCancel() {
                        // App code
                    }
                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    private void GoogleSignInSetup(){
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);

        // checking if user is already logged in
        if(googleSignInAccount != null){
            bundle.putString("SIGNINTYPE","Google");
//            openHomeActivity = new Intent(SignInPage.this,HomeActivity.class);
            openDisplayStoreActivity = new Intent(SignInPage.this, DisplayStores.class);
            openDisplayStoreActivity.putExtras(bundle);
            startActivity(openDisplayStoreActivity);
            finish();
        }
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        //getting signed in account after user selected an account from google accounts dialog
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        handleGoogleSignInTask(task);
                    }
                });
    }

    private void handleGoogleSignInTask(Task<GoogleSignInAccount> task){
        try {
            GoogleSignInAccount acct = task.getResult(ApiException.class);

            String personName = acct.getDisplayName();
//            String personGivenName = acct.getGivenName();
//            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
//            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            //opening HomeActivity
            bundle.putString("SIGNINTYPE","Google");
//            openHomeActivity = new Intent(SignInPage.this,HomeActivity.class);
            openDisplayStoreActivity = new Intent(SignInPage.this, DisplayStores.class);
            openDisplayStoreActivity.putExtras(bundle);
            startActivity(openDisplayStoreActivity);
            finish();
        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed or Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}