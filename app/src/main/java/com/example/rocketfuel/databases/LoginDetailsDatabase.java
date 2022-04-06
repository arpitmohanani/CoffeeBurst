package com.example.rocketfuel.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.rocketfuel.interfaces.LoginDetailsDao;
import com.example.rocketfuel.model.LoginDetails;

@Database(entities = {LoginDetails.class}, version = 1, exportSchema = false)
public abstract class LoginDetailsDatabase extends RoomDatabase {
    public abstract LoginDetailsDao loginDetailsDao();
}
