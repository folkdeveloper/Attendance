package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.TimedMetaData;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.annotation.Nullable;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class TimeActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference fgboys;
    private String collection = "";
    private static final String TAG = "MainActivity";
    ListView mListView;
    private TextView mTextView;
    private long date1 = 0, date2 = 0;
    private int total = 0, number1 = 0, number2 = 0, number3 = 0, number4 = 0, number5 = 0,
            number6 = 0, number7 = 0, number8 = 0, number9 = 0, number10 = 0, number11 = 0,
            number12 = 0, number13 = 0, number14 = 0, number15 = 0, number16 = 0, number17 = 0,
            number18 = 0, number19 = 0, number20 = 0, number21 = 0, number22 = 0, number23 = 0,
            number24 = 0, number25 = 0, number26 = 0, number27 = 0, number28 = 0, number29 = 0,
            number30 = 0, number31 = 0, number32 = 0, number33 = 0, number34 = 0, number35 = 0,
            number36 = 0, number37 = 0, number38 = 0, number39 = 0, number40 = 0, number41 = 0,
            number42 = 0, number43 = 0, number44 = 0, number45 = 0, number46 = 0, number47 = 0,
            number48 = 0;
    private String url = "";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String spinnerPrograms = "";
    private String spinnerCategories = "";
    private String spinnerSessions = "";
    List<String> programsList;
    List<String> categoriesList;
    List<String> sessionsList;
    final ArrayList<String> finalPrograms = new ArrayList<String>();
    ArrayList<String> finalCategories = new ArrayList<String>();
    ArrayList<String> finalSessions = new ArrayList<String>();
    Button btn;
    ArrayAdapter<String> adapterPrograms;
    ArrayAdapter<String> adapterCategories;
    ArrayAdapter<String> adapterSessions;
    String spinProgram = "";
    String spinCategory = "";
    final String[] categories = new String[]{"ALL"};
    final String[] sessions = new String[]{"ALL"};
    String[] programs = new String[]{"ALL"};
    Spinner program, category, session;
    int posProgram, posCategory, posSession;
    private String date1Str = "", date2Str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        collection = getIntent().getStringExtra("Collection");
        db = FirebaseFirestore.getInstance();
        fgboys = db.collection(collection);
        mTextView = findViewById(R.id.textView2);
        mTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mListView = findViewById(R.id.list_item);
        date1 = getIntent().getLongExtra("Date1",date1);
        date2 = getIntent().getLongExtra("Date2",date2);
        date1Str = getIntent().getStringExtra("Date1Str");
        date2Str = getIntent().getStringExtra("Date2Str");

        programsList = Arrays.asList(programs);
        finalPrograms.addAll(programsList);

        program = findViewById(R.id.program);
        category = findViewById(R.id.category);
        session = findViewById(R.id.session);

        adapterPrograms = new ArrayAdapter<String>(
                this,R.layout.spinner_item,finalPrograms);
        adapterPrograms.setDropDownViewResource(R.layout.spinner_item);
        program.setAdapter(adapterPrograms);

//        mTextView.setText(date1Str + date2Str);

        fgboys
//                .whereEqualTo("zzdate", date)
                .whereGreaterThanOrEqualTo("edate", date1)
                .whereLessThanOrEqualTo("edate", date2)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        TreeMap<String, Integer> count = new TreeMap<>();

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);

                            if (note.getProgram() == null)
                                continue;

                            if (finalPrograms.contains(note.getProgram())) {
                                continue;
                            } else {
                                finalPrograms.add(note.getProgram());
                                adapterPrograms.notifyDataSetChanged();
                            }
                        }
                    }
                });

        program.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPrograms = parent.getItemAtPosition(position).toString();
                posProgram = position;

                if (spinnerPrograms.equals("ALL")) {
                    spinProgram = "1";
                    populateCategories(spinProgram);
                } else {
                    spinProgram = "2";
                    populateCategories(spinProgram);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void populateCategories(String spinProgram) {
        final String spinPrograms = spinnerPrograms;

        if (spinProgram.equals("1")) {
            finalCategories = new ArrayList<String>();
            categoriesList = Arrays.asList(categories);
            finalCategories.addAll(categoriesList);

            adapterCategories = new ArrayAdapter<String>
                    (this,R.layout.spinner_item,finalCategories);

            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getCategory() == null)
                                    continue;

                                if (finalCategories.contains(note.getCategory())) {
                                    continue;
                                } else {
                                    finalCategories.add(note.getCategory());
                                    adapterCategories.notifyDataSetChanged();
                                }

                            }
                        }
                    });
            adapterCategories.setDropDownViewResource(R.layout.spinner_item);
            category.setAdapter(adapterCategories);

            category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerCategories = parent.getItemAtPosition(position).toString();
                    posCategory = position;

                    if (spinnerCategories.equals("ALL")) {
                        spinCategory = "1";
                        populateSessionsPrograms(spinCategory,spinPrograms);
                    } else {
                        spinCategory = "2";
                        populateSessionsPrograms(spinCategory,spinPrograms);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else {
            finalCategories = new ArrayList<String>();
            categoriesList = Arrays.asList(categories);
            finalCategories.addAll(categoriesList);

            adapterCategories = new ArrayAdapter<String>
                    (this,R.layout.spinner_item,finalCategories);

            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("program",spinPrograms)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getCategory() == null)
                                    continue;

                                if (finalCategories.contains(note.getCategory())) {
                                    continue;
                                } else {
                                    finalCategories.add(note.getCategory());
                                    adapterCategories.notifyDataSetChanged();
                                }

                            }
                        }
                    });
            adapterCategories.setDropDownViewResource(R.layout.spinner_item);
            category.setAdapter(adapterCategories);

            category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerCategories = parent.getItemAtPosition(position).toString();
                    posCategory = position;

                    if (spinnerCategories.equals("ALL")) {
                        spinCategory = "1";
                        populateSessions(spinCategory,spinPrograms);
                    } else {
                        spinCategory = "2";
                        populateSessions(spinCategory,spinPrograms);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public void populateSessionsPrograms(String spinCategory, String spinProgram) {
        final String spinCategories = spinCategory;
        final String spinPrograms = spinProgram;

        if (spinCategory.equals("1")) {
            finalSessions = new ArrayList<String>();
            sessionsList = Arrays.asList(sessions);
            finalSessions.addAll(sessionsList);

            adapterSessions = new ArrayAdapter<String>
                    (this, R.layout.spinner_item, finalSessions);

            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getSession() == null)
                                    continue;

                                if (finalSessions.contains(note.getSession())) {
                                    continue;
                                } else {
                                    finalSessions.add(note.getSession());
                                    adapterSessions.notifyDataSetChanged();
                                }

                            }
                        }
                    });
            adapterSessions.setDropDownViewResource(R.layout.spinner_item);
            session.setAdapter(adapterSessions);

            session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerSessions = parent.getItemAtPosition(position).toString();
                    posSession = position;

                    number1 = 0; number2 = 0; number3 = 0; number4 = 0; number5 = 0;
                    number6 = 0; number7 = 0; number8 = 0; number9 = 0; number10 = 0; number11 = 0;
                    number12 = 0; number13 = 0; number14 = 0; number15 = 0; number16 = 0; number17 = 0;
                    number18 = 0; number19 = 0; number20 = 0; number21 = 0; number22 = 0; number23 = 0;
                    number24 = 0; number25 = 0; number26 = 0; number27 = 0; number28 = 0; number29 = 0;
                    number30 = 0; number31 = 0; number32 = 0; number33 = 0; number34 = 0; number35 = 0;
                    number36 = 0; number37 = 0; number38 = 0; number39 = 0; number40 = 0; number41 = 0;
                    number42 = 0; number43 = 0; number44 = 0; number45 = 0; number46 = 0; number47 = 0;
                    number48 = 0; total=0;

                    if (spinnerSessions.equals("ALL")) {
                        fgboys
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, Integer> countName = new TreeMap<>();
                                        TreeMap<String, Integer> count = new TreeMap<>();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            if (note.getName() == null) {
                                                continue;
                                            }

                                            if (note.getUrl() == null) {
                                                note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                                url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                            }

                                            if (countName.containsKey(note.getName())) {
                                                continue;
                                            } else {
                                                countName.put(note.getName(), 1);
                                                total++;
                                                count.put("ALL", total);
                                            }

                                            long epoch = note.getEdate() * 1000;
                                            Date date = new Date(epoch);
                                            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                            format.setTimeZone(TimeZone.getDefault());
                                            String formatted = format.format(date);
                                            String time = formatted.substring(11, 13) + formatted.substring(14, 16) + formatted.substring(17, 19);

//                                            if (note.getName().equals("Prajwal Kumar")) {
//                                                mTextView.setText(String.valueOf(note.getEdate()));
//                                            }

//                                            if (!time.equals("170000")) {
//                                                mTextView.setText(time);
//                                            }

                                            if ((Integer.valueOf(time) >= 000000) && (Integer.valueOf(time) <= 003000)) {
                                                number1++;
                                            } else if ((Integer.valueOf(time) >= 003000) && (Integer.valueOf(time) <= 010000)) {
                                                number2++;
                                            } else if ((Integer.valueOf(time) >= 010000) && (Integer.valueOf(time) <= 0130000)) {
                                                number3++;
                                            } else if ((Integer.valueOf(time) >= 013000) && (Integer.valueOf(time) <= 020000)) {
                                                number4++;
                                            } else if ((Integer.valueOf(time) >= 020000) && (Integer.valueOf(time) <= 023000)) {
                                                number5++;
                                            } else if ((Integer.valueOf(time) >= 023000) && (Integer.valueOf(time) <= 030000)) {
                                                number6++;
                                            } else if ((Integer.valueOf(time) >= 030000) && (Integer.valueOf(time) <= 033000)) {
                                                number7++;
                                            } else if ((Integer.valueOf(time) >= 033000) && (Integer.valueOf(time) <= 040000)) {
                                                number8++;
                                            } else if ((Integer.valueOf(time) >= 040000) && (Integer.valueOf(time) <= 043000)) {
                                                number9++;
                                            } else if ((Integer.valueOf(time) >= 043000) && (Integer.valueOf(time) <= 050000)) {
                                                number10++;
                                            } else if ((Integer.valueOf(time) >= 050000) && (Integer.valueOf(time) <= 0053000)) {
                                                number11++;
                                            } else if ((Integer.valueOf(time) >= 053000) && (Integer.valueOf(time) <= 060000)) {
                                                number12++;
                                            } else if ((Integer.valueOf(time) >= 060000) && (Integer.valueOf(time) <= 063000)) {
                                                number13++;
                                            } else if ((Integer.valueOf(time) >= 063000) && (Integer.valueOf(time) <= 070000)) {
                                                number14++;
                                            } else if ((Integer.valueOf(time) >= 070000) && (Integer.valueOf(time) <= 073000)) {
                                                number15++;
                                            } else if ((Integer.valueOf(time) >= 073000) && (Integer.valueOf(time) <= 80000)) {
                                                number16++;
                                            } else if ((Integer.valueOf(time) >= 80000) && (Integer.valueOf(time) <= 83000)) {
                                                number17++;
                                            } else if ((Integer.valueOf(time) >= 83000) && (Integer.valueOf(time) <= 90000)) {
                                                number18++;
                                            } else if ((Integer.valueOf(time) >= 90000) && (Integer.valueOf(time) <= 93000)) {
                                                number19++;
                                            } else if ((Integer.valueOf(time) >= 93000) && (Integer.valueOf(time) <= 100000)) {
                                                number20++;
                                            } else if ((Integer.valueOf(time) >= 100000) && (Integer.valueOf(time) <= 103000)) {
                                                number21++;
                                            } else if ((Integer.valueOf(time) >= 103000) && (Integer.valueOf(time) <= 110000)) {
                                                number22++;
                                            } else if ((Integer.valueOf(time) >= 110000) && (Integer.valueOf(time) <= 113000)) {
                                                number23++;
                                            } else if ((Integer.valueOf(time) >= 113000) && (Integer.valueOf(time) <= 120000)) {
                                                number24++;
                                            } else if ((Integer.valueOf(time) >= 120000) && (Integer.valueOf(time) <= 123000)) {
                                                number25++;
                                            } else if ((Integer.valueOf(time) >= 123000) && (Integer.valueOf(time) <= 130000)) {
                                                number26++;
                                            } else if ((Integer.valueOf(time) >= 130000) && (Integer.valueOf(time) <= 133000)) {
                                                number27++;
                                            } else if ((Integer.valueOf(time) >= 133000) && (Integer.valueOf(time) <= 140000)) {
                                                number28++;
                                            } else if ((Integer.valueOf(time) >= 140000) && (Integer.valueOf(time) <= 143000)) {
                                                number29++;
                                            } else if ((Integer.valueOf(time) >= 143000) && (Integer.valueOf(time) <= 150000)) {
                                                number30++;
                                            } else if ((Integer.valueOf(time) >= 150000) && (Integer.valueOf(time) <= 153000)) {
                                                number31++;
                                            } else if ((Integer.valueOf(time) >= 153000) && (Integer.valueOf(time) <= 160000)) {
                                                number32++;
                                            } else if ((Integer.valueOf(time) >= 160000) && (Integer.valueOf(time) <= 163000)) {
                                                number33++;
                                            } else if ((Integer.valueOf(time) >= 163000) && (Integer.valueOf(time) <= 170000)) {
                                                number34++;
                                            } else if ((Integer.valueOf(time) >= 170000) && (Integer.valueOf(time) <= 173000)) {
                                                number35++;
                                            } else if ((Integer.valueOf(time) >= 173000) && (Integer.valueOf(time) <= 180000)) {
                                                number36++;
                                            } else if ((Integer.valueOf(time) >= 180000) && (Integer.valueOf(time) <= 183000)) {
                                                number37++;
                                            } else if ((Integer.valueOf(time) >= 183000) && (Integer.valueOf(time) <= 190000)) {
                                                number38++;
                                            } else if ((Integer.valueOf(time) >= 190000) && (Integer.valueOf(time) <= 193000)) {
                                                number39++;
                                            } else if ((Integer.valueOf(time) >= 193000) && (Integer.valueOf(time) <= 200000)) {
                                                number40++;
                                            } else if ((Integer.valueOf(time) >= 200000) && (Integer.valueOf(time) <= 203000)) {
                                                number41++;
                                            } else if ((Integer.valueOf(time) >= 203000) && (Integer.valueOf(time) <= 210000)) {
                                                number42++;
                                            } else if ((Integer.valueOf(time) >= 210000) && (Integer.valueOf(time) <= 213000)) {
                                                number43++;
                                            } else if ((Integer.valueOf(time) >= 213000) && (Integer.valueOf(time) <= 220000)) {
                                                number44++;
                                            } else if ((Integer.valueOf(time) >= 220000) && (Integer.valueOf(time) <= 223000)) {
                                                number45++;
                                            } else if ((Integer.valueOf(time) >= 223000) && (Integer.valueOf(time) <= 230000)) {
                                                number46++;
                                            } else if ((Integer.valueOf(time) >= 230000) && (Integer.valueOf(time) <= 233000)) {
                                                number47++;
                                            } else if ((Integer.valueOf(time) >= 233000) && (Integer.valueOf(time) <= 240000)) {
                                                number48++;
                                            }
                                        }

                                        if (number1 > 0) {
                                            count.put("00:00-00:30", number1);
                                        }
                                        if (number2 > 0) {
                                            count.put("00:30-01:00", number2);
                                        }
                                        if (number3 > 0) {
                                            count.put("01:00-01:30", number3);
                                        }
                                        if (number4 > 0) {
                                            count.put("01:30-02:00", number4);
                                        }
                                        if (number5 > 0) {
                                            count.put("02:00-02:30", number5);
                                        }
                                        if (number6 > 0) {
                                            count.put("02:30-03:00", number6);
                                        }
                                        if (number7 > 0) {
                                            count.put("03:00-03:30", number7);
                                        }
                                        if (number8 > 0) {
                                            count.put("03:30-04:00", number8);
                                        }
                                        if (number9 > 0) {
                                            count.put("04:00-04:30", number9);
                                        }
                                        if (number10 > 0) {
                                            count.put("04:30-05:00", number10);
                                        }
                                        if (number11 > 0) {
                                            count.put("05:00-05:30", number11);
                                        }
                                        if (number12 > 0) {
                                            count.put("05:30-06:00", number12);
                                        }
                                        if (number13 > 0) {
                                            count.put("06:00-06:30", number13);
                                        }
                                        if (number14 > 0) {
                                            count.put("06:30-07:00", number14);
                                        }
                                        if (number15 > 0) {
                                            count.put("07:00-07:30", number15);
                                        }
                                        if (number16 > 0) {
                                            count.put("07:30-08:00", number16);
                                        }
                                        if (number17 > 0) {
                                            count.put("08:00-08:30", number17);
                                        }
                                        if (number18 > 0) {
                                            count.put("08:30-09:00", number18);
                                        }
                                        if (number19 > 0) {
                                            count.put("09:00-09:30", number19);
                                        }
                                        if (number20 > 0) {
                                            count.put("09:30-10:00", number20);
                                        }
                                        if (number21 > 0) {
                                            count.put("10:00-10:30", number21);
                                        }
                                        if (number22 > 0) {
                                            count.put("10:30-11:00", number22);
                                        }
                                        if (number23 > 0) {
                                            count.put("11:00-11:30", number23);
                                        }
                                        if (number24 > 0) {
                                            count.put("11:30-12:00", number24);
                                        }
                                        if (number25 > 0) {
                                            count.put("12:00-12:30", number25);
                                        }
                                        if (number26 > 0) {
                                            count.put("12:30-13:00", number26);
                                        }
                                        if (number27 > 0) {
                                            count.put("13:00-13:30", number27);
                                        }
                                        if (number28 > 0) {
                                            count.put("13:30-14:00", number28);
                                        }
                                        if (number29 > 0) {
                                            count.put("14:00-14:30", number29);
                                        }
                                        if (number30 > 0) {
                                            count.put("14:30-15:00", number30);
                                        }
                                        if (number31 > 0) {
                                            count.put("15:00-15:30", number31);
                                        }
                                        if (number32 > 0) {
                                            count.put("15:30-16:00", number32);
                                        }
                                        if (number33 > 0) {
                                            count.put("16:00-16:30", number33);
                                        }
                                        if (number34 > 0) {
                                            count.put("16:30-17:00", number34);
                                        }
                                        if (number35 > 0) {
                                            count.put("17:00-17:30", number35);
                                        }
                                        if (number36 > 0) {
                                            count.put("17:30-18:00", number36);
                                        }
                                        if (number37 > 0) {
                                            count.put("18:00-18:30", number37);
                                        }
                                        if (number38 > 0) {
                                            count.put("18:30-19:00", number38);
                                        }
                                        if (number39 > 0) {
                                            count.put("19:00-19:30", number39);
                                        }
                                        if (number40 > 0) {
                                            count.put("19:30-20:00", number40);
                                        }
                                        if (number41 > 0) {
                                            count.put("20:00-20:30", number41);
                                        }
                                        if (number42 > 0) {
                                            count.put("20:30-21:00", number42);
                                        }
                                        if (number43 > 0) {
                                            count.put("21:00-21:30", number43);
                                        }
                                        if (number44 > 0) {
                                            count.put("21:30-22:00", number44);
                                        }
                                        if (number45 > 0) {
                                            count.put("22:00-22:30", number45);
                                        }
                                        if (number46 > 0) {
                                            count.put("22:30-23:00", number46);
                                        }
                                        if (number47 > 0) {
                                            count.put("23:00-23:30", number47);
                                        }
                                        if (number48 > 0) {
                                            count.put("23:30-24:00", number48);
                                        }

