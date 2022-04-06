package com.example.rocketfuel.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "loginDetails")
public class LoginDetails {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "username")
    private String Username;

    @ColumnInfo(name = "fullname")
    private String FullName;

    @ColumnInfo(name = "password")
    private String Password;

    public LoginDetails(){

    }

    public LoginDetails(@NonNull String username, String fullname, String password) {
        Username = username;
        FullName = fullname;
        Password = password;
    }

    @NonNull
    public String getUsername() {
        return Username;
    }

    public void setUsername(@NonNull String username) {
        Username = username;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
