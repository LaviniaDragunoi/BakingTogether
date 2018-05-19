package com.example.user.bakingtogether.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.R;

public class StepActivity extends AppCompatActivity {

    private static final int DEFAULT_ID = -1;
    private AppRoomDatabase roomDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        roomDB = AppRoomDatabase.getsInstance(this);


    }

}
