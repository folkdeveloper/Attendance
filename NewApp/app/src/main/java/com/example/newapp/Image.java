package com.example.newapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Image extends AppCompatDialogFragment {

    String url;
    private Context mContext;
    private int mResource;
    private ImageView showImage;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

//        ImageView showImage = new ImageView(getActivity());
//        builder.setView(showImage);
        url = getArguments().getString("URL");

        builder.setNegativeButton("ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface arg0, int arg1)
            {
            }
        });

        return builder.create();

//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.image, null);
//        builder
//                .setView(dialogView)
////                .set
//                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });

//        Glide.with(getActivity()).load(url).crossFade().into(dialogView);
//        return builder.create();
    }
}