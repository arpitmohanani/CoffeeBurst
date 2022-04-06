package com.example.rocketfuel.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rocketfuel.R;
import com.example.rocketfuel.databases.LoginDetailsDatabase;
import com.example.rocketfuel.interfaces.LoginDetailsDao;
import com.example.rocketfuel.model.LoginDetails;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUpPage extends AppCompatActivity {

    EditText fullName;
    EditText userName;
    EditText newPassword;
    EditText confirmPassword;
    MaterialButton createAccount;
    boolean signUpSuccess;
    LoginDetailsDatabase db;
    LoginDetails loginDetails;
    boolean isUsernameExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullName = findViewById(R.id.fullName);
        userName = findViewById(R.id.editTextUserName);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        createAccount = findViewById(R.id.btnCreateAccount);
        signUpSuccess = false;

        fullName.setText("Arpit");
        userName.setText("arpit");
        newPassword.setText("12345678");
        confirmPassword.setText("12345678");

        db = Room.databaseBuilder(getApplicationContext(),LoginDetailsDatabase.class,"LoginDetails.db").build();
        LoginDetailsDao loginDetailsDao = db.loginDetailsDao();
        ExecutorService executorService = Executors.newSingleThreadExecutor();



        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fullName.getText().toString().isEmpty()){
                    Toast.makeText(SignUpPage.this,"Please enter your full name", Toast.LENGTH_SHORT).show();
                }else if(userName.getText().toString().isEmpty()){
                    Toast.makeText(SignUpPage.this,"Please enter a username", Toast.LENGTH_SHORT).show();
                }else if(isSpecialCharacterPresent(userName.getText().toString())){
                    Toast.makeText(SignUpPage.this,"UserName must not contain any Special character", Toast.LENGTH_SHORT).show();
                }else if(userName.getText().toString().contains(" ")){
                    Toast.makeText(SignUpPage.this,"UserName must not contain any spaces", Toast.LENGTH_SHORT).show();
                }else if(newPassword.getText().toString().isEmpty()){
                    Toast.makeText(SignUpPage.this,"Please enter a password", Toast.LENGTH_SHORT).show();
                }else if(newPassword.getText().toString().length() < 8){
                    Toast.makeText(SignUpPage.this,"Password must contain atleast 8 characters", Toast.LENGTH_SHORT).show();
                }else if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())){
                    Toast.makeText(SignUpPage.this,"Password does not match with confirmed password", Toast.LENGTH_SHORT).show();
                }else{
                    Handler handler = new Handler();
                    executorService.execute(() -> {
                        try {

                            isUsernameExists = false;
                            List<LoginDetails> AllLoginDetails = loginDetailsDao.GetAllCredentials();
                            for(int i = 0 ; i < AllLoginDetails.size(); i++){
                                if(AllLoginDetails.get(i).getUsername().equals(userName.getText().toString())){
                                    isUsernameExists = true;
                                    break;
                                }
                            }
                            if(!isUsernameExists){
                                loginDetails = new LoginDetails(userName.getText().toString(),fullName.getText().toString(),newPassword.getText().toString());
                                loginDetailsDao.insertOneCredential(loginDetails);
                                handler.post(new Runnable(){
                                    public void run() {
                                        Toast.makeText(SignUpPage.this,"Account created successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                startActivity(new Intent(SignUpPage.this,SignInPage.class));
                                finish();
                            }else{
                                handler.post(new Runnable(){
                                    public void run() {
                                        Toast.makeText(SignUpPage.this,"Username unavailable. Try using some other username", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }catch (Exception ex){
                            Log.d("DBDEMO", ex.getMessage());
                        }
                    });
                }
            }
        });
    }


    public static boolean isSpecialCharacterPresent(String userName) {
        String specialCharactersString = "!@#$%&*()'+,-./:;<=>?[]^_`{|}";
        for (int i=0; i < userName.length() ; i++)
        {
            char ch = userName.charAt(i);
            if(specialCharactersString.contains(Character.toString(ch))) {
                return true;
            }
        }
        return false;
    }
}