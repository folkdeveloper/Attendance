package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailsFinal extends AppCompatActivity {

    private String name = "";
    private String phone = "";
    private String fg = "";
    private String program = "";
    private String time = "";
    private String date = "";
    private String japa = "";
    private String reading = "";
    private String area = "";
    private String session = "";
    private String url = "";
    private String source = "";
    private String college = "";
    private String occupation = "";
    private String branch = "";
    private String zone = "";
    private String organisation = "";
    private String tl = "";
    private String level = "";
    private String category = "";
    private String res_interest = "";
    private String origin = "";
    private Context mContext;
    private ImageButton callButton;

    private TextView mTextView1, mTextView2, mTextView3, mTextView4, mTextView5, mTextView6,
            mTextView7, mTextView8, mTextView9, mTextView10, mTextView11, mTextView12, mTextView13,
            mTextView14, mTextView15, mTextView16, mTextView17, mTextView18, mTextView19, mTextView20,
            mTextView21;

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_final);
        name = getIntent().getStringExtra("Name");
        phone = getIntent().getStringExtra("Phone");
        fg = getIntent().getStringExtra("FG");
        program = getIntent().getStringExtra("Program");
        time = getIntent().getStringExtra("Time");
        date = getIntent().getStringExtra("Date");
        japa = getIntent().getStringExtra("Japa");
        reading = getIntent().getStringExtra("Reading");
        area = getIntent().getStringExtra("Area");
        session = getIntent().getStringExtra("Session");
        url = getIntent().getStringExtra("URL");
        source = getIntent().getStringExtra("Source");
        college = getIntent().getStringExtra("College");
        occupation = getIntent().getStringExtra("Occupation");
        branch = getIntent().getStringExtra("Branch");
        zone = getIntent().getStringExtra("Zone");
        organisation = getIntent().getStringExtra("Organisation");
        tl = getIntent().getStringExtra("TL");
        level = getIntent().getStringExtra("Level");
        category = getIntent().getStringExtra("Category");
        res_interest = getIntent().getStringExtra("Res");
        origin = getIntent().getStringExtra("Origin");

        mTextView1 = findViewById(R.id.textView1);
        mTextView2 = findViewById(R.id.textView2);
        mTextView3 = findViewById(R.id.textView3);
        mTextView4 = findViewById(R.id.textView4);
        mTextView5 = findViewById(R.id.textView5);
        mTextView6 = findViewById(R.id.textView6);
        mTextView7 = findViewById(R.id.textView7);
        mTextView8 = findViewById(R.id.textView8);
        mTextView9 = findViewById(R.id.textView9);
        mTextView10 = findViewById(R.id.textView10);
        mTextView11 = findViewById(R.id.textView11);
        mTextView12 = findViewById(R.id.textView12);
        mTextView13 = findViewById(R.id.textView13);
        mTextView14 = findViewById(R.id.textView14);
        mTextView15 = findViewById(R.id.textView15);
        mTextView16 = findViewById(R.id.textView16);
        mTextView17 = findViewById(R.id.textView17);
        mTextView18 = findViewById(R.id.textView18);
        mTextView19 = findViewById(R.id.textView19);
        mTextView20 = findViewById(R.id.textView20);
        mTextView21 = findViewById(R.id.textView21);
        callButton = findViewById(R.id.call);

        mImageView = findViewById(R.id.imageView);

        mTextView1.setText(name);
        mTextView2.setText("Phone: " + phone);
        mTextView3.setText("FG: " + fg);
        mTextView4.setText("Program: " + program);
        mTextView5.setText("Time: " + time);
        mTextView6.setText("Date: " + date);
        mTextView7.setText("Japa: " + japa);
        mTextView8.setText("Reading: " + reading);
        mTextView9.setText("Area: " + area);
        mTextView10.setText("Session: " + session);
        mTextView11.setText("Source: " + source);
        mTextView12.setText("Branch: " + branch);
        mTextView13.setText("College: " + college);
        mTextView14.setText("Occupation: " + occupation);
        mTextView15.setText("Zone: " + zone);
        mTextView16.setText("TL: " + tl);
        mTextView17.setText("Level: " + level);
        mTextView18.setText("Organisation: " + organisation);
        mTextView19.setText("Category: " + category);
        mTextView20.setText("Residency Interest: " + res_interest);
        if (res_interest.equals("Yes")) {
            mTextView20.setBackgroundColor(20);
        }
        mTextView21.setText("Origin: " + origin);

        if(url.equals("")) {
            url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
        }

        Glide.with(this).load(url).crossFade().into(mImageView);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsFinal.this, DialogPhoto.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                bundle.putString("URL", url);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+phone.trim()));
                startActivity(callIntent);
            }
        });
    }
}