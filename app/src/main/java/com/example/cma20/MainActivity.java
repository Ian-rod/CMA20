package com.example.cma20;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar appbar=getSupportActionBar();
        appbar.setTitle("\t \t \tLogin Page");
        appbar.setSubtitle("\t \t \tLog in as a Manager or an employee");
        appbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0000cd")));
        setContentView(R.layout.activity_main);
    }
    //Enforce authentication ....firebase persist.....Log out button on every appbar
    public void EmployeePage(View view)
    {
        //animate the login button
        //navigate to Employees page
        //pass in Manager name on appbar th show the active Manager
        Intent EmployeePage=new Intent(getApplicationContext(), EmployeePage.class);
        startActivity(EmployeePage);
    }
}