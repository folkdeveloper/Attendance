package com.example.newapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.TreeMap;

import javax.annotation.Nullable;

public class TLActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference fgboys;
    private String collection = "";
    ListView mListView;
    public static String session = "";
    public static String spinPrograms = "";
    public static String spinCategories = "";
    private long date1 = 0, date2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tl);
        collection = getIntent().getStringExtra("Collection");
        db = FirebaseFirestore.getInstance();
        fgboys = db.collection(collection);
        mListView = findViewById(R.id.list_tl);
        session = getIntent().getStringExtra("Session");
        date1 = getIntent().getLongExtra("Date1",date1);
        date2 = getIntent().getLongExtra("Date2",date2);
        spinPrograms= getIntent().getStringExtra("SpinPrograms");
        spinCategories= getIntent().getStringExtra("SpinCategories");

        Log.d("TLActivity", "onCreate: Spinner: " + spinPrograms);
    }

    @Override
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
                                if (count.containsKey(note.getZtl())){
                                    int number = count.get(note.getZtl());
                                    number++;
                                    count.put(note.getZtl(),number);
                                } else {
                                    count.put(note.getZtl(),1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter(adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLActivity.this,FGActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Session",session);
                                    extras.putLong("Date1",date1);
                                    extras.putLong("Date2",date2);
                                    extras.putString("TL",tl);
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
                                if (count.containsKey(note.getZtl())){
                                    int number = count.get(note.getZtl());
                                    number++;
                                    count.put(note.getZtl(),number);
                                } else {
                                    count.put(note.getZtl(),1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter(adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLActivity.this,FGActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Session",session);
                                    extras.putLong("Date1",date1);
                                    extras.putLong("Date2",date2);
                                    extras.putString("TL",tl);
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
                                if (count.containsKey(note.getZtl())){
                                    int number = count.get(note.getZtl());
                                    number++;
                                    count.put(note.getZtl(),number);
                                } else {
                                    count.put(note.getZtl(),1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter(adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLActivity.this,FGActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Session",session);
                                    extras.putLong("Date1",date1);
                                    extras.putLong("Date2",date2);
                                    extras.putString("TL",tl);
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
                                if (count.containsKey(note.getZtl())){
                                    int number = count.get(note.getZtl());
                                    number++;
                                    count.put(note.getZtl(),number);
                                } else {
                                    count.put(note.getZtl(),1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter(adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLActivity.this,FGActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Session",session);
                                    extras.putLong("Date1",date1);
                                    extras.putLong("Date2",date2);
                                    extras.putString("TL",tl);
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
                                if (count.containsKey(note.getZtl())){
                                    int number = count.get(note.getZtl());
                                    number++;
                                    count.put(note.getZtl(),number);
                                } else {
                                    count.put(note.getZtl(),1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter(adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLActivity.this,FGActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Session",session);
                                    extras.putLong("Date1",date1);
                                    extras.putLong("Date2",date2);
                                    extras.putString("TL",tl);
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
                                if (count.containsKey(note.getZtl())){
                                    int number = count.get(note.getZtl());
                                    number++;
                                    count.put(note.getZtl(),number);
                                } else {
                                    count.put(note.getZtl(),1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter(adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLActivity.this,FGActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Session",session);
                                    extras.putLong("Date1",date1);
                                    extras.putLong("Date2",date2);
                                    extras.putString("TL",tl);
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
                                if (count.containsKey(note.getZtl())){
                                    int number = count.get(note.getZtl());
                                    number++;
                                    count.put(note.getZtl(),number);
                                } else {
                                    count.put(note.getZtl(),1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter(adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLActivity.this,FGActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Session",session);
                                    extras.putLong("Date1",date1);
                                    extras.putLong("Date2",date2);
                                    extras.putString("TL",tl);
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
                                if (count.containsKey(note.getZtl())){
                                    int number = count.get(note.getZtl());
                                    number++;
                                    count.put(note.getZtl(),number);
                                } else {
                                    count.put(note.getZtl(),1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter(adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLActivity.this,FGActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Session",session);
                                    extras.putLong("Date1",date1);
                                    extras.putLong("Date2",date2);
                                    extras.putString("TL",tl);
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
        Intent intent = new Intent(TLActivity.this, DateSelector.class);
        startActivity(intent);
    }
}