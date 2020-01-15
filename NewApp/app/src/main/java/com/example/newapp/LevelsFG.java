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

public class LevelsFG extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference fgboys;
    private String collection = "";
    private static final String TAG = "MainActivity";
    ListView mListView;
    private TextView mTextView;
    private long date1 = 0, date2 = 0;
    private String level = "";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    public static String spinPrograms = "";
    public static String spinCategories = "";
    public static String spinSessions = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels_fg);
        collection = getIntent().getStringExtra("Collection");
        db = FirebaseFirestore.getInstance();
        fgboys = db.collection(collection);
        mTextView = findViewById(R.id.textView2);
        mTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mListView = findViewById(R.id.list_fgs);
        date1 = getIntent().getLongExtra("Date1",date1);
        date2 = getIntent().getLongExtra("Date2",date2);
        level = getIntent().getStringExtra("Level");
        spinPrograms= getIntent().getStringExtra("SpinPrograms");
        spinCategories= getIntent().getStringExtra("SpinCategories");
        spinSessions= getIntent().getStringExtra("SpinSessions");
//        mTextView.setText(spinPrograms + " " + spinCategories + " " + spinSessions);
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
        if (level.equals("ALL")) {
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
//                HashMap<String,Integer> numbers = new HashMap<String,Integer>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (count.containsKey(note.getFg())) {
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(), number);
                                } else {
                                    count.put(note.getFg(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(LevelsFG.this, LevelDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("Level", level);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms",spinPrograms);
                                    bundle.putString("SpinCategories",spinCategories);
                                    bundle.putString("SpinSessions",spinSessions);
                                    bundle.putString("Collection",collection);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        } else {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("zfl", level)
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

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (count.containsKey(note.getFg())) {
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(), number);
                                } else {
                                    count.put(note.getFg(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(LevelsFG.this, LevelDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("Level", level);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms",spinPrograms);
                                    bundle.putString("SpinCategories",spinCategories);
                                    bundle.putString("SpinSessions",spinSessions);
                                    bundle.putString("Collection",collection);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
    }

    public void populateListCategoriesAndSessions() {
        if (level.equals("ALL")) {
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

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (count.containsKey(note.getFg())) {
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(), number);
                                } else {
                                    count.put(note.getFg(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(LevelsFG.this, LevelDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("Level", level);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms",spinPrograms);
                                    bundle.putString("SpinCategories",spinCategories);
                                    bundle.putString("SpinSessions",spinSessions);
                                    bundle.putString("Collection",collection);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        } else {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("zfl", level)
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

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (count.containsKey(note.getFg())) {
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(), number);
                                } else {
                                    count.put(note.getFg(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(LevelsFG.this, LevelDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("Level", level);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms",spinPrograms);
                                    bundle.putString("SpinCategories",spinCategories);
                                    bundle.putString("SpinSessions",spinSessions);
                                    bundle.putString("Collection",collection);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
    }

    public void populateListProgramsAndSessions() {
        if (level.equals("ALL")) {
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
//                HashMap<String,Integer> numbers = new HashMap<String,Integer>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (count.containsKey(note.getFg())) {
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(), number);
                                } else {
                                    count.put(note.getFg(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(LevelsFG.this, LevelDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("Level", level);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms",spinPrograms);
                                    bundle.putString("SpinCategories",spinCategories);
                                    bundle.putString("SpinSessions",spinSessions);
                                    bundle.putString("Collection",collection);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        } else {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("zfl", level)
                    .whereEqualTo("category",spinCategories)
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

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (count.containsKey(note.getFg())) {
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(), number);
                                } else {
                                    count.put(note.getFg(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(LevelsFG.this, LevelDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("Level", level);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms",spinPrograms);
                                    bundle.putString("SpinCategories",spinCategories);
                                    bundle.putString("SpinSessions",spinSessions);
                                    bundle.putString("Collection",collection);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
    }

    public void populateListProgramsAndCategories() {
        if (level.equals("ALL")) {
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

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (count.containsKey(note.getFg())) {
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(), number);
                                } else {
                                    count.put(note.getFg(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(LevelsFG.this, LevelDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("Level", level);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms",spinPrograms);
                                    bundle.putString("SpinCategories",spinCategories);
                                    bundle.putString("SpinSessions",spinSessions);
                                    bundle.putString("Collection",collection);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        } else {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("zfl", level)
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

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (count.containsKey(note.getFg())) {
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(), number);
                                } else {
                                    count.put(note.getFg(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(LevelsFG.this, LevelDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("Level", level);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms",spinPrograms);
                                    bundle.putString("SpinCategories",spinCategories);
                                    bundle.putString("SpinSessions",spinSessions);
                                    bundle.putString("Collection",collection);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
    }

    public void populateListPrograms() {
        if (level.equals("ALL")) {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
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

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (count.containsKey(note.getFg())) {
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(), number);
                                } else {
                                    count.put(note.getFg(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(LevelsFG.this, LevelDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("Level", level);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms",spinPrograms);
                                    bundle.putString("SpinCategories",spinCategories);
                                    bundle.putString("SpinSessions",spinSessions);
                                    bundle.putString("Collection",collection);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        } else {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("zfl", level)
                    .whereEqualTo("category",spinCategories)
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

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (count.containsKey(note.getFg())) {
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(), number);
                                } else {
                                    count.put(note.getFg(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(LevelsFG.this, LevelDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("Level", level);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms",spinPrograms);
                                    bundle.putString("SpinCategories",spinCategories);
                                    bundle.putString("SpinSessions",spinSessions);
                                    bundle.putString("Collection",collection);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
    }

    public void populateListCategories() {
        if (level.equals("ALL")) {
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
//                HashMap<String,Integer> numbers = new HashMap<String,Integer>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (count.containsKey(note.getFg())) {
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(), number);
                                } else {
                                    count.put(note.getFg(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(LevelsFG.this, LevelDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("Level", level);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms",spinPrograms);
                                    bundle.putString("SpinCategories",spinCategories);
                                    bundle.putString("SpinSessions",spinSessions);
                                    bundle.putString("Collection",collection);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        } else {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("zfl", level)
                    .whereEqualTo("program",spinPrograms)
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

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (count.containsKey(note.getFg())) {
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(), number);
                                } else {
                                    count.put(note.getFg(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(LevelsFG.this, LevelDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("Level", level);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms",spinPrograms);
                                    bundle.putString("SpinCategories",spinCategories);
                                    bundle.putString("SpinSessions",spinSessions);
                                    bundle.putString("Collection",collection);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
    }

    public void populateListSessions() {
        if (level.equals("ALL")) {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("program",spinPrograms)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            TreeMap<String, Integer> count = new TreeMap<>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (count.containsKey(note.getFg())) {
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(), number);
                                } else {
                                    count.put(note.getFg(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(LevelsFG.this, LevelDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("Level", level);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms",spinPrograms);
                                    bundle.putString("SpinCategories",spinCategories);
                                    bundle.putString("SpinSessions",spinSessions);
                                    bundle.putString("Collection",collection);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        } else {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("zfl", level)
                    .whereEqualTo("category",spinCategories)
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

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (count.containsKey(note.getFg())) {
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(), number);
                                } else {
                                    count.put(note.getFg(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(LevelsFG.this, LevelDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("Level", level);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms",spinPrograms);
                                    bundle.putString("SpinCategories",spinCategories);
                                    bundle.putString("SpinSessions",spinSessions);
                                    bundle.putString("Collection",collection);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
    }

    public void populateList() {
        if (level.equals("ALL")) {
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
//                HashMap<String,Integer> numbers = new HashMap<String,Integer>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (count.containsKey(note.getFg())) {
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(), number);
                                } else {
                                    count.put(note.getFg(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(LevelsFG.this, LevelDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("Level", level);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms",spinPrograms);
                                    bundle.putString("SpinCategories",spinCategories);
                                    bundle.putString("SpinSessions",spinSessions);
                                    bundle.putString("Collection",collection);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        } else {
            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("zfl", level)
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
//                HashMap<String,Integer> numbers = new HashMap<String,Integer>();
                            String data = "";
                            Log.d(TAG, "onEvent: Out");

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (count.containsKey(note.getFg())) {
                                    int number = count.get(note.getFg());
                                    number++;
                                    count.put(note.getFg(), number);
                                } else {
                                    count.put(note.getFg(), 1);
                                }
                            }

                            final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(LevelsFG.this, LevelDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1",date1);
                                    bundle.putLong("Date2",date2);
                                    bundle.putString("Level", level);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms",spinPrograms);
                                    bundle.putString("SpinCategories",spinCategories);
                                    bundle.putString("SpinSessions",spinSessions);
                                    bundle.putString("Collection",collection);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
    }

    public void select1(View v) {
        Intent intent = new Intent(LevelsFG.this, DateSelector.class);
        startActivity(intent);
    }
}