package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class ColorDetailsBlank extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference fgboys = db.collection("AttendanceDemo");
    ListView mListView;
    TextView mTextView;
    public static String blank = "";
    public static String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_details_blank);
        mListView = findViewById(R.id.list_view);
        mTextView = findViewById(R.id.totalAmount);
        date = getIntent().getStringExtra("Date");
        mTextView.setText(date);
    }

    protected void onStart() {
        super.onStart();

//        mTextView.setText(date);

        fgboys
                .whereEqualTo("zzdate",date)
                .orderBy("name")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        Log.d("Details", "onEvent: Out");
                        String data = "";
                        ArrayList<Note> details = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            details.add(note);

//                            if (note.getColor() == null) {
//                                note.setColor("blank");
//                                Log.d("Details", "onEvent: Event"+note.area + note.fg);
//                                details.add(note);
//                            } else {
//                                continue;
//                            }
                        }
                        DetailsAdapter adapterD = new DetailsAdapter(ColorDetailsBlank.this,R.layout.details_layout,details);
                        mListView.setAdapter(adapterD);
                        View headerView = getLayoutInflater().inflate(R.layout.details_header,null);
                        mListView.addHeaderView(headerView);
                    }
                });
    }
}