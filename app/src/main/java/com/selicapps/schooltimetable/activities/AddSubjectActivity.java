package com.selicapps.schooltimetable.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.selicapps.schooltimetable.classes.ListModelClass;
import com.selicapps.schooltimetable.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class AddSubjectActivity extends AppCompatActivity {

    private static final String MONDAY_DATA = "monday_data";
    private static final String TUESDAY_DATA = "tuesday_data";
    private static final String WEDNESDAY_DATA = "wednesday_data";
    private static final String THURSDAY_DATA = "thursday_data";
    private static final String FRIDAY_DATA = "friday_data";
    public static final String CURRENT_BUTTON = "currentbutton";
    public static final String DAY = "day";

    TextInputEditText subjectname, classroomname, teachername;
    TimePicker start, stop;
    Button addsubject, back;
    ArrayList<ListModelClass> monday_list = new ArrayList<>();
    ArrayList<ListModelClass> tuesday_list = new ArrayList<>();
    ArrayList<ListModelClass> wednesday_list = new ArrayList<>();
    ArrayList<ListModelClass> thursday_list = new ArrayList<>();
    ArrayList<ListModelClass> friday_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        subjectname = findViewById(R.id.subject_name_text);
        classroomname = findViewById(R.id.classroom_name_text);
        teachername = findViewById(R.id.teacher_name_text);
        start = findViewById(R.id.datePickerStart);
        stop = findViewById(R.id.datePickerEnd);
        addsubject = findViewById(R.id.add_subject_btn);
        back = findViewById(R.id.back_button);
        start.setIs24HourView(true);
        stop.setIs24HourView(true);

        start.setHour(6);
        start.setMinute(0);
        stop.setHour(6);
        stop.setMinute(0);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddSubjectActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        String enter_subject = this.getString(R.string.enter_subject);
        String enter_correct_time = this.getString(R.string.enter_correct_time);

        addsubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject = Objects.requireNonNull(subjectname.getText()).toString();
                String classroom = Objects.requireNonNull(classroomname.getText()).toString();
                String teacher = Objects.requireNonNull(teachername.getText()).toString();
                if(subject.isEmpty()){
                    subjectname.setError(enter_subject);
                }else{
                    int starthour = start.getHour();
                    int startminute = start.getMinute();
                    int stophour = stop.getHour();
                    int stopminute = stop.getMinute();
                    String sstarthour = String.valueOf(start.getHour());
                    String sstartminute = String.valueOf(start.getMinute());
                    String sstophour = String.valueOf(stop.getHour());
                    String sstopminute = String.valueOf(stop.getMinute());

                    if(sstarthour.length()==1){
                        sstarthour = "0" + sstarthour;
                    }if(sstartminute.length()==1){
                        sstartminute = "0" + sstartminute;
                    }if(sstophour.length()==1){
                        sstophour = "0" + sstophour;
                    }if(sstopminute.length()==1){
                        sstopminute = "0" + sstopminute;
                    }

                    if(starthour>stophour) {
                        Toast.makeText(AddSubjectActivity.this, enter_correct_time, Toast.LENGTH_SHORT).show();
                    }else if(starthour==stophour && startminute>stopminute){
                        Toast.makeText(AddSubjectActivity.this, enter_correct_time, Toast.LENGTH_SHORT).show();
                    }else{
                        /*RESETING VALUES OF INPUTES AFTER CLICK ON BUTTON*/
                        subjectname.getText().clear();
                        classroomname.getText().clear();
                        teachername.getText().clear();
                        start.setHour(6);
                        start.setMinute(0);
                        stop.setHour(6);
                        stop.setMinute(0);

                        SharedPreferences loadsp = getSharedPreferences(CURRENT_BUTTON, MODE_PRIVATE);
                        String dday = loadsp.getString(DAY, "");

                        switch (dday) {
                            case "monday":
                                SharedPreferences spmonday = getApplicationContext().getSharedPreferences(MONDAY_DATA, MODE_PRIVATE);
                                SharedPreferences.Editor emonday = spmonday.edit();
                                Gson gson = new Gson();
                                monday_list = LoadPrevousList("monday", spmonday, gson);
                                monday_list.add(new ListModelClass(subject, sstarthour + ":" + sstartminute, sstophour + ":" + sstopminute,
                                        classroom, teacher));
                                String json = gson.toJson(monday_list);
                                emonday.putString("monday", json);
                                emonday.apply();
                                break;
                            case "tuesday":
                                SharedPreferences sptuesday = getApplicationContext().getSharedPreferences(TUESDAY_DATA, MODE_PRIVATE);
                                SharedPreferences.Editor etuesday = sptuesday.edit();
                                Gson gson1 = new Gson();
                                tuesday_list = LoadPrevousList("tuesday", sptuesday, gson1);
                                tuesday_list.add(new ListModelClass(subject, sstarthour + ":" + sstartminute, sstophour + ":" + sstopminute,
                                        classroom, teacher));
                                String json1 = gson1.toJson(tuesday_list);
                                etuesday.putString("tuesday", json1);
                                etuesday.apply();
                                break;
                            case "wednesday":
                                SharedPreferences spwednesday = getApplicationContext().getSharedPreferences(WEDNESDAY_DATA, MODE_PRIVATE);
                                SharedPreferences.Editor ewednesday = spwednesday.edit();
                                Gson gson2 = new Gson();
                                wednesday_list = LoadPrevousList(dday, spwednesday, gson2);
                                wednesday_list.add(new ListModelClass(subject, sstarthour + ":" + sstartminute, sstophour + ":" + sstopminute,
                                        classroom, teacher));
                                String json2 = gson2.toJson(wednesday_list);
                                ewednesday.putString("wednesday", json2);
                                ewednesday.apply();
                                break;
                            case "thursday":
                                SharedPreferences spthursday = getApplicationContext().getSharedPreferences(THURSDAY_DATA, MODE_PRIVATE);
                                SharedPreferences.Editor ethursday = spthursday.edit();
                                Gson gson3 = new Gson();
                                thursday_list = LoadPrevousList("thursday", spthursday, gson3);
                                thursday_list.add(new ListModelClass(subject, sstarthour + ":" + sstartminute, sstophour + ":" + sstopminute,
                                        classroom, teacher));
                                String json3 = gson3.toJson(thursday_list);
                                ethursday.putString("thursday", json3);
                                ethursday.apply();
                                break;
                            case "friday":
                                SharedPreferences spfriday = getApplicationContext().getSharedPreferences(FRIDAY_DATA, MODE_PRIVATE);
                                SharedPreferences.Editor efriday = spfriday.edit();
                                Gson gson4 = new Gson();
                                friday_list = LoadPrevousList("friday", spfriday, gson4);
                                friday_list.add(new ListModelClass(subject, sstarthour + ":" + sstartminute, sstophour + ":" + sstopminute,
                                        classroom, teacher));
                                String json4 = gson4.toJson(friday_list);
                                efriday.putString("friday", json4);
                                efriday.apply();
                                break;
                        }
                    }
                }
            }

            private ArrayList<ListModelClass> LoadPrevousList(String day, SharedPreferences sp, Gson gson) {
                ArrayList<ListModelClass> list;
                ArrayList<ListModelClass> rlist = new ArrayList<>();

                String json = sp.getString(day, null);
                Type type = new TypeToken<ArrayList<ListModelClass>>() {

                }.getType();
                list = gson.fromJson(json, type);
                if(list!=null){
                    for (int j = 0; j < list.size(); j++) {
                        rlist.add(new ListModelClass(list.get(j).subject, list.get(j).start_time, list.get(j).stop_time,
                                list.get(j).classroom, list.get(j).teacher));
                    }
                }
                return rlist;
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}