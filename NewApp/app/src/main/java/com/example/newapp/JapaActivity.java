package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
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

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.Nullable;

public class JapaActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference fgboys;
    private String collection = "";
    private static final String TAG = "JapaMainActivity";
    ListView mListView;
    private TextView mTextView;
    private long date1 = 0, date2 = 0;
    private int total = 0;
    private int number0=0, number2=0, number4=0, number8=0, number15=0, number16=0;
    private double per0=0.0, per2=0.0, per4=0.0, per8=0.0, per15=0.0, per16=0.0;
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
    ArrayAdapter<String> adapterPrograms;
    ArrayAdapter<String> adapterCategories;
    ArrayAdapter<String> adapterSessions;
    String spinProgram = "";
    String spinCategory = "";
    final String[] categories = new String[]{"ALL"};
    String[] programs = new String[]{"ALL"};
    String[] sessions = new String[]{"ALL"};

    Spinner program, category, session;
    int posProgram, posCategory, posSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_japa);
        collection = getIntent().getStringExtra("Collection");
        db = FirebaseFirestore.getInstance();
        fgboys = db.collection(collection);
        mTextView = findViewById(R.id.textView2);
        mTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mListView = findViewById(R.id.list_item);
        date1 = getIntent().getLongExtra("Date1",date1);
        date2 = getIntent().getLongExtra("Date2",date2);

        programsList = Arrays.asList(programs);
        finalPrograms.addAll(programsList);

        program = findViewById(R.id.program);
        category = findViewById(R.id.category);
        session = findViewById(R.id.session);

        adapterPrograms = new ArrayAdapter<String>(
                this,R.layout.spinner_item,finalPrograms);
        adapterPrograms.setDropDownViewResource(R.layout.spinner_item);
        program.setAdapter(adapterPrograms);

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

        session.setSelection(posSession);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Spinner program = findViewById(R.id.program);

        program.setSelection(posProgram);
        category.setSelection(posCategory);
        session.setSelection(posSession);
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
            Spinner category = findViewById(R.id.category);
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
                    .whereEqualTo("program",spinnerPrograms)
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
            Spinner category = findViewById(R.id.category);
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

    public void populateSessionsPrograms(String spinCategory, final String spinProgram) {
        final String spinCategories = spinCategory;
        final String spinPrograms = spinProgram;

        if (spinCategory.equals("1")) {
            finalSessions = new ArrayList<String>();
            sessionsList = Arrays.asList(sessions);
            finalSessions.addAll(sessionsList);

            adapterSessions = new ArrayAdapter<String>
                    (this, R.layout.spinner_item, finalSessions);
            total=0;

            fgboys
//                    .whereEqualTo("zzdate", date)
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
            Spinner session = findViewById(R.id.session);
            adapterSessions.setDropDownViewResource(R.layout.spinner_item);
            session.setAdapter(adapterSessions);

            session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerSessions = parent.getItemAtPosition(position).toString();
                    posSession = position;

                    if (spinnerSessions.equals("ALL")) {
                        total=0;
                        number0=0; number2=0; number4=0; number8=0; number15=0; number16=0;
                        fgboys
//                                .whereEqualTo("zzdate",date)
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, JapaClass> count = new TreeMap<>();
                                        String data = "";
                                        Log.d(TAG, "onEvent: Out");

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            Log.d(TAG, "onEvent: Area" + note.getFg());

                                            String japa = note.getJapa();
                                            if (japa.equals("0")) {
                                                number0++;
                                                total++;
                                            } else if (japa.equals("1") || (japa.equals("2"))) {
                                                number2++;
                                                total++;
                                            } else if (japa.equals("3") || (japa.equals("4"))) {
                                                number4++;
                                                total++;
                                            } else if (japa.equals("5") || (japa.equals("6")) || (japa.equals("7")) || (japa.equals("8"))) {
                                                number8++;
                                                total++;
                                            } else if (japa.equals("9") || (japa.equals("10")) || (japa.equals("11")) ||
                                                    (japa.equals("12")) || (japa.equals("13")) || (japa.equals("14"))
                                                    || (japa.equals("15"))) {
                                                number15++;
                                                total++;
                                            } else {
                                                number16++;
                                                total++;
                                            }
                                        }
                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        per0 = Double.parseDouble(df.format(Math.ceil(number0* 100)/total));
                                        per2 = Double.parseDouble(df.format(Math.ceil(number2* 100)/total));
                                        per4 = Double.parseDouble(df.format(Math.ceil(number4* 100)/total));
                                        per8 = Double.parseDouble(df.format(Math.ceil(number8* 100)/total));
                                        per15 = Double.parseDouble(df.format(Math.ceil(number15* 100)/total));
                                        per16 = Double.parseDouble(df.format(Math.ceil(number16* 100)/total));

                                        count.put("0 rounds", new JapaClass(number0, per0));
                                        count.put("01-02 rounds", new JapaClass(number2, per2));
                                        count.put("03-04 rounds", new JapaClass(number4, per4));
                                        count.put("05-08 rounds", new JapaClass(number8, per8));
                                        count.put("09-15 rounds", new JapaClass(number15, per15));
                                        count.put("16-more rounds", new JapaClass(number16, per16));
                                        count.put("ALL", new JapaClass(total,100));

                                        Log.d("Count", "onEvent: " + number0 + " " + number2 + " " + number4 + " " + number8 + " " + number15 + " " +
                                                " " + number16 + "    " + total);

                                        Log.d("Count", "onEvent: " + per0 + " " + per2 + " " + per4 + " " + per8 + " " + per15 + " " +
                                                " " + per16 + "    " + total);

                                        final JapaActivityAdapter adapter = new JapaActivityAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String japaValue = adapter.getItem(i).getKey();
                                                String japa = "";
                                                switch (japaValue) {
                                                    case "0 rounds":
                                                        japa = "0";
                                                        break;
                                                    case "01-02 rounds":
                                                        japa = "1";
                                                        break;
                                                    case "03-04 rounds":
                                                        japa = "2";
                                                        break;
                                                    case "05-08 rounds":
                                                        japa = "3";
                                                        break;
                                                    case "09-15 rounds":
                                                        japa = "4";
                                                        break;
                                                    case "16-more rounds":
                                                        japa = "6";
                                                        break;
                                                    case "ALL":
                                                        japa = "5";
                                                }
                                                Intent intent = new Intent(JapaActivity.this, Japa.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Japa", japa);
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
                        total=0;
                        number0=0; number2=0; number4=0; number8=0; number15=0; number16=0;
                        fgboys
//                                .whereEqualTo("zzdate",date)
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("session",spinnerSessions)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, JapaClass> count = new TreeMap<>();
                                        String data = "";
                                        Log.d(TAG, "onEvent: Out");

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            Log.d(TAG, "onEvent: Area" + note.getFg());

                                            String japa = note.getJapa();
                                            if (japa.equals("0")) {
                                                number0++;
                                                total++;
                                            } else if (japa.equals("1") || (japa.equals("2"))) {
                                                number2++;
                                                total++;
                                            } else if (japa.equals("3") || (japa.equals("4"))) {
                                                number4++;
                                                total++;
                                            } else if (japa.equals("5") || (japa.equals("6")) || (japa.equals("7")) || (japa.equals("8"))) {
                                                number8++;
                                                total++;
                                            } else if (japa.equals("9") || (japa.equals("10")) || (japa.equals("11")) ||
                                                    (japa.equals("12")) || (japa.equals("13")) || (japa.equals("14"))
                                                    || (japa.equals("15"))) {
                                                number15++;
                                                total++;
                                            } else {
                                                number16++;
                                                total++;
                                            }
                                        }
                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        per0 = Double.parseDouble(df.format(Math.ceil(number0* 100)/total));
                                        per2 = Double.parseDouble(df.format(Math.ceil(number2* 100)/total));
                                        per4 = Double.parseDouble(df.format(Math.ceil(number4* 100)/total));
                                        per8 = Double.parseDouble(df.format(Math.ceil(number8* 100)/total));
                                        per15 = Double.parseDouble(df.format(Math.ceil(number15* 100)/total));
                                        per16 = Double.parseDouble(df.format(Math.ceil(number16* 100)/total));

                                        count.put("0 rounds", new JapaClass(number0, per0));
                                        count.put("01-02 rounds", new JapaClass(number2, per2));
                                        count.put("03-04 rounds", new JapaClass(number4, per4));
                                        count.put("05-08 rounds", new JapaClass(number8, per8));
                                        count.put("09-15 rounds", new JapaClass(number15, per15));
                                        count.put("16-more rounds", new JapaClass(number16, per16));
                                        count.put("ALL", new JapaClass(total,100));

                                        Log.d("Count", "onEvent: " + number0 + " " + number2 + " " + number4 + " " + number8 + " " + number15 + " " +
                                                " " + number16 + "    " + total);

                                        Log.d("Count", "onEvent: " + per0 + " " + per2 + " " + per4 + " " + per8 + " " + per15 + " " +
                                                " " + per16 + "    " + total);

                                        final JapaActivityAdapter adapter = new JapaActivityAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String japaValue = adapter.getItem(i).getKey();
                                                String japa = "";
                                                switch (japaValue) {
                                                    case "0 rounds":
                                                        japa = "0";
                                                        break;
                                                    case "01-02 rounds":
                                                        japa = "1";
                                                        break;
                                                    case "03-04 rounds":
                                                        japa = "2";
                                                        break;
                                                    case "05-08 rounds":
                                                        japa = "3";
                                                        break;
                                                    case "09-15 rounds":
                                                        japa = "4";
                                                        break;
                                                    case "16-more rounds":
                                                        japa = "6";
                                                        break;
                                                    case "ALL":
                                                        japa = "5";
                                                }
                                                Intent intent = new Intent(JapaActivity.this, Japa.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Japa", japa);
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
            total=0;

            fgboys
//                    .whereEqualTo("zzdate", date)
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

                                if (finalSessions.contains(note.getSession())) {
                                    continue;
                                } else {
                                    finalSessions.add(note.getSession());
                                    adapterSessions.notifyDataSetChanged();
                                }

                            }
                        }
                    });
            Spinner session = findViewById(R.id.session);
            adapterSessions.setDropDownViewResource(R.layout.spinner_item);
            session.setAdapter(adapterSessions);

            session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spinnerSessions.equals("ALL")) {
                        spinnerSessions = parent.getItemAtPosition(position).toString();
                        posSession = position;

                        total=0;
                        number0=0; number2=0; number4=0; number8=0; number15=0; number16=0;
                        fgboys
//                                .whereEqualTo("zzdate",date)
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("category",spinnerCategories)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, JapaClass> count = new TreeMap<>();
                                        String data = "";
                                        Log.d(TAG, "onEvent: Out");

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            Log.d(TAG, "onEvent: Area" + note.getZtl());

                                            String japa = note.getJapa();
                                            if (japa.equals("0")) {
                                                number0++;
                                                total++;
                                            } else if (japa.equals("1") || (japa.equals("2"))) {
                                                number2++;
                                                total++;
                                            } else if (japa.equals("3") || (japa.equals("4"))) {
                                                number4++;
                                                total++;
                                            } else if (japa.equals("5") || (japa.equals("6")) || (japa.equals("7")) || (japa.equals("8"))) {
                                                number8++;
                                                total++;
                                            } else if (japa.equals("9") || (japa.equals("10")) || (japa.equals("11")) ||
                                                    (japa.equals("12")) || (japa.equals("13")) || (japa.equals("14"))
                                                    || (japa.equals("15"))) {
                                                number15++;
                                                total++;
                                            } else {
                                                number16++;
                                                total++;
                                            }
                                        }
                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        per0 = Double.parseDouble(df.format(Math.ceil(number0* 100)/total));
                                        per2 = Double.parseDouble(df.format(Math.ceil(number2* 100)/total));
                                        per4 = Double.parseDouble(df.format(Math.ceil(number4* 100)/total));
                                        per8 = Double.parseDouble(df.format(Math.ceil(number8* 100)/total));
                                        per15 = Double.parseDouble(df.format(Math.ceil(number15* 100)/total));
                                        per16 = Double.parseDouble(df.format(Math.ceil(number16* 100)/total));

                                        count.put("0 rounds", new JapaClass(number0, per0));
                                        count.put("01-02 rounds", new JapaClass(number2, per2));
                                        count.put("03-04 rounds", new JapaClass(number4, per4));
                                        count.put("05-08 rounds", new JapaClass(number8, per8));
                                        count.put("09-15 rounds", new JapaClass(number15, per15));
                                        count.put("16-more rounds", new JapaClass(number16, per16));
                                        count.put("ALL", new JapaClass(total,100));

                                        Log.d("Count", "onEvent: " + number0 + " " + number2 + " " + number4 + " " + number8 + " " + number15 + " " +
                                                " " + number16 + "    " + total);

                                        Log.d("Count", "onEvent: " + per0 + " " + per2 + " " + per4 + " " + per8 + " " + per15 + " " +
                                                " " + per16 + "    " + total);

                                        final JapaActivityAdapter adapter = new JapaActivityAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String japaValue = adapter.getItem(i).getKey();
                                                String japa = "";
                                                switch (japaValue) {
                                                    case "0 rounds":
                                                        japa = "0";
                                                        break;
                                                    case "01-02 rounds":
                                                        japa = "1";
                                                        break;
                                                    case "03-04 rounds":
                                                        japa = "2";
                                                        break;
                                                    case "05-08 rounds":
                                                        japa = "3";
                                                        break;
                                                    case "09-15 rounds":
                                                        japa = "4";
                                                        break;
                                                    case "16-more rounds":
                                                        japa = "6";
                                                        break;
                                                    case "ALL":
                                                        japa = "5";
                                                }
                                                Intent intent = new Intent(JapaActivity.this, Japa.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Japa", japa);
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
                        total=0;
                        number0=0; number2=0; number4=0; number8=0; number15=0; number16=0;
                        fgboys
//                                .whereEqualTo("zzdate",date)
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("session",spinnerSessions)
                                .whereEqualTo("category",spinnerCategories)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, JapaClass> count = new TreeMap<>();
                                        String data = "";
                                        Log.d(TAG, "onEvent: Out");

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            Log.d(TAG, "onEvent: Area" + note.getZtl());

                                            String japa = note.getJapa();
                                            if (japa.equals("0")) {
                                                number0++;
                                                total++;
                                            } else if (japa.equals("1") || (japa.equals("2"))) {
                                                number2++;
                                                total++;
                                            } else if (japa.equals("3") || (japa.equals("4"))) {
                                                number4++;
                                                total++;
                                            } else if (japa.equals("5") || (japa.equals("6")) || (japa.equals("7")) || (japa.equals("8"))) {
                                                number8++;
                                                total++;
                                            } else if (japa.equals("9") || (japa.equals("10")) || (japa.equals("11")) ||
                                                    (japa.equals("12")) || (japa.equals("13")) || (japa.equals("14"))
                                                    || (japa.equals("15"))) {
                                                number15++;
                                                total++;
                                            } else {
                                                number16++;
                                                total++;
                                            }
                                        }
                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        per0 = Double.parseDouble(df.format(Math.ceil(number0* 100)/total));
                                        per2 = Double.parseDouble(df.format(Math.ceil(number2* 100)/total));
                                        per4 = Double.parseDouble(df.format(Math.ceil(number4* 100)/total));
                                        per8 = Double.parseDouble(df.format(Math.ceil(number8* 100)/total));
                                        per15 = Double.parseDouble(df.format(Math.ceil(number15* 100)/total));
                                        per16 = Double.parseDouble(df.format(Math.ceil(number16* 100)/total));

                                        count.put("0 rounds", new JapaClass(number0, per0));
                                        count.put("01-02 rounds", new JapaClass(number2, per2));
                                        count.put("03-04 rounds", new JapaClass(number4, per4));
                                        count.put("05-08 rounds", new JapaClass(number8, per8));
                                        count.put("09-15 rounds", new JapaClass(number15, per15));
                                        count.put("16-more rounds", new JapaClass(number16, per16));
                                        count.put("ALL", new JapaClass(total,100));

                                        Log.d("Count", "onEvent: " + number0 + " " + number2 + " " + number4 + " " + number8 + " " + number15 + " " +
                                                " " + number16 + "    " + total);

                                        Log.d("Count", "onEvent: " + per0 + " " + per2 + " " + per4 + " " + per8 + " " + per15 + " " +
                                                " " + per16 + "    " + total);

                                        final JapaActivityAdapter adapter = new JapaActivityAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String japaValue = adapter.getItem(i).getKey();
                                                String japa = "";
                                                switch (japaValue) {
                                                    case "0 rounds":
                                                        japa = "0";
                                                        break;
                                                    case "01-02 rounds":
                                                        japa = "1";
                                                        break;
                                                    case "03-04 rounds":
                                                        japa = "2";
                                                        break;
                                                    case "05-08 rounds":
                                                        japa = "3";
                                                        break;
                                                    case "09-15 rounds":
                                                        japa = "4";
                                                        break;
                                                    case "16-more rounds":
                                                        japa = "6";
                                                        break;
                                                    case "ALL":
                                                        japa = "5";
                                                }
                                                Intent intent = new Intent(JapaActivity.this, Japa.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Japa", japa);
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

    public void populateSessions(String spinCategory, final String spinProgram) {
        final String spinCategories = spinCategory;
        final String spinPrograms = spinProgram;

        if (spinCategory.equals("1")) {
            finalSessions = new ArrayList<String>();
            sessionsList = Arrays.asList(sessions);
            finalSessions.addAll(sessionsList);

            adapterSessions = new ArrayAdapter<String>
                    (this, R.layout.spinner_item, finalSessions);
            total=0;

            fgboys
//                    .whereEqualTo("zzdate", date)
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
            Spinner session = findViewById(R.id.session);
            adapterSessions.setDropDownViewResource(R.layout.spinner_item);
            session.setAdapter(adapterSessions);

            session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerSessions = parent.getItemAtPosition(position).toString();

                    if (spinnerSessions.equals("ALL")) {
                        total=0;
                        number0=0; number2=0; number4=0; number8=0; number15=0; number16=0;
                        fgboys
//                                .whereEqualTo("zzdate",date)
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("program",spinnerPrograms)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, JapaClass> count = new TreeMap<>();
                                        String data = "";
                                        Log.d(TAG, "onEvent: Out");

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            Log.d(TAG, "onEvent: Area" + note.getZtl());

                                            String japa = note.getJapa();
                                            if (japa.equals("0")) {
                                                number0++;
                                                total++;
                                            } else if (japa.equals("1") || (japa.equals("2"))) {
                                                number2++;
                                                total++;
                                            } else if (japa.equals("3") || (japa.equals("4"))) {
                                                number4++;
                                                total++;
                                            } else if (japa.equals("5") || (japa.equals("6")) || (japa.equals("7")) || (japa.equals("8"))) {
                                                number8++;
                                                total++;
                                            } else if (japa.equals("9") || (japa.equals("10")) || (japa.equals("11")) ||
                                                    (japa.equals("12")) || (japa.equals("13")) || (japa.equals("14"))
                                                    || (japa.equals("15"))) {
                                                number15++;
                                                total++;
                                            } else {
                                                number16++;
                                                total++;
                                            }
                                        }
                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        per0 = Double.parseDouble(df.format(Math.ceil(number0* 100)/total));
                                        per2 = Double.parseDouble(df.format(Math.ceil(number2* 100)/total));
                                        per4 = Double.parseDouble(df.format(Math.ceil(number4* 100)/total));
                                        per8 = Double.parseDouble(df.format(Math.ceil(number8* 100)/total));
                                        per15 = Double.parseDouble(df.format(Math.ceil(number15* 100)/total));
                                        per16 = Double.parseDouble(df.format(Math.ceil(number16* 100)/total));

                                        count.put("0 rounds", new JapaClass(number0, per0));
                                        count.put("01-02 rounds", new JapaClass(number2, per2));
                                        count.put("03-04 rounds", new JapaClass(number4, per4));
                                        count.put("05-08 rounds", new JapaClass(number8, per8));
                                        count.put("09-15 rounds", new JapaClass(number15, per15));
                                        count.put("16-more rounds", new JapaClass(number16, per16));
                                        count.put("ALL", new JapaClass(total,100));

                                        Log.d("Count", "onEvent: " + number0 + " " + number2 + " " + number4 + " " + number8 + " " + number15 + " " +
                                                " " + number16 + "    " + total);

                                        Log.d("Count", "onEvent: " + per0 + " " + per2 + " " + per4 + " " + per8 + " " + per15 + " " +
                                                " " + per16 + "    " + total);

                                        final JapaActivityAdapter adapter = new JapaActivityAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String japaValue = adapter.getItem(i).getKey();
                                                String japa = "";
                                                switch (japaValue) {
                                                    case "0 rounds":
                                                        japa = "0";
                                                        break;
                                                    case "01-02 rounds":
                                                        japa = "1";
                                                        break;
                                                    case "03-04 rounds":
                                                        japa = "2";
                                                        break;
                                                    case "05-08 rounds":
                                                        japa = "3";
                                                        break;
                                                    case "09-15 rounds":
                                                        japa = "4";
                                                        break;
                                                    case "16-more rounds":
                                                        japa = "6";
                                                        break;
                                                    case "ALL":
                                                        japa = "5";
                                                }
                                                Intent intent = new Intent(JapaActivity.this, Japa.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Japa", japa);
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
                        total=0;
                        number0=0; number2=0; number4=0; number8=0; number15=0; number16=0;
                        fgboys
//                                .whereEqualTo("zzdate",date)
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("session",spinnerSessions)
                                .whereEqualTo("program",spinnerPrograms)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, JapaClass> count = new TreeMap<>();
                                        String data = "";
                                        Log.d(TAG, "onEvent: Out");

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            Log.d(TAG, "onEvent: Area" + note.getZtl());

                                            String japa = note.getJapa();
                                            if (japa.equals("0")) {
                                                number0++;
                                                total++;
                                            } else if (japa.equals("1") || (japa.equals("2"))) {
                                                number2++;
                                                total++;
                                            } else if (japa.equals("3") || (japa.equals("4"))) {
                                                number4++;
                                                total++;
                                            } else if (japa.equals("5") || (japa.equals("6")) || (japa.equals("7")) || (japa.equals("8"))) {
                                                number8++;
                                                total++;
                                            } else if (japa.equals("9") || (japa.equals("10")) || (japa.equals("11")) ||
                                                    (japa.equals("12")) || (japa.equals("13")) || (japa.equals("14"))
                                                    || (japa.equals("15"))) {
                                                number15++;
                                                total++;
                                            } else {
                                                number16++;
                                                total++;
                                            }
                                        }
                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        per0 = Double.parseDouble(df.format(Math.ceil(number0* 100)/total));
                                        per2 = Double.parseDouble(df.format(Math.ceil(number2* 100)/total));
                                        per4 = Double.parseDouble(df.format(Math.ceil(number4* 100)/total));
                                        per8 = Double.parseDouble(df.format(Math.ceil(number8* 100)/total));
                                        per15 = Double.parseDouble(df.format(Math.ceil(number15* 100)/total));
                                        per16 = Double.parseDouble(df.format(Math.ceil(number16* 100)/total));

                                        count.put("0 rounds", new JapaClass(number0, per0));
                                        count.put("01-02 rounds", new JapaClass(number2, per2));
                                        count.put("03-04 rounds", new JapaClass(number4, per4));
                                        count.put("05-08 rounds", new JapaClass(number8, per8));
                                        count.put("09-15 rounds", new JapaClass(number15, per15));
                                        count.put("16-more rounds", new JapaClass(number16, per16));
                                        count.put("ALL", new JapaClass(total,100));

                                        Log.d("Count", "onEvent: " + number0 + " " + number2 + " " + number4 + " " + number8 + " " + number15 + " " +
                                                " " + number16 + "    " + total);

                                        Log.d("Count", "onEvent: " + per0 + " " + per2 + " " + per4 + " " + per8 + " " + per15 + " " +
                                                " " + per16 + "    " + total);

                                        final JapaActivityAdapter adapter = new JapaActivityAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String japaValue = adapter.getItem(i).getKey();
                                                String japa = "";
                                                switch (japaValue) {
                                                    case "0 rounds":
                                                        japa = "0";
                                                        break;
                                                    case "01-02 rounds":
                                                        japa = "1";
                                                        break;
                                                    case "03-04 rounds":
                                                        japa = "2";
                                                        break;
                                                    case "05-08 rounds":
                                                        japa = "3";
                                                        break;
                                                    case "09-15 rounds":
                                                        japa = "4";
                                                        break;
                                                    case "16-more rounds":
                                                        japa = "6";
                                                        break;
                                                    case "ALL":
                                                        japa = "5";
                                                }
                                                Intent intent = new Intent(JapaActivity.this, Japa.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Japa", japa);
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
            total=0;

            fgboys
//                    .whereEqualTo("zzdate", date)
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

                                if (finalSessions.contains(note.getSession())) {
                                    continue;
                                } else {
                                    finalSessions.add(note.getSession());
                                    adapterSessions.notifyDataSetChanged();
                                }

                            }
                        }
                    });
            Spinner session = findViewById(R.id.session);
            adapterSessions.setDropDownViewResource(R.layout.spinner_item);
            session.setAdapter(adapterSessions);

            session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spinnerSessions.equals("ALL")) {
                        spinnerSessions = parent.getItemAtPosition(position).toString();

                        total=0;
                        number0=0; number2=0; number4=0; number8=0; number15=0; number16=0;
                        fgboys
//                                .whereEqualTo("zzdate",date)
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

                                        TreeMap<String, JapaClass> count = new TreeMap<>();
                                        String data = "";
                                        Log.d(TAG, "onEvent: Out");

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            Log.d(TAG, "onEvent: Area" + note.getZtl());

                                            String japa = note.getJapa();
                                            if (japa.equals("0")) {
                                                number0++;
                                                total++;
                                            } else if (japa.equals("1") || (japa.equals("2"))) {
                                                number2++;
                                                total++;
                                            } else if (japa.equals("3") || (japa.equals("4"))) {
                                                number4++;
                                                total++;
                                            } else if (japa.equals("5") || (japa.equals("6")) || (japa.equals("7")) || (japa.equals("8"))) {
                                                number8++;
                                                total++;
                                            } else if (japa.equals("9") || (japa.equals("10")) || (japa.equals("11")) ||
                                                    (japa.equals("12")) || (japa.equals("13")) || (japa.equals("14"))
                                                    || (japa.equals("15"))) {
                                                number15++;
                                                total++;
                                            } else {
                                                number16++;
                                                total++;
                                            }
                                        }
                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        per0 = Double.parseDouble(df.format(Math.ceil(number0* 100)/total));
                                        per2 = Double.parseDouble(df.format(Math.ceil(number2* 100)/total));
                                        per4 = Double.parseDouble(df.format(Math.ceil(number4* 100)/total));
                                        per8 = Double.parseDouble(df.format(Math.ceil(number8* 100)/total));
                                        per15 = Double.parseDouble(df.format(Math.ceil(number15* 100)/total));
                                        per16 = Double.parseDouble(df.format(Math.ceil(number16* 100)/total));

                                        count.put("0 rounds", new JapaClass(number0, per0));
                                        count.put("01-02 rounds", new JapaClass(number2, per2));
                                        count.put("03-04 rounds", new JapaClass(number4, per4));
                                        count.put("05-08 rounds", new JapaClass(number8, per8));
                                        count.put("09-15 rounds", new JapaClass(number15, per15));
                                        count.put("16-more rounds", new JapaClass(number16, per16));
                                        count.put("ALL", new JapaClass(total,100));

                                        Log.d("Count", "onEvent: " + number0 + " " + number2 + " " + number4 + " " + number8 + " " + number15 + " " +
                                                " " + number16 + "    " + total);

                                        Log.d("Count", "onEvent: " + per0 + " " + per2 + " " + per4 + " " + per8 + " " + per15 + " " +
                                                " " + per16 + "    " + total);

                                        final JapaActivityAdapter adapter = new JapaActivityAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String japaValue = adapter.getItem(i).getKey();
                                                String japa = "";
                                                switch (japaValue) {
                                                    case "0 rounds":
                                                        japa = "0";
                                                        break;
                                                    case "01-02 rounds":
                                                        japa = "1";
                                                        break;
                                                    case "03-04 rounds":
                                                        japa = "2";
                                                        break;
                                                    case "05-08 rounds":
                                                        japa = "3";
                                                        break;
                                                    case "09-15 rounds":
                                                        japa = "4";
                                                        break;
                                                    case "16-more rounds":
                                                        japa = "6";
                                                        break;
                                                    case "ALL":
                                                        japa = "5";
                                                }
                                                Intent intent = new Intent(JapaActivity.this, Japa.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Japa", japa);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinSessions", spinnerCategories);
                                                bundle.putString("Collection",collection);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                    } else {
                        total=0;
                        number0=0; number2=0; number4=0; number8=0; number15=0; number16=0;
                        fgboys
//                                .whereEqualTo("zzdate",date)
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("session",spinnerSessions)
                                .whereEqualTo("category",spinnerCategories)
                                .whereEqualTo("program",spinnerPrograms)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, JapaClass> count = new TreeMap<>();
                                        String data = "";
                                        Log.d(TAG, "onEvent: Out");

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            Log.d(TAG, "onEvent: Area" + note.getZtl());

                                            String japa = note.getJapa();
                                            if (japa.equals("0")) {
                                                number0++;
                                                total++;
                                            } else if (japa.equals("1") || (japa.equals("2"))) {
                                                number2++;
                                                total++;
                                            } else if (japa.equals("3") || (japa.equals("4"))) {
                                                number4++;
                                                total++;
                                            } else if (japa.equals("5") || (japa.equals("6")) || (japa.equals("7")) || (japa.equals("8"))) {
                                                number8++;
                                                total++;
                                            } else if (japa.equals("9") || (japa.equals("10")) || (japa.equals("11")) ||
                                                    (japa.equals("12")) || (japa.equals("13")) || (japa.equals("14"))
                                                    || (japa.equals("15"))) {
                                                number15++;
                                                total++;
                                            } else {
                                                number16++;
                                                total++;
                                            }
                                        }
                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        per0 = Double.parseDouble(df.format(Math.ceil(number0* 100)/total));
                                        per2 = Double.parseDouble(df.format(Math.ceil(number2* 100)/total));
                                        per4 = Double.parseDouble(df.format(Math.ceil(number4* 100)/total));
                                        per8 = Double.parseDouble(df.format(Math.ceil(number8* 100)/total));
                                        per15 = Double.parseDouble(df.format(Math.ceil(number15* 100)/total));
                                        per16 = Double.parseDouble(df.format(Math.ceil(number16* 100)/total));

                                        count.put("0 rounds", new JapaClass(number0, per0));
                                        count.put("01-02 rounds", new JapaClass(number2, per2));
                                        count.put("03-04 rounds", new JapaClass(number4, per4));
                                        count.put("05-08 rounds", new JapaClass(number8, per8));
                                        count.put("09-15 rounds", new JapaClass(number15, per15));
                                        count.put("16-more rounds", new JapaClass(number16, per16));
                                        count.put("ALL", new JapaClass(total,100));

                                        Log.d("Count", "onEvent: " + number0 + " " + number2 + " " + number4 + " " + number8 + " " + number15 + " " +
                                                " " + number16 + "    " + total);

                                        Log.d("Count", "onEvent: " + per0 + " " + per2 + " " + per4 + " " + per8 + " " + per15 + " " +
                                                " " + per16 + "    " + total);

                                        final JapaActivityAdapter adapter = new JapaActivityAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String japaValue = adapter.getItem(i).getKey();
                                                String japa = "";
                                                switch (japaValue) {
                                                    case "0 rounds":
                                                        japa = "0";
                                                        break;
                                                    case "01-02 rounds":
                                                        japa = "1";
                                                        break;
                                                    case "03-04 rounds":
                                                        japa = "2";
                                                        break;
                                                    case "05-08 rounds":
                                                        japa = "3";
                                                        break;
                                                    case "09-15 rounds":
                                                        japa = "4";
                                                        break;
                                                    case "16-more rounds":
                                                        japa = "6";
                                                        break;
                                                    case "ALL":
                                                        japa = "5";
                                                }
                                                Intent intent = new Intent(JapaActivity.this, Japa.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Japa", japa);
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