package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
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

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.TreeMap;

import javax.annotation.Nullable;

public class OccupationFG extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference fgboys;
    private String collection = "";
    private static final String TAG = "OccupationActivity";
    ListView mListView;
    private TextView mTextView;
    private long date1 = 0, date2 = 0;
    private String tl = "";
    public static String spinPrograms = "";
    public static String spinCategories = "";
    public static String spinSessions = "";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    TreeMap<String,Occupation> count = null;
    public Double p1, p2, p3 ,p4, p5, p6, p7, p8;
    public int totalStudents, totalWorking, totalSelf, totalOthers, total;
    TextView mTextView6, mTextView7, mTextView8 , mTextView9, mTextView10, mTextView12, mTextView13,
            mTextView14, mTextView15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occupation_fg);
        collection = getIntent().getStringExtra("Collection");
        db = FirebaseFirestore.getInstance();
        fgboys = db.collection(collection);
        mTextView = findViewById(R.id.textView2);
        mTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mListView = findViewById(R.id.list_item);
        tl = getIntent().getStringExtra("TL");
        date1 = getIntent().getLongExtra("Date1",date1);
        date2 = getIntent().getLongExtra("Date2",date2);
        totalStudents = 0;
        totalWorking = 0;
        totalSelf = 0;
        totalOthers = 0;
        total = 0;
        p1 = 0.0; p2 = 0.0; p3 = 0.0; p4 = 0.0;
        p5 = 0.0; p6 = 0.0; p7 = 0.0; p8 = 0.0;
        mTextView6 = (TextView) findViewById(R.id.text6);
        mTextView7 = (TextView) findViewById(R.id.text7);
        mTextView8 = (TextView) findViewById(R.id.text8);
        mTextView9 = (TextView) findViewById(R.id.text9);
        mTextView10 = (TextView) findViewById(R.id.text10);
        mTextView12 = (TextView) findViewById(R.id.text12);
        mTextView13 = (TextView) findViewById(R.id.text13);
        mTextView14 = (TextView) findViewById(R.id.text14);
        mTextView15 = (TextView) findViewById(R.id.text15);
        spinPrograms= getIntent().getStringExtra("SpinPrograms");
        spinCategories= getIntent().getStringExtra("SpinCategories");
        spinSessions= getIntent().getStringExtra("SpinSessions");

//        mTextView.setText(spinPrograms + " " + spinCategories + " " + spinSessions);
    }

    @Override
    protected void onStart() {
        super.onStart();

        count = new TreeMap<>();
        count.clear();
        mListView.setAdapter(null);
        totalStudents = 0;
        totalWorking = 0;
        totalSelf = 0;
        totalOthers = 0;
        total = 0;
        p1 = 0.0; p2 = 0.0; p3 = 0.0; p4 = 0.0;
        p5 = 0.0; p6 = 0.0; p7 = 0.0; p8 = 0.0;

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
        fgboys
                .whereEqualTo("ztl",tl)
//                .whereEqualTo("zzdate",date)
                .whereGreaterThanOrEqualTo("edate", date1)
                .whereLessThanOrEqualTo("edate", date2)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        TreeMap<String, Occupation> count = new TreeMap<>();
                        String data = "";
                        Log.d(TAG, "onEvent: Out");

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            total++;
                            Log.d(TAG, "onEvent: Occupation"
                                    + note.getOccupation() + " " + note.getFg());

                            if (note.getOccupation() == null) {
                                note.setOccupation("Not updated");
                            }

                            if (count.containsKey(note.getFg())) {
//                                int students = count.get(note.getFg());
                                Occupation occupation = count.get(note.getFg());
                                int students = occupation.getStudents();
                                int working = occupation.getWorking();
                                int self = occupation.getSelf();
                                int others = occupation.getOthers();

                                if (note.getOccupation().equals("Student")) {
                                    students++;
                                    totalStudents++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else if (note.getOccupation().equals("Working")) {
                                    working++;
                                    totalWorking++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else if (note.getOccupation().equals("Business/Self Employed")) {
                                    self++;
                                    totalSelf++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else {
                                    others++;
                                    totalOthers++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                            } else {
                                switch (note.getOccupation()) {
                                    case "Student" :
                                        totalStudents++;
                                        count.put(note.getFg(), new Occupation(1,0,0,0));
                                        break;
                                    case "Working" :
                                        totalWorking++;
                                        count.put(note.getFg(), new Occupation(0,1,0,0));
                                        break;
                                    case "Business/Self Employed" :
                                        totalSelf++;
                                        count.put(note.getFg(), new Occupation(0,0,1,0));
                                        break;
                                    default :
                                        totalOthers++;
                                        count.put(note.getFg(), new Occupation(0,0,0,1));
                                        break;
                                }
                            }
                        }

                        final OccupationAdapter adapter = new OccupationAdapter(count);
                        mListView.setAdapter((ListAdapter)adapter);

                        DecimalFormat df = new DecimalFormat("##.#");
                        df.setRoundingMode(RoundingMode.DOWN);

                        Double p1 = ((double)totalStudents/total) * 100;
                        Double p2 = ((double)totalWorking/total) * 100;
                        Double p3 = ((double)totalSelf/total) * 100;
                        Double p4 = ((double)totalOthers/total) * 100;
                        Double p5 = Double.parseDouble(df.format(p1));
                        Double p6 = Double.parseDouble(df.format(p2));
                        Double p7 = Double.parseDouble(df.format(p3));
                        Double p8 = Double.parseDouble(df.format(p4));

                        if (p5.equals(100.0)) {
                            mTextView12.setText(String.valueOf(100));
                        } else {
                            mTextView12.setText(p5.toString());
                        }

                        if (p6.equals(100.0)) {
                            mTextView13.setText(String.valueOf(100));
                        } else {
                            mTextView13.setText(p6.toString());
                        }

                        if (p7.equals(100.0)) {
                            mTextView14.setText(String.valueOf(100));
                        } else {
                            mTextView14.setText(p7.toString());
                        }

                        if (p8.equals(100.0)) {
                            mTextView15.setText(String.valueOf(100));
                        } else {
                            mTextView15.setText(p8.toString());
                        }

                        Log.d(TAG, "onEvent: p1: " + totalStudents);

                        mTextView6.setText(String.valueOf(total));
                        mTextView7.setText(String.valueOf(totalStudents));
                        mTextView8.setText(String.valueOf(totalWorking));
                        mTextView9.setText(String.valueOf(totalSelf));
                        mTextView10.setText(String.valueOf(totalOthers));

                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String fg = adapter.getItem(i).getKey();
                                Intent intent = new Intent(OccupationFG.this, OccupationDetails.class);
                                Bundle bundle = new Bundle();
                                bundle.putLong("Date1",date1);
                                bundle.putLong("Date2",date2);
                                bundle.putString("TL", tl);
                                bundle.putString("FG", fg);
                                bundle.putString("SpinPrograms", spinPrograms);
                                bundle.putString("SpinCategories", spinCategories);
                                bundle.putString("SpinSessions", spinSessions);
                                bundle.putString("Collection",collection);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                });
    }

    public void populateListProgramsAndCategories() {
        fgboys
                .whereEqualTo("ztl",tl)
//                .whereEqualTo("zzdate",date)
                .whereGreaterThanOrEqualTo("edate", date1)
                .whereLessThanOrEqualTo("edate", date2)
                .whereEqualTo("session",spinSessions)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        TreeMap<String, Occupation> count = new TreeMap<>();
                        String data = "";
                        Log.d(TAG, "onEvent: Out");

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            total++;
                            Log.d(TAG, "onEvent: Occupation"
                                    + note.getOccupation() + " " + note.getFg());

                            if (note.getOccupation() == null) {
                                note.setOccupation("Not updated");
                            }

                            if (count.containsKey(note.getFg())) {
//                                int students = count.get(note.getFg());
                                Occupation occupation = count.get(note.getFg());
                                int students = occupation.getStudents();
                                int working = occupation.getWorking();
                                int self = occupation.getSelf();
                                int others = occupation.getOthers();

                                if (note.getOccupation().equals("Student")) {
                                    students++;
                                    totalStudents++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else if (note.getOccupation().equals("Working")) {
                                    working++;
                                    totalWorking++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else if (note.getOccupation().equals("Business/Self Employed")) {
                                    self++;
                                    totalSelf++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else {
                                    others++;
                                    totalOthers++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                            } else {
                                switch (note.getOccupation()) {
                                    case "Student" :
                                        totalStudents++;
                                        count.put(note.getFg(), new Occupation(1,0,0,0));
                                        break;
                                    case "Working" :
                                        totalWorking++;
                                        count.put(note.getFg(), new Occupation(0,1,0,0));
                                        break;
                                    case "Business/Self Employed" :
                                        totalSelf++;
                                        count.put(note.getFg(), new Occupation(0,0,1,0));
                                        break;
                                    default :
                                        totalOthers++;
                                        count.put(note.getFg(), new Occupation(0,0,0,1));
                                        break;
                                }
                            }
                        }

                        final OccupationAdapter adapter = new OccupationAdapter(count);
                        mListView.setAdapter((ListAdapter)adapter);

                        DecimalFormat df = new DecimalFormat("##.#");
                        df.setRoundingMode(RoundingMode.DOWN);

                        Double p1 = ((double)totalStudents/total) * 100;
                        Double p2 = ((double)totalWorking/total) * 100;
                        Double p3 = ((double)totalSelf/total) * 100;
                        Double p4 = ((double)totalOthers/total) * 100;
                        Double p5 = Double.parseDouble(df.format(p1));
                        Double p6 = Double.parseDouble(df.format(p2));
                        Double p7 = Double.parseDouble(df.format(p3));
                        Double p8 = Double.parseDouble(df.format(p4));

                        if (p5.equals(100.0)) {
                            mTextView12.setText(String.valueOf(100));
                        } else {
                            mTextView12.setText(p5.toString());
                        }

                        if (p6.equals(100.0)) {
                            mTextView13.setText(String.valueOf(100));
                        } else {
                            mTextView13.setText(p6.toString());
                        }

                        if (p7.equals(100.0)) {
                            mTextView14.setText(String.valueOf(100));
                        } else {
                            mTextView14.setText(p7.toString());
                        }

                        if (p8.equals(100.0)) {
                            mTextView15.setText(String.valueOf(100));
                        } else {
                            mTextView15.setText(p8.toString());
                        }

                        Log.d(TAG, "onEvent: p1: " + totalStudents);

                        mTextView6.setText(String.valueOf(total));
                        mTextView7.setText(String.valueOf(totalStudents));
                        mTextView8.setText(String.valueOf(totalWorking));
                        mTextView9.setText(String.valueOf(totalSelf));
                        mTextView10.setText(String.valueOf(totalOthers));

                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String fg = adapter.getItem(i).getKey();
                                Intent intent = new Intent(OccupationFG.this, OccupationDetails.class);
                                Bundle bundle = new Bundle();
                                bundle.putLong("Date1",date1);
                                bundle.putLong("Date2",date2);
                                bundle.putString("TL", tl);
                                bundle.putString("FG", fg);
                                bundle.putString("SpinPrograms", spinPrograms);
                                bundle.putString("SpinCategories", spinCategories);
                                bundle.putString("SpinSessions", spinSessions);
                                bundle.putString("Collection",collection);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                });
    }

    public void populateListProgramsAndSessions() {
        fgboys
                .whereEqualTo("ztl",tl)
//                .whereEqualTo("zzdate",date)
                .whereGreaterThanOrEqualTo("edate", date1)
                .whereLessThanOrEqualTo("edate", date2)
                .whereEqualTo("category",spinCategories)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        TreeMap<String, Occupation> count = new TreeMap<>();
                        String data = "";
                        Log.d(TAG, "onEvent: Out");

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            total++;
                            Log.d(TAG, "onEvent: Occupation"
                                    + note.getOccupation() + " " + note.getFg());

                            if (note.getOccupation() == null) {
                                note.setOccupation("Not updated");
                            }

                            if (count.containsKey(note.getFg())) {
//                                int students = count.get(note.getFg());
                                Occupation occupation = count.get(note.getFg());
                                int students = occupation.getStudents();
                                int working = occupation.getWorking();
                                int self = occupation.getSelf();
                                int others = occupation.getOthers();

                                if (note.getOccupation().equals("Student")) {
                                    students++;
                                    totalStudents++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else if (note.getOccupation().equals("Working")) {
                                    working++;
                                    totalWorking++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else if (note.getOccupation().equals("Business/Self Employed")) {
                                    self++;
                                    totalSelf++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else {
                                    others++;
                                    totalOthers++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                            } else {
                                switch (note.getOccupation()) {
                                    case "Student" :
                                        totalStudents++;
                                        count.put(note.getFg(), new Occupation(1,0,0,0));
                                        break;
                                    case "Working" :
                                        totalWorking++;
                                        count.put(note.getFg(), new Occupation(0,1,0,0));
                                        break;
                                    case "Business/Self Employed" :
                                        totalSelf++;
                                        count.put(note.getFg(), new Occupation(0,0,1,0));
                                        break;
                                    default :
                                        totalOthers++;
                                        count.put(note.getFg(), new Occupation(0,0,0,1));
                                        break;
                                }
                            }
                        }

                        final OccupationAdapter adapter = new OccupationAdapter(count);
                        mListView.setAdapter((ListAdapter)adapter);

                        DecimalFormat df = new DecimalFormat("##.#");
                        df.setRoundingMode(RoundingMode.DOWN);

                        Double p1 = ((double)totalStudents/total) * 100;
                        Double p2 = ((double)totalWorking/total) * 100;
                        Double p3 = ((double)totalSelf/total) * 100;
                        Double p4 = ((double)totalOthers/total) * 100;
                        Double p5 = Double.parseDouble(df.format(p1));
                        Double p6 = Double.parseDouble(df.format(p2));
                        Double p7 = Double.parseDouble(df.format(p3));
                        Double p8 = Double.parseDouble(df.format(p4));

                        if (p5.equals(100.0)) {
                            mTextView12.setText(String.valueOf(100));
                        } else {
                            mTextView12.setText(p5.toString());
                        }

                        if (p6.equals(100.0)) {
                            mTextView13.setText(String.valueOf(100));
                        } else {
                            mTextView13.setText(p6.toString());
                        }

                        if (p7.equals(100.0)) {
                            mTextView14.setText(String.valueOf(100));
                        } else {
                            mTextView14.setText(p7.toString());
                        }

                        if (p8.equals(100.0)) {
                            mTextView15.setText(String.valueOf(100));
                        } else {
                            mTextView15.setText(p8.toString());
                        }

                        Log.d(TAG, "onEvent: p1: " + totalStudents);

                        mTextView6.setText(String.valueOf(total));
                        mTextView7.setText(String.valueOf(totalStudents));
                        mTextView8.setText(String.valueOf(totalWorking));
                        mTextView9.setText(String.valueOf(totalSelf));
                        mTextView10.setText(String.valueOf(totalOthers));

                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String fg = adapter.getItem(i).getKey();
                                Intent intent = new Intent(OccupationFG.this, OccupationDetails.class);
                                Bundle bundle = new Bundle();
                                bundle.putLong("Date1",date1);
                                bundle.putLong("Date2",date2);
                                bundle.putString("TL", tl);
                                bundle.putString("FG", fg);
                                bundle.putString("SpinPrograms", spinPrograms);
                                bundle.putString("SpinCategories", spinCategories);
                                bundle.putString("SpinSessions", spinSessions);
                                bundle.putString("Collection",collection);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                });
    }

    public void populateListCategoriesAndSessions() {
        fgboys
                .whereEqualTo("ztl",tl)
//                .whereEqualTo("zzdate",date)
                .whereGreaterThanOrEqualTo("edate", date1)
                .whereLessThanOrEqualTo("edate", date2)
                .whereEqualTo("program",spinPrograms)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        TreeMap<String, Occupation> count = new TreeMap<>();
                        String data = "";
                        Log.d(TAG, "onEvent: Out");

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            total++;
                            Log.d(TAG, "onEvent: Occupation"
                                    + note.getOccupation() + " " + note.getFg());

                            if (note.getOccupation() == null) {
                                note.setOccupation("Not updated");
                            }

                            if (count.containsKey(note.getFg())) {
//                                int students = count.get(note.getFg());
                                Occupation occupation = count.get(note.getFg());
                                int students = occupation.getStudents();
                                int working = occupation.getWorking();
                                int self = occupation.getSelf();
                                int others = occupation.getOthers();

                                if (note.getOccupation().equals("Student")) {
                                    students++;
                                    totalStudents++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else if (note.getOccupation().equals("Working")) {
                                    working++;
                                    totalWorking++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else if (note.getOccupation().equals("Business/Self Employed")) {
                                    self++;
                                    totalSelf++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else {
                                    others++;
                                    totalOthers++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                            } else {
                                switch (note.getOccupation()) {
                                    case "Student" :
                                        totalStudents++;
                                        count.put(note.getFg(), new Occupation(1,0,0,0));
                                        break;
                                    case "Working" :
                                        totalWorking++;
                                        count.put(note.getFg(), new Occupation(0,1,0,0));
                                        break;
                                    case "Business/Self Employed" :
                                        totalSelf++;
                                        count.put(note.getFg(), new Occupation(0,0,1,0));
                                        break;
                                    default :
                                        totalOthers++;
                                        count.put(note.getFg(), new Occupation(0,0,0,1));
                                        break;
                                }
                            }
                        }

                        final OccupationAdapter adapter = new OccupationAdapter(count);
                        mListView.setAdapter((ListAdapter)adapter);

                        DecimalFormat df = new DecimalFormat("##.#");
                        df.setRoundingMode(RoundingMode.DOWN);

                        Double p1 = ((double)totalStudents/total) * 100;
                        Double p2 = ((double)totalWorking/total) * 100;
                        Double p3 = ((double)totalSelf/total) * 100;
                        Double p4 = ((double)totalOthers/total) * 100;
                        Double p5 = Double.parseDouble(df.format(p1));
                        Double p6 = Double.parseDouble(df.format(p2));
                        Double p7 = Double.parseDouble(df.format(p3));
                        Double p8 = Double.parseDouble(df.format(p4));

                        if (p5.equals(100.0)) {
                            mTextView12.setText(String.valueOf(100));
                        } else {
                            mTextView12.setText(p5.toString());
                        }

                        if (p6.equals(100.0)) {
                            mTextView13.setText(String.valueOf(100));
                        } else {
                            mTextView13.setText(p6.toString());
                        }

                        if (p7.equals(100.0)) {
                            mTextView14.setText(String.valueOf(100));
                        } else {
                            mTextView14.setText(p7.toString());
                        }

                        if (p8.equals(100.0)) {
                            mTextView15.setText(String.valueOf(100));
                        } else {
                            mTextView15.setText(p8.toString());
                        }

                        Log.d(TAG, "onEvent: p1: " + totalStudents);

                        mTextView6.setText(String.valueOf(total));
                        mTextView7.setText(String.valueOf(totalStudents));
                        mTextView8.setText(String.valueOf(totalWorking));
                        mTextView9.setText(String.valueOf(totalSelf));
                        mTextView10.setText(String.valueOf(totalOthers));

                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String fg = adapter.getItem(i).getKey();
                                Intent intent = new Intent(OccupationFG.this, OccupationDetails.class);
                                Bundle bundle = new Bundle();
                                bundle.putLong("Date1",date1);
                                bundle.putLong("Date2",date2);
                                bundle.putString("TL", tl);
                                bundle.putString("FG", fg);
                                bundle.putString("SpinPrograms", spinPrograms);
                                bundle.putString("SpinCategories", spinCategories);
                                bundle.putString("SpinSessions", spinSessions);
                                bundle.putString("Collection",collection);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                });
    }

    public void populateListPrograms() {
        fgboys
                .whereEqualTo("ztl",tl)
//                .whereEqualTo("zzdate",date)
                .whereGreaterThanOrEqualTo("edate", date1)
                .whereLessThanOrEqualTo("edate", date2)
                .whereEqualTo("category",spinCategories)
                .whereEqualTo("session",spinSessions)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        TreeMap<String, Occupation> count = new TreeMap<>();
                        String data = "";
                        Log.d(TAG, "onEvent: Out");

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            total++;
                            Log.d(TAG, "onEvent: Occupation"
                                    + note.getOccupation() + " " + note.getFg());

                            if (note.getOccupation() == null) {
                                note.setOccupation("Not updated");
                            }

                            if (count.containsKey(note.getFg())) {
//                                int students = count.get(note.getFg());
                                Occupation occupation = count.get(note.getFg());
                                int students = occupation.getStudents();
                                int working = occupation.getWorking();
                                int self = occupation.getSelf();
                                int others = occupation.getOthers();

                                if (note.getOccupation().equals("Student")) {
                                    students++;
                                    totalStudents++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else if (note.getOccupation().equals("Working")) {
                                    working++;
                                    totalWorking++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else if (note.getOccupation().equals("Business/Self Employed")) {
                                    self++;
                                    totalSelf++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else {
                                    others++;
                                    totalOthers++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                            } else {
                                switch (note.getOccupation()) {
                                    case "Student" :
                                        totalStudents++;
                                        count.put(note.getFg(), new Occupation(1,0,0,0));
                                        break;
                                    case "Working" :
                                        totalWorking++;
                                        count.put(note.getFg(), new Occupation(0,1,0,0));
                                        break;
                                    case "Business/Self Employed" :
                                        totalSelf++;
                                        count.put(note.getFg(), new Occupation(0,0,1,0));
                                        break;
                                    default :
                                        totalOthers++;
                                        count.put(note.getFg(), new Occupation(0,0,0,1));
                                        break;
                                }
                            }
                        }

                        final OccupationAdapter adapter = new OccupationAdapter(count);
                        mListView.setAdapter((ListAdapter)adapter);

                        DecimalFormat df = new DecimalFormat("##.#");
                        df.setRoundingMode(RoundingMode.DOWN);

                        Double p1 = ((double)totalStudents/total) * 100;
                        Double p2 = ((double)totalWorking/total) * 100;
                        Double p3 = ((double)totalSelf/total) * 100;
                        Double p4 = ((double)totalOthers/total) * 100;
                        Double p5 = Double.parseDouble(df.format(p1));
                        Double p6 = Double.parseDouble(df.format(p2));
                        Double p7 = Double.parseDouble(df.format(p3));
                        Double p8 = Double.parseDouble(df.format(p4));

                        if (p5.equals(100.0)) {
                            mTextView12.setText(String.valueOf(100));
                        } else {
                            mTextView12.setText(p5.toString());
                        }

                        if (p6.equals(100.0)) {
                            mTextView13.setText(String.valueOf(100));
                        } else {
                            mTextView13.setText(p6.toString());
                        }

                        if (p7.equals(100.0)) {
                            mTextView14.setText(String.valueOf(100));
                        } else {
                            mTextView14.setText(p7.toString());
                        }

                        if (p8.equals(100.0)) {
                            mTextView15.setText(String.valueOf(100));
                        } else {
                            mTextView15.setText(p8.toString());
                        }

                        Log.d(TAG, "onEvent: p1: " + totalStudents);

                        mTextView6.setText(String.valueOf(total));
                        mTextView7.setText(String.valueOf(totalStudents));
                        mTextView8.setText(String.valueOf(totalWorking));
                        mTextView9.setText(String.valueOf(totalSelf));
                        mTextView10.setText(String.valueOf(totalOthers));

                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String fg = adapter.getItem(i).getKey();
                                Intent intent = new Intent(OccupationFG.this, OccupationDetails.class);
                                Bundle bundle = new Bundle();
                                bundle.putLong("Date1",date1);
                                bundle.putLong("Date2",date2);
                                bundle.putString("TL", tl);
                                bundle.putString("FG", fg);
                                bundle.putString("SpinPrograms", spinPrograms);
                                bundle.putString("SpinCategories", spinCategories);
                                bundle.putString("SpinSessions", spinSessions);
                                bundle.putString("Collection",collection);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                });
    }

    public void populateListCategories() {
        fgboys
                .whereEqualTo("ztl",tl)
//                .whereEqualTo("zzdate",date)
                .whereGreaterThanOrEqualTo("edate", date1)
                .whereLessThanOrEqualTo("edate", date2)
                .whereEqualTo("program",spinPrograms)
                .whereEqualTo("session",spinSessions)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        TreeMap<String, Occupation> count = new TreeMap<>();
                        String data = "";
                        Log.d(TAG, "onEvent: Out");

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            total++;
                            Log.d(TAG, "onEvent: Occupation"
                                    + note.getOccupation() + " " + note.getFg());

                            if (note.getOccupation() == null) {
                                note.setOccupation("Not updated");
                            }

                            if (count.containsKey(note.getFg())) {
//                                int students = count.get(note.getFg());
                                Occupation occupation = count.get(note.getFg());
                                int students = occupation.getStudents();
                                int working = occupation.getWorking();
                                int self = occupation.getSelf();
                                int others = occupation.getOthers();

                                if (note.getOccupation().equals("Student")) {
                                    students++;
                                    totalStudents++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else if (note.getOccupation().equals("Working")) {
                                    working++;
                                    totalWorking++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else if (note.getOccupation().equals("Business/Self Employed")) {
                                    self++;
                                    totalSelf++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else {
                                    others++;
                                    totalOthers++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                            } else {
                                switch (note.getOccupation()) {
                                    case "Student" :
                                        totalStudents++;
                                        count.put(note.getFg(), new Occupation(1,0,0,0));
                                        break;
                                    case "Working" :
                                        totalWorking++;
                                        count.put(note.getFg(), new Occupation(0,1,0,0));
                                        break;
                                    case "Business/Self Employed" :
                                        totalSelf++;
                                        count.put(note.getFg(), new Occupation(0,0,1,0));
                                        break;
                                    default :
                                        totalOthers++;
                                        count.put(note.getFg(), new Occupation(0,0,0,1));
                                        break;
                                }
                            }
                        }

                        final OccupationAdapter adapter = new OccupationAdapter(count);
                        mListView.setAdapter((ListAdapter)adapter);

                        DecimalFormat df = new DecimalFormat("##.#");
                        df.setRoundingMode(RoundingMode.DOWN);

                        Double p1 = ((double)totalStudents/total) * 100;
                        Double p2 = ((double)totalWorking/total) * 100;
                        Double p3 = ((double)totalSelf/total) * 100;
                        Double p4 = ((double)totalOthers/total) * 100;
                        Double p5 = Double.parseDouble(df.format(p1));
                        Double p6 = Double.parseDouble(df.format(p2));
                        Double p7 = Double.parseDouble(df.format(p3));
                        Double p8 = Double.parseDouble(df.format(p4));

                        if (p5.equals(100.0)) {
                            mTextView12.setText(String.valueOf(100));
                        } else {
                            mTextView12.setText(p5.toString());
                        }

                        if (p6.equals(100.0)) {
                            mTextView13.setText(String.valueOf(100));
                        } else {
                            mTextView13.setText(p6.toString());
                        }

                        if (p7.equals(100.0)) {
                            mTextView14.setText(String.valueOf(100));
                        } else {
                            mTextView14.setText(p7.toString());
                        }

                        if (p8.equals(100.0)) {
                            mTextView15.setText(String.valueOf(100));
                        } else {
                            mTextView15.setText(p8.toString());
                        }

                        Log.d(TAG, "onEvent: p1: " + totalStudents);

                        mTextView6.setText(String.valueOf(total));
                        mTextView7.setText(String.valueOf(totalStudents));
                        mTextView8.setText(String.valueOf(totalWorking));
                        mTextView9.setText(String.valueOf(totalSelf));
                        mTextView10.setText(String.valueOf(totalOthers));

                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String fg = adapter.getItem(i).getKey();
                                Intent intent = new Intent(OccupationFG.this, OccupationDetails.class);
                                Bundle bundle = new Bundle();
                                bundle.putLong("Date1",date1);
                                bundle.putLong("Date2",date2);
                                bundle.putString("TL", tl);
                                bundle.putString("FG", fg);
                                bundle.putString("SpinPrograms", spinPrograms);
                                bundle.putString("SpinCategories", spinCategories);
                                bundle.putString("SpinSessions", spinSessions);
                                bundle.putString("Collection",collection);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                });
    }

    public void populateListSessions() {
        fgboys
                .whereEqualTo("ztl",tl)
//                .whereEqualTo("zzdate",date)
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

                        TreeMap<String, Occupation> count = new TreeMap<>();
                        String data = "";
                        Log.d(TAG, "onEvent: Out");

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            total++;
                            Log.d(TAG, "onEvent: Occupation"
                                    + note.getOccupation() + " " + note.getFg());

                            if (note.getOccupation() == null) {
                                note.setOccupation("Not updated");
                            }

                            if (count.containsKey(note.getFg())) {
//                                int students = count.get(note.getFg());
                                Occupation occupation = count.get(note.getFg());
                                int students = occupation.getStudents();
                                int working = occupation.getWorking();
                                int self = occupation.getSelf();
                                int others = occupation.getOthers();

                                if (note.getOccupation().equals("Student")) {
                                    students++;
                                    totalStudents++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else if (note.getOccupation().equals("Working")) {
                                    working++;
                                    totalWorking++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else if (note.getOccupation().equals("Business/Self Employed")) {
                                    self++;
                                    totalSelf++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else {
                                    others++;
                                    totalOthers++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                            } else {
                                switch (note.getOccupation()) {
                                    case "Student" :
                                        totalStudents++;
                                        count.put(note.getFg(), new Occupation(1,0,0,0));
                                        break;
                                    case "Working" :
                                        totalWorking++;
                                        count.put(note.getFg(), new Occupation(0,1,0,0));
                                        break;
                                    case "Business/Self Employed" :
                                        totalSelf++;
                                        count.put(note.getFg(), new Occupation(0,0,1,0));
                                        break;
                                    default :
                                        totalOthers++;
                                        count.put(note.getFg(), new Occupation(0,0,0,1));
                                        break;
                                }
                            }
                        }

                        final OccupationAdapter adapter = new OccupationAdapter(count);
                        mListView.setAdapter((ListAdapter)adapter);

                        DecimalFormat df = new DecimalFormat("##.#");
                        df.setRoundingMode(RoundingMode.DOWN);

                        Double p1 = ((double)totalStudents/total) * 100;
                        Double p2 = ((double)totalWorking/total) * 100;
                        Double p3 = ((double)totalSelf/total) * 100;
                        Double p4 = ((double)totalOthers/total) * 100;
                        Double p5 = Double.parseDouble(df.format(p1));
                        Double p6 = Double.parseDouble(df.format(p2));
                        Double p7 = Double.parseDouble(df.format(p3));
                        Double p8 = Double.parseDouble(df.format(p4));

                        if (p5.equals(100.0)) {
                            mTextView12.setText(String.valueOf(100));
                        } else {
                            mTextView12.setText(p5.toString());
                        }

                        if (p6.equals(100.0)) {
                            mTextView13.setText(String.valueOf(100));
                        } else {
                            mTextView13.setText(p6.toString());
                        }

                        if (p7.equals(100.0)) {
                            mTextView14.setText(String.valueOf(100));
                        } else {
                            mTextView14.setText(p7.toString());
                        }

                        if (p8.equals(100.0)) {
                            mTextView15.setText(String.valueOf(100));
                        } else {
                            mTextView15.setText(p8.toString());
                        }

                        Log.d(TAG, "onEvent: p1: " + totalStudents);

                        mTextView6.setText(String.valueOf(total));
                        mTextView7.setText(String.valueOf(totalStudents));
                        mTextView8.setText(String.valueOf(totalWorking));
                        mTextView9.setText(String.valueOf(totalSelf));
                        mTextView10.setText(String.valueOf(totalOthers));

                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String fg = adapter.getItem(i).getKey();
                                Intent intent = new Intent(OccupationFG.this, OccupationDetails.class);
                                Bundle bundle = new Bundle();
                                bundle.putLong("Date1",date1);
                                bundle.putLong("Date2",date2);
                                bundle.putString("TL", tl);
                                bundle.putString("FG", fg);
                                bundle.putString("SpinPrograms", spinPrograms);
                                bundle.putString("SpinCategories", spinCategories);
                                bundle.putString("SpinSessions", spinSessions);
                                bundle.putString("Collection",collection);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                });
    }

    public void populateList() {
        fgboys
                .whereEqualTo("ztl",tl)
//                .whereEqualTo("zzdate",date)
                .whereGreaterThanOrEqualTo("edate", date1)
                .whereLessThanOrEqualTo("edate", date2)
                .whereEqualTo("program",spinPrograms)
                .whereEqualTo("session",spinSessions)
                .whereEqualTo("category",spinCategories)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        TreeMap<String, Occupation> count = new TreeMap<>();
                        String data = "";
                        Log.d(TAG, "onEvent: Out");

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            total++;
                            Log.d(TAG, "onEvent: Occupation"
                                    + note.getOccupation() + " " + note.getFg());

                            if (note.getOccupation() == null) {
                                note.setOccupation("Not updated");
                            }

                            if (count.containsKey(note.getFg())) {
//                                int students = count.get(note.getFg());
                                Occupation occupation = count.get(note.getFg());
                                int students = occupation.getStudents();
                                int working = occupation.getWorking();
                                int self = occupation.getSelf();
                                int others = occupation.getOthers();

                                if (note.getOccupation().equals("Student")) {
                                    students++;
                                    totalStudents++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else if (note.getOccupation().equals("Working")) {
                                    working++;
                                    totalWorking++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else if (note.getOccupation().equals("Business/Self Employed")) {
                                    self++;
                                    totalSelf++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                                else {
                                    others++;
                                    totalOthers++;
                                    count.put(note.getFg(),new Occupation(students,working,self,others));
                                }
                            } else {
                                switch (note.getOccupation()) {
                                    case "Student" :
                                        totalStudents++;
                                        count.put(note.getFg(), new Occupation(1,0,0,0));
                                        break;
                                    case "Working" :
                                        totalWorking++;
                                        count.put(note.getFg(), new Occupation(0,1,0,0));
                                        break;
                                    case "Business/Self Employed" :
                                        totalSelf++;
                                        count.put(note.getFg(), new Occupation(0,0,1,0));
                                        break;
                                    default :
                                        totalOthers++;
                                        count.put(note.getFg(), new Occupation(0,0,0,1));
                                        break;
                                }
                            }
                        }

                        final OccupationAdapter adapter = new OccupationAdapter(count);
                        mListView.setAdapter((ListAdapter)adapter);

                        DecimalFormat df = new DecimalFormat("##.#");
                        df.setRoundingMode(RoundingMode.DOWN);

                        Double p1 = ((double)totalStudents/total) * 100;
                        Double p2 = ((double)totalWorking/total) * 100;
                        Double p3 = ((double)totalSelf/total) * 100;
                        Double p4 = ((double)totalOthers/total) * 100;
                        Double p5 = Double.parseDouble(df.format(p1));
                        Double p6 = Double.parseDouble(df.format(p2));
                        Double p7 = Double.parseDouble(df.format(p3));
                        Double p8 = Double.parseDouble(df.format(p4));

                        if (p5.equals(100.0)) {
                            mTextView12.setText(String.valueOf(100));
                        } else {
                            mTextView12.setText(p5.toString());
                        }

                        if (p6.equals(100.0)) {
                            mTextView13.setText(String.valueOf(100));
                        } else {
                            mTextView13.setText(p6.toString());
                        }

                        if (p7.equals(100.0)) {
                            mTextView14.setText(String.valueOf(100));
                        } else {
                            mTextView14.setText(p7.toString());
                        }

                        if (p8.equals(100.0)) {
                            mTextView15.setText(String.valueOf(100));
                        } else {
                            mTextView15.setText(p8.toString());
                        }

                        Log.d(TAG, "onEvent: p1: " + totalStudents);

                        mTextView6.setText(String.valueOf(total));
                        mTextView7.setText(String.valueOf(totalStudents));
                        mTextView8.setText(String.valueOf(totalWorking));
                        mTextView9.setText(String.valueOf(totalSelf));
                        mTextView10.setText(String.valueOf(totalOthers));

                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String fg = adapter.getItem(i).getKey();
                                Intent intent = new Intent(OccupationFG.this, OccupationDetails.class);
                                Bundle bundle = new Bundle();
                                bundle.putLong("Date1",date1);
                                bundle.putLong("Date2",date2);
                                bundle.putString("TL", tl);
                                bundle.putString("FG", fg);
                                bundle.putString("SpinPrograms", spinPrograms);
                                bundle.putString("SpinCategories", spinCategories);
                                bundle.putString("SpinSessions", spinSessions);
                                bundle.putString("Collection",collection);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void select1(View v) {
        Intent intent = new Intent(OccupationFG.this, DateSelector.class);
        startActivity(intent);
    }
}
