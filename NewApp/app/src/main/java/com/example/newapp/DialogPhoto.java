package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DialogPhoto extends AppCompatActivity {

    String url = "";
    String name = "";
    ImageView mImageView;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_photo);
        url = getIntent().getStringExtra("URL");
        name = getIntent().getStringExtra("Name");
        mImageView = findViewById(R.id.img);
//        mTextView = findViewById(R.id.textView7);
        Glide.with(this).load(url).crossFade().into(mImageView);

        Log.d("DialogPhoto", "onCreate: URL : "+ url) ;
//        mTextView.setText(name);

        setTitle(name);
    }
}
