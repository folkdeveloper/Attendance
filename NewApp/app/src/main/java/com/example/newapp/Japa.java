package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.TreeMap;

import javax.annotation.Nullable;

public class Japa extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference fgboys;
    private String collection = "";
    private static final String TAG = "JapaActivity";
    ListView mListView;
    private TextView mTextView;
    private long date1 = 0, date2 = 0;
    private String japa = "";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    public static String spinPrograms = "";
    public static String spinCategories = "";
    public static String spinSessions = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_japa2);
        collection = getIntent().getStringExtra("Collection");
        db = FirebaseFirestore.getInstance();
        fgboys = db.collection(collection);
        mTextView = findViewById(R.id.textView2);
        mTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mListView = findViewById(R.id.list_item);
        date1 = getIntent().getLongExtra("Date1",date1);
        date2 = getIntent().getLongExtra("Date2",date2);
        japa = getIntent().getStringExtra("Japa");
        spinPrograms= getIntent().getStringExtra("SpinPrograms");
        spinCategories= getIntent().getStringExtra("SpinCategories");
        spinSessions= getIntent().getStringExtra("SpinSessions");
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
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            int numberAll=0;
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                Log.d(TAG, "onEvent: Japa" + note.getZtl());

                                numberAll++;
                                count.put("ALL",numberAll);

                                if (count.containsKey(note.getZtl())) {
                                    int number = count.get(note.getZtl());
                                    number++;
                                    count.put(note.getZtl(), number);
                                } else {
                                    count.put(note.getZtl(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();

                                    if (tl.equals("ALL")) {
                                        Intent intent = new Intent(Japa.this, JapaTLDetails.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(Japa.this, JapaFG.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
        } else {
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

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            int numberAllZero = 0, numberAllTwo=0, numberAllFour=0, numberAllEight=0
                                    , numberAllFifteen=0, numberAllSixteen=0;

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                Log.d(TAG, "onEvent: Japa" + note.getZtl());

                                switch (japa) {
                                    case "0":
                                        if (note.getJapa().equals("0")) {
                                            numberAllZero++;
                                            count.put("ALL",numberAllZero);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "1":
                                        if ((note.getJapa().equals("1")) || (note.getJapa().equals("2"))) {
                                            numberAllTwo++;
                                            count.put("ALL",numberAllTwo);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "2":
                                        if ((note.getJapa().equals("3")) || (note.getJapa().equals("4"))) {
                                            numberAllFour++;
                                            count.put("ALL",numberAllFour);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "3":
                                        if ((note.getJapa().equals("5")) || (note.getJapa().equals("6"))
                                                || (note.getJapa().equals("7")) || (note.getJapa().equals("8"))) {
                                            numberAllEight++;
                                            count.put("ALL",numberAllEight);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "4":
                                        if ((note.getJapa().equals("9")) || (note.getJapa().equals("10")) ||
                                                (note.getJapa().equals("11")) || (note.getJapa().equals("12")) ||
                                                (note.getJapa().equals("13")) || (note.getJapa().equals("14")) ||
                                                (note.getJapa().equals("15"))) {
                                            numberAllFifteen++;
                                            count.put("ALL",numberAllFifteen);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "6":
                                        if ((note.getJapa().equals("16"))) {
                                            numberAllSixteen++;
                                            count.put("ALL",numberAllSixteen);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();

                                    if (tl.equals("ALL")) {
                                        Intent intent = new Intent(Japa.this, JapaTLDetails.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(Japa.this, JapaFG.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
        }
    }

    public void populateListProgramsAndCategories() {
        if (japa.equals("5")) {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("session",spinSessions)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
//                HashMap<String,Integer> numbers = new HashMap<String,Integer>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            int numberAll=0;
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                Log.d(TAG, "onEvent: Japa" + note.getZtl());

                                numberAll++;
                                count.put("ALL",numberAll);

                                if (count.containsKey(note.getZtl())) {
                                    int number = count.get(note.getZtl());
                                    number++;
                                    count.put(note.getZtl(), number);
                                } else {
                                    count.put(note.getZtl(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();

                                    if (tl.equals("ALL")) {
                                        Intent intent = new Intent(Japa.this, JapaTLDetails.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(Japa.this, JapaFG.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
        } else {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("session",spinSessions)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
//                HashMap<String,Integer> numbers = new HashMap<String,Integer>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            int numberAllZero = 0, numberAllTwo=0, numberAllFour=0, numberAllEight=0
                                    , numberAllFifteen=0, numberAllSixteen=0;

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                Log.d(TAG, "onEvent: Japa" + note.getZtl());

                                switch (japa) {
                                    case "0":
                                        if (note.getJapa().equals("0")) {
                                            numberAllZero++;
                                            count.put("ALL",numberAllZero);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "1":
                                        if ((note.getJapa().equals("1")) || (note.getJapa().equals("2"))) {
                                            numberAllTwo++;
                                            count.put("ALL",numberAllTwo);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "2":
                                        if ((note.getJapa().equals("3")) || (note.getJapa().equals("4"))) {
                                            numberAllFour++;
                                            count.put("ALL",numberAllFour);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "3":
                                        if ((note.getJapa().equals("5")) || (note.getJapa().equals("6"))
                                                || (note.getJapa().equals("7")) || (note.getJapa().equals("8"))) {
                                            numberAllEight++;
                                            count.put("ALL",numberAllEight);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "4":
                                        if ((note.getJapa().equals("9")) || (note.getJapa().equals("10")) ||
                                                (note.getJapa().equals("11")) || (note.getJapa().equals("12")) ||
                                                (note.getJapa().equals("13")) || (note.getJapa().equals("14")) ||
                                                (note.getJapa().equals("15"))) {
                                            numberAllFifteen++;
                                            count.put("ALL",numberAllFifteen);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "6":
                                        if ((note.getJapa().equals("16"))) {
                                            numberAllSixteen++;
                                            count.put("ALL",numberAllSixteen);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();

                                    if (tl.equals("ALL")) {
                                        Intent intent = new Intent(Japa.this, JapaTLDetails.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(Japa.this, JapaFG.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
        }
    }

    public void populateListCategoriesAndSessions() {
        if (japa.equals("5")) {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("program",spinPrograms)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
//                HashMap<String,Integer> numbers = new HashMap<String,Integer>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            int numberAll=0;
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                Log.d(TAG, "onEvent: Japa" + note.getZtl());

                                numberAll++;
                                count.put("ALL",numberAll);

                                if (count.containsKey(note.getZtl())) {
                                    int number = count.get(note.getZtl());
                                    number++;
                                    count.put(note.getZtl(), number);
                                } else {
                                    count.put(note.getZtl(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();

                                    if (tl.equals("ALL")) {
                                        Intent intent = new Intent(Japa.this, JapaTLDetails.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(Japa.this, JapaFG.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
        } else {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("program",spinPrograms)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
//                HashMap<String,Integer> numbers = new HashMap<String,Integer>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            int numberAllZero = 0, numberAllTwo=0, numberAllFour=0, numberAllEight=0
                                    , numberAllFifteen=0, numberAllSixteen=0;

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                Log.d(TAG, "onEvent: Japa" + note.getZtl());

                                switch (japa) {
                                    case "0":
                                        if (note.getJapa().equals("0")) {
                                            numberAllZero++;
                                            count.put("ALL",numberAllZero);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "1":
                                        if ((note.getJapa().equals("1")) || (note.getJapa().equals("2"))) {
                                            numberAllTwo++;
                                            count.put("ALL",numberAllTwo);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "2":
                                        if ((note.getJapa().equals("3")) || (note.getJapa().equals("4"))) {
                                            numberAllFour++;
                                            count.put("ALL",numberAllFour);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "3":
                                        if ((note.getJapa().equals("5")) || (note.getJapa().equals("6"))
                                                || (note.getJapa().equals("7")) || (note.getJapa().equals("8"))) {
                                            numberAllEight++;
                                            count.put("ALL",numberAllEight);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "4":
                                        if ((note.getJapa().equals("9")) || (note.getJapa().equals("10")) ||
                                                (note.getJapa().equals("11")) || (note.getJapa().equals("12")) ||
                                                (note.getJapa().equals("13")) || (note.getJapa().equals("14")) ||
                                                (note.getJapa().equals("15"))) {
                                            numberAllFifteen++;
                                            count.put("ALL",numberAllFifteen);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "6":
                                        if ((note.getJapa().equals("16"))) {
                                            numberAllSixteen++;
                                            count.put("ALL",numberAllSixteen);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();

                                    if (tl.equals("ALL")) {
                                        Intent intent = new Intent(Japa.this, JapaTLDetails.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(Japa.this, JapaFG.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
        }
    }

    public void populateListProgramsAndSessions() {
        if (japa.equals("5")) {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("category",spinCategories)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            int numberAll=0;
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                Log.d(TAG, "onEvent: Japa" + note.getZtl());

                                numberAll++;
                                count.put("ALL",numberAll);

                                if (count.containsKey(note.getZtl())) {
                                    int number = count.get(note.getZtl());
                                    number++;
                                    count.put(note.getZtl(), number);
                                } else {
                                    count.put(note.getZtl(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();

                                    if (tl.equals("ALL")) {
                                        Intent intent = new Intent(Japa.this, JapaTLDetails.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(Japa.this, JapaFG.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
        } else {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("category",spinCategories)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            int numberAllZero = 0, numberAllTwo=0, numberAllFour=0, numberAllEight=0
                                    , numberAllFifteen=0, numberAllSixteen=0;

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                Log.d(TAG, "onEvent: Japa" + note.getZtl());

                                switch (japa) {
                                    case "0":
                                        if (note.getJapa().equals("0")) {
                                            numberAllZero++;
                                            count.put("ALL",numberAllZero);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "1":
                                        if ((note.getJapa().equals("1")) || (note.getJapa().equals("2"))) {
                                            numberAllTwo++;
                                            count.put("ALL",numberAllTwo);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "2":
                                        if ((note.getJapa().equals("3")) || (note.getJapa().equals("4"))) {
                                            numberAllFour++;
                                            count.put("ALL",numberAllFour);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "3":
                                        if ((note.getJapa().equals("5")) || (note.getJapa().equals("6"))
                                                || (note.getJapa().equals("7")) || (note.getJapa().equals("8"))) {
                                            numberAllEight++;
                                            count.put("ALL",numberAllEight);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "4":
                                        if ((note.getJapa().equals("9")) || (note.getJapa().equals("10")) ||
                                                (note.getJapa().equals("11")) || (note.getJapa().equals("12")) ||
                                                (note.getJapa().equals("13")) || (note.getJapa().equals("14")) ||
                                                (note.getJapa().equals("15"))) {
                                            numberAllFifteen++;
                                            count.put("ALL",numberAllFifteen);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "6":
                                        if ((note.getJapa().equals("16"))) {
                                            numberAllSixteen++;
                                            count.put("ALL",numberAllSixteen);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();

                                    if (tl.equals("ALL")) {
                                        Intent intent = new Intent(Japa.this, JapaTLDetails.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(Japa.this, JapaFG.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
        }
    }

    public void populateListSessions() {
        if (japa.equals("5")) {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("category",spinCategories)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            int numberAll=0;
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                Log.d(TAG, "onEvent: Japa" + note.getZtl());

                                numberAll++;
                                count.put("ALL",numberAll);

                                if (count.containsKey(note.getZtl())) {
                                    int number = count.get(note.getZtl());
                                    number++;
                                    count.put(note.getZtl(), number);
                                } else {
                                    count.put(note.getZtl(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();

                                    if (tl.equals("ALL")) {
                                        Intent intent = new Intent(Japa.this, JapaTLDetails.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(Japa.this, JapaFG.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
        } else {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("category",spinCategories)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            int numberAllZero = 0, numberAllTwo=0, numberAllFour=0, numberAllEight=0
                                    , numberAllFifteen=0, numberAllSixteen=0;

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                Log.d(TAG, "onEvent: Japa" + note.getZtl());

                                switch (japa) {
                                    case "0":
                                        if (note.getJapa().equals("0")) {
                                            numberAllZero++;
                                            count.put("ALL",numberAllZero);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "1":
                                        if ((note.getJapa().equals("1")) || (note.getJapa().equals("2"))) {
                                            numberAllTwo++;
                                            count.put("ALL",numberAllTwo);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "2":
                                        if ((note.getJapa().equals("3")) || (note.getJapa().equals("4"))) {
                                            numberAllFour++;
                                            count.put("ALL",numberAllFour);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "3":
                                        if ((note.getJapa().equals("5")) || (note.getJapa().equals("6"))
                                                || (note.getJapa().equals("7")) || (note.getJapa().equals("8"))) {
                                            numberAllEight++;
                                            count.put("ALL",numberAllEight);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "4":
                                        if ((note.getJapa().equals("9")) || (note.getJapa().equals("10")) ||
                                                (note.getJapa().equals("11")) || (note.getJapa().equals("12")) ||
                                                (note.getJapa().equals("13")) || (note.getJapa().equals("14")) ||
                                                (note.getJapa().equals("15"))) {
                                            numberAllFifteen++;
                                            count.put("ALL",numberAllFifteen);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "6":
                                        if ((note.getJapa().equals("16"))) {
                                            numberAllSixteen++;
                                            count.put("ALL",numberAllSixteen);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();

                                    if (tl.equals("ALL")) {
                                        Intent intent = new Intent(Japa.this, JapaTLDetails.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(Japa.this, JapaFG.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
        }
    }

    public void populateListPrograms() {
        if (japa.equals("5")) {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("session",spinSessions)
                    .whereEqualTo("category",spinCategories)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            int numberAll=0;
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                Log.d(TAG, "onEvent: Japa" + note.getZtl());

                                numberAll++;
                                count.put("ALL",numberAll);

                                if (count.containsKey(note.getZtl())) {
                                    int number = count.get(note.getZtl());
                                    number++;
                                    count.put(note.getZtl(), number);
                                } else {
                                    count.put(note.getZtl(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();

                                    if (tl.equals("ALL")) {
                                        Intent intent = new Intent(Japa.this, JapaTLDetails.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(Japa.this, JapaFG.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
        } else {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("session",spinSessions)
                    .whereEqualTo("category",spinCategories)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            int numberAllZero = 0, numberAllTwo=0, numberAllFour=0, numberAllEight=0
                                    , numberAllFifteen=0, numberAllSixteen=0;

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                Log.d(TAG, "onEvent: Japa" + note.getZtl());

                                switch (japa) {
                                    case "0":
                                        if (note.getJapa().equals("0")) {
                                            numberAllZero++;
                                            count.put("ALL",numberAllZero);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "1":
                                        if ((note.getJapa().equals("1")) || (note.getJapa().equals("2"))) {
                                            numberAllTwo++;
                                            count.put("ALL",numberAllTwo);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "2":
                                        if ((note.getJapa().equals("3")) || (note.getJapa().equals("4"))) {
                                            numberAllFour++;
                                            count.put("ALL",numberAllFour);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "3":
                                        if ((note.getJapa().equals("5")) || (note.getJapa().equals("6"))
                                                || (note.getJapa().equals("7")) || (note.getJapa().equals("8"))) {
                                            numberAllEight++;
                                            count.put("ALL",numberAllEight);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "4":
                                        if ((note.getJapa().equals("9")) || (note.getJapa().equals("10")) ||
                                                (note.getJapa().equals("11")) || (note.getJapa().equals("12")) ||
                                                (note.getJapa().equals("13")) || (note.getJapa().equals("14")) ||
                                                (note.getJapa().equals("15"))) {
                                            numberAllFifteen++;
                                            count.put("ALL",numberAllFifteen);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "6":
                                        if ((note.getJapa().equals("16"))) {
                                            numberAllSixteen++;
                                            count.put("ALL",numberAllSixteen);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();

                                    if (tl.equals("ALL")) {
                                        Intent intent = new Intent(Japa.this, JapaTLDetails.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(Japa.this, JapaFG.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
        }
    }

    public void populateListCategories() {
        if (japa.equals("5")) {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("session",spinSessions)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            int numberAll=0;
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                Log.d(TAG, "onEvent: Japa" + note.getZtl());

                                numberAll++;
                                count.put("ALL",numberAll);

                                if (count.containsKey(note.getZtl())) {
                                    int number = count.get(note.getZtl());
                                    number++;
                                    count.put(note.getZtl(), number);
                                } else {
                                    count.put(note.getZtl(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();

                                    if (tl.equals("ALL")) {
                                        Intent intent = new Intent(Japa.this, JapaTLDetails.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(Japa.this, JapaFG.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
        } else {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("session",spinSessions)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            int numberAllZero = 0, numberAllTwo=0, numberAllFour=0, numberAllEight=0
                                    , numberAllFifteen=0, numberAllSixteen=0;

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                Log.d(TAG, "onEvent: Japa" + note.getZtl());

                                switch (japa) {
                                    case "0":
                                        if (note.getJapa().equals("0")) {
                                            numberAllZero++;
                                            count.put("ALL",numberAllZero);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "1":
                                        if ((note.getJapa().equals("1")) || (note.getJapa().equals("2"))) {
                                            numberAllTwo++;
                                            count.put("ALL",numberAllTwo);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "2":
                                        if ((note.getJapa().equals("3")) || (note.getJapa().equals("4"))) {
                                            numberAllFour++;
                                            count.put("ALL",numberAllFour);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "3":
                                        if ((note.getJapa().equals("5")) || (note.getJapa().equals("6"))
                                                || (note.getJapa().equals("7")) || (note.getJapa().equals("8"))) {
                                            numberAllEight++;
                                            count.put("ALL",numberAllEight);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "4":
                                        if ((note.getJapa().equals("9")) || (note.getJapa().equals("10")) ||
                                                (note.getJapa().equals("11")) || (note.getJapa().equals("12")) ||
                                                (note.getJapa().equals("13")) || (note.getJapa().equals("14")) ||
                                                (note.getJapa().equals("15"))) {
                                            numberAllFifteen++;
                                            count.put("ALL",numberAllFifteen);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "6":
                                        if ((note.getJapa().equals("16"))) {
                                            numberAllSixteen++;
                                            count.put("ALL",numberAllSixteen);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();

                                    if (tl.equals("ALL")) {
                                        Intent intent = new Intent(Japa.this, JapaTLDetails.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(Japa.this, JapaFG.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
        }
    }

    public void populateList() {
        if (japa.equals("5")) {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("session",spinSessions)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            int numberAll=0;
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                Log.d(TAG, "onEvent: Japa" + note.getZtl());

                                numberAll++;
                                count.put("ALL",numberAll);

                                if (count.containsKey(note.getZtl())) {
                                    int number = count.get(note.getZtl());
                                    number++;
                                    count.put(note.getZtl(), number);
                                } else {
                                    count.put(note.getZtl(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();

                                    if (tl.equals("ALL")) {
                                        Intent intent = new Intent(Japa.this, JapaTLDetails.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(Japa.this, JapaFG.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
        } else {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("session",spinSessions)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            int numberAllZero = 0, numberAllTwo=0, numberAllFour=0, numberAllEight=0
                                    , numberAllFifteen=0, numberAllSixteen=0;

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                Log.d(TAG, "onEvent: Japa" + note.getZtl());

                                switch (japa) {
                                    case "0":
                                        if (note.getJapa().equals("0")) {
                                            numberAllZero++;
                                            count.put("ALL",numberAllZero);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "1":
                                        if ((note.getJapa().equals("1")) || (note.getJapa().equals("2"))) {
                                            numberAllTwo++;
                                            count.put("ALL",numberAllTwo);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "2":
                                        if ((note.getJapa().equals("3")) || (note.getJapa().equals("4"))) {
                                            numberAllFour++;
                                            count.put("ALL",numberAllFour);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "3":
                                        if ((note.getJapa().equals("5")) || (note.getJapa().equals("6"))
                                                || (note.getJapa().equals("7")) || (note.getJapa().equals("8"))) {
                                            numberAllEight++;
                                            count.put("ALL",numberAllEight);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "4":
                                        if ((note.getJapa().equals("9")) || (note.getJapa().equals("10")) ||
                                                (note.getJapa().equals("11")) || (note.getJapa().equals("12")) ||
                                                (note.getJapa().equals("13")) || (note.getJapa().equals("14")) ||
                                                (note.getJapa().equals("15"))) {
                                            numberAllFifteen++;
                                            count.put("ALL",numberAllFifteen);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                    case "6":
                                        if ((note.getJapa().equals("16"))) {
                                            numberAllSixteen++;
                                            count.put("ALL",numberAllSixteen);
                                            if (count.containsKey(note.getZtl())) {
                                                int number = count.get(note.getZtl());
                                                number++;
                                                count.put(note.getZtl(), number);
                                            } else {
                                                count.put(note.getZtl(), 1);
                                            }
                                        }
                                        break;
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String tl = adapter.getItem(i).getKey();

                                    if (tl.equals("ALL")) {
                                        Intent intent = new Intent(Japa.this, JapaTLDetails.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(Japa.this, JapaFG.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1",date1);
                                        bundle.putLong("Date2",date2);
                                        bundle.putString("TL", tl);
                                        bundle.putString("Japa", japa);
                                        bundle.putString("SpinPrograms", spinPrograms);
                                        bundle.putString("SpinCategories", spinCategories);
                                        bundle.putString("SpinSessions", spinSessions);
                                        bundle.putString("Collection",collection);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
        }
    }
}