package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.Nullable;

public class SourceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseFirestore db;
    private CollectionReference fgboys;
    private String collection = "";
    private static final String TAG = "MainActivity";
    ListView mListView;
    private TextView mTextView;
    private long date1 = 0, date2 = 0;
    private int total1 = 0, total2 = 0;
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
    int posProgram, posCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);
        collection = getIntent().getStringExtra("Collection");
        db = FirebaseFirestore.getInstance();
        fgboys = db.collection(collection);
        mTextView = findViewById(R.id.textView2);
        mTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mListView = findViewById(R.id.list_item);
        date1 = getIntent().getLongExtra("Date1",date1);
        date2 = getIntent().getLongExtra("Date2",date2);
        program = findViewById(R.id.program);
        category = findViewById(R.id.category);
        session = findViewById(R.id.session);
    }

    @Override
    protected void onStart() {
        super.onStart();

        programsList = Arrays.asList(programs);
        finalPrograms.addAll(programsList);

        adapterPrograms = new ArrayAdapter<String>(
                this,R.layout.spinner_item,finalPrograms);
        adapterPrograms.setDropDownViewResource(R.layout.spinner_item);
        program.setAdapter(adapterPrograms);

        fgboys
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
                            if ((finalPrograms.contains(note.getProgram())) || (note.getProgram() == null)) {
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

    public void populateCategories(String spinProgram) {
        final String spinPrograms = spinnerPrograms;

        if (spinProgram.equals("1")) {
            finalCategories = new ArrayList<String>();
            categoriesList = Arrays.asList(categories);
            finalCategories.addAll(categoriesList);

            adapterCategories = new ArrayAdapter<String>
                    (this,R.layout.spinner_item,finalCategories);

            fgboys
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

                                if ((finalCategories.contains(note.getCategory())) || (note.getCategory() == null)) {
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

                                if ((finalCategories.contains(note.getCategory())) || (note.getCategory() == null)) {
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
                    (this,R.layout.spinner_item,finalSessions);

            fgboys
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
            adapterSessions.setDropDownViewResource(R.layout.spinner_item);
            session.setAdapter(adapterSessions);

            session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerSessions = parent.getItemAtPosition(position).toString();
                    total2 = 0;

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

                                        TreeMap<String, Integer> count = new TreeMap<>();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            if (note.getSource() == null) {
                                                note.setSource("NONE");
                                            }

                                            total2++;
                                            count.put("ALL", total2);

                                            if (count.containsKey(note.getSource())) {
                                                int number = count.get(note.getSource());
                                                number++;
                                                count.put(note.getSource(), number);
                                            } else {
                                                count.put(note.getSource(), 1);
                                            }
                                        }

                                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                //                final String fg_name = mListView.getItemAtPosition(i).toString();
                                                //                TextView fgName = (TextView) findViewById(R.id.text_view_1);
                                                String level = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(SourceActivity.this, SourceDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Level", level);
                                                Log.d("TLActivity", "onItemClick: Main: " + spinPrograms);
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
                        total2 = 0;
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

                                        TreeMap<String, Integer> count = new TreeMap<>();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            if (note.getSource() == null) {
                                                note.setSource("NONE");
                                            }

                                            total2++;
                                            count.put("ALL", total2);

                                            if (count.containsKey(note.getSource())) {
                                                int number = count.get(note.getSource());
                                                number++;
                                                count.put(note.getSource(), number);
                                            } else {
                                                count.put(note.getSource(), 1);
                                            }
                                        }

                                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                //                final String fg_name = mListView.getItemAtPosition(i).toString();
                                                //                TextView fgName = (TextView) findViewById(R.id.text_view_1);
                                                String level = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(SourceActivity.this, SourceDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Level", level);
                                                Log.d("TLActivity", "onItemClick: Main: " + spinPrograms);
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
                    (this,R.layout.spinner_item,finalSessions);
            total2 = 0;

            fgboys
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
                    total2 = 0;

                    if (spinnerSessions.equals("ALL")) {
                        fgboys
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("category",spinnerCategories)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, Integer> count = new TreeMap<>();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            if (note.getSource() == null) {
                                                note.setSource("NONE");
                                            }

                                            total2++;
                                            count.put("ALL", total2);

                                            if (count.containsKey(note.getSource())) {
                                                int number = count.get(note.getSource());
                                                number++;
                                                count.put(note.getSource(), number);
                                            } else {
                                                count.put(note.getSource(), 1);
                                            }
                                        }

                                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                //                final String fg_name = mListView.getItemAtPosition(i).toString();
                                                //                TextView fgName = (TextView) findViewById(R.id.text_view_1);
                                                String level = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(SourceActivity.this, LevelsFG.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Level", level);
                                                Log.d("TLActivity", "onItemClick: Main: " + spinPrograms);
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
                        fgboys
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

                                        TreeMap<String, Integer> count = new TreeMap<>();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            if (note.getSource() == null) {
                                                note.setSource("NONE");
                                            }

                                            total2++;
                                            count.put("ALL", total2);

                                            if (count.containsKey(note.getSource())) {
                                                int number = count.get(note.getSource());
                                                number++;
                                                count.put(note.getSource(), number);
                                            } else {
                                                count.put(note.getSource(), 1);
                                            }
                                        }

                                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                //                final String fg_name = mListView.getItemAtPosition(i).toString();
                                                //                TextView fgName = (TextView) findViewById(R.id.text_view_1);
                                                String level = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(SourceActivity.this, SourceDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Level", level);
                                                Log.d("TLActivity", "onItemClick: Main: " + spinPrograms);
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
                    (this,R.layout.spinner_item,finalSessions);
            total2 = 0;

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
            adapterSessions.setDropDownViewResource(R.layout.spinner_item);
            session.setAdapter(adapterSessions);

            session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerSessions = parent.getItemAtPosition(position).toString();
                    total2 = 0;

                    if (spinnerSessions.equals("ALL")) {
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

                                        TreeMap<String, Integer> count = new TreeMap<>();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            if (note.getSource() == null) {
                                                note.setSource("NONE");
                                            }

                                            total2++;
                                            count.put("ALL", total2);

                                            if (count.containsKey(note.getSource())) {
                                                int number = count.get(note.getSource());
                                                number++;
                                                count.put(note.getSource(), number);
                                            } else {
                                                count.put(note.getSource(), 1);
                                            }
                                        }

                                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                //                final String fg_name = mListView.getItemAtPosition(i).toString();
                                                //                TextView fgName = (TextView) findViewById(R.id.text_view_1);
                                                String level = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(SourceActivity.this, SourceDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Level", level);
                                                Log.d("TLActivity", "onItemClick: Main: " + spinPrograms);
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
                        fgboys
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

                                        TreeMap<String, Integer> count = new TreeMap<>();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            if (note.getSource() == null) {
                                                note.setSource("NONE");
                                            }

                                            total2++;
                                            count.put("ALL", total2);

                                            if (count.containsKey(note.getSource())) {
                                                int number = count.get(note.getSource());
                                                number++;
                                                count.put(note.getSource(), number);
                                            } else {
                                                count.put(note.getSource(), 1);
                                            }
                                        }

                                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                //                final String fg_name = mListView.getItemAtPosition(i).toString();
                                                //                TextView fgName = (TextView) findViewById(R.id.text_view_1);
                                                String level = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(SourceActivity.this, SourceDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Level", level);
                                                Log.d("TLActivity", "onItemClick: Main: " + spinPrograms);
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
                    (this,R.layout.spinner_item,finalSessions);
            total2 = 0;

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
            adapterSessions.setDropDownViewResource(R.layout.spinner_item);
            session.setAdapter(adapterSessions);

            session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerSessions = parent.getItemAtPosition(position).toString();
                    total2 = 0;

                    if (spinnerSessions.equals("ALL")) {
                        fgboys
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("program",spinnerPrograms)
                                .whereEqualTo("category",spinnerCategories)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, Integer> count = new TreeMap<>();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            if (note.getSource() == null) {
                                                note.setSource("NONE");
                                            }

                                            total2++;
                                            count.put("ALL", total2);

                                            if (count.containsKey(note.getSource())) {
                                                int number = count.get(note.getSource());
                                                number++;
                                                count.put(note.getSource(), number);
                                            } else {
                                                count.put(note.getSource(), 1);
                                            }
                                        }

                                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                //                final String fg_name = mListView.getItemAtPosition(i).toString();
                                                //                TextView fgName = (TextView) findViewById(R.id.text_view_1);
                                                String level = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(SourceActivity.this, SourceDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Level", level);
                                                Log.d("TLActivity", "onItemClick: Main: " + spinPrograms);
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
                        fgboys
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

                                        TreeMap<String, Integer> count = new TreeMap<>();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);
                                            if (note.getSource() == null) {
                                                note.setSource("NONE");
                                            }

                                            total2++;
                                            count.put("ALL", total2);

                                            if (count.containsKey(note.getSource())) {
                                                int number = count.get(note.getSource());
                                                number++;
                                                count.put(note.getSource(), number);
                                            } else {
                                                count.put(note.getSource(), 1);
                                            }
                                        }

                                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                //                final String fg_name = mListView.getItemAtPosition(i).toString();
                                                //                TextView fgName = (TextView) findViewById(R.id.text_view_1);
                                                String level = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(SourceActivity.this, SourceDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Level", level);
                                                Log.d("TLActivity", "onItemClick: Main: " + spinPrograms);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void select1(View v) {
        Intent intent = new Intent(SourceActivity.this, DateSelector.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        programs = new String[]{};
    }
}