//                                        mTextView2.setText(String.valueOf(total));

                                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String time = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(TimeActivity.this, TimeDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Date1Str",date1Str);
                                                bundle.putString("Date2Str",date2Str);
                                                bundle.putString("Time",time);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection",collection);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                    } else {
                        number1 = 0; number2 = 0; number3 = 0; number4 = 0; number5 = 0;
                        number6 = 0; number7 = 0; number8 = 0; number9 = 0; number10 = 0; number11 = 0;
                        number12 = 0; number13 = 0; number14 = 0; number15 = 0; number16 = 0; number17 = 0;
                        number18 = 0; number19 = 0; number20 = 0; number21 = 0; number22 = 0; number23 = 0;
                        number24 = 0; number25 = 0; number26 = 0; number27 = 0; number28 = 0; number29 = 0;
                        number30 = 0; number31 = 0; number32 = 0; number33 = 0; number34 = 0; number35 = 0;
                        number36 = 0; number37 = 0; number38 = 0; number39 = 0; number40 = 0; number41 = 0;
                        number42 = 0; number43 = 0; number44 = 0; number45 = 0; number46 = 0; number47 = 0;
                        number48 = 0; total=0;

                        fgboys
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("session",spinnerSessions)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, Integer> countName = new TreeMap<>();
                                        TreeMap<String, Integer> count = new TreeMap<>();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            if (note.getName() == null) {
                                                continue;
                                            }

                                            if (note.getUrl() == null) {
                                                note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                                url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                            }

                                            if (countName.containsKey(note.getName())) {
                                                continue;
                                            } else {
                                                countName.put(note.getName(), 1);
                                                total++;
                                                count.put("ALL", total);
                                            }

                                            long epoch = note.getEdate() * 1000;
                                            Date date = new Date(epoch);
                                            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                            format.setTimeZone(TimeZone.getDefault());
                                            String formatted = format.format(date);
                                            String time = formatted.substring(11, 13) + formatted.substring(14, 16) + formatted.substring(17, 19);

                                            if ((Integer.valueOf(time) >= 000000) && (Integer.valueOf(time) <= 003000)) {
                                                number1++;
                                            } else if ((Integer.valueOf(time) >= 003000) && (Integer.valueOf(time) <= 010000)) {
                                                number2++;
                                            } else if ((Integer.valueOf(time) >= 010000) && (Integer.valueOf(time) <= 0130000)) {
                                                number3++;
                                            } else if ((Integer.valueOf(time) >= 013000) && (Integer.valueOf(time) <= 020000)) {
                                                number4++;
                                            } else if ((Integer.valueOf(time) >= 020000) && (Integer.valueOf(time) <= 023000)) {
                                                number5++;
                                            } else if ((Integer.valueOf(time) >= 023000) && (Integer.valueOf(time) <= 030000)) {
                                                number6++;
                                            } else if ((Integer.valueOf(time) >= 030000) && (Integer.valueOf(time) <= 033000)) {
                                                number7++;
                                            } else if ((Integer.valueOf(time) >= 033000) && (Integer.valueOf(time) <= 040000)) {
                                                number8++;
                                            } else if ((Integer.valueOf(time) >= 040000) && (Integer.valueOf(time) <= 043000)) {
                                                number9++;
                                            } else if ((Integer.valueOf(time) >= 043000) && (Integer.valueOf(time) <= 050000)) {
                                                number10++;
                                            } else if ((Integer.valueOf(time) >= 050000) && (Integer.valueOf(time) <= 0053000)) {
                                                number11++;
                                            } else if ((Integer.valueOf(time) >= 053000) && (Integer.valueOf(time) <= 060000)) {
                                                number12++;
                                            } else if ((Integer.valueOf(time) >= 060000) && (Integer.valueOf(time) <= 063000)) {
                                                number13++;
                                            } else if ((Integer.valueOf(time) >= 063000) && (Integer.valueOf(time) <= 070000)) {
                                                number14++;
                                            } else if ((Integer.valueOf(time) >= 070000) && (Integer.valueOf(time) <= 073000)) {
                                                number15++;
                                            } else if ((Integer.valueOf(time) >= 073000) && (Integer.valueOf(time) <= 80000)) {
                                                number16++;
                                            } else if ((Integer.valueOf(time) >= 80000) && (Integer.valueOf(time) <= 83000)) {
                                                number17++;
                                            } else if ((Integer.valueOf(time) >= 83000) && (Integer.valueOf(time) <= 90000)) {
                                                number18++;
                                            } else if ((Integer.valueOf(time) >= 90000) && (Integer.valueOf(time) <= 93000)) {
                                                number19++;
                                            } else if ((Integer.valueOf(time) >= 93000) && (Integer.valueOf(time) <= 100000)) {
                                                number20++;
                                            } else if ((Integer.valueOf(time) >= 100000) && (Integer.valueOf(time) <= 103000)) {
                                                number21++;
                                            } else if ((Integer.valueOf(time) >= 103000) && (Integer.valueOf(time) <= 110000)) {
                                                number22++;
                                            } else if ((Integer.valueOf(time) >= 110000) && (Integer.valueOf(time) <= 113000)) {
                                                number23++;
                                            } else if ((Integer.valueOf(time) >= 113000) && (Integer.valueOf(time) <= 120000)) {
                                                number24++;
                                            } else if ((Integer.valueOf(time) >= 120000) && (Integer.valueOf(time) <= 123000)) {
                                                number25++;
                                            } else if ((Integer.valueOf(time) >= 123000) && (Integer.valueOf(time) <= 130000)) {
                                                number26++;
                                            } else if ((Integer.valueOf(time) >= 130000) && (Integer.valueOf(time) <= 133000)) {
                                                number27++;
                                            } else if ((Integer.valueOf(time) >= 133000) && (Integer.valueOf(time) <= 140000)) {
                                                number28++;
                                            } else if ((Integer.valueOf(time) >= 140000) && (Integer.valueOf(time) <= 143000)) {
                                                number29++;
                                            } else if ((Integer.valueOf(time) >= 143000) && (Integer.valueOf(time) <= 150000)) {
                                                number30++;
                                            } else if ((Integer.valueOf(time) >= 150000) && (Integer.valueOf(time) <= 153000)) {
                                                number31++;
                                            } else if ((Integer.valueOf(time) >= 153000) && (Integer.valueOf(time) <= 160000)) {
                                                number32++;
                                            } else if ((Integer.valueOf(time) >= 160000) && (Integer.valueOf(time) <= 163000)) {
                                                number33++;
                                            } else if ((Integer.valueOf(time) >= 163000) && (Integer.valueOf(time) <= 170000)) {
                                                number34++;
                                            } else if ((Integer.valueOf(time) >= 170000) && (Integer.valueOf(time) <= 173000)) {
                                                number35++;
                                            } else if ((Integer.valueOf(time) >= 173000) && (Integer.valueOf(time) <= 180000)) {
                                                number36++;
                                            } else if ((Integer.valueOf(time) >= 180000) && (Integer.valueOf(time) <= 183000)) {
                                                number37++;
                                            } else if ((Integer.valueOf(time) >= 183000) && (Integer.valueOf(time) <= 190000)) {
                                                number38++;
                                            } else if ((Integer.valueOf(time) >= 190000) && (Integer.valueOf(time) <= 193000)) {
                                                number39++;
                                            } else if ((Integer.valueOf(time) >= 193000) && (Integer.valueOf(time) <= 200000)) {
                                                number40++;
                                            } else if ((Integer.valueOf(time) >= 200000) && (Integer.valueOf(time) <= 203000)) {
                                                number41++;
                                            } else if ((Integer.valueOf(time) >= 203000) && (Integer.valueOf(time) <= 210000)) {
                                                number42++;
                                            } else if ((Integer.valueOf(time) >= 210000) && (Integer.valueOf(time) <= 213000)) {
                                                number43++;
                                            } else if ((Integer.valueOf(time) >= 213000) && (Integer.valueOf(time) <= 220000)) {
                                                number44++;
                                            } else if ((Integer.valueOf(time) >= 220000) && (Integer.valueOf(time) <= 223000)) {
                                                number45++;
                                            } else if ((Integer.valueOf(time) >= 223000) && (Integer.valueOf(time) <= 230000)) {
                                                number46++;
                                            } else if ((Integer.valueOf(time) >= 230000) && (Integer.valueOf(time) <= 233000)) {
                                                number47++;
                                            } else if ((Integer.valueOf(time) >= 233000) && (Integer.valueOf(time) <= 240000)) {
                                                number48++;
                                            }
                                        }

                                        if (number1 > 0) {
                                            count.put("00:00-00:30", number1);
                                        }
                                        if (number2 > 0) {
                                            count.put("00:30-01:00", number2);
                                        }
                                        if (number3 > 0) {
                                            count.put("01:00-01:30", number3);
                                        }
                                        if (number4 > 0) {
                                            count.put("01:30-02:00", number4);
                                        }
                                        if (number5 > 0) {
                                            count.put("02:00-02:30", number5);
                                        }
                                        if (number6 > 0) {
                                            count.put("02:30-03:00", number6);
                                        }
                                        if (number7 > 0) {
                                            count.put("03:00-03:30", number7);
                                        }
                                        if (number8 > 0) {
                                            count.put("03:30-04:00", number8);
                                        }
                                        if (number9 > 0) {
                                            count.put("04:00-04:30", number9);
                                        }
                                        if (number10 > 0) {
                                            count.put("04:30-05:00", number10);
                                        }
                                        if (number11 > 0) {
                                            count.put("05:00-05:30", number11);
                                        }
                                        if (number12 > 0) {
                                            count.put("05:30-06:00", number12);
                                        }
                                        if (number13 > 0) {
                                            count.put("06:00-06:30", number13);
                                        }
                                        if (number14 > 0) {
                                            count.put("06:30-07:00", number14);
                                        }
                                        if (number15 > 0) {
                                            count.put("07:00-07:30", number15);
                                        }
                                        if (number16 > 0) {
                                            count.put("07:30-08:00", number16);
                                        }
                                        if (number17 > 0) {
                                            count.put("08:00-08:30", number17);
                                        }
                                        if (number18 > 0) {
                                            count.put("08:30-09:00", number18);
                                        }
                                        if (number19 > 0) {
                                            count.put("09:00-09:30", number19);
                                        }
                                        if (number20 > 0) {
                                            count.put("09:30-10:00", number20);
                                        }
                                        if (number21 > 0) {
                                            count.put("10:00-10:30", number21);
                                        }
                                        if (number22 > 0) {
                                            count.put("10:30-11:00", number22);
                                        }
                                        if (number23 > 0) {
                                            count.put("11:00-11:30", number23);
                                        }
                                        if (number24 > 0) {
                                            count.put("11:30-12:00", number24);
                                        }
                                        if (number25 > 0) {
                                            count.put("12:00-12:30", number25);
                                        }
                                        if (number26 > 0) {
                                            count.put("12:30-13:00", number26);
                                        }
                                        if (number27 > 0) {
                                            count.put("13:00-13:30", number27);
                                        }
                                        if (number28 > 0) {
                                            count.put("13:30-14:00", number28);
                                        }
                                        if (number29 > 0) {
                                            count.put("14:00-14:30", number29);
                                        }
                                        if (number30 > 0) {
                                            count.put("14:30-15:00", number30);
                                        }
                                        if (number31 > 0) {
                                            count.put("15:00-15:30", number31);
                                        }
                                        if (number32 > 0) {
                                            count.put("15:30-16:00", number32);
                                        }
                                        if (number33 > 0) {
                                            count.put("16:00-16:30", number33);
                                        }
                                        if (number34 > 0) {
                                            count.put("16:30-17:00", number34);
                                        }
                                        if (number35 > 0) {
                                            count.put("17:00-17:30", number35);
                                        }
                                        if (number36 > 0) {
                                            count.put("17:30-18:00", number36);
                                        }
                                        if (number37 > 0) {
                                            count.put("18:00-18:30", number37);
                                        }
                                        if (number38 > 0) {
                                            count.put("18:30-19:00", number38);
                                        }
                                        if (number39 > 0) {
                                            count.put("19:00-19:30", number39);
                                        }
                                        if (number40 > 0) {
                                            count.put("19:30-20:00", number40);
                                        }
                                        if (number41 > 0) {
                                            count.put("20:00-20:30", number41);
                                        }
                                        if (number42 > 0) {
                                            count.put("20:30-21:00", number42);
                                        }
                                        if (number43 > 0) {
                                            count.put("21:00-21:30", number43);
                                        }
                                        if (number44 > 0) {
                                            count.put("21:30-22:00", number44);
                                        }
                                        if (number45 > 0) {
                                            count.put("22:00-22:30", number45);
                                        }
                                        if (number46 > 0) {
                                            count.put("22:30-23:00", number46);
                                        }
                                        if (number47 > 0) {
                                            count.put("23:00-23:30", number47);
                                        }
                                        if (number48 > 0) {
                                            count.put("23:30-24:00", number48);
                                        }

//                                        mTextView2.setText(String.valueOf(total));

                                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String time = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(TimeActivity.this, TimeDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Date1Str",date1Str);
                                                bundle.putString("Date2Str",date2Str);
                                                bundle.putString("Time",time);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection",collection);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                });
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            finalSessions = new ArrayList<String>();
            sessionsList = Arrays.asList(sessions);
            finalSessions.addAll(sessionsList);

            adapterSessions = new ArrayAdapter<String>
                    (this, R.layout.spinner_item, finalSessions);

            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("category",spinnerCategories)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getSession() == null)
                                    continue;

                                if (finalSessions.contains(note.getSession())) {
                                    continue;
                                } else {
                                    finalSessions.add(note.getSession());
                                    adapterSessions.notifyDataSetChanged();
                                }

                            }
                        }
                    });
            adapterSessions.setDropDownViewResource(R.layout.spinner_item);
            session.setAdapter(adapterSessions);

            session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerSessions = parent.getItemAtPosition(position).toString();
                    posSession = position;

                    if (spinnerSessions.equals("ALL")) {
                        number1 = 0; number2 = 0; number3 = 0; number4 = 0; number5 = 0;
                        number6 = 0; number7 = 0; number8 = 0; number9 = 0; number10 = 0; number11 = 0;
                        number12 = 0; number13 = 0; number14 = 0; number15 = 0; number16 = 0; number17 = 0;
                        number18 = 0; number19 = 0; number20 = 0; number21 = 0; number22 = 0; number23 = 0;
                        number24 = 0; number25 = 0; number26 = 0; number27 = 0; number28 = 0; number29 = 0;
                        number30 = 0; number31 = 0; number32 = 0; number33 = 0; number34 = 0; number35 = 0;
                        number36 = 0; number37 = 0; number38 = 0; number39 = 0; number40 = 0; number41 = 0;
                        number42 = 0; number43 = 0; number44 = 0; number45 = 0; number46 = 0; number47 = 0;
                        number48 = 0; total=0;

                        fgboys
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("category", spinnerCategories)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, Integer> countName = new TreeMap<>();
                                        TreeMap<String, Integer> count = new TreeMap<>();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            if (note.getName() == null) {
                                                continue;
                                            }

                                            if (note.getUrl() == null) {
                                                note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                                url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                            }

                                            if (countName.containsKey(note.getName())) {
                                                continue;
                                            } else {
                                                countName.put(note.getName(), 1);
                                                total++;
                                                count.put("ALL", total);
                                            }

                                            long epoch = note.getEdate() * 1000;
                                            Date date = new Date(epoch);
                                            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                            format.setTimeZone(TimeZone.getDefault());
                                            String formatted = format.format(date);
                                            String time = formatted.substring(11, 13) + formatted.substring(14, 16) + formatted.substring(17, 19);

                                            if ((Integer.valueOf(time) >= 000000) && (Integer.valueOf(time) <= 003000)) {
                                                number1++;
                                            } else if ((Integer.valueOf(time) >= 003000) && (Integer.valueOf(time) <= 010000)) {
                                                number2++;
                                            } else if ((Integer.valueOf(time) >= 010000) && (Integer.valueOf(time) <= 0130000)) {
                                                number3++;
                                            } else if ((Integer.valueOf(time) >= 013000) && (Integer.valueOf(time) <= 020000)) {
                                                number4++;
                                            } else if ((Integer.valueOf(time) >= 020000) && (Integer.valueOf(time) <= 023000)) {
                                                number5++;
                                            } else if ((Integer.valueOf(time) >= 023000) && (Integer.valueOf(time) <= 030000)) {
                                                number6++;
                                            } else if ((Integer.valueOf(time) >= 030000) && (Integer.valueOf(time) <= 033000)) {
                                                number7++;
                                            } else if ((Integer.valueOf(time) >= 033000) && (Integer.valueOf(time) <= 040000)) {
                                                number8++;
                                            } else if ((Integer.valueOf(time) >= 040000) && (Integer.valueOf(time) <= 043000)) {
                                                number9++;
                                            } else if ((Integer.valueOf(time) >= 043000) && (Integer.valueOf(time) <= 050000)) {
                                                number10++;
                                            } else if ((Integer.valueOf(time) >= 050000) && (Integer.valueOf(time) <= 0053000)) {
                                                number11++;
                                            } else if ((Integer.valueOf(time) >= 053000) && (Integer.valueOf(time) <= 060000)) {
                                                number12++;
                                            } else if ((Integer.valueOf(time) >= 060000) && (Integer.valueOf(time) <= 063000)) {
                                                number13++;
                                            } else if ((Integer.valueOf(time) >= 063000) && (Integer.valueOf(time) <= 070000)) {
                                                number14++;
                                            } else if ((Integer.valueOf(time) >= 070000) && (Integer.valueOf(time) <= 073000)) {
                                                number15++;
                                            } else if ((Integer.valueOf(time) >= 073000) && (Integer.valueOf(time) <= 80000)) {
                                                number16++;
                                            } else if ((Integer.valueOf(time) >= 80000) && (Integer.valueOf(time) <= 83000)) {
                                                number17++;
                                            } else if ((Integer.valueOf(time) >= 83000) && (Integer.valueOf(time) <= 90000)) {
                                                number18++;
                                            } else if ((Integer.valueOf(time) >= 90000) && (Integer.valueOf(time) <= 93000)) {
                                                number19++;
                                            } else if ((Integer.valueOf(time) >= 93000) && (Integer.valueOf(time) <= 100000)) {
                                                number20++;
                                            } else if ((Integer.valueOf(time) >= 100000) && (Integer.valueOf(time) <= 103000)) {
                                                number21++;
                                            } else if ((Integer.valueOf(time) >= 103000) && (Integer.valueOf(time) <= 110000)) {
                                                number22++;
                                            } else if ((Integer.valueOf(time) >= 110000) && (Integer.valueOf(time) <= 113000)) {
                                                number23++;
                                            } else if ((Integer.valueOf(time) >= 113000) && (Integer.valueOf(time) <= 120000)) {
                                                number24++;
                                            } else if ((Integer.valueOf(time) >= 120000) && (Integer.valueOf(time) <= 123000)) {
                                                number25++;
                                            } else if ((Integer.valueOf(time) >= 123000) && (Integer.valueOf(time) <= 130000)) {
                                                number26++;
                                            } else if ((Integer.valueOf(time) >= 130000) && (Integer.valueOf(time) <= 133000)) {
                                                number27++;
                                            } else if ((Integer.valueOf(time) >= 133000) && (Integer.valueOf(time) <= 140000)) {
                                                number28++;
                                            } else if ((Integer.valueOf(time) >= 140000) && (Integer.valueOf(time) <= 143000)) {
                                                number29++;
                                            } else if ((Integer.valueOf(time) >= 143000) && (Integer.valueOf(time) <= 150000)) {
                                                number30++;
                                            } else if ((Integer.valueOf(time) >= 150000) && (Integer.valueOf(time) <= 153000)) {
                                                number31++;
                                            } else if ((Integer.valueOf(time) >= 153000) && (Integer.valueOf(time) <= 160000)) {
                                                number32++;
                                            } else if ((Integer.valueOf(time) >= 160000) && (Integer.valueOf(time) <= 163000)) {
                                                number33++;
                                            } else if ((Integer.valueOf(time) >= 163000) && (Integer.valueOf(time) <= 170000)) {
                                                number34++;
                                            } else if ((Integer.valueOf(time) >= 170000) && (Integer.valueOf(time) <= 173000)) {
                                                number35++;
                                            } else if ((Integer.valueOf(time) >= 173000) && (Integer.valueOf(time) <= 180000)) {
                                                number36++;
                                            } else if ((Integer.valueOf(time) >= 180000) && (Integer.valueOf(time) <= 183000)) {
                                                number37++;
                                            } else if ((Integer.valueOf(time) >= 183000) && (Integer.valueOf(time) <= 190000)) {
                                                number38++;
                                            } else if ((Integer.valueOf(time) >= 190000) && (Integer.valueOf(time) <= 193000)) {
                                                number39++;
                                            } else if ((Integer.valueOf(time) >= 193000) && (Integer.valueOf(time) <= 200000)) {
                                                number40++;
                                            } else if ((Integer.valueOf(time) >= 200000) && (Integer.valueOf(time) <= 203000)) {
                                                number41++;
                                            } else if ((Integer.valueOf(time) >= 203000) && (Integer.valueOf(time) <= 210000)) {
                                                number42++;
                                            } else if ((Integer.valueOf(time) >= 210000) && (Integer.valueOf(time) <= 213000)) {
                                                number43++;
                                            } else if ((Integer.valueOf(time) >= 213000) && (Integer.valueOf(time) <= 220000)) {
                                                number44++;
                                            } else if ((Integer.valueOf(time) >= 220000) && (Integer.valueOf(time) <= 223000)) {
                                                number45++;
                                            } else if ((Integer.valueOf(time) >= 223000) && (Integer.valueOf(time) <= 230000)) {
                                                number46++;
                                            } else if ((Integer.valueOf(time) >= 230000) && (Integer.valueOf(time) <= 233000)) {
                                                number47++;
                                            } else if ((Integer.valueOf(time) >= 233000) && (Integer.valueOf(time) <= 240000)) {
                                                number48++;
                                            }
                                        }

                                        if (number1 > 0) {
                                            count.put("00:00-00:30", number1);
                                        }
                                        if (number2 > 0) {
                                            count.put("00:30-01:00", number2);
                                        }
                                        if (number3 > 0) {
                                            count.put("01:00-01:30", number3);
                                        }
                                        if (number4 > 0) {
                                            count.put("01:30-02:00", number4);
                                        }
                                        if (number5 > 0) {
                                            count.put("02:00-02:30", number5);
                                        }
                                        if (number6 > 0) {
                                            count.put("02:30-03:00", number6);
                                        }
                                        if (number7 > 0) {
                                            count.put("03:00-03:30", number7);
                                        }
                                        if (number8 > 0) {
                                            count.put("03:30-04:00", number8);
                                        }
                                        if (number9 > 0) {
                                            count.put("04:00-04:30", number9);
                                        }
                                        if (number10 > 0) {
                                            count.put("04:30-05:00", number10);
                                        }
                                        if (number11 > 0) {
                                            count.put("05:00-05:30", number11);
                                        }
                                        if (number12 > 0) {
                                            count.put("05:30-06:00", number12);
                                        }
                                        if (number13 > 0) {
                                            count.put("06:00-06:30", number13);
                                        }
                                        if (number14 > 0) {
                                            count.put("06:30-07:00", number14);
                                        }
                                        if (number15 > 0) {
                                            count.put("07:00-07:30", number15);
                                        }
                                        if (number16 > 0) {
                                            count.put("07:30-08:00", number16);
                                        }
                                        if (number17 > 0) {
                                            count.put("08:00-08:30", number17);
                                        }
                                        if (number18 > 0) {
                                            count.put("08:30-09:00", number18);
                                        }
                                        if (number19 > 0) {
                                            count.put("09:00-09:30", number19);
                                        }
                                        if (number20 > 0) {
                                            count.put("09:30-10:00", number20);
                                        }
                                        if (number21 > 0) {
                                            count.put("10:00-10:30", number21);
                                        }
                                        if (number22 > 0) {
                                            count.put("10:30-11:00", number22);
                                        }
                                        if (number23 > 0) {
                                            count.put("11:00-11:30", number23);
                                        }
                                        if (number24 > 0) {
                                            count.put("11:30-12:00", number24);
                                        }
                                        if (number25 > 0) {
                                            count.put("12:00-12:30", number25);
                                        }
                                        if (number26 > 0) {
                                            count.put("12:30-13:00", number26);
                                        }
                                        if (number27 > 0) {
                                            count.put("13:00-13:30", number27);
                                        }
                                        if (number28 > 0) {
                                            count.put("13:30-14:00", number28);
                                        }
                                        if (number29 > 0) {
                                            count.put("14:00-14:30", number29);
                                        }
                                        if (number30 > 0) {
                                            count.put("14:30-15:00", number30);
                                        }
                                        if (number31 > 0) {
                                            count.put("15:00-15:30", number31);
                                        }
                                        if (number32 > 0) {
                                            count.put("15:30-16:00", number32);
                                        }
                                        if (number33 > 0) {
                                            count.put("16:00-16:30", number33);
                                        }
                                        if (number34 > 0) {
                                            count.put("16:30-17:00", number34);
                                        }
                                        if (number35 > 0) {
                                            count.put("17:00-17:30", number35);
                                        }
                                        if (number36 > 0) {
                                            count.put("17:30-18:00", number36);
                                        }
                                        if (number37 > 0) {
                                            count.put("18:00-18:30", number37);
                                        }
                                        if (number38 > 0) {
                                            count.put("18:30-19:00", number38);
                                        }
                                        if (number39 > 0) {
                                            count.put("19:00-19:30", number39);
                                        }
                                        if (number40 > 0) {
                                            count.put("19:30-20:00", number40);
                                        }
                                        if (number41 > 0) {
                                            count.put("20:00-20:30", number41);
                                        }
                                        if (number42 > 0) {
                                            count.put("20:30-21:00", number42);
                                        }
                                        if (number43 > 0) {
                                            count.put("21:00-21:30", number43);
                                        }
                                        if (number44 > 0) {
                                            count.put("21:30-22:00", number44);
                                        }
                                        if (number45 > 0) {
                                            count.put("22:00-22:30", number45);
                                        }
                                        if (number46 > 0) {
                                            count.put("22:30-23:00", number46);
                                        }
                                        if (number47 > 0) {
                                            count.put("23:00-23:30", number47);
                                        }
                                        if (number48 > 0) {
                                            count.put("23:30-24:00", number48);
                                        }

