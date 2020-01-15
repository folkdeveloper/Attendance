package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }

    public void select1(View v) {
        Intent intent = new Intent(FirstActivity.this, DateSelector.class);
        Bundle bundle = new Bundle();
        bundle.putString("Collection","AttendanceDemo");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void select2(View v) {
        Intent intent = new Intent(FirstActivity.this, DateSelector.class);
        Bundle bundle = new Bundle();
        bundle.putString("Collection","RegistrationDemo");
        intent.putExtras(bundle);
        startActivity(intent);
    }
}