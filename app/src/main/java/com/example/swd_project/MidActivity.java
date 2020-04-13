package com.example.swd_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MidActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mid);
    }

    public void ClicktoShowListCourse(View view) {
        Intent intent =  new Intent(getApplicationContext(),ShowListCourseActivity.class);
        startActivity(intent);
    }

    public void clickToShowListProgram(View view) {
        Intent intent =  new Intent(getApplicationContext(),ShowListProgramActivity.class);
        startActivity(intent);
    }

    public void clickToLogout(View view) {
        sharedPreferences =  this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        finishAffinity();
        startActivity(intent);
    }
}