//                                        mTextView2.setText(String.valueOf(total));

                                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String time = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(TimeActivity.this, TimeDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Date1Str",date1Str);
                                                bundle.putString("Date2Str",date2Str);
                                                bundle.putString("Time",time);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection",collection);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                    } else {
                        number1 = 0; number2 = 0; number3 = 0; number4 = 0; number5 = 0;
                        number6 = 0; number7 = 0; number8 = 0; number9 = 0; number10 = 0; number11 = 0;
                        number12 = 0; number13 = 0; number14 = 0; number15 = 0; number16 = 0; number17 = 0;
                        number18 = 0; number19 = 0; number20 = 0; number21 = 0; number22 = 0; number23 = 0;
                        number24 = 0; number25 = 0; number26 = 0; number27 = 0; number28 = 0; number29 = 0;
                        number30 = 0; number31 = 0; number32 = 0; number33 = 0; number34 = 0; number35 = 0;
                        number36 = 0; number37 = 0; number38 = 0; number39 = 0; number40 = 0; number41 = 0;
                        number42 = 0; number43 = 0; number44 = 0; number45 = 0; number46 = 0; number47 = 0;
                        number48 = 0; total=0;

                        fgboys
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("category",spinnerCategories)
                                .whereEqualTo("session",spinnerSessions)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, Integer> countName = new TreeMap<>();
                                        TreeMap<String, Integer> count = new TreeMap<>();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            if (note.getName() == null) {
                                                continue;
                                            }

                                            if (note.getUrl() == null) {
                                                note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                                url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                            }

                                            if (countName.containsKey(note.getName())) {
                                                continue;
                                            } else {
                                                countName.put(note.getName(), 1);
                                                total++;
                                                count.put("ALL", total);
                                            }

                                            long epoch = note.getEdate() * 1000;
                                            Date date = new Date(epoch);
                                            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                            format.setTimeZone(TimeZone.getDefault());
                                            String formatted = format.format(date);
                                            String time = formatted.substring(11, 13) + formatted.substring(14, 16) + formatted.substring(17, 19);

                                            if ((Integer.valueOf(time) >= 000000) && (Integer.valueOf(time) <= 003000)) {
                                                number1++;
                                            } else if ((Integer.valueOf(time) >= 003000) && (Integer.valueOf(time) <= 010000)) {
                                                number2++;
                                            } else if ((Integer.valueOf(time) >= 010000) && (Integer.valueOf(time) <= 0130000)) {
                                                number3++;
                                            } else if ((Integer.valueOf(time) >= 013000) && (Integer.valueOf(time) <= 020000)) {
                                                number4++;
                                            } else if ((Integer.valueOf(time) >= 020000) && (Integer.valueOf(time) <= 023000)) {
                                                number5++;
                                            } else if ((Integer.valueOf(time) >= 023000) && (Integer.valueOf(time) <= 030000)) {
                                                number6++;
                                            } else if ((Integer.valueOf(time) >= 030000) && (Integer.valueOf(time) <= 033000)) {
                                                number7++;
                                            } else if ((Integer.valueOf(time) >= 033000) && (Integer.valueOf(time) <= 040000)) {
                                                number8++;
                                            } else if ((Integer.valueOf(time) >= 040000) && (Integer.valueOf(time) <= 043000)) {
                                                number9++;
                                            } else if ((Integer.valueOf(time) >= 043000) && (Integer.valueOf(time) <= 050000)) {
                                                number10++;
                                            } else if ((Integer.valueOf(time) >= 050000) && (Integer.valueOf(time) <= 0053000)) {
                                                number11++;
                                            } else if ((Integer.valueOf(time) >= 053000) && (Integer.valueOf(time) <= 060000)) {
                                                number12++;
                                            } else if ((Integer.valueOf(time) >= 060000) && (Integer.valueOf(time) <= 063000)) {
                                                number13++;
                                            } else if ((Integer.valueOf(time) >= 063000) && (Integer.valueOf(time) <= 070000)) {
                                                number14++;
                                            } else if ((Integer.valueOf(time) >= 070000) && (Integer.valueOf(time) <= 073000)) {
                                                number15++;
                                            } else if ((Integer.valueOf(time) >= 073000) && (Integer.valueOf(time) <= 80000)) {
                                                number16++;
                                            } else if ((Integer.valueOf(time) >= 80000) && (Integer.valueOf(time) <= 83000)) {
                                                number17++;
                                            } else if ((Integer.valueOf(time) >= 83000) && (Integer.valueOf(time) <= 90000)) {
                                                number18++;
                                            } else if ((Integer.valueOf(time) >= 90000) && (Integer.valueOf(time) <= 93000)) {
                                                number19++;
                                            } else if ((Integer.valueOf(time) >= 93000) && (Integer.valueOf(time) <= 100000)) {
                                                number20++;
                                            } else if ((Integer.valueOf(time) >= 100000) && (Integer.valueOf(time) <= 103000)) {
                                                number21++;
                                            } else if ((Integer.valueOf(time) >= 103000) && (Integer.valueOf(time) <= 110000)) {
                                                number22++;
                                            } else if ((Integer.valueOf(time) >= 110000) && (Integer.valueOf(time) <= 113000)) {
                                                number23++;
                                            } else if ((Integer.valueOf(time) >= 113000) && (Integer.valueOf(time) <= 120000)) {
                                                number24++;
                                            } else if ((Integer.valueOf(time) >= 120000) && (Integer.valueOf(time) <= 123000)) {
                                                number25++;
                                            } else if ((Integer.valueOf(time) >= 123000) && (Integer.valueOf(time) <= 130000)) {
                                                number26++;
                                            } else if ((Integer.valueOf(time) >= 130000) && (Integer.valueOf(time) <= 133000)) {
                                                number27++;
                                            } else if ((Integer.valueOf(time) >= 133000) && (Integer.valueOf(time) <= 140000)) {
                                                number28++;
                                            } else if ((Integer.valueOf(time) >= 140000) && (Integer.valueOf(time) <= 143000)) {
                                                number29++;
                                            } else if ((Integer.valueOf(time) >= 143000) && (Integer.valueOf(time) <= 150000)) {
                                                number30++;
                                            } else if ((Integer.valueOf(time) >= 150000) && (Integer.valueOf(time) <= 153000)) {
                                                number31++;
                                            } else if ((Integer.valueOf(time) >= 153000) && (Integer.valueOf(time) <= 160000)) {
                                                number32++;
                                            } else if ((Integer.valueOf(time) >= 160000) && (Integer.valueOf(time) <= 163000)) {
                                                number33++;
                                            } else if ((Integer.valueOf(time) >= 163000) && (Integer.valueOf(time) <= 170000)) {
                                                number34++;
                                            } else if ((Integer.valueOf(time) >= 170000) && (Integer.valueOf(time) <= 173000)) {
                                                number35++;
                                            } else if ((Integer.valueOf(time) >= 173000) && (Integer.valueOf(time) <= 180000)) {
                                                number36++;
                                            } else if ((Integer.valueOf(time) >= 180000) && (Integer.valueOf(time) <= 183000)) {
                                                number37++;
                                            } else if ((Integer.valueOf(time) >= 183000) && (Integer.valueOf(time) <= 190000)) {
                                                number38++;
                                            } else if ((Integer.valueOf(time) >= 190000) && (Integer.valueOf(time) <= 193000)) {
                                                number39++;
                                            } else if ((Integer.valueOf(time) >= 193000) && (Integer.valueOf(time) <= 200000)) {
                                                number40++;
                                            } else if ((Integer.valueOf(time) >= 200000) && (Integer.valueOf(time) <= 203000)) {
                                                number41++;
                                            } else if ((Integer.valueOf(time) >= 203000) && (Integer.valueOf(time) <= 210000)) {
                                                number42++;
                                            } else if ((Integer.valueOf(time) >= 210000) && (Integer.valueOf(time) <= 213000)) {
                                                number43++;
                                            } else if ((Integer.valueOf(time) >= 213000) && (Integer.valueOf(time) <= 220000)) {
                                                number44++;
                                            } else if ((Integer.valueOf(time) >= 220000) && (Integer.valueOf(time) <= 223000)) {
                                                number45++;
                                            } else if ((Integer.valueOf(time) >= 223000) && (Integer.valueOf(time) <= 230000)) {
                                                number46++;
                                            } else if ((Integer.valueOf(time) >= 230000) && (Integer.valueOf(time) <= 233000)) {
                                                number47++;
                                            } else if ((Integer.valueOf(time) >= 233000) && (Integer.valueOf(time) <= 240000)) {
                                                number48++;
                                            }
                                        }

                                        if (number1 > 0) {
                                            count.put("00:00-00:30", number1);
                                        }
                                        if (number2 > 0) {
                                            count.put("00:30-01:00", number2);
                                        }
                                        if (number3 > 0) {
                                            count.put("01:00-01:30", number3);
                                        }
                                        if (number4 > 0) {
                                            count.put("01:30-02:00", number4);
                                        }
                                        if (number5 > 0) {
                                            count.put("02:00-02:30", number5);
                                        }
                                        if (number6 > 0) {
                                            count.put("02:30-03:00", number6);
                                        }
                                        if (number7 > 0) {
                                            count.put("03:00-03:30", number7);
                                        }
                                        if (number8 > 0) {
                                            count.put("03:30-04:00", number8);
                                        }
                                        if (number9 > 0) {
                                            count.put("04:00-04:30", number9);
                                        }
                                        if (number10 > 0) {
                                            count.put("04:30-05:00", number10);
                                        }
                                        if (number11 > 0) {
                                            count.put("05:00-05:30", number11);
                                        }
                                        if (number12 > 0) {
                                            count.put("05:30-06:00", number12);
                                        }
                                        if (number13 > 0) {
                                            count.put("06:00-06:30", number13);
                                        }
                                        if (number14 > 0) {
                                            count.put("06:30-07:00", number14);
                                        }
                                        if (number15 > 0) {
                                            count.put("07:00-07:30", number15);
                                        }
                                        if (number16 > 0) {
                                            count.put("07:30-08:00", number16);
                                        }
                                        if (number17 > 0) {
                                            count.put("08:00-08:30", number17);
                                        }
                                        if (number18 > 0) {
                                            count.put("08:30-09:00", number18);
                                        }
                                        if (number19 > 0) {
                                            count.put("09:00-09:30", number19);
                                        }
                                        if (number20 > 0) {
                                            count.put("09:30-10:00", number20);
                                        }
                                        if (number21 > 0) {
                                            count.put("10:00-10:30", number21);
                                        }
                                        if (number22 > 0) {
                                            count.put("10:30-11:00", number22);
                                        }
                                        if (number23 > 0) {
                                            count.put("11:00-11:30", number23);
                                        }
                                        if (number24 > 0) {
                                            count.put("11:30-12:00", number24);
                                        }
                                        if (number25 > 0) {
                                            count.put("12:00-12:30", number25);
                                        }
                                        if (number26 > 0) {
                                            count.put("12:30-13:00", number26);
                                        }
                                        if (number27 > 0) {
                                            count.put("13:00-13:30", number27);
                                        }
                                        if (number28 > 0) {
                                            count.put("13:30-14:00", number28);
                                        }
                                        if (number29 > 0) {
                                            count.put("14:00-14:30", number29);
                                        }
                                        if (number30 > 0) {
                                            count.put("14:30-15:00", number30);
                                        }
                                        if (number31 > 0) {
                                            count.put("15:00-15:30", number31);
                                        }
                                        if (number32 > 0) {
                                            count.put("15:30-16:00", number32);
                                        }
                                        if (number33 > 0) {
                                            count.put("16:00-16:30", number33);
                                        }
                                        if (number34 > 0) {
                                            count.put("16:30-17:00", number34);
                                        }
                                        if (number35 > 0) {
                                            count.put("17:00-17:30", number35);
                                        }
                                        if (number36 > 0) {
                                            count.put("17:30-18:00", number36);
                                        }
                                        if (number37 > 0) {
                                            count.put("18:00-18:30", number37);
                                        }
                                        if (number38 > 0) {
                                            count.put("18:30-19:00", number38);
                                        }
                                        if (number39 > 0) {
                                            count.put("19:00-19:30", number39);
                                        }
                                        if (number40 > 0) {
                                            count.put("19:30-20:00", number40);
                                        }
                                        if (number41 > 0) {
                                            count.put("20:00-20:30", number41);
                                        }
                                        if (number42 > 0) {
                                            count.put("20:30-21:00", number42);
                                        }
                                        if (number43 > 0) {
                                            count.put("21:00-21:30", number43);
                                        }
                                        if (number44 > 0) {
                                            count.put("21:30-22:00", number44);
                                        }
                                        if (number45 > 0) {
                                            count.put("22:00-22:30", number45);
                                        }
                                        if (number46 > 0) {
                                            count.put("22:30-23:00", number46);
                                        }
                                        if (number47 > 0) {
                                            count.put("23:00-23:30", number47);
                                        }
                                        if (number48 > 0) {
                                            count.put("23:30-24:00", number48);
                                        }

