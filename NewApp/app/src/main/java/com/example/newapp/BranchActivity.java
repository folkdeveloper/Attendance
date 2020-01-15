package com.example.newapp;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.Nullable;

public class BranchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseFirestore db;
    private CollectionReference fgboys;
    private String collection = "";
    private static final String TAG = "AreaActivity";
    ListView mListView;
    private TextView mTextView;
    private long date1 = 0, date2 = 0;
    private int total1 = 0, total2 = 0;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String spinnerPrograms = "";
    private String spinnerCategories = "";
    List<String> programsList;
    List<String> categoriesList;
    ArrayList<String> finalPrograms = new ArrayList<String>();
    ArrayList<String> finalCategories = new ArrayList<String>();
    Button btn;
    ArrayAdapter<String> adapterPrograms;
    ArrayAdapter<String> adapterCategories;
    String spinCategory = "";
    String[] programs = new String[]{"ALL"};
    final String[] categories = new String[]{"ALL"};
    Spinner program, category;
    int posProgram, posCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);
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
    }

    @Override
    protected void onStart() {
        super.onStart();

        //        Spinner program = findViewById(R.id.program);
        programsList = Arrays.asList(programs);
        finalPrograms.addAll(programsList);
        programs = new String[]{};

        adapterPrograms = new ArrayAdapter<String>(
                this, R.layout.spinner_item, finalPrograms);
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
                posProgram = position;

                if (spinnerPrograms.equals("ALL")) {
                    spinCategory = "1";
                    populateCategories(spinCategory, spinnerPrograms);
                } else {
                    spinCategory = "2";
                    populateCategories(spinCategory, spinnerPrograms);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void populateCategories(String spinCategory, final String spinnerPrograms) {
        final String spinPrograms = spinnerPrograms;
        final String[] categories = new String[]{"ALL"};

        if (spinCategory.equals("1")) {
            finalCategories = new ArrayList<String>();
            categoriesList = Arrays.asList(categories);
            finalCategories.addAll(categoriesList);

            adapterCategories = new ArrayAdapter<String>
                    (this,R.layout.spinner_item,finalCategories);
            total2 = 0;

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
                    total2 = 0;
                    posCategory = position;

                    if (spinnerCategories.equals("ALL")) {
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
                                            if (count.containsKey(note.getBranch())) {
                                                int number = count.get(note.getBranch());
                                                number++;
                                                total2++;
                                                count.put(note.getBranch(), number);
                                                count.put("ALL", total2);
                                            } else {
                                                total2++;
                                                count.put(note.getBranch(), 1);
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
                                                String area = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(BranchActivity.this, BranchDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Area", area);
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
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, Integer> count = new TreeMap<>();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);
                                            if (count.containsKey(note.getBranch())) {
                                                int number = count.get(note.getBranch());
                                                number++;
                                                total2++;
                                                count.put(note.getBranch(), number);
                                                count.put("ALL", total2);
                                            } else {
                                                total2++;
                                                count.put(note.getBranch(), 1);
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
                                                String area = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(BranchActivity.this, BranchDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Area", area);
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
                    total2 = 0;
                    posCategory = position;

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
                                            if (count.containsKey(note.getBranch())) {
                                                int number = count.get(note.getBranch());
                                                number++;
                                                total2++;
                                                count.put(note.getBranch(), number);
                                                count.put("ALL", total2);
                                            } else {
                                                total2++;
                                                count.put(note.getBranch(), 1);
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
                                                String area = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(BranchActivity.this, BranchDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Area", area);
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
//                HashMap<String,Integer> numbers = new HashMap<String,Integer>();
//                            Log.d(TAG, "onEvent: Out" + spinnerText);
//                            Toast.makeText(MainActivity.this, spinnerText, Toast.LENGTH_SHORT).show();

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);
                                            if (count.containsKey(note.getBranch())) {
                                                int number = count.get(note.getBranch());
                                                number++;
                                                total2++;
                                                count.put(note.getBranch(), number);
                                                count.put("ALL", total2);
                                            } else {
                                                total2++;
                                                count.put(note.getBranch(), 1);
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
                                                String area = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(BranchActivity.this, BranchDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1",date1);
                                                bundle.putLong("Date2",date2);
                                                bundle.putString("Area", area);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        program.setSelection(posProgram);
        category.setSelection(posCategory);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("Programs",program.getItemAtPosition(0).toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            category.setSelection(posCategory);
        }
    }

    public void select1(View v) {
        Intent intent = new Intent(BranchActivity.this, DateSelector.class);
        startActivity(intent);
    }
}