package com.example.furnitures.calculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.example.furnitures.R;
import com.example.furnitures.calculator.furniture.FurnitureFragment;

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
public class CalculatorActivity
    extends AppCompatActivity {

    @NonNull
    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, CalculatorActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        //Toolbar toolbar = findViewById(R.id.activity_calculator__toolbar);
        //setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager()//
                .beginTransaction()//
                .add(R.id.activity_calculator__container, FurnitureFragment.newInstance(), FurnitureFragment.class.getName())//
                .commit();
        }
    }
}