//                                        mTextView2.setText(String.valueOf(total));

                                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String time = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(TimeActivity.this, TimeDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Date1Str",date1Str);
                                                bundle.putString("Date2Str",date2Str);
                                                bundle.putString("Time",time);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection",collection);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                });
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public void populateSessions(String spinCategory, String spinProgram) {
        final String spinCategories = spinCategory;
        final String spinPrograms = spinProgram;

        if (spinCategory.equals("1")) {
            finalSessions = new ArrayList<String>();
            sessionsList = Arrays.asList(sessions);
            finalSessions.addAll(sessionsList);

            adapterSessions = new ArrayAdapter<String>
                    (this, R.layout.spinner_item, finalSessions);

            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("program",spinnerPrograms)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getSession() == null)
                                    continue;

                                if (finalSessions.contains(note.getSession())) {
                                    continue;
                                } else {
                                    finalSessions.add(note.getSession());
                                    adapterSessions.notifyDataSetChanged();
                                }

                            }
                        }
                    });
            adapterSessions.setDropDownViewResource(R.layout.spinner_item);
            session.setAdapter(adapterSessions);

            session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerSessions = parent.getItemAtPosition(position).toString();
                    posSession = position;

                    if (spinnerSessions.equals("ALL")) {
                        number1 = 0; number2 = 0; number3 = 0; number4 = 0; number5 = 0;
                        number6 = 0; number7 = 0; number8 = 0; number9 = 0; number10 = 0; number11 = 0;
                        number12 = 0; number13 = 0; number14 = 0; number15 = 0; number16 = 0; number17 = 0;
                        number18 = 0; number19 = 0; number20 = 0; number21 = 0; number22 = 0; number23 = 0;
                        number24 = 0; number25 = 0; number26 = 0; number27 = 0; number28 = 0; number29 = 0;
                        number30 = 0; number31 = 0; number32 = 0; number33 = 0; number34 = 0; number35 = 0;
                        number36 = 0; number37 = 0; number38 = 0; number39 = 0; number40 = 0; number41 = 0;
                        number42 = 0; number43 = 0; number44 = 0; number45 = 0; number46 = 0; number47 = 0;
                        number48 = 0; total=0;

                        fgboys
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("program",spinnerPrograms)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, Integer> countName = new TreeMap<>();
                                        TreeMap<String, Integer> count = new TreeMap<>();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            if (note.getName() == null) {
                                                continue;
                                            }

                                            if (note.getUrl() == null) {
                                                note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                                url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                            }

                                            if (countName.containsKey(note.getName())) {
                                                continue;
                                            } else {
                                                countName.put(note.getName(), 1);
                                                total++;
                                                count.put("ALL", total);
                                            }

                                            long epoch = note.getEdate() * 1000;
                                            Date date = new Date(epoch);
                                            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                            format.setTimeZone(TimeZone.getDefault());
                                            String formatted = format.format(date);
                                            String time = formatted.substring(11, 13) + formatted.substring(14, 16) + formatted.substring(17, 19);

                                            if ((Integer.valueOf(time) >= 000000) && (Integer.valueOf(time) <= 003000)) {
                                                number1++;
                                            } else if ((Integer.valueOf(time) >= 003000) && (Integer.valueOf(time) <= 010000)) {
                                                number2++;
                                            } else if ((Integer.valueOf(time) >= 010000) && (Integer.valueOf(time) <= 0130000)) {
                                                number3++;
                                            } else if ((Integer.valueOf(time) >= 013000) && (Integer.valueOf(time) <= 020000)) {
                                                number4++;
                                            } else if ((Integer.valueOf(time) >= 020000) && (Integer.valueOf(time) <= 023000)) {
                                                number5++;
                                            } else if ((Integer.valueOf(time) >= 023000) && (Integer.valueOf(time) <= 030000)) {
                                                number6++;
                                            } else if ((Integer.valueOf(time) >= 030000) && (Integer.valueOf(time) <= 033000)) {
                                                number7++;
                                            } else if ((Integer.valueOf(time) >= 033000) && (Integer.valueOf(time) <= 040000)) {
                                                number8++;
                                            } else if ((Integer.valueOf(time) >= 040000) && (Integer.valueOf(time) <= 043000)) {
                                                number9++;
                                            } else if ((Integer.valueOf(time) >= 043000) && (Integer.valueOf(time) <= 050000)) {
                                                number10++;
                                            } else if ((Integer.valueOf(time) >= 050000) && (Integer.valueOf(time) <= 0053000)) {
                                                number11++;
                                            } else if ((Integer.valueOf(time) >= 053000) && (Integer.valueOf(time) <= 060000)) {
                                                number12++;
                                            } else if ((Integer.valueOf(time) >= 060000) && (Integer.valueOf(time) <= 063000)) {
                                                number13++;
                                            } else if ((Integer.valueOf(time) >= 063000) && (Integer.valueOf(time) <= 070000)) {
                                                number14++;
                                            } else if ((Integer.valueOf(time) >= 070000) && (Integer.valueOf(time) <= 073000)) {
                                                number15++;
                                            } else if ((Integer.valueOf(time) >= 073000) && (Integer.valueOf(time) <= 80000)) {
                                                number16++;
                                            } else if ((Integer.valueOf(time) >= 80000) && (Integer.valueOf(time) <= 83000)) {
                                                number17++;
                                            } else if ((Integer.valueOf(time) >= 83000) && (Integer.valueOf(time) <= 90000)) {
                                                number18++;
                                            } else if ((Integer.valueOf(time) >= 90000) && (Integer.valueOf(time) <= 93000)) {
                                                number19++;
                                            } else if ((Integer.valueOf(time) >= 93000) && (Integer.valueOf(time) <= 100000)) {
                                                number20++;
                                            } else if ((Integer.valueOf(time) >= 100000) && (Integer.valueOf(time) <= 103000)) {
                                                number21++;
                                            } else if ((Integer.valueOf(time) >= 103000) && (Integer.valueOf(time) <= 110000)) {
                                                number22++;
                                            } else if ((Integer.valueOf(time) >= 110000) && (Integer.valueOf(time) <= 113000)) {
                                                number23++;
                                            } else if ((Integer.valueOf(time) >= 113000) && (Integer.valueOf(time) <= 120000)) {
                                                number24++;
                                            } else if ((Integer.valueOf(time) >= 120000) && (Integer.valueOf(time) <= 123000)) {
                                                number25++;
                                            } else if ((Integer.valueOf(time) >= 123000) && (Integer.valueOf(time) <= 130000)) {
                                                number26++;
                                            } else if ((Integer.valueOf(time) >= 130000) && (Integer.valueOf(time) <= 133000)) {
                                                number27++;
                                            } else if ((Integer.valueOf(time) >= 133000) && (Integer.valueOf(time) <= 140000)) {
                                                number28++;
                                            } else if ((Integer.valueOf(time) >= 140000) && (Integer.valueOf(time) <= 143000)) {
                                                number29++;
                                            } else if ((Integer.valueOf(time) >= 143000) && (Integer.valueOf(time) <= 150000)) {
                                                number30++;
                                            } else if ((Integer.valueOf(time) >= 150000) && (Integer.valueOf(time) <= 153000)) {
                                                number31++;
                                            } else if ((Integer.valueOf(time) >= 153000) && (Integer.valueOf(time) <= 160000)) {
                                                number32++;
                                            } else if ((Integer.valueOf(time) >= 160000) && (Integer.valueOf(time) <= 163000)) {
                                                number33++;
                                            } else if ((Integer.valueOf(time) >= 163000) && (Integer.valueOf(time) <= 170000)) {
                                                number34++;
                                            } else if ((Integer.valueOf(time) >= 170000) && (Integer.valueOf(time) <= 173000)) {
                                                number35++;
                                            } else if ((Integer.valueOf(time) >= 173000) && (Integer.valueOf(time) <= 180000)) {
                                                number36++;
                                            } else if ((Integer.valueOf(time) >= 180000) && (Integer.valueOf(time) <= 183000)) {
                                                number37++;
                                            } else if ((Integer.valueOf(time) >= 183000) && (Integer.valueOf(time) <= 190000)) {
                                                number38++;
                                            } else if ((Integer.valueOf(time) >= 190000) && (Integer.valueOf(time) <= 193000)) {
                                                number39++;
                                            } else if ((Integer.valueOf(time) >= 193000) && (Integer.valueOf(time) <= 200000)) {
                                                number40++;
                                            } else if ((Integer.valueOf(time) >= 200000) && (Integer.valueOf(time) <= 203000)) {
                                                number41++;
                                            } else if ((Integer.valueOf(time) >= 203000) && (Integer.valueOf(time) <= 210000)) {
                                                number42++;
                                            } else if ((Integer.valueOf(time) >= 210000) && (Integer.valueOf(time) <= 213000)) {
                                                number43++;
                                            } else if ((Integer.valueOf(time) >= 213000) && (Integer.valueOf(time) <= 220000)) {
                                                number44++;
                                            } else if ((Integer.valueOf(time) >= 220000) && (Integer.valueOf(time) <= 223000)) {
                                                number45++;
                                            } else if ((Integer.valueOf(time) >= 223000) && (Integer.valueOf(time) <= 230000)) {
                                                number46++;
                                            } else if ((Integer.valueOf(time) >= 230000) && (Integer.valueOf(time) <= 233000)) {
                                                number47++;
                                            } else if ((Integer.valueOf(time) >= 233000) && (Integer.valueOf(time) <= 240000)) {
                                                number48++;
                                            }
                                        }

                                        if (number1 > 0) {
                                            count.put("00:00-00:30", number1);
                                        }
                                        if (number2 > 0) {
                                            count.put("00:30-01:00", number2);
                                        }
                                        if (number3 > 0) {
                                            count.put("01:00-01:30", number3);
                                        }
                                        if (number4 > 0) {
                                            count.put("01:30-02:00", number4);
                                        }
                                        if (number5 > 0) {
                                            count.put("02:00-02:30", number5);
                                        }
                                        if (number6 > 0) {
                                            count.put("02:30-03:00", number6);
                                        }
                                        if (number7 > 0) {
                                            count.put("03:00-03:30", number7);
                                        }
                                        if (number8 > 0) {
                                            count.put("03:30-04:00", number8);
                                        }
                                        if (number9 > 0) {
                                            count.put("04:00-04:30", number9);
                                        }
                                        if (number10 > 0) {
                                            count.put("04:30-05:00", number10);
                                        }
                                        if (number11 > 0) {
                                            count.put("05:00-05:30", number11);
                                        }
                                        if (number12 > 0) {
                                            count.put("05:30-06:00", number12);
                                        }
                                        if (number13 > 0) {
                                            count.put("06:00-06:30", number13);
                                        }
                                        if (number14 > 0) {
                                            count.put("06:30-07:00", number14);
                                        }
                                        if (number15 > 0) {
                                            count.put("07:00-07:30", number15);
                                        }
                                        if (number16 > 0) {
                                            count.put("07:30-08:00", number16);
                                        }
                                        if (number17 > 0) {
                                            count.put("08:00-08:30", number17);
                                        }
                                        if (number18 > 0) {
                                            count.put("08:30-09:00", number18);
                                        }
                                        if (number19 > 0) {
                                            count.put("09:00-09:30", number19);
                                        }
                                        if (number20 > 0) {
                                            count.put("09:30-10:00", number20);
                                        }
                                        if (number21 > 0) {
                                            count.put("10:00-10:30", number21);
                                        }
                                        if (number22 > 0) {
                                            count.put("10:30-11:00", number22);
                                        }
                                        if (number23 > 0) {
                                            count.put("11:00-11:30", number23);
                                        }
                                        if (number24 > 0) {
                                            count.put("11:30-12:00", number24);
                                        }
                                        if (number25 > 0) {
                                            count.put("12:00-12:30", number25);
                                        }
                                        if (number26 > 0) {
                                            count.put("12:30-13:00", number26);
                                        }
                                        if (number27 > 0) {
                                            count.put("13:00-13:30", number27);
                                        }
                                        if (number28 > 0) {
                                            count.put("13:30-14:00", number28);
                                        }
                                        if (number29 > 0) {
                                            count.put("14:00-14:30", number29);
                                        }
                                        if (number30 > 0) {
                                            count.put("14:30-15:00", number30);
                                        }
                                        if (number31 > 0) {
                                            count.put("15:00-15:30", number31);
                                        }
                                        if (number32 > 0) {
                                            count.put("15:30-16:00", number32);
                                        }
                                        if (number33 > 0) {
                                            count.put("16:00-16:30", number33);
                                        }
                                        if (number34 > 0) {
                                            count.put("16:30-17:00", number34);
                                        }
                                        if (number35 > 0) {
                                            count.put("17:00-17:30", number35);
                                        }
                                        if (number36 > 0) {
                                            count.put("17:30-18:00", number36);
                                        }
                                        if (number37 > 0) {
                                            count.put("18:00-18:30", number37);
                                        }
                                        if (number38 > 0) {
                                            count.put("18:30-19:00", number38);
                                        }
                                        if (number39 > 0) {
                                            count.put("19:00-19:30", number39);
                                        }
                                        if (number40 > 0) {
                                            count.put("19:30-20:00", number40);
                                        }
                                        if (number41 > 0) {
                                            count.put("20:00-20:30", number41);
                                        }
                                        if (number42 > 0) {
                                            count.put("20:30-21:00", number42);
                                        }
                                        if (number43 > 0) {
                                            count.put("21:00-21:30", number43);
                                        }
                                        if (number44 > 0) {
                                            count.put("21:30-22:00", number44);
                                        }
                                        if (number45 > 0) {
                                            count.put("22:00-22:30", number45);
                                        }
                                        if (number46 > 0) {
                                            count.put("22:30-23:00", number46);
                                        }
                                        if (number47 > 0) {
                                            count.put("23:00-23:30", number47);
                                        }
                                        if (number48 > 0) {
                                            count.put("23:30-24:00", number48);
                                        }

//                                        mTextView2.setText(String.valueOf(total));

                                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String time = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(TimeActivity.this, TimeDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Date1Str",date1Str);
                                                bundle.putString("Date2Str",date2Str);
                                                bundle.putString("Time",time);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection",collection);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                    } else {
                        number1 = 0; number2 = 0; number3 = 0; number4 = 0; number5 = 0;
                        number6 = 0; number7 = 0; number8 = 0; number9 = 0; number10 = 0; number11 = 0;
                        number12 = 0; number13 = 0; number14 = 0; number15 = 0; number16 = 0; number17 = 0;
                        number18 = 0; number19 = 0; number20 = 0; number21 = 0; number22 = 0; number23 = 0;
                        number24 = 0; number25 = 0; number26 = 0; number27 = 0; number28 = 0; number29 = 0;
                        number30 = 0; number31 = 0; number32 = 0; number33 = 0; number34 = 0; number35 = 0;
                        number36 = 0; number37 = 0; number38 = 0; number39 = 0; number40 = 0; number41 = 0;
                        number42 = 0; number43 = 0; number44 = 0; number45 = 0; number46 = 0; number47 = 0;
                        number48 = 0; total=0;

                        fgboys
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("proram",spinnerPrograms)
                                .whereEqualTo("session",spinnerSessions)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, Integer> countName = new TreeMap<>();
                                        TreeMap<String, Integer> count = new TreeMap<>();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            if (note.getName() == null) {
                                                continue;
                                            }

                                            if (note.getUrl() == null) {
                                                note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                                url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                            }

                                            if (countName.containsKey(note.getName())) {
                                                continue;
                                            } else {
                                                countName.put(note.getName(), 1);
                                                total++;
                                                count.put("ALL", total);
                                            }

                                            long epoch = note.getEdate() * 1000;
                                            Date date = new Date(epoch);
                                            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                            format.setTimeZone(TimeZone.getDefault());
                                            String formatted = format.format(date);
                                            String time = formatted.substring(11, 13) + formatted.substring(14, 16) + formatted.substring(17, 19);

                                            if ((Integer.valueOf(time) >= 000000) && (Integer.valueOf(time) <= 003000)) {
                                                number1++;
                                            } else if ((Integer.valueOf(time) >= 003000) && (Integer.valueOf(time) <= 010000)) {
                                                number2++;
                                            } else if ((Integer.valueOf(time) >= 010000) && (Integer.valueOf(time) <= 0130000)) {
                                                number3++;
                                            } else if ((Integer.valueOf(time) >= 013000) && (Integer.valueOf(time) <= 020000)) {
                                                number4++;
                                            } else if ((Integer.valueOf(time) >= 020000) && (Integer.valueOf(time) <= 023000)) {
                                                number5++;
                                            } else if ((Integer.valueOf(time) >= 023000) && (Integer.valueOf(time) <= 030000)) {
                                                number6++;
                                            } else if ((Integer.valueOf(time) >= 030000) && (Integer.valueOf(time) <= 033000)) {
                                                number7++;
                                            } else if ((Integer.valueOf(time) >= 033000) && (Integer.valueOf(time) <= 040000)) {
                                                number8++;
                                            } else if ((Integer.valueOf(time) >= 040000) && (Integer.valueOf(time) <= 043000)) {
                                                number9++;
                                            } else if ((Integer.valueOf(time) >= 043000) && (Integer.valueOf(time) <= 050000)) {
                                                number10++;
                                            } else if ((Integer.valueOf(time) >= 050000) && (Integer.valueOf(time) <= 0053000)) {
                                                number11++;
                                            } else if ((Integer.valueOf(time) >= 053000) && (Integer.valueOf(time) <= 060000)) {
                                                number12++;
                                            } else if ((Integer.valueOf(time) >= 060000) && (Integer.valueOf(time) <= 063000)) {
                                                number13++;
                                            } else if ((Integer.valueOf(time) >= 063000) && (Integer.valueOf(time) <= 070000)) {
                                                number14++;
                                            } else if ((Integer.valueOf(time) >= 070000) && (Integer.valueOf(time) <= 073000)) {
                                                number15++;
                                            } else if ((Integer.valueOf(time) >= 073000) && (Integer.valueOf(time) <= 80000)) {
                                                number16++;
                                            } else if ((Integer.valueOf(time) >= 80000) && (Integer.valueOf(time) <= 83000)) {
                                                number17++;
                                            } else if ((Integer.valueOf(time) >= 83000) && (Integer.valueOf(time) <= 90000)) {
                                                number18++;
                                            } else if ((Integer.valueOf(time) >= 90000) && (Integer.valueOf(time) <= 93000)) {
                                                number19++;
                                            } else if ((Integer.valueOf(time) >= 93000) && (Integer.valueOf(time) <= 100000)) {
                                                number20++;
                                            } else if ((Integer.valueOf(time) >= 100000) && (Integer.valueOf(time) <= 103000)) {
                                                number21++;
                                            } else if ((Integer.valueOf(time) >= 103000) && (Integer.valueOf(time) <= 110000)) {
                                                number22++;
                                            } else if ((Integer.valueOf(time) >= 110000) && (Integer.valueOf(time) <= 113000)) {
                                                number23++;
                                            } else if ((Integer.valueOf(time) >= 113000) && (Integer.valueOf(time) <= 120000)) {
                                                number24++;
                                            } else if ((Integer.valueOf(time) >= 120000) && (Integer.valueOf(time) <= 123000)) {
                                                number25++;
                                            } else if ((Integer.valueOf(time) >= 123000) && (Integer.valueOf(time) <= 130000)) {
                                                number26++;
                                            } else if ((Integer.valueOf(time) >= 130000) && (Integer.valueOf(time) <= 133000)) {
                                                number27++;
                                            } else if ((Integer.valueOf(time) >= 133000) && (Integer.valueOf(time) <= 140000)) {
                                                number28++;
                                            } else if ((Integer.valueOf(time) >= 140000) && (Integer.valueOf(time) <= 143000)) {
                                                number29++;
                                            } else if ((Integer.valueOf(time) >= 143000) && (Integer.valueOf(time) <= 150000)) {
                                                number30++;
                                            } else if ((Integer.valueOf(time) >= 150000) && (Integer.valueOf(time) <= 153000)) {
                                                number31++;
                                            } else if ((Integer.valueOf(time) >= 153000) && (Integer.valueOf(time) <= 160000)) {
                                                number32++;
                                            } else if ((Integer.valueOf(time) >= 160000) && (Integer.valueOf(time) <= 163000)) {
                                                number33++;
                                            } else if ((Integer.valueOf(time) >= 163000) && (Integer.valueOf(time) <= 170000)) {
                                                number34++;
                                            } else if ((Integer.valueOf(time) >= 170000) && (Integer.valueOf(time) <= 173000)) {
                                                number35++;
                                            } else if ((Integer.valueOf(time) >= 173000) && (Integer.valueOf(time) <= 180000)) {
                                                number36++;
                                            } else if ((Integer.valueOf(time) >= 180000) && (Integer.valueOf(time) <= 183000)) {
                                                number37++;
                                            } else if ((Integer.valueOf(time) >= 183000) && (Integer.valueOf(time) <= 190000)) {
                                                number38++;
                                            } else if ((Integer.valueOf(time) >= 190000) && (Integer.valueOf(time) <= 193000)) {
                                                number39++;
                                            } else if ((Integer.valueOf(time) >= 193000) && (Integer.valueOf(time) <= 200000)) {
                                                number40++;
                                            } else if ((Integer.valueOf(time) >= 200000) && (Integer.valueOf(time) <= 203000)) {
                                                number41++;
                                            } else if ((Integer.valueOf(time) >= 203000) && (Integer.valueOf(time) <= 210000)) {
                                                number42++;
                                            } else if ((Integer.valueOf(time) >= 210000) && (Integer.valueOf(time) <= 213000)) {
                                                number43++;
                                            } else if ((Integer.valueOf(time) >= 213000) && (Integer.valueOf(time) <= 220000)) {
                                                number44++;
                                            } else if ((Integer.valueOf(time) >= 220000) && (Integer.valueOf(time) <= 223000)) {
                                                number45++;
                                            } else if ((Integer.valueOf(time) >= 223000) && (Integer.valueOf(time) <= 230000)) {
                                                number46++;
                                            } else if ((Integer.valueOf(time) >= 230000) && (Integer.valueOf(time) <= 233000)) {
                                                number47++;
                                            } else if ((Integer.valueOf(time) >= 233000) && (Integer.valueOf(time) <= 240000)) {
                                                number48++;
                                            }
                                        }

                                        if (number1 > 0) {
                                            count.put("00:00-00:30", number1);
                                        }
                                        if (number2 > 0) {
                                            count.put("00:30-01:00", number2);
                                        }
                                        if (number3 > 0) {
                                            count.put("01:00-01:30", number3);
                                        }
                                        if (number4 > 0) {
                                            count.put("01:30-02:00", number4);
                                        }
                                        if (number5 > 0) {
                                            count.put("02:00-02:30", number5);
                                        }
                                        if (number6 > 0) {
                                            count.put("02:30-03:00", number6);
                                        }
                                        if (number7 > 0) {
                                            count.put("03:00-03:30", number7);
                                        }
                                        if (number8 > 0) {
                                            count.put("03:30-04:00", number8);
                                        }
                                        if (number9 > 0) {
                                            count.put("04:00-04:30", number9);
                                        }
                                        if (number10 > 0) {
                                            count.put("04:30-05:00", number10);
                                        }
                                        if (number11 > 0) {
                                            count.put("05:00-05:30", number11);
                                        }
                                        if (number12 > 0) {
                                            count.put("05:30-06:00", number12);
                                        }
                                        if (number13 > 0) {
                                            count.put("06:00-06:30", number13);
                                        }
                                        if (number14 > 0) {
                                            count.put("06:30-07:00", number14);
                                        }
                                        if (number15 > 0) {
                                            count.put("07:00-07:30", number15);
                                        }
                                        if (number16 > 0) {
                                            count.put("07:30-08:00", number16);
                                        }
                                        if (number17 > 0) {
                                            count.put("08:00-08:30", number17);
                                        }
                                        if (number18 > 0) {
                                            count.put("08:30-09:00", number18);
                                        }
                                        if (number19 > 0) {
                                            count.put("09:00-09:30", number19);
                                        }
                                        if (number20 > 0) {
                                            count.put("09:30-10:00", number20);
                                        }
                                        if (number21 > 0) {
                                            count.put("10:00-10:30", number21);
                                        }
                                        if (number22 > 0) {
                                            count.put("10:30-11:00", number22);
                                        }
                                        if (number23 > 0) {
                                            count.put("11:00-11:30", number23);
                                        }
                                        if (number24 > 0) {
                                            count.put("11:30-12:00", number24);
                                        }
                                        if (number25 > 0) {
                                            count.put("12:00-12:30", number25);
                                        }
                                        if (number26 > 0) {
                                            count.put("12:30-13:00", number26);
                                        }
                                        if (number27 > 0) {
                                            count.put("13:00-13:30", number27);
                                        }
                                        if (number28 > 0) {
                                            count.put("13:30-14:00", number28);
                                        }
                                        if (number29 > 0) {
                                            count.put("14:00-14:30", number29);
                                        }
                                        if (number30 > 0) {
                                            count.put("14:30-15:00", number30);
                                        }
                                        if (number31 > 0) {
                                            count.put("15:00-15:30", number31);
                                        }
                                        if (number32 > 0) {
                                            count.put("15:30-16:00", number32);
                                        }
                                        if (number33 > 0) {
                                            count.put("16:00-16:30", number33);
                                        }
                                        if (number34 > 0) {
                                            count.put("16:30-17:00", number34);
                                        }
                                        if (number35 > 0) {
                                            count.put("17:00-17:30", number35);
                                        }
                                        if (number36 > 0) {
                                            count.put("17:30-18:00", number36);
                                        }
                                        if (number37 > 0) {
                                            count.put("18:00-18:30", number37);
                                        }
                                        if (number38 > 0) {
                                            count.put("18:30-19:00", number38);
                                        }
                                        if (number39 > 0) {
                                            count.put("19:00-19:30", number39);
                                        }
                                        if (number40 > 0) {
                                            count.put("19:30-20:00", number40);
                                        }
                                        if (number41 > 0) {
                                            count.put("20:00-20:30", number41);
                                        }
                                        if (number42 > 0) {
                                            count.put("20:30-21:00", number42);
                                        }
                                        if (number43 > 0) {
                                            count.put("21:00-21:30", number43);
                                        }
                                        if (number44 > 0) {
                                            count.put("21:30-22:00", number44);
                                        }
                                        if (number45 > 0) {
                                            count.put("22:00-22:30", number45);
                                        }
                                        if (number46 > 0) {
                                            count.put("22:30-23:00", number46);
                                        }
                                        if (number47 > 0) {
                                            count.put("23:00-23:30", number47);
                                        }
                                        if (number48 > 0) {
                                            count.put("23:30-24:00", number48);
                                        }

//                                        mTextView2.setText(String.valueOf(total));

                                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String time = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(TimeActivity.this, TimeDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Date1Str",date1Str);
                                                bundle.putString("Date2Str",date2Str);
                                                bundle.putString("Time",time);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection",collection);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            finalSessions = new ArrayList<String>();
            sessionsList = Arrays.asList(sessions);
            finalSessions.addAll(sessionsList);

            adapterSessions = new ArrayAdapter<String>
                    (this, R.layout.spinner_item, finalSessions);

            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("program",spinnerPrograms)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getSession() == null)
                                    continue;

                                if (finalSessions.contains(note.getSession())) {
                                    continue;
                                } else {
                                    finalSessions.add(note.getSession());
                                    adapterSessions.notifyDataSetChanged();
                                }

                            }
                        }
                    });
            adapterSessions.setDropDownViewResource(R.layout.spinner_item);
            session.setAdapter(adapterSessions);

            session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerSessions = parent.getItemAtPosition(position).toString();
                    posSession = position;

                    if (spinnerSessions.equals("ALL")) {
                        number1 = 0; number2 = 0; number3 = 0; number4 = 0; number5 = 0;
                        number6 = 0; number7 = 0; number8 = 0; number9 = 0; number10 = 0; number11 = 0;
                        number12 = 0; number13 = 0; number14 = 0; number15 = 0; number16 = 0; number17 = 0;
                        number18 = 0; number19 = 0; number20 = 0; number21 = 0; number22 = 0; number23 = 0;
                        number24 = 0; number25 = 0; number26 = 0; number27 = 0; number28 = 0; number29 = 0;
                        number30 = 0; number31 = 0; number32 = 0; number33 = 0; number34 = 0; number35 = 0;
                        number36 = 0; number37 = 0; number38 = 0; number39 = 0; number40 = 0; number41 = 0;
                        number42 = 0; number43 = 0; number44 = 0; number45 = 0; number46 = 0; number47 = 0;
                        number48 = 0; total=0;

                        fgboys
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("category",spinnerCategories)
                                .whereEqualTo("program",spinnerPrograms)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, Integer> countName = new TreeMap<>();
                                        TreeMap<String, Integer> count = new TreeMap<>();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            if (note.getName() == null) {
                                                continue;
                                            }

                                            if (note.getUrl() == null) {
                                                note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                                url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                            }

                                            if (countName.containsKey(note.getName())) {
                                                continue;
                                            } else {
                                                countName.put(note.getName(), 1);
                                                total++;
                                                count.put("ALL", total);
                                            }

                                            long epoch = note.getEdate() * 1000;
                                            Date date = new Date(epoch);
                                            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                            format.setTimeZone(TimeZone.getDefault());
                                            String formatted = format.format(date);
                                            String time = formatted.substring(11, 13) + formatted.substring(14, 16) + formatted.substring(17, 19);

                                            if ((Integer.valueOf(time) >= 000000) && (Integer.valueOf(time) <= 003000)) {
                                                number1++;
                                            } else if ((Integer.valueOf(time) >= 003000) && (Integer.valueOf(time) <= 010000)) {
                                                number2++;
                                            } else if ((Integer.valueOf(time) >= 010000) && (Integer.valueOf(time) <= 0130000)) {
                                                number3++;
                                            } else if ((Integer.valueOf(time) >= 013000) && (Integer.valueOf(time) <= 020000)) {
                                                number4++;
                                            } else if ((Integer.valueOf(time) >= 020000) && (Integer.valueOf(time) <= 023000)) {
                                                number5++;
                                            } else if ((Integer.valueOf(time) >= 023000) && (Integer.valueOf(time) <= 030000)) {
                                                number6++;
                                            } else if ((Integer.valueOf(time) >= 030000) && (Integer.valueOf(time) <= 033000)) {
                                                number7++;
                                            } else if ((Integer.valueOf(time) >= 033000) && (Integer.valueOf(time) <= 040000)) {
                                                number8++;
                                            } else if ((Integer.valueOf(time) >= 040000) && (Integer.valueOf(time) <= 043000)) {
                                                number9++;
                                            } else if ((Integer.valueOf(time) >= 043000) && (Integer.valueOf(time) <= 050000)) {
                                                number10++;
                                            } else if ((Integer.valueOf(time) >= 050000) && (Integer.valueOf(time) <= 0053000)) {
                                                number11++;
                                            } else if ((Integer.valueOf(time) >= 053000) && (Integer.valueOf(time) <= 060000)) {
                                                number12++;
                                            } else if ((Integer.valueOf(time) >= 060000) && (Integer.valueOf(time) <= 063000)) {
                                                number13++;
                                            } else if ((Integer.valueOf(time) >= 063000) && (Integer.valueOf(time) <= 070000)) {
                                                number14++;
                                            } else if ((Integer.valueOf(time) >= 070000) && (Integer.valueOf(time) <= 073000)) {
                                                number15++;
                                            } else if ((Integer.valueOf(time) >= 073000) && (Integer.valueOf(time) <= 80000)) {
                                                number16++;
                                            } else if ((Integer.valueOf(time) >= 80000) && (Integer.valueOf(time) <= 83000)) {
                                                number17++;
                                            } else if ((Integer.valueOf(time) >= 83000) && (Integer.valueOf(time) <= 90000)) {
                                                number18++;
                                            } else if ((Integer.valueOf(time) >= 90000) && (Integer.valueOf(time) <= 93000)) {
                                                number19++;
                                            } else if ((Integer.valueOf(time) >= 93000) && (Integer.valueOf(time) <= 100000)) {
                                                number20++;
                                            } else if ((Integer.valueOf(time) >= 100000) && (Integer.valueOf(time) <= 103000)) {
                                                number21++;
                                            } else if ((Integer.valueOf(time) >= 103000) && (Integer.valueOf(time) <= 110000)) {
                                                number22++;
                                            } else if ((Integer.valueOf(time) >= 110000) && (Integer.valueOf(time) <= 113000)) {
                                                number23++;
                                            } else if ((Integer.valueOf(time) >= 113000) && (Integer.valueOf(time) <= 120000)) {
                                                number24++;
                                            } else if ((Integer.valueOf(time) >= 120000) && (Integer.valueOf(time) <= 123000)) {
                                                number25++;
                                            } else if ((Integer.valueOf(time) >= 123000) && (Integer.valueOf(time) <= 130000)) {
                                                number26++;
                                            } else if ((Integer.valueOf(time) >= 130000) && (Integer.valueOf(time) <= 133000)) {
                                                number27++;
                                            } else if ((Integer.valueOf(time) >= 133000) && (Integer.valueOf(time) <= 140000)) {
                                                number28++;
                                            } else if ((Integer.valueOf(time) >= 140000) && (Integer.valueOf(time) <= 143000)) {
                                                number29++;
                                            } else if ((Integer.valueOf(time) >= 143000) && (Integer.valueOf(time) <= 150000)) {
                                                number30++;
                                            } else if ((Integer.valueOf(time) >= 150000) && (Integer.valueOf(time) <= 153000)) {
                                                number31++;
                                            } else if ((Integer.valueOf(time) >= 153000) && (Integer.valueOf(time) <= 160000)) {
                                                number32++;
                                            } else if ((Integer.valueOf(time) >= 160000) && (Integer.valueOf(time) <= 163000)) {
                                                number33++;
                                            } else if ((Integer.valueOf(time) >= 163000) && (Integer.valueOf(time) <= 170000)) {
                                                number34++;
                                            } else if ((Integer.valueOf(time) >= 170000) && (Integer.valueOf(time) <= 173000)) {
                                                number35++;
                                            } else if ((Integer.valueOf(time) >= 173000) && (Integer.valueOf(time) <= 180000)) {
                                                number36++;
                                            } else if ((Integer.valueOf(time) >= 180000) && (Integer.valueOf(time) <= 183000)) {
                                                number37++;
                                            } else if ((Integer.valueOf(time) >= 183000) && (Integer.valueOf(time) <= 190000)) {
                                                number38++;
                                            } else if ((Integer.valueOf(time) >= 190000) && (Integer.valueOf(time) <= 193000)) {
                                                number39++;
                                            } else if ((Integer.valueOf(time) >= 193000) && (Integer.valueOf(time) <= 200000)) {
                                                number40++;
                                            } else if ((Integer.valueOf(time) >= 200000) && (Integer.valueOf(time) <= 203000)) {
                                                number41++;
                                            } else if ((Integer.valueOf(time) >= 203000) && (Integer.valueOf(time) <= 210000)) {
                                                number42++;
                                            } else if ((Integer.valueOf(time) >= 210000) && (Integer.valueOf(time) <= 213000)) {
                                                number43++;
                                            } else if ((Integer.valueOf(time) >= 213000) && (Integer.valueOf(time) <= 220000)) {
                                                number44++;
                                            } else if ((Integer.valueOf(time) >= 220000) && (Integer.valueOf(time) <= 223000)) {
                                                number45++;
                                            } else if ((Integer.valueOf(time) >= 223000) && (Integer.valueOf(time) <= 230000)) {
                                                number46++;
                                            } else if ((Integer.valueOf(time) >= 230000) && (Integer.valueOf(time) <= 233000)) {
                                                number47++;
                                            } else if ((Integer.valueOf(time) >= 233000) && (Integer.valueOf(time) <= 240000)) {
                                                number48++;
                                            }
                                        }

                                        if (number1 > 0) {
                                            count.put("00:00-00:30", number1);
                                        }
                                        if (number2 > 0) {
                                            count.put("00:30-01:00", number2);
                                        }
                                        if (number3 > 0) {
                                            count.put("01:00-01:30", number3);
                                        }
                                        if (number4 > 0) {
                                            count.put("01:30-02:00", number4);
                                        }
                                        if (number5 > 0) {
                                            count.put("02:00-02:30", number5);
                                        }
                                        if (number6 > 0) {
                                            count.put("02:30-03:00", number6);
                                        }
                                        if (number7 > 0) {
                                            count.put("03:00-03:30", number7);
                                        }
                                        if (number8 > 0) {
                                            count.put("03:30-04:00", number8);
                                        }
                                        if (number9 > 0) {
                                            count.put("04:00-04:30", number9);
                                        }
                                        if (number10 > 0) {
                                            count.put("04:30-05:00", number10);
                                        }
                                        if (number11 > 0) {
                                            count.put("05:00-05:30", number11);
                                        }
                                        if (number12 > 0) {
                                            count.put("05:30-06:00", number12);
                                        }
                                        if (number13 > 0) {
                                            count.put("06:00-06:30", number13);
                                        }
                                        if (number14 > 0) {
                                            count.put("06:30-07:00", number14);
                                        }
                                        if (number15 > 0) {
                                            count.put("07:00-07:30", number15);
                                        }
                                        if (number16 > 0) {
                                            count.put("07:30-08:00", number16);
                                        }
                                        if (number17 > 0) {
                                            count.put("08:00-08:30", number17);
                                        }
                                        if (number18 > 0) {
                                            count.put("08:30-09:00", number18);
                                        }
                                        if (number19 > 0) {
                                            count.put("09:00-09:30", number19);
                                        }
                                        if (number20 > 0) {
                                            count.put("09:30-10:00", number20);
                                        }
                                        if (number21 > 0) {
                                            count.put("10:00-10:30", number21);
                                        }
                                        if (number22 > 0) {
                                            count.put("10:30-11:00", number22);
                                        }
                                        if (number23 > 0) {
                                            count.put("11:00-11:30", number23);
                                        }
                                        if (number24 > 0) {
                                            count.put("11:30-12:00", number24);
                                        }
                                        if (number25 > 0) {
                                            count.put("12:00-12:30", number25);
                                        }
                                        if (number26 > 0) {
                                            count.put("12:30-13:00", number26);
                                        }
                                        if (number27 > 0) {
                                            count.put("13:00-13:30", number27);
                                        }
                                        if (number28 > 0) {
                                            count.put("13:30-14:00", number28);
                                        }
                                        if (number29 > 0) {
                                            count.put("14:00-14:30", number29);
                                        }
                                        if (number30 > 0) {
                                            count.put("14:30-15:00", number30);
                                        }
                                        if (number31 > 0) {
                                            count.put("15:00-15:30", number31);
                                        }
                                        if (number32 > 0) {
                                            count.put("15:30-16:00", number32);
                                        }
                                        if (number33 > 0) {
                                            count.put("16:00-16:30", number33);
                                        }
                                        if (number34 > 0) {
                                            count.put("16:30-17:00", number34);
                                        }
                                        if (number35 > 0) {
                                            count.put("17:00-17:30", number35);
                                        }
                                        if (number36 > 0) {
                                            count.put("17:30-18:00", number36);
                                        }
                                        if (number37 > 0) {
                                            count.put("18:00-18:30", number37);
                                        }
                                        if (number38 > 0) {
                                            count.put("18:30-19:00", number38);
                                        }
                                        if (number39 > 0) {
                                            count.put("19:00-19:30", number39);
                                        }
                                        if (number40 > 0) {
                                            count.put("19:30-20:00", number40);
                                        }
                                        if (number41 > 0) {
                                            count.put("20:00-20:30", number41);
                                        }
                                        if (number42 > 0) {
                                            count.put("20:30-21:00", number42);
                                        }
                                        if (number43 > 0) {
                                            count.put("21:00-21:30", number43);
                                        }
                                        if (number44 > 0) {
                                            count.put("21:30-22:00", number44);
                                        }
                                        if (number45 > 0) {
                                            count.put("22:00-22:30", number45);
                                        }
                                        if (number46 > 0) {
                                            count.put("22:30-23:00", number46);
                                        }
                                        if (number47 > 0) {
                                            count.put("23:00-23:30", number47);
                                        }
                                        if (number48 > 0) {
                                            count.put("23:30-24:00", number48);
                                        }

//                                        mTextView2.setText(String.valueOf(total));

                                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String time = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(TimeActivity.this, TimeDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Date1Str",date1Str);
                                                bundle.putString("Date2Str",date2Str);
                                                bundle.putString("Time",time);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection",collection);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });

                    } else {
                        number1 = 0; number2 = 0; number3 = 0; number4 = 0; number5 = 0;
                        number6 = 0; number7 = 0; number8 = 0; number9 = 0; number10 = 0; number11 = 0;
                        number12 = 0; number13 = 0; number14 = 0; number15 = 0; number16 = 0; number17 = 0;
                        number18 = 0; number19 = 0; number20 = 0; number21 = 0; number22 = 0; number23 = 0;
                        number24 = 0; number25 = 0; number26 = 0; number27 = 0; number28 = 0; number29 = 0;
                        number30 = 0; number31 = 0; number32 = 0; number33 = 0; number34 = 0; number35 = 0;
                        number36 = 0; number37 = 0; number38 = 0; number39 = 0; number40 = 0; number41 = 0;
                        number42 = 0; number43 = 0; number44 = 0; number45 = 0; number46 = 0; number47 = 0;
                        number48 = 0; total=0;

                        fgboys
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("program", spinnerPrograms)
                                .whereEqualTo("category", spinnerCategories)
                                .whereEqualTo("session", spinnerSessions)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, Integer> countName = new TreeMap<>();
                                        TreeMap<String, Integer> count = new TreeMap<>();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            if (note.getName() == null) {
                                                continue;
                                            }

                                            if (note.getUrl() == null) {
                                                note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                                url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                            }

                                            if (countName.containsKey(note.getName())) {
                                                continue;
                                            } else {
                                                countName.put(note.getName(), 1);
                                                total++;
                                                count.put("ALL", total);
                                            }

                                            long epoch = note.getEdate() * 1000;
                                            Date date = new Date(epoch);
                                            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                            format.setTimeZone(TimeZone.getDefault());
                                            String formatted = format.format(date);
                                            String time = formatted.substring(11, 13) + formatted.substring(14, 16) + formatted.substring(17, 19);

                                            if ((Integer.valueOf(time) >= 000000) && (Integer.valueOf(time) <= 003000)) {
                                                number1++;
                                            } else if ((Integer.valueOf(time) >= 003000) && (Integer.valueOf(time) <= 010000)) {
                                                number2++;
                                            } else if ((Integer.valueOf(time) >= 010000) && (Integer.valueOf(time) <= 0130000)) {
                                                number3++;
                                            } else if ((Integer.valueOf(time) >= 013000) && (Integer.valueOf(time) <= 020000)) {
                                                number4++;
                                            } else if ((Integer.valueOf(time) >= 020000) && (Integer.valueOf(time) <= 023000)) {
                                                number5++;
                                            } else if ((Integer.valueOf(time) >= 023000) && (Integer.valueOf(time) <= 030000)) {
                                                number6++;
                                            } else if ((Integer.valueOf(time) >= 030000) && (Integer.valueOf(time) <= 033000)) {
                                                number7++;
                                            } else if ((Integer.valueOf(time) >= 033000) && (Integer.valueOf(time) <= 040000)) {
                                                number8++;
                                            } else if ((Integer.valueOf(time) >= 040000) && (Integer.valueOf(time) <= 043000)) {
                                                number9++;
                                            } else if ((Integer.valueOf(time) >= 043000) && (Integer.valueOf(time) <= 050000)) {
                                                number10++;
                                            } else if ((Integer.valueOf(time) >= 050000) && (Integer.valueOf(time) <= 0053000)) {
                                                number11++;
                                            } else if ((Integer.valueOf(time) >= 053000) && (Integer.valueOf(time) <= 060000)) {
                                                number12++;
                                            } else if ((Integer.valueOf(time) >= 060000) && (Integer.valueOf(time) <= 063000)) {
                                                number13++;
                                            } else if ((Integer.valueOf(time) >= 063000) && (Integer.valueOf(time) <= 070000)) {
                                                number14++;
                                            } else if ((Integer.valueOf(time) >= 070000) && (Integer.valueOf(time) <= 073000)) {
                                                number15++;
                                            } else if ((Integer.valueOf(time) >= 073000) && (Integer.valueOf(time) <= 80000)) {
                                                number16++;
                                            } else if ((Integer.valueOf(time) >= 80000) && (Integer.valueOf(time) <= 83000)) {
                                                number17++;
                                            } else if ((Integer.valueOf(time) >= 83000) && (Integer.valueOf(time) <= 90000)) {
                                                number18++;
                                            } else if ((Integer.valueOf(time) >= 90000) && (Integer.valueOf(time) <= 93000)) {
                                                number19++;
                                            } else if ((Integer.valueOf(time) >= 93000) && (Integer.valueOf(time) <= 100000)) {
                                                number20++;
                                            } else if ((Integer.valueOf(time) >= 100000) && (Integer.valueOf(time) <= 103000)) {
                                                number21++;
                                            } else if ((Integer.valueOf(time) >= 103000) && (Integer.valueOf(time) <= 110000)) {
                                                number22++;
                                            } else if ((Integer.valueOf(time) >= 110000) && (Integer.valueOf(time) <= 113000)) {
                                                number23++;
                                            } else if ((Integer.valueOf(time) >= 113000) && (Integer.valueOf(time) <= 120000)) {
                                                number24++;
                                            } else if ((Integer.valueOf(time) >= 120000) && (Integer.valueOf(time) <= 123000)) {
                                                number25++;
                                            } else if ((Integer.valueOf(time) >= 123000) && (Integer.valueOf(time) <= 130000)) {
                                                number26++;
                                            } else if ((Integer.valueOf(time) >= 130000) && (Integer.valueOf(time) <= 133000)) {
                                                number27++;
                                            } else if ((Integer.valueOf(time) >= 133000) && (Integer.valueOf(time) <= 140000)) {
                                                number28++;
                                            } else if ((Integer.valueOf(time) >= 140000) && (Integer.valueOf(time) <= 143000)) {
                                                number29++;
                                            } else if ((Integer.valueOf(time) >= 143000) && (Integer.valueOf(time) <= 150000)) {
                                                number30++;
                                            } else if ((Integer.valueOf(time) >= 150000) && (Integer.valueOf(time) <= 153000)) {
                                                number31++;
                                            } else if ((Integer.valueOf(time) >= 153000) && (Integer.valueOf(time) <= 160000)) {
                                                number32++;
                                            } else if ((Integer.valueOf(time) >= 160000) && (Integer.valueOf(time) <= 163000)) {
                                                number33++;
                                            } else if ((Integer.valueOf(time) >= 163000) && (Integer.valueOf(time) <= 170000)) {
                                                number34++;
                                            } else if ((Integer.valueOf(time) >= 170000) && (Integer.valueOf(time) <= 173000)) {
                                                number35++;
                                            } else if ((Integer.valueOf(time) >= 173000) && (Integer.valueOf(time) <= 180000)) {
                                                number36++;
                                            } else if ((Integer.valueOf(time) >= 180000) && (Integer.valueOf(time) <= 183000)) {
                                                number37++;
                                            } else if ((Integer.valueOf(time) >= 183000) && (Integer.valueOf(time) <= 190000)) {
                                                number38++;
                                            } else if ((Integer.valueOf(time) >= 190000) && (Integer.valueOf(time) <= 193000)) {
                                                number39++;
                                            } else if ((Integer.valueOf(time) >= 193000) && (Integer.valueOf(time) <= 200000)) {
                                                number40++;
                                            } else if ((Integer.valueOf(time) >= 200000) && (Integer.valueOf(time) <= 203000)) {
                                                number41++;
                                            } else if ((Integer.valueOf(time) >= 203000) && (Integer.valueOf(time) <= 210000)) {
                                                number42++;
                                            } else if ((Integer.valueOf(time) >= 210000) && (Integer.valueOf(time) <= 213000)) {
                                                number43++;
                                            } else if ((Integer.valueOf(time) >= 213000) && (Integer.valueOf(time) <= 220000)) {
                                                number44++;
                                            } else if ((Integer.valueOf(time) >= 220000) && (Integer.valueOf(time) <= 223000)) {
                                                number45++;
                                            } else if ((Integer.valueOf(time) >= 223000) && (Integer.valueOf(time) <= 230000)) {
                                                number46++;
                                            } else if ((Integer.valueOf(time) >= 230000) && (Integer.valueOf(time) <= 233000)) {
                                                number47++;
                                            } else if ((Integer.valueOf(time) >= 233000) && (Integer.valueOf(time) <= 240000)) {
                                                number48++;
                                            }
                                        }

                                        if (number1 > 0) {
                                            count.put("00:00-00:30", number1);
                                        }
                                        if (number2 > 0) {
                                            count.put("00:30-01:00", number2);
                                        }
                                        if (number3 > 0) {
                                            count.put("01:00-01:30", number3);
                                        }
                                        if (number4 > 0) {
                                            count.put("01:30-02:00", number4);
                                        }
                                        if (number5 > 0) {
                                            count.put("02:00-02:30", number5);
                                        }
                                        if (number6 > 0) {
                                            count.put("02:30-03:00", number6);
                                        }
                                        if (number7 > 0) {
                                            count.put("03:00-03:30", number7);
                                        }
                                        if (number8 > 0) {
                                            count.put("03:30-04:00", number8);
                                        }
                                        if (number9 > 0) {
                                            count.put("04:00-04:30", number9);
                                        }
                                        if (number10 > 0) {
                                            count.put("04:30-05:00", number10);
                                        }
                                        if (number11 > 0) {
                                            count.put("05:00-05:30", number11);
                                        }
                                        if (number12 > 0) {
                                            count.put("05:30-06:00", number12);
                                        }
                                        if (number13 > 0) {
                                            count.put("06:00-06:30", number13);
                                        }
                                        if (number14 > 0) {
                                            count.put("06:30-07:00", number14);
                                        }
                                        if (number15 > 0) {
                                            count.put("07:00-07:30", number15);
                                        }
                                        if (number16 > 0) {
                                            count.put("07:30-08:00", number16);
                                        }
                                        if (number17 > 0) {
                                            count.put("08:00-08:30", number17);
                                        }
                                        if (number18 > 0) {
                                            count.put("08:30-09:00", number18);
                                        }
                                        if (number19 > 0) {
                                            count.put("09:00-09:30", number19);
                                        }
                                        if (number20 > 0) {
                                            count.put("09:30-10:00", number20);
                                        }
                                        if (number21 > 0) {
                                            count.put("10:00-10:30", number21);
                                        }
                                        if (number22 > 0) {
                                            count.put("10:30-11:00", number22);
                                        }
                                        if (number23 > 0) {
                                            count.put("11:00-11:30", number23);
                                        }
                                        if (number24 > 0) {
                                            count.put("11:30-12:00", number24);
                                        }
                                        if (number25 > 0) {
                                            count.put("12:00-12:30", number25);
                                        }
                                        if (number26 > 0) {
                                            count.put("12:30-13:00", number26);
                                        }
                                        if (number27 > 0) {
                                            count.put("13:00-13:30", number27);
                                        }
                                        if (number28 > 0) {
                                            count.put("13:30-14:00", number28);
                                        }
                                        if (number29 > 0) {
                                            count.put("14:00-14:30", number29);
                                        }
                                        if (number30 > 0) {
                                            count.put("14:30-15:00", number30);
                                        }
                                        if (number31 > 0) {
                                            count.put("15:00-15:30", number31);
                                        }
                                        if (number32 > 0) {
                                            count.put("15:30-16:00", number32);
                                        }
                                        if (number33 > 0) {
                                            count.put("16:00-16:30", number33);
                                        }
                                        if (number34 > 0) {
                                            count.put("16:30-17:00", number34);
                                        }
                                        if (number35 > 0) {
                                            count.put("17:00-17:30", number35);
                                        }
                                        if (number36 > 0) {
                                            count.put("17:30-18:00", number36);
                                        }
                                        if (number37 > 0) {
                                            count.put("18:00-18:30", number37);
                                        }
                                        if (number38 > 0) {
                                            count.put("18:30-19:00", number38);
                                        }
                                        if (number39 > 0) {
                                            count.put("19:00-19:30", number39);
                                        }
                                        if (number40 > 0) {
                                            count.put("19:30-20:00", number40);
                                        }
                                        if (number41 > 0) {
                                            count.put("20:00-20:30", number41);
                                        }
                                        if (number42 > 0) {
                                            count.put("20:30-21:00", number42);
                                        }
                                        if (number43 > 0) {
                                            count.put("21:00-21:30", number43);
                                        }
                                        if (number44 > 0) {
                                            count.put("21:30-22:00", number44);
                                        }
                                        if (number45 > 0) {
                                            count.put("22:00-22:30", number45);
                                        }
                                        if (number46 > 0) {
                                            count.put("22:30-23:00", number46);
                                        }
                                        if (number47 > 0) {
                                            count.put("23:00-23:30", number47);
                                        }
                                        if (number48 > 0) {
                                            count.put("23:30-24:00", number48);
                                        }

//                                        mTextView2.setText(String.valueOf(total));

                                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String time = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(TimeActivity.this, TimeDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Date1Str",date1Str);
                                                bundle.putString("Date2Str",date2Str);
                                                bundle.putString("Time",time);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection",collection);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
}