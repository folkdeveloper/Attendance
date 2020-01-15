package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseFirestore db;
    private CollectionReference fgboys;
    private String collection = "";
    private TextView mTextView;
    private static final String TAG = "MainActivity";
    private static final String FOLK_GUIDE = "fg";
    ListView mListView;
    private long date1 = 0, date2 = 0;
    private int total1 = 0, total2 = 0;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String spinnerPrograms = "";
    private String spinnerCategories = "";
    List<String> programsList;
    List<String> categoriesList;
    final ArrayList<String> finalPrograms = new ArrayList<String>();
    ArrayList<String> finalCategories = new ArrayList<String>();
    Button btn;
    ArrayAdapter<String> adapterPrograms;
    ArrayAdapter<String> adapterCategories;
    String spinCategory = "";
    final String[] categories = new String[]{"ALL"};
    String[] programs = new String[]{"ALL"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        collection = getIntent().getStringExtra("Collection");
        db = FirebaseFirestore.getInstance();
        fgboys = db.collection(collection);
        mTextView = findViewById(R.id.textView2);
        mTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mListView = findViewById(R.id.list_item);
        date1 = getIntent().getLongExtra("Date1",date1);
        date2 = getIntent().getLongExtra("Date2",date2);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void select1(View v) {
        Intent intent = new Intent(MainActivity.this, DateSelector.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Spinner program = findViewById(R.id.program);
        programsList = Arrays.asList(programs);
        finalPrograms.addAll(programsList);
        programs = new String[]{};

        adapterPrograms = new ArrayAdapter<String>(
                this,R.layout.spinner_item,finalPrograms);
        adapterPrograms.setDropDownViewResource(R.layout.spinner_item);
        program.setAdapter(adapterPrograms);

        fgboys
//                .whereEqualTo("edate", date1)
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
                    spinCategory = "1";
                    populateCategories(spinCategory,spinnerPrograms);
                } else {
                    spinCategory = "2";
                    populateCategories(spinCategory,spinnerPrograms);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void populateCategories(String spinCategory, final String spinnerPrograms) {
        final String spinPrograms = spinnerPrograms;

        if (spinCategory.equals("1")) {
            finalCategories = new ArrayList<String>();
            categoriesList = Arrays.asList(categories);
            finalCategories.addAll(categoriesList);

            adapterCategories = new ArrayAdapter<String>
                    (this,R.layout.spinner_item,finalCategories);
            total2 = 0;

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
                                if ((finalCategories.contains(note.getCategory())) || (note.getCategory() == null)) {
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
                    total2 = 0;

                    if (spinnerCategories.equals("ALL")) {
                        fgboys
//                                .whereEqualTo("zzdate", date)
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, MainClass> count = new TreeMap<>();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            if (note.getSession() == null)
                                                continue;

                                            if (count.containsKey(note.getSession())) {
                                                int number = count.get(note.getSession()).getNumber();
                                                number++;
                                                total2++;
                                                count.put(note.getSession(), new MainClass(note.getCategory(),number));
                                                count.put("ALL", new MainClass("ALL",total2));
                                            } else {
                                                total2++;
                                                count.put(note.getSession(), new MainClass(note.getCategory(),1));
                                                count.put("ALL", new MainClass("ALL",total2));
                                            }
                                        }

                                        final MainActivityAdapter adapter = new MainActivityAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String session = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(MainActivity.this, TLActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Session", session);
                                                Log.d("TLActivity", "onItemClick: Main: " + spinPrograms);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("Collection",collection);
                                                intent.putExtras(bundle);
                                                startActivity(intent);

                                            }
                                        });
                                    }
                                });
                    } else {
                        fgboys
//                                .whereEqualTo("zzdate", date)
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
                                            if (count.containsKey(note.getSession())) {
                                                int number = count.get(note.getSession());
                                                number++;
                                                total2++;
                                                count.put(note.getSession(), number);
                                                count.put("ALL", total2);
                                            } else {
                                                total2++;
                                                count.put(note.getSession(), 1);
                                                count.put("ALL", total2);
                                            }
                                        }

                                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                //                final String fg_name = mListView.getItemAtPosition(i).toString();
                                                //                TextView fgName = (TextView) findViewById(R.id.text_view_1);
                                                String session = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(MainActivity.this, TLActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Session", session);
                                                Log.d("TLActivity", "onItemClick: Main: " + spinPrograms);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
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
        else {
            finalCategories = new ArrayList<String>();
            categoriesList = Arrays.asList(categories);
            finalCategories.addAll(categoriesList);

            adapterCategories = new ArrayAdapter<String>
                    (this,R.layout.spinner_item,finalCategories);
            total2 = 0;

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
                    total2 = 0;

                    if (spinnerCategories.equals("ALL")) {
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

                                        TreeMap<String, Integer> count = new TreeMap<>();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);
                                            if (count.containsKey(note.getSession())) {
                                                int number = count.get(note.getSession());
                                                number++;
                                                total2++;
                                                count.put(note.getSession(), number);
                                                count.put("ALL", total2);
                                            } else {
                                                total2++;
                                                count.put(note.getSession(), 1);
                                                count.put("ALL", total2);
                                            }
                                        }

                                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String session = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(MainActivity.this, TLActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Session", session);
                                                Log.d("TLActivity", "onItemClick: Main: " + spinPrograms);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
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
                                .whereEqualTo("category",spinnerCategories)
                                .whereEqualTo("program",spinPrograms)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, Integer> count = new TreeMap<>();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);
                                            if (count.containsKey(note.getSession())) {
                                                int number = count.get(note.getSession());
                                                number++;
                                                total2++;
                                                count.put(note.getSession(), number);
                                                count.put("ALL", total2);
                                            } else {
                                                total2++;
                                                count.put(note.getSession(), 1);
                                                count.put("ALL", total2);
                                            }
                                        }

                                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String session = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(MainActivity.this, TLActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Session", session);
                                                Log.d("TLActivity", "onItemClick: Main: " + spinPrograms);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
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