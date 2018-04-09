package com.araia.henock.volve.Activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.araia.henock.volve.Fragments.PaymentType;
import com.araia.henock.volve.R;

public class PaymentActivity extends AppCompatActivity {
    Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new PaymentType(), "PaymentType").commit();
    }
}