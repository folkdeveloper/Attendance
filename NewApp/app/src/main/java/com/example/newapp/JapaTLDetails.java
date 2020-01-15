package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class JapaTLDetails extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference fgboys = db.collection("AttendanceDemo");
    ListView mListView;
    public static String tl = "";
    public static String fg = "";
    private long date1 = 0, date2 = 0;
    public static String japa = "";
    private String url = "";
    public static String spinPrograms = "";
    public static String spinCategories = "";
    public static String spinSessions = "";
    EditText searchFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_japa_tldetails);
        mListView = findViewById(R.id.list_view);
        tl = getIntent().getStringExtra("TL");
        fg = getIntent().getStringExtra("FG");
        date1 = getIntent().getLongExtra("Date1",date1);
        date2 = getIntent().getLongExtra("Date2",date2);
        japa = getIntent().getStringExtra("Japa");
        spinPrograms= getIntent().getStringExtra("SpinPrograms");
        spinCategories= getIntent().getStringExtra("SpinCategories");
        spinSessions= getIntent().getStringExtra("SpinSessions");
        searchFilter = findViewById(R.id.searchFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if ((spinPrograms.equals("ALL")) && (spinCategories.equals("ALL")) && (spinSessions.equals("ALL"))) {
            populateListProgramsAndCategoriesAndSessions();
        } else if ((spinPrograms.equals("ALL")) && (spinCategories.equals("ALL")) && (!spinSessions.equals("ALL"))) {
            populateListProgramsAndCategories();
        } else if ((spinPrograms.equals("ALL")) && (!spinCategories.equals("ALL")) && (spinSessions.equals("ALL"))) {
            populateListProgramsAndSessions();
        } else if ((!spinPrograms.equals("ALL")) && (spinCategories.equals("ALL")) && (spinSessions.equals("ALL"))){
            populateListCategoriesAndSessions();
        } else if ((spinPrograms.equals("ALL")) && (!spinCategories.equals("ALL")) && (!spinSessions.equals("ALL"))) {
            populateListPrograms();
        } else if ((!spinPrograms.equals("ALL")) && (spinCategories.equals("ALL")) && (!spinSessions.equals("ALL"))) {
            populateListCategories();
        } else if ((!spinPrograms.equals("ALL")) && (!spinCategories.equals("ALL")) && (spinSessions.equals("ALL"))) {
            populateListSessions();
        } else if ((!spinPrograms.equals("ALL")) && (!spinCategories.equals("ALL")) && (!spinSessions.equals("ALL"))) {
            populateList();
        }
    }

    public void populateListProgramsAndCategoriesAndSessions() {
        if (japa.equals("5")) {
            fgboys
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                details.add(note);
                            }
                            final JapaAdapter adapterD = new JapaAdapter(JapaTLDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(JapaTLDetails.this, DetailsFinal.class);
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
                                    bundle.putString("Source",adapterD.getItem(position).getSource());
                                    bundle.putString("College",adapterD.getItem(position).getCollege());
                                    bundle.putString("Occupation",adapterD.getItem(position).getOccupation());
                                    bundle.putString("Branch",adapterD.getItem(position).getBranch());
                                    bundle.putString("Zone",adapterD.getItem(position).getZzone());
                                    bundle.putString("Organisation",adapterD.getItem(position).getOrganization());
                                    bundle.putString("TL",adapterD.getItem(position).getZtl());
                                    bundle.putString("Level",adapterD.getItem(position).getZfl());
                                    bundle.putString("Category",adapterD.getItem(position).getCategory());
                                    if (adapterD.getItem(position).getRes_interest() != null)
                                        bundle.putString("Res",adapterD.getItem(position).getRes_interest());
                                    else
                                        bundle.putString("Res","NA");
                                    if (adapterD.getItem(position).getOrigin() != null)
                                        bundle.putString("Origin",adapterD.getItem(position).getOrigin());
                                    else
                                        bundle.putString("Res","NA");
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
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                switch (japa) {
                                    case "0" :
                                        if ((note.getJapa().equals("0"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "1" :
                                        if ((note.getJapa().equals("1")) || (note.getJapa().equals("2"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "2" :
                                        if ((note.getJapa().equals("3")) || (note.getJapa().equals("4"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "3" :
                                        if ((note.getJapa().equals("5")) || (note.getJapa().equals("6"))
                                                || (note.getJapa().equals("7")) || (note.getJapa().equals("8"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "4" :
                                        if ((note.getJapa().equals("9")) || (note.getJapa().equals("10")) ||
                                                (note.getJapa().equals("11")) || (note.getJapa().equals("12")) ||
                                                (note.getJapa().equals("13")) || (note.getJapa().equals("14")) ||
                                                (note.getJapa().equals("15"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "6" :
                                        if (note.getJapa().equals("16")) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                }
                            }
                            final JapaAdapter adapterD = new JapaAdapter(JapaTLDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(JapaTLDetails.this, DetailsFinal.class);
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
                                    bundle.putString("Source",adapterD.getItem(position).getSource());
                                    bundle.putString("College",adapterD.getItem(position).getCollege());
                                    bundle.putString("Occupation",adapterD.getItem(position).getOccupation());
                                    bundle.putString("Branch",adapterD.getItem(position).getBranch());
                                    bundle.putString("Zone",adapterD.getItem(position).getZzone());
                                    bundle.putString("Organisation",adapterD.getItem(position).getOrganization());
                                    bundle.putString("TL",adapterD.getItem(position).getZtl());
                                    bundle.putString("Level",adapterD.getItem(position).getZfl());
                                    bundle.putString("Category",adapterD.getItem(position).getCategory());
                                    if (adapterD.getItem(position).getRes_interest() != null)
                                        bundle.putString("Res",adapterD.getItem(position).getRes_interest());
                                    else
                                        bundle.putString("Res","NA");
                                    if (adapterD.getItem(position).getOrigin() != null)
                                        bundle.putString("Origin",adapterD.getItem(position).getOrigin());
                                    else
                                        bundle.putString("Res","NA");
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

    public void populateListProgramsAndSessions() {
        if (japa.equals("5")) {
            fgboys
                    .whereEqualTo("category",spinCategories)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                details.add(note);
                            }
                            final JapaAdapter adapterD = new JapaAdapter(JapaTLDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(JapaTLDetails.this, DetailsFinal.class);
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
                                    bundle.putString("Source",adapterD.getItem(position).getSource());
                                    bundle.putString("College",adapterD.getItem(position).getCollege());
                                    bundle.putString("Occupation",adapterD.getItem(position).getOccupation());
                                    bundle.putString("Branch",adapterD.getItem(position).getBranch());
                                    bundle.putString("Zone",adapterD.getItem(position).getZzone());
                                    bundle.putString("Organisation",adapterD.getItem(position).getOrganization());
                                    bundle.putString("TL",adapterD.getItem(position).getZtl());
                                    bundle.putString("Level",adapterD.getItem(position).getZfl());
                                    bundle.putString("Category",adapterD.getItem(position).getCategory());
                                    if (adapterD.getItem(position).getRes_interest() != null)
                                        bundle.putString("Res",adapterD.getItem(position).getRes_interest());
                                    else
                                        bundle.putString("Res","NA");
                                    if (adapterD.getItem(position).getOrigin() != null)
                                        bundle.putString("Origin",adapterD.getItem(position).getOrigin());
                                    else
                                        bundle.putString("Res","NA");
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
                    .whereEqualTo("category",spinCategories)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                switch (japa) {
                                    case "0" :
                                        if ((note.getJapa().equals("0"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "1" :
                                        if ((note.getJapa().equals("1")) || (note.getJapa().equals("2"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "2" :
                                        if ((note.getJapa().equals("3")) || (note.getJapa().equals("4"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "3" :
                                        if ((note.getJapa().equals("5")) || (note.getJapa().equals("6"))
                                                || (note.getJapa().equals("7")) || (note.getJapa().equals("8"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "4" :
                                        if ((note.getJapa().equals("9")) || (note.getJapa().equals("10")) ||
                                                (note.getJapa().equals("11")) || (note.getJapa().equals("12")) ||
                                                (note.getJapa().equals("13")) || (note.getJapa().equals("14")) ||
                                                (note.getJapa().equals("15"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "6" :
                                        if (note.getJapa().equals("16")) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                }
                            }
                            final JapaAdapter adapterD = new JapaAdapter(JapaTLDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(JapaTLDetails.this, DetailsFinal.class);
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
                                    bundle.putString("Source",adapterD.getItem(position).getSource());
                                    bundle.putString("College",adapterD.getItem(position).getCollege());
                                    bundle.putString("Occupation",adapterD.getItem(position).getOccupation());
                                    bundle.putString("Branch",adapterD.getItem(position).getBranch());
                                    bundle.putString("Zone",adapterD.getItem(position).getZzone());
                                    bundle.putString("Organisation",adapterD.getItem(position).getOrganization());
                                    bundle.putString("TL",adapterD.getItem(position).getZtl());
                                    bundle.putString("Level",adapterD.getItem(position).getZfl());
                                    bundle.putString("Category",adapterD.getItem(position).getCategory());
                                    if (adapterD.getItem(position).getRes_interest() != null)
                                        bundle.putString("Res",adapterD.getItem(position).getRes_interest());
                                    else
                                        bundle.putString("Res","NA");
                                    if (adapterD.getItem(position).getOrigin() != null)
                                        bundle.putString("Origin",adapterD.getItem(position).getOrigin());
                                    else
                                        bundle.putString("Res","NA");
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

    public void populateListProgramsAndCategories() {
        if (japa.equals("5")) {
            fgboys
                    .whereEqualTo("session",spinSessions)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                details.add(note);
                            }
                            final JapaAdapter adapterD = new JapaAdapter(JapaTLDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(JapaTLDetails.this, DetailsFinal.class);
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
                                    bundle.putString("Source",adapterD.getItem(position).getSource());
                                    bundle.putString("College",adapterD.getItem(position).getCollege());
                                    bundle.putString("Occupation",adapterD.getItem(position).getOccupation());
                                    bundle.putString("Branch",adapterD.getItem(position).getBranch());
                                    bundle.putString("Zone",adapterD.getItem(position).getZzone());
                                    bundle.putString("Organisation",adapterD.getItem(position).getOrganization());
                                    bundle.putString("TL",adapterD.getItem(position).getZtl());
                                    bundle.putString("Level",adapterD.getItem(position).getZfl());
                                    bundle.putString("Category",adapterD.getItem(position).getCategory());
                                    if (adapterD.getItem(position).getRes_interest() != null)
                                        bundle.putString("Res",adapterD.getItem(position).getRes_interest());
                                    else
                                        bundle.putString("Res","NA");
                                    if (adapterD.getItem(position).getOrigin() != null)
                                        bundle.putString("Origin",adapterD.getItem(position).getOrigin());
                                    else
                                        bundle.putString("Res","NA");
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
                    .whereEqualTo("session",spinSessions)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                switch (japa) {
                                    case "0" :
                                        if ((note.getJapa().equals("0"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "1" :
                                        if ((note.getJapa().equals("1")) || (note.getJapa().equals("2"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "2" :
                                        if ((note.getJapa().equals("3")) || (note.getJapa().equals("4"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "3" :
                                        if ((note.getJapa().equals("5")) || (note.getJapa().equals("6"))
                                                || (note.getJapa().equals("7")) || (note.getJapa().equals("8"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "4" :
                                        if ((note.getJapa().equals("9")) || (note.getJapa().equals("10")) ||
                                                (note.getJapa().equals("11")) || (note.getJapa().equals("12")) ||
                                                (note.getJapa().equals("13")) || (note.getJapa().equals("14")) ||
                                                (note.getJapa().equals("15"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "6" :
                                        if (note.getJapa().equals("16")) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                }
                            }
                            final JapaAdapter adapterD = new JapaAdapter(JapaTLDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(JapaTLDetails.this, DetailsFinal.class);
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
                                    bundle.putString("Source",adapterD.getItem(position).getSource());
                                    bundle.putString("College",adapterD.getItem(position).getCollege());
                                    bundle.putString("Occupation",adapterD.getItem(position).getOccupation());
                                    bundle.putString("Branch",adapterD.getItem(position).getBranch());
                                    bundle.putString("Zone",adapterD.getItem(position).getZzone());
                                    bundle.putString("Organisation",adapterD.getItem(position).getOrganization());
                                    bundle.putString("TL",adapterD.getItem(position).getZtl());
                                    bundle.putString("Level",adapterD.getItem(position).getZfl());
                                    bundle.putString("Category",adapterD.getItem(position).getCategory());
                                    if (adapterD.getItem(position).getRes_interest() != null)
                                        bundle.putString("Res",adapterD.getItem(position).getRes_interest());
                                    else
                                        bundle.putString("Res","NA");
                                    if (adapterD.getItem(position).getOrigin() != null)
                                        bundle.putString("Origin",adapterD.getItem(position).getOrigin());
                                    else
                                        bundle.putString("Res","NA");
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

    public void populateListCategoriesAndSessions() {
        if (japa.equals("5")) {
            fgboys
                    .whereEqualTo("program",spinPrograms)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                details.add(note);
                            }
                            final JapaAdapter adapterD = new JapaAdapter(JapaTLDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(JapaTLDetails.this, DetailsFinal.class);
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
                                    bundle.putString("Source",adapterD.getItem(position).getSource());
                                    bundle.putString("College",adapterD.getItem(position).getCollege());
                                    bundle.putString("Occupation",adapterD.getItem(position).getOccupation());
                                    bundle.putString("Branch",adapterD.getItem(position).getBranch());
                                    bundle.putString("Zone",adapterD.getItem(position).getZzone());
                                    bundle.putString("Organisation",adapterD.getItem(position).getOrganization());
                                    bundle.putString("TL",adapterD.getItem(position).getZtl());
                                    bundle.putString("Level",adapterD.getItem(position).getZfl());
                                    bundle.putString("Category",adapterD.getItem(position).getCategory());
                                    if (adapterD.getItem(position).getRes_interest() != null)
                                        bundle.putString("Res",adapterD.getItem(position).getRes_interest());
                                    else
                                        bundle.putString("Res","NA");
                                    if (adapterD.getItem(position).getOrigin() != null)
                                        bundle.putString("Origin",adapterD.getItem(position).getOrigin());
                                    else
                                        bundle.putString("Res","NA");
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
                    .whereEqualTo("program",spinPrograms)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                switch (japa) {
                                    case "0" :
                                        if ((note.getJapa().equals("0"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "1" :
                                        if ((note.getJapa().equals("1")) || (note.getJapa().equals("2"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "2" :
                                        if ((note.getJapa().equals("3")) || (note.getJapa().equals("4"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "3" :
                                        if ((note.getJapa().equals("5")) || (note.getJapa().equals("6"))
                                                || (note.getJapa().equals("7")) || (note.getJapa().equals("8"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "4" :
                                        if ((note.getJapa().equals("9")) || (note.getJapa().equals("10")) ||
                                                (note.getJapa().equals("11")) || (note.getJapa().equals("12")) ||
                                                (note.getJapa().equals("13")) || (note.getJapa().equals("14")) ||
                                                (note.getJapa().equals("15"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "6" :
                                        if (note.getJapa().equals("16")) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                }
                            }
                            final JapaAdapter adapterD = new JapaAdapter(JapaTLDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(JapaTLDetails.this, DetailsFinal.class);
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
                                    bundle.putString("Source",adapterD.getItem(position).getSource());
                                    bundle.putString("College",adapterD.getItem(position).getCollege());
                                    bundle.putString("Occupation",adapterD.getItem(position).getOccupation());
                                    bundle.putString("Branch",adapterD.getItem(position).getBranch());
                                    bundle.putString("Zone",adapterD.getItem(position).getZzone());
                                    bundle.putString("Organisation",adapterD.getItem(position).getOrganization());
                                    bundle.putString("TL",adapterD.getItem(position).getZtl());
                                    bundle.putString("Level",adapterD.getItem(position).getZfl());
                                    bundle.putString("Category",adapterD.getItem(position).getCategory());
                                    if (adapterD.getItem(position).getRes_interest() != null)
                                        bundle.putString("Res",adapterD.getItem(position).getRes_interest());
                                    else
                                        bundle.putString("Res","NA");
                                    if (adapterD.getItem(position).getOrigin() != null)
                                        bundle.putString("Origin",adapterD.getItem(position).getOrigin());
                                    else
                                        bundle.putString("Res","NA");
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
        if (japa.equals("5")) {
            fgboys
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("session",spinSessions)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                details.add(note);
                            }
                            final JapaAdapter adapterD = new JapaAdapter(JapaTLDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(JapaTLDetails.this, DetailsFinal.class);
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
                                    bundle.putString("Source",adapterD.getItem(position).getSource());
                                    bundle.putString("College",adapterD.getItem(position).getCollege());
                                    bundle.putString("Occupation",adapterD.getItem(position).getOccupation());
                                    bundle.putString("Branch",adapterD.getItem(position).getBranch());
                                    bundle.putString("Zone",adapterD.getItem(position).getZzone());
                                    bundle.putString("Organisation",adapterD.getItem(position).getOrganization());
                                    bundle.putString("TL",adapterD.getItem(position).getZtl());
                                    bundle.putString("Level",adapterD.getItem(position).getZfl());
                                    bundle.putString("Category",adapterD.getItem(position).getCategory());
                                    if (adapterD.getItem(position).getRes_interest() != null)
                                        bundle.putString("Res",adapterD.getItem(position).getRes_interest());
                                    else
                                        bundle.putString("Res","NA");
                                    if (adapterD.getItem(position).getOrigin() != null)
                                        bundle.putString("Origin",adapterD.getItem(position).getOrigin());
                                    else
                                        bundle.putString("Res","NA");
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
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("session",spinSessions)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                switch (japa) {
                                    case "0" :
                                        if ((note.getJapa().equals("0"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "1" :
                                        if ((note.getJapa().equals("1")) || (note.getJapa().equals("2"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "2" :
                                        if ((note.getJapa().equals("3")) || (note.getJapa().equals("4"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "3" :
                                        if ((note.getJapa().equals("5")) || (note.getJapa().equals("6"))
                                                || (note.getJapa().equals("7")) || (note.getJapa().equals("8"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "4" :
                                        if ((note.getJapa().equals("9")) || (note.getJapa().equals("10")) ||
                                                (note.getJapa().equals("11")) || (note.getJapa().equals("12")) ||
                                                (note.getJapa().equals("13")) || (note.getJapa().equals("14")) ||
                                                (note.getJapa().equals("15"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "6" :
                                        if (note.getJapa().equals("16")) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                }
                            }
                            final JapaAdapter adapterD = new JapaAdapter(JapaTLDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(JapaTLDetails.this, DetailsFinal.class);
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
                                    bundle.putString("Source",adapterD.getItem(position).getSource());
                                    bundle.putString("College",adapterD.getItem(position).getCollege());
                                    bundle.putString("Occupation",adapterD.getItem(position).getOccupation());
                                    bundle.putString("Branch",adapterD.getItem(position).getBranch());
                                    bundle.putString("Zone",adapterD.getItem(position).getZzone());
                                    bundle.putString("Organisation",adapterD.getItem(position).getOrganization());
                                    bundle.putString("TL",adapterD.getItem(position).getZtl());
                                    bundle.putString("Level",adapterD.getItem(position).getZfl());
                                    bundle.putString("Category",adapterD.getItem(position).getCategory());
                                    if (adapterD.getItem(position).getRes_interest() != null)
                                        bundle.putString("Res",adapterD.getItem(position).getRes_interest());
                                    else
                                        bundle.putString("Res","NA");
                                    if (adapterD.getItem(position).getOrigin() != null)
                                        bundle.putString("Origin",adapterD.getItem(position).getOrigin());
                                    else
                                        bundle.putString("Res","NA");
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
        if (japa.equals("5")) {
            fgboys
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("session",spinSessions)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                details.add(note);
                            }
                            final JapaAdapter adapterD = new JapaAdapter(JapaTLDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(JapaTLDetails.this, DetailsFinal.class);
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
                                    bundle.putString("Source",adapterD.getItem(position).getSource());
                                    bundle.putString("College",adapterD.getItem(position).getCollege());
                                    bundle.putString("Occupation",adapterD.getItem(position).getOccupation());
                                    bundle.putString("Branch",adapterD.getItem(position).getBranch());
                                    bundle.putString("Zone",adapterD.getItem(position).getZzone());
                                    bundle.putString("Organisation",adapterD.getItem(position).getOrganization());
                                    bundle.putString("TL",adapterD.getItem(position).getZtl());
                                    bundle.putString("Level",adapterD.getItem(position).getZfl());
                                    bundle.putString("Category",adapterD.getItem(position).getCategory());
                                    if (adapterD.getItem(position).getRes_interest() != null)
                                        bundle.putString("Res",adapterD.getItem(position).getRes_interest());
                                    else
                                        bundle.putString("Res","NA");
                                    if (adapterD.getItem(position).getOrigin() != null)
                                        bundle.putString("Origin",adapterD.getItem(position).getOrigin());
                                    else
                                        bundle.putString("Res","NA");
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
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("session",spinSessions)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                switch (japa) {
                                    case "0" :
                                        if ((note.getJapa().equals("0"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "1" :
                                        if ((note.getJapa().equals("1")) || (note.getJapa().equals("2"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "2" :
                                        if ((note.getJapa().equals("3")) || (note.getJapa().equals("4"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "3" :
                                        if ((note.getJapa().equals("5")) || (note.getJapa().equals("6"))
                                                || (note.getJapa().equals("7")) || (note.getJapa().equals("8"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "4" :
                                        if ((note.getJapa().equals("9")) || (note.getJapa().equals("10")) ||
                                                (note.getJapa().equals("11")) || (note.getJapa().equals("12")) ||
                                                (note.getJapa().equals("13")) || (note.getJapa().equals("14")) ||
                                                (note.getJapa().equals("15"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "6" :
                                        if (note.getJapa().equals("16")) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                }
                            }
                            final JapaAdapter adapterD = new JapaAdapter(JapaTLDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(JapaTLDetails.this, DetailsFinal.class);
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
                                    bundle.putString("Source",adapterD.getItem(position).getSource());
                                    bundle.putString("College",adapterD.getItem(position).getCollege());
                                    bundle.putString("Occupation",adapterD.getItem(position).getOccupation());
                                    bundle.putString("Branch",adapterD.getItem(position).getBranch());
                                    bundle.putString("Zone",adapterD.getItem(position).getZzone());
                                    bundle.putString("Organisation",adapterD.getItem(position).getOrganization());
                                    bundle.putString("TL",adapterD.getItem(position).getZtl());
                                    bundle.putString("Level",adapterD.getItem(position).getZfl());
                                    bundle.putString("Category",adapterD.getItem(position).getCategory());
                                    if (adapterD.getItem(position).getRes_interest() != null)
                                        bundle.putString("Res",adapterD.getItem(position).getRes_interest());
                                    else
                                        bundle.putString("Res","NA");
                                    if (adapterD.getItem(position).getOrigin() != null)
                                        bundle.putString("Origin",adapterD.getItem(position).getOrigin());
                                    else
                                        bundle.putString("Res","NA");
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

    public void populateListSessions() {
        if (japa.equals("5")) {
            fgboys
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("program",spinPrograms)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                details.add(note);
                            }
                            final JapaAdapter adapterD = new JapaAdapter(JapaTLDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(JapaTLDetails.this, DetailsFinal.class);
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
                                    bundle.putString("Source",adapterD.getItem(position).getSource());
                                    bundle.putString("College",adapterD.getItem(position).getCollege());
                                    bundle.putString("Occupation",adapterD.getItem(position).getOccupation());
                                    bundle.putString("Branch",adapterD.getItem(position).getBranch());
                                    bundle.putString("Zone",adapterD.getItem(position).getZzone());
                                    bundle.putString("Organisation",adapterD.getItem(position).getOrganization());
                                    bundle.putString("TL",adapterD.getItem(position).getZtl());
                                    bundle.putString("Level",adapterD.getItem(position).getZfl());
                                    bundle.putString("Category",adapterD.getItem(position).getCategory());
                                    if (adapterD.getItem(position).getRes_interest() != null)
                                        bundle.putString("Res",adapterD.getItem(position).getRes_interest());
                                    else
                                        bundle.putString("Res","NA");
                                    if (adapterD.getItem(position).getOrigin() != null)
                                        bundle.putString("Origin",adapterD.getItem(position).getOrigin());
                                    else
                                        bundle.putString("Res","NA");
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
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("program",spinPrograms)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                switch (japa) {
                                    case "0" :
                                        if ((note.getJapa().equals("0"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "1" :
                                        if ((note.getJapa().equals("1")) || (note.getJapa().equals("2"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "2" :
                                        if ((note.getJapa().equals("3")) || (note.getJapa().equals("4"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "3" :
                                        if ((note.getJapa().equals("5")) || (note.getJapa().equals("6"))
                                                || (note.getJapa().equals("7")) || (note.getJapa().equals("8"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "4" :
                                        if ((note.getJapa().equals("9")) || (note.getJapa().equals("10")) ||
                                                (note.getJapa().equals("11")) || (note.getJapa().equals("12")) ||
                                                (note.getJapa().equals("13")) || (note.getJapa().equals("14")) ||
                                                (note.getJapa().equals("15"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "6" :
                                        if (note.getJapa().equals("16")) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                }
                            }
                            final JapaAdapter adapterD = new JapaAdapter(JapaTLDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(JapaTLDetails.this, DetailsFinal.class);
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
                                    bundle.putString("Source",adapterD.getItem(position).getSource());
                                    bundle.putString("College",adapterD.getItem(position).getCollege());
                                    bundle.putString("Occupation",adapterD.getItem(position).getOccupation());
                                    bundle.putString("Branch",adapterD.getItem(position).getBranch());
                                    bundle.putString("Zone",adapterD.getItem(position).getZzone());
                                    bundle.putString("Organisation",adapterD.getItem(position).getOrganization());
                                    bundle.putString("TL",adapterD.getItem(position).getZtl());
                                    bundle.putString("Level",adapterD.getItem(position).getZfl());
                                    bundle.putString("Category",adapterD.getItem(position).getCategory());
                                    if (adapterD.getItem(position).getRes_interest() != null)
                                        bundle.putString("Res",adapterD.getItem(position).getRes_interest());
                                    else
                                        bundle.putString("Res","NA");
                                    if (adapterD.getItem(position).getOrigin() != null)
                                        bundle.putString("Origin",adapterD.getItem(position).getOrigin());
                                    else
                                        bundle.putString("Res","NA");
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
        if (japa.equals("5")) {
            fgboys
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("session",spinSessions)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                details.add(note);
                            }
                            final JapaAdapter adapterD = new JapaAdapter(JapaTLDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(JapaTLDetails.this, DetailsFinal.class);
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
                                    bundle.putString("Source",adapterD.getItem(position).getSource());
                                    bundle.putString("College",adapterD.getItem(position).getCollege());
                                    bundle.putString("Occupation",adapterD.getItem(position).getOccupation());
                                    bundle.putString("Branch",adapterD.getItem(position).getBranch());
                                    bundle.putString("Zone",adapterD.getItem(position).getZzone());
                                    bundle.putString("Organisation",adapterD.getItem(position).getOrganization());
                                    bundle.putString("TL",adapterD.getItem(position).getZtl());
                                    bundle.putString("Level",adapterD.getItem(position).getZfl());
                                    bundle.putString("Category",adapterD.getItem(position).getCategory());
                                    if (adapterD.getItem(position).getRes_interest() != null)
                                        bundle.putString("Res",adapterD.getItem(position).getRes_interest());
                                    else
                                        bundle.putString("Res","NA");
                                    if (adapterD.getItem(position).getOrigin() != null)
                                        bundle.putString("Origin",adapterD.getItem(position).getOrigin());
                                    else
                                        bundle.putString("Res","NA");
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
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("session",spinSessions)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .orderBy("edate")
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

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                switch (japa) {
                                    case "0" :
                                        if ((note.getJapa().equals("0"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "1" :
                                        if ((note.getJapa().equals("1")) || (note.getJapa().equals("2"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "2" :
                                        if ((note.getJapa().equals("3")) || (note.getJapa().equals("4"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "3" :
                                        if ((note.getJapa().equals("5")) || (note.getJapa().equals("6"))
                                                || (note.getJapa().equals("7")) || (note.getJapa().equals("8"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "4" :
                                        if ((note.getJapa().equals("9")) || (note.getJapa().equals("10")) ||
                                                (note.getJapa().equals("11")) || (note.getJapa().equals("12")) ||
                                                (note.getJapa().equals("13")) || (note.getJapa().equals("14")) ||
                                                (note.getJapa().equals("15"))) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                    case "6" :
                                        if (note.getJapa().equals("16")) {
                                            Log.d("Details", "onEvent: Event"+note.japa + note.fg);
                                            details.add(note);
                                        }
                                        break;
                                }
                            }
                            final JapaAdapter adapterD = new JapaAdapter(JapaTLDetails.this,R.layout.details_layout,details);
                            mListView.setAdapter(adapterD);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = adapterD.getItem(position).getName();
                                    Intent intent = new Intent(JapaTLDetails.this, DetailsFinal.class);
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
                                    bundle.putString("Source",adapterD.getItem(position).getSource());
                                    bundle.putString("College",adapterD.getItem(position).getCollege());
                                    bundle.putString("Occupation",adapterD.getItem(position).getOccupation());
                                    bundle.putString("Branch",adapterD.getItem(position).getBranch());
                                    bundle.putString("Zone",adapterD.getItem(position).getZzone());
                                    bundle.putString("Organisation",adapterD.getItem(position).getOrganization());
                                    bundle.putString("TL",adapterD.getItem(position).getZtl());
                                    bundle.putString("Level",adapterD.getItem(position).getZfl());
                                    bundle.putString("Category",adapterD.getItem(position).getCategory());
                                    if (adapterD.getItem(position).getRes_interest() != null)
                                        bundle.putString("Res",adapterD.getItem(position).getRes_interest());
                                    else
                                        bundle.putString("Res","NA");
                                    if (adapterD.getItem(position).getOrigin() != null)
                                        bundle.putString("Origin",adapterD.getItem(position).getOrigin());
                                    else
                                        bundle.putString("Res","NA");
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
        Intent intent = new Intent(JapaTLDetails.this, DateSelector.class);
        startActivity(intent);
    }
}