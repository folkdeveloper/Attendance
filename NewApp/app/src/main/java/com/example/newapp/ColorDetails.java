package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class ColorDetails extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference fgboys;
    private String collection = "";
    ListView mListView;
    public static String color = "";
    private long date1 = 0, date2 = 0;
    public static String fg = "";
    private String url = "";
    public static String spinPrograms = "";
    public static String spinCategories = "";
    EditText searchFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_details);
        collection = getIntent().getStringExtra("Collection");
        db = FirebaseFirestore.getInstance();
        fgboys = db.collection(collection);
        mListView = findViewById(R.id.list_view);
        color = getIntent().getStringExtra("Color");
        date1 = getIntent().getLongExtra("Date1",date1);
        date2 = getIntent().getLongExtra("Date2",date2);
        fg = getIntent().getStringExtra("FG");
        spinPrograms = getIntent().getStringExtra("SpinPrograms");
        spinCategories = getIntent().getStringExtra("SpinCategories");
        searchFilter = findViewById(R.id.searchFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if ((spinPrograms.equals("ALL")) && (spinCategories.equals("ALL"))) {
            populateListProgramsAndCategories();
        } else if ((spinPrograms.equals("ALL")) && (!spinCategories.equals("ALL"))) {
            populateListPrograms();
        } else if ((!spinPrograms.equals("ALL")) && (spinCategories.equals("ALL"))) {
            populateListCategories();
        } else {
            populateList();
        }
    }

    public void populateListProgramsAndCategories() {
        if (color.equals("ALL")) {
            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
                    .orderBy("fg")
                    .whereEqualTo("fg",fg)
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                Log.d("Color", "onEvent: Event"+note.area + note.fg);
                                details.add(note);
                            }
                            final ColorsAdapter adapterD = new ColorsAdapter(ColorDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(ColorDetails.this, DetailsFinal.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("URL",adapterD.getItem(position).getUrl());
                                    bundle.putString("Name",name);
                                    bundle.putString("Phone",adapterD.getItem(position).getZmob());
                                    bundle.putString("FG",adapterD.getItem(position).getFg());
                                    bundle.putString("Program",adapterD.getItem(position).getProgram());
                                    bundle.putString("Time",adapterD.getItem(position).getTime());
                                    bundle.putString("Date",adapterD.getItem(position).getZzdate());
                                    bundle.putString("Japa",adapterD.getItem(position).getJapa());
                                    bundle.putString("Reading",adapterD.getItem(position).getZread());
                                    bundle.putString("Area",adapterD.getItem(position).getArea());
                                    bundle.putString("Session",adapterD.getItem(position).getSession());
                                    bundle.putString("URL",adapterD.getItem(position).getUrl());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });

                            searchFilter.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    adapterD.getFilter().filter(s.toString());
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }
                    });
        } else {
            fgboys
                    .whereEqualTo("color",color)
//                    .whereEqualTo("zzdate",date)
                    .whereEqualTo("fg",fg)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
                    .orderBy("fg")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                Log.d("Color", "onEvent: Event"+note.area + note.fg);
                                details.add(note);
                            }
                            final ColorsAdapter adapterD = new ColorsAdapter(ColorDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(ColorDetails.this, DetailsFinal.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("URL",adapterD.getItem(position).getUrl());
                                    bundle.putString("Name",name);
                                    bundle.putString("Phone",adapterD.getItem(position).getZmob());
                                    bundle.putString("FG",adapterD.getItem(position).getFg());
                                    bundle.putString("Program",adapterD.getItem(position).getProgram());
                                    bundle.putString("Time",adapterD.getItem(position).getTime());
                                    bundle.putString("Date",adapterD.getItem(position).getZzdate());
                                    bundle.putString("Japa",adapterD.getItem(position).getJapa());
                                    bundle.putString("Reading",adapterD.getItem(position).getZread());
                                    bundle.putString("Area",adapterD.getItem(position).getArea());
                                    bundle.putString("Session",adapterD.getItem(position).getSession());
                                    bundle.putString("URL",adapterD.getItem(position).getUrl());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });

                            searchFilter.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    adapterD.getFilter().filter(s.toString());
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }
                    });
        }
    }

    public void populateListPrograms() {
        if (color.equals("ALL")) {
            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereEqualTo("fg",fg)
                    .whereEqualTo("category",spinCategories)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
                    .orderBy("fg")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                Log.d("Color", "onEvent: Event"+note.area + note.fg);
                                details.add(note);
                            }
                            final ColorsAdapter adapterD = new ColorsAdapter(ColorDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(ColorDetails.this, DetailsFinal.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("URL",adapterD.getItem(position).getUrl());
                                    bundle.putString("Name",name);
                                    bundle.putString("Phone",adapterD.getItem(position).getZmob());
                                    bundle.putString("FG",adapterD.getItem(position).getFg());
                                    bundle.putString("Program",adapterD.getItem(position).getProgram());
                                    bundle.putString("Time",adapterD.getItem(position).getTime());
                                    bundle.putString("Date",adapterD.getItem(position).getZzdate());
                                    bundle.putString("Japa",adapterD.getItem(position).getJapa());
                                    bundle.putString("Reading",adapterD.getItem(position).getZread());
                                    bundle.putString("Area",adapterD.getItem(position).getArea());
                                    bundle.putString("Session",adapterD.getItem(position).getSession());
                                    bundle.putString("URL",adapterD.getItem(position).getUrl());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });

                            searchFilter.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    adapterD.getFilter().filter(s.toString());
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }
                    });
        } else {
            fgboys
                    .whereEqualTo("color",color)
//                    .whereEqualTo("zzdate",date)
                    .whereEqualTo("fg",fg)
                    .whereEqualTo("category",spinCategories)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
                    .orderBy("fg")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                Log.d("Color", "onEvent: Event"+note.area + note.fg);
                                details.add(note);
                            }
                            final ColorsAdapter adapterD = new ColorsAdapter(ColorDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(ColorDetails.this, DetailsFinal.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("URL",adapterD.getItem(position).getUrl());
                                    bundle.putString("Name",name);
                                    bundle.putString("Phone",adapterD.getItem(position).getZmob());
                                    bundle.putString("FG",adapterD.getItem(position).getFg());
                                    bundle.putString("Program",adapterD.getItem(position).getProgram());
                                    bundle.putString("Time",adapterD.getItem(position).getTime());
                                    bundle.putString("Date",adapterD.getItem(position).getZzdate());
                                    bundle.putString("Japa",adapterD.getItem(position).getJapa());
                                    bundle.putString("Reading",adapterD.getItem(position).getZread());
                                    bundle.putString("Area",adapterD.getItem(position).getArea());
                                    bundle.putString("Session",adapterD.getItem(position).getSession());
                                    bundle.putString("URL",adapterD.getItem(position).getUrl());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });

                            searchFilter.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    adapterD.getFilter().filter(s.toString());
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }
                    });
        }
    }

    public void populateListCategories() {
        if (color.equals("ALL")) {
            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
                    .orderBy("fg")
                    .whereEqualTo("fg",fg)
                    .whereEqualTo("program",spinPrograms)
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                Log.d("Color", "onEvent: Event"+note.area + note.fg);
                                details.add(note);
                            }
                            final ColorsAdapter adapterD = new ColorsAdapter(ColorDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(ColorDetails.this, DetailsFinal.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("URL",adapterD.getItem(position).getUrl());
                                    bundle.putString("Name",name);
                                    bundle.putString("Phone",adapterD.getItem(position).getZmob());
                                    bundle.putString("FG",adapterD.getItem(position).getFg());
                                    bundle.putString("Program",adapterD.getItem(position).getProgram());
                                    bundle.putString("Time",adapterD.getItem(position).getTime());
                                    bundle.putString("Date",adapterD.getItem(position).getZzdate());
                                    bundle.putString("Japa",adapterD.getItem(position).getJapa());
                                    bundle.putString("Reading",adapterD.getItem(position).getZread());
                                    bundle.putString("Area",adapterD.getItem(position).getArea());
                                    bundle.putString("Session",adapterD.getItem(position).getSession());
                                    bundle.putString("URL",adapterD.getItem(position).getUrl());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });

                            searchFilter.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    adapterD.getFilter().filter(s.toString());
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }
                    });
        } else {
            fgboys
                    .whereEqualTo("color",color)
//                    .whereEqualTo("zzdate",date)
                    .whereEqualTo("fg",fg)
                    .whereEqualTo("program",spinPrograms)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
                    .orderBy("fg")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                Log.d("Color", "onEvent: Event"+note.area + note.fg);
                                details.add(note);
                            }
                            final ColorsAdapter adapterD = new ColorsAdapter(ColorDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(ColorDetails.this, DetailsFinal.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("URL",adapterD.getItem(position).getUrl());
                                    bundle.putString("Name",name);
                                    bundle.putString("Phone",adapterD.getItem(position).getZmob());
                                    bundle.putString("FG",adapterD.getItem(position).getFg());
                                    bundle.putString("Program",adapterD.getItem(position).getProgram());
                                    bundle.putString("Time",adapterD.getItem(position).getTime());
                                    bundle.putString("Date",adapterD.getItem(position).getZzdate());
                                    bundle.putString("Japa",adapterD.getItem(position).getJapa());
                                    bundle.putString("Reading",adapterD.getItem(position).getZread());
                                    bundle.putString("Area",adapterD.getItem(position).getArea());
                                    bundle.putString("Session",adapterD.getItem(position).getSession());
                                    bundle.putString("URL",adapterD.getItem(position).getUrl());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });

                            searchFilter.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    adapterD.getFilter().filter(s.toString());
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }
                    });
        }
    }

    public void populateList() {
        if (color.equals("ALL")) {
            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
                    .orderBy("fg")
                    .whereEqualTo("fg",fg)
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("category",spinCategories)
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                Log.d("Color", "onEvent: Event"+note.area + note.fg);
                                details.add(note);
                            }
                            final ColorsAdapter adapterD = new ColorsAdapter(ColorDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(ColorDetails.this, DetailsFinal.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("URL",adapterD.getItem(position).getUrl());
                                    bundle.putString("Name",name);
                                    bundle.putString("Phone",adapterD.getItem(position).getZmob());
                                    bundle.putString("FG",adapterD.getItem(position).getFg());
                                    bundle.putString("Program",adapterD.getItem(position).getProgram());
                                    bundle.putString("Time",adapterD.getItem(position).getTime());
                                    bundle.putString("Date",adapterD.getItem(position).getZzdate());
                                    bundle.putString("Japa",adapterD.getItem(position).getJapa());
                                    bundle.putString("Reading",adapterD.getItem(position).getZread());
                                    bundle.putString("Area",adapterD.getItem(position).getArea());
                                    bundle.putString("Session",adapterD.getItem(position).getSession());
                                    bundle.putString("URL",adapterD.getItem(position).getUrl());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });

                            searchFilter.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    adapterD.getFilter().filter(s.toString());
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }
                    });
        } else {
            fgboys
                    .whereEqualTo("color",color)
//                    .whereEqualTo("zzdate",date)
                    .whereEqualTo("fg",fg)
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("category",spinCategories)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
                    .orderBy("fg")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                Log.d("Color", "onEvent: Event"+note.area + note.fg);
                                details.add(note);
                            }
                            final ColorsAdapter adapterD = new ColorsAdapter(ColorDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(ColorDetails.this, DetailsFinal.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("URL",adapterD.getItem(position).getUrl());
                                    bundle.putString("Name",name);
                                    bundle.putString("Phone",adapterD.getItem(position).getZmob());
                                    bundle.putString("FG",adapterD.getItem(position).getFg());
                                    bundle.putString("Program",adapterD.getItem(position).getProgram());
                                    bundle.putString("Time",adapterD.getItem(position).getTime());
                                    bundle.putString("Date",adapterD.getItem(position).getZzdate());
                                    bundle.putString("Japa",adapterD.getItem(position).getJapa());
                                    bundle.putString("Reading",adapterD.getItem(position).getZread());
                                    bundle.putString("Area",adapterD.getItem(position).getArea());
                                    bundle.putString("Session",adapterD.getItem(position).getSession());
                                    bundle.putString("URL",adapterD.getItem(position).getUrl());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });

                            searchFilter.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    adapterD.getFilter().filter(s.toString());
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }
                    });
        }
    }

    public void select1(View v) {
        Intent intent = new Intent(ColorDetails.this, DateSelector.class);
        startActivity(intent);
    }
}