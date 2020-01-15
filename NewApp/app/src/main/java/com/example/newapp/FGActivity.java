package com.example.newapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.TreeMap;

import javax.annotation.Nullable;

public class FGActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference fgboys;
    private String collection = "";
    ListView mListView;
    public static String session = "";
//    public static String sub_purpose = "";
    public static String tl = "";
    public static String date = "";
    public static String spinPrograms = "";
    public static String spinCategories = "";
    private long date1 = 0, date2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fgs);
        collection = getIntent().getStringExtra("Collection");
        db = FirebaseFirestore.getInstance();
        fgboys = db.collection(collection);
        mListView = findViewById(R.id.list_fgs);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        session = extras.getString("Session");
        tl = extras.getString("TL");
        date1 = getIntent().getLongExtra("Date1",date1);
        date2 = getIntent().getLongExtra("Date2",date2);
        date = extras.getString("Date");
        spinPrograms= getIntent().getStringExtra("SpinPrograms");
        spinCategories= getIntent().getStringExtra("SpinCategories");
    }

    protected void onStart() {
        super.onStart();

        if ((spinPrograms.equals("ALL")) && (spinCategories.equals("ALL"))){
            populateListProgramsAndCategories();
        }

        else if ((spinPrograms.equals("ALL")) && (!spinCategories.equals("ALL"))) {
            populateListPrograms();
        }

        else if ((!spinPrograms.equals("ALL")) && (spinCategories.equals("ALL"))) {
            populateListCategories();
        }

        else {
            populateList();
        }
    }

    public void populateListProgramsAndCategories() {
        if (session.equals("ALL")) {
            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("ztl",tl)
                    .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);
                                if (count.containsKey(note.getFg())){
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(),number);
                                } else {
                                    count.put(note.getFg(),1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter(adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(FGActivity.this,LevelActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Session",session);
                                    extras.putLong("Date1",date1);
                                    extras.putLong("Date2",date2);
                                    extras.putString("TL",tl);
                                    extras.putString("FG",fg);
                                    extras.putString("SpinPrograms",spinPrograms);
                                    extras.putString("SpinCategories",spinCategories);
                                    extras.putString("Collection",collection);
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        } else {
            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("session",session)
                    .whereEqualTo("ztl",tl)
                    .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);
                                if (count.containsKey(note.getFg())){
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(),number);
                                } else {
                                    count.put(note.getFg(),1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter(adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(FGActivity.this,LevelActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Session",session);
                                    extras.putString("TL",tl);
                                    extras.putString("FG",fg);
                                    extras.putLong("Date1",date1);
                                    extras.putLong("Date2",date2);
                                    extras.putString("SpinPrograms",spinPrograms);
                                    extras.putString("SpinCategories",spinCategories);
                                    extras.putString("Collection",collection);
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
    }

    public void populateListPrograms() {
        if (session.equals("ALL")) {
            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("ztl",tl)
                    .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);
                                if (count.containsKey(note.getFg())){
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(),number);
                                } else {
                                    count.put(note.getFg(),1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter(adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(FGActivity.this,LevelActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Session",session);
                                    extras.putLong("Date1",date1);
                                    extras.putLong("Date2",date2);
                                    extras.putString("TL",tl);
                                    extras.putString("FG",fg);
                                    extras.putString("SpinPrograms",spinPrograms);
                                    extras.putString("SpinCategories",spinCategories);
                                    extras.putString("Collection",collection);
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        } else {
            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("session",session)
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("ztl",tl)
                    .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);
                                if (count.containsKey(note.getFg())){
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(),number);
                                } else {
                                    count.put(note.getFg(),1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter(adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(FGActivity.this,LevelActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Session",session);
                                    extras.putLong("Date1",date1);
                                    extras.putLong("Date2",date2);
                                    extras.putString("TL",tl);
                                    extras.putString("FG",fg);
                                    extras.putString("SpinPrograms",spinPrograms);
                                    extras.putString("SpinCategories",spinCategories);
                                    extras.putString("Collection",collection);
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
    }

    public void populateListCategories() {
        if (session.equals("ALL")) {
            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("ztl",tl)
                    .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);
                                if (count.containsKey(note.getFg())){
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(),number);
                                } else {
                                    count.put(note.getFg(),1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter(adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(FGActivity.this,LevelActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Session",session);
                                    extras.putLong("Date1",date1);
                                    extras.putLong("Date2",date2);
                                    extras.putString("TL",tl);
                                    extras.putString("FG",fg);
                                    extras.putString("SpinPrograms",spinPrograms);
                                    extras.putString("SpinCategories",spinCategories);
                                    extras.putString("Collection",collection);
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        } else {
            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("session",session)
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("ztl",tl)
                    .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);
                                if (count.containsKey(note.getFg())){
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(),number);
                                } else {
                                    count.put(note.getFg(),1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter(adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(FGActivity.this,LevelActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Session",session);
                                    extras.putLong("Date1",date1);
                                    extras.putLong("Date2",date2);
                                    extras.putString("TL",tl);
                                    extras.putString("FG",fg);
                                    extras.putString("SpinPrograms",spinPrograms);
                                    extras.putString("SpinCategories",spinCategories);
                                    extras.putString("Collection",collection);
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
    }

    public void populateList() {
        if (session.equals("ALL")) {
            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("ztl",tl)
                    .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);
                                if (count.containsKey(note.getFg())){
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(),number);
                                } else {
                                    count.put(note.getFg(),1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter(adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(FGActivity.this,LevelActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Session",session);
                                    extras.putLong("Date1",date1);
                                    extras.putLong("Date2",date2);
                                    extras.putString("TL",tl);
                                    extras.putString("FG",fg);
                                    extras.putString("SpinPrograms",spinPrograms);
                                    extras.putString("SpinCategories",spinCategories);
                                    extras.putString("Collection",collection);
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        } else {
            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("session",session)
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("ztl",tl)
                    .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);
                                if (count.containsKey(note.getFg())){
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(),number);
                                } else {
                                    count.put(note.getFg(),1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter(adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(FGActivity.this,LevelActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Session",session);
                                    extras.putLong("Date1",date1);
                                    extras.putLong("Date2",date2);
                                    extras.putString("TL",tl);
                                    extras.putString("FG",fg);
                                    extras.putString("SpinPrograms",spinPrograms);
                                    extras.putString("SpinCategories",spinCategories);
                                    extras.putString("Collection",collection);
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
    }

    public void select1(View v) {
        Intent intent = new Intent(FGActivity.this, DateSelector.class);
        startActivity(intent);
    }
}
