package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateSelector extends AppCompatActivity {

    private TextView mDisplayDate1, mDisplayDate2;
    private String date1 = "", date2 = "";
    private long epoch1 = 0, epoch2 = 0;
    private DatePickerDialog.OnDateSetListener mDateSetListener1, mDateSetListener2;
    TextView mTextView;
    private String collection = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_selector);

        collection = getIntent().getStringExtra("Collection");
        mDisplayDate1 = (TextView) findViewById(R.id.tvDate1);
        mDisplayDate2 = (TextView) findViewById(R.id.tvDate2);
        mTextView = findViewById(R.id.textView5);

        mDisplayDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        DateSelector.this,
//                        R.style.Theme_AppCompat_DayNight_Dialog,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener1,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
                cal.add(Calendar.MONTH,-1);
                long minDate = cal.getTime().getTime();
//                dialog.getDatePicker().setMinDate(minDate);
                dialog.getDatePicker().setMaxDate((System.currentTimeMillis()));
            }
        });

        mDisplayDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        DateSelector.this,
//                        R.style.Theme_AppCompat_DayNight_Dialog,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener2,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
                dialog.getDatePicker().setMaxDate((System.currentTimeMillis()));
            }
        });

        mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String mon = "";

                if (month <= 9) {
                    mon = "0" + month;

                    String dayOf = "";
                    if (day < 10) {
                        dayOf = "0" + day;
                        date1 = year + "-" + mon + "-" + dayOf;
                    } else {
                        date1 = year + "-" + mon + "-" + day;
                    }
                } else {
                    String dayOf = "";
                    if (day < 10) {
                        dayOf = "0" + day;
                        date1 = year + "-" + month + "-" + dayOf;
                    } else {
                        date1 = year + "-" + month + "-" + day;
                    }
                }
                try {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date dateParse1 = df.parse(date1);
                    epoch1 = (dateParse1.getTime())/1000;
                    epoch2 = (dateParse1.getTime())/1000;
                    mDisplayDate1.setText(date1);
                    date2 = date1;
                    mDisplayDate2.setText(date1);
                }
                catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                epoch2 = epoch1 + (60*60*24) - 1;
                date2 = date1;
                mDisplayDate1.setText(date1);
                mDisplayDate2.setText(date1);
            }
        };

        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String mon = "";

                if (month <= 9) {
                    mon = "0" + month;

                    String dayOf = "";
                    if (day < 10) {
                        dayOf = "0" + day;
                        date2 = year + "-" + mon + "-" + dayOf;
                    } else {
                        date2 = year + "-" + mon + "-" + day;
                    }
                } else {
                    String dayOf = "";
                    if (day < 10) {
                        dayOf = "0" + day;
                        date2 = year + "-" + month + "-" + dayOf;
                    } else {
                        date2 = year + "-" + month + "-" + day;
                    }
                }
                try {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date dateParse2 = df.parse(date2);
                    epoch2 = (dateParse2.toInstant().toEpochMilli())/1000;
                    mDisplayDate2.setText(date2);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                long secondInaDay = 60 * 60 * 24;
                epoch2 += secondInaDay-1;
            }
        };
    }

    public void select1(View v) {
        if (date1.equals("")) {
            Toast.makeText(this, "Please select a from date", Toast.LENGTH_SHORT).show();
        }
        else if (date2.equals("")) {
            Toast.makeText(this, "Please select a to date", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(DateSelector.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("Date1",epoch1);
            bundle.putLong("Date2",epoch2);
            bundle.putString("Collection",collection);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void select2(View v) {
        if (date1.equals("")) {
            Toast.makeText(this, "Please select a from date", Toast.LENGTH_SHORT).show();
        }
        else if (date2.equals("")) {
            Toast.makeText(this, "Please select a to date", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(DateSelector.this, AreaActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("Date1",epoch1);
            bundle.putLong("Date2",epoch2);
            bundle.putString("Collection",collection);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void select3(View v) {
        if (date1.equals("")) {
            Toast.makeText(this, "Please select a from date", Toast.LENGTH_SHORT).show();
        }
        else if (date2.equals("")) {
            Toast.makeText(this, "Please select a to date", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(DateSelector.this, Level.class);
            Bundle bundle = new Bundle();
            bundle.putLong("Date1",epoch1);
            bundle.putLong("Date2",epoch2);
            bundle.putString("Collection",collection);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void select4(View v) {
        if (date1.equals("")) {
            Toast.makeText(this, "Please select a from date", Toast.LENGTH_SHORT).show();
        }
        else if (date2.equals("")) {
            Toast.makeText(this, "Please select a to date", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(DateSelector.this, ColorActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("Date1",epoch1);
            bundle.putLong("Date2",epoch2);
            bundle.putString("Collection",collection);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void select5(View v) {
        if (date1.equals("")) {
            Toast.makeText(this, "Please select a from date", Toast.LENGTH_SHORT).show();
        }
        else if (date2.equals("")) {
            Toast.makeText(this, "Please select a to date", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(DateSelector.this, OccupationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("Date1",epoch1);
            bundle.putLong("Date2",epoch2);
            bundle.putString("Collection",collection);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void select6(View v) {
        if (date1.equals("")) {
            Toast.makeText(this, "Please select a from date", Toast.LENGTH_SHORT).show();
        }
        else if (date2.equals("")) {
            Toast.makeText(this, "Please select a to date", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(DateSelector.this, JapaActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("Date1",epoch1);
            bundle.putLong("Date2",epoch2);
            bundle.putString("Collection",collection);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void select7(View v) {
        if (date1.equals("")) {
            Toast.makeText(this, "Please select a from date", Toast.LENGTH_SHORT).show();
        }
        else if (date2.equals("")) {
            Toast.makeText(this, "Please select a to date", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(DateSelector.this, BranchActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("Date1",epoch1);
            bundle.putLong("Date2",epoch2);
            bundle.putString("Collection",collection);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void select8(View v) {
        if (date1.equals("")) {
            Toast.makeText(this, "Please select a from date", Toast.LENGTH_SHORT).show();
        }
        else if (date2.equals("")) {
            Toast.makeText(this, "Please select a to date", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(DateSelector.this, OrganizationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("Date1",epoch1);
            bundle.putLong("Date2",epoch2);
            bundle.putString("Collection",collection);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void select9(View v) {
        if (date1.equals("")) {
            Toast.makeText(this, "Please select a from date", Toast.LENGTH_SHORT).show();
        }
        else if (date2.equals("")) {
            Toast.makeText(this, "Please select a to date", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(DateSelector.this, CollegeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("Date1",epoch1);
            bundle.putLong("Date2",epoch2);
            bundle.putString("Collection",collection);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void select10(View v) {
        if (date1.equals("")) {
            Toast.makeText(this, "Please select a from date", Toast.LENGTH_SHORT).show();
        }
        else if (date2.equals("")) {
            Toast.makeText(this, "Please select a to date", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(DateSelector.this, SourceActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("Date1",epoch1);
            bundle.putLong("Date2",epoch2);
            bundle.putString("Collection",collection);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void select11(View v) {
        if (date1.equals("")) {
            Toast.makeText(this, "Please select a from date", Toast.LENGTH_SHORT).show();
        }
        else if (date2.equals("")) {
            Toast.makeText(this, "Please select a to date", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(DateSelector.this, UniqueActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("Date1",epoch1);
            bundle.putLong("Date2",epoch2);
            bundle.putString("Collection",collection);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void select12(View v) {
        if (date1.equals("")) {
            Toast.makeText(this, "Please select a from date", Toast.LENGTH_SHORT).show();
        }
        else if (date2.equals("")) {
            Toast.makeText(this, "Please select a to date", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(DateSelector.this, TimeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("Date1",epoch1);
            bundle.putLong("Date2",epoch2);
            bundle.putString("Date1Str",date1);
            bundle.putString("Date2Str",date2);
            bundle.putString("Collection",collection);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void select13(View v) {
        if (date1.equals("")) {
            Toast.makeText(this, "Please select a from date", Toast.LENGTH_SHORT).show();
        }
        else if (date2.equals("")) {
            Toast.makeText(this, "Please select a to date", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(DateSelector.this, ResActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("Date1",epoch1);
            bundle.putLong("Date2",epoch2);
            bundle.putString("Date1Str",date1);
            bundle.putString("Date2Str",date2);
            bundle.putString("Collection",collection);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}