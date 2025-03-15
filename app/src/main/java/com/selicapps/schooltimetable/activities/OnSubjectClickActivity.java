package com.selicapps.schooltimetable.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.selicapps.schooltimetable.classes.ListModelClass;
import com.selicapps.schooltimetable.R;
import com.selicapps.schooltimetable.classes.ReminderBroadcast;
import com.selicapps.schooltimetable.classes.TaskModelClass;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class OnSubjectClickActivity extends AppCompatActivity {
    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    TextView teacher, time, classroom;
    ImageView delete;

    Button addtask, back;
    DatePicker datePicker;
    TimePicker timePicker;

    public static final String TASK_DATA = "task_data";
    private static final String MONDAY_DATA = "monday_data";
    private static final String TUESDAY_DATA = "tuesday_data";
    private static final String WEDNESDAY_DATA = "wednesday_data";
    private static final String THURSDAY_DATA = "thursday_data";
    private static final String FRIDAY_DATA = "friday_data";
    public static final String CURRENT_BUTTON = "currentbutton";
    public static final String DAY = "day";

    ArrayList<ListModelClass> monday_list = new ArrayList<>();
    ArrayList<ListModelClass> tuesday_list = new ArrayList<>();
    ArrayList<ListModelClass> wednesday_list = new ArrayList<>();
    ArrayList<ListModelClass> thursday_list = new ArrayList<>();
    ArrayList<ListModelClass> friday_list = new ArrayList<>();

    ArrayList<TaskModelClass> task_list = new ArrayList<>();

    String teacher_text, time_text, classroom_text, subject_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_subject_click);

        String[] items = {this.getString(R.string.homework_pick_string), this.getString(R.string.presentation_pick_string),
                this.getString(R.string.test_pick_string), this.getString(R.string.oral_exam_pick_string),
                this.getString(R.string.other_pick_string)};

        teacher = findViewById(R.id.teacher_dis);
        time = findViewById(R.id.time_dis);
        classroom = findViewById(R.id.classroom_dis);
        delete = findViewById(R.id.delete_sub);
        addtask = findViewById(R.id.add_task_btn);
        datePicker = findViewById(R.id.simpleDatePicker);
        timePicker = findViewById(R.id.simpleTimePicker);
        back = findViewById(R.id.back_button_task);
        timePicker.setIs24HourView(true);

        teacher_text = MainActivity.getmTeacher();
        time_text = MainActivity.getmTime();
        classroom_text = MainActivity.getmClassroom();
        subject_text = MainActivity.getmSubject();

        teacher.setText(teacher_text);
        time.setText(time_text);
        classroom.setText(classroom_text);

        autoCompleteTextView = findViewById(R.id.Auto_complete_text);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);
        autoCompleteTextView.setAdapter(adapterItems);

        SharedPreferences loadsp = getSharedPreferences(CURRENT_BUTTON, MODE_PRIVATE);
        String dday = loadsp.getString(DAY, "");

        String correct_time_string = this.getString(R.string.enter_correct_time);
        String enter_task_string = this.getString(R.string.enter_task);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OnSubjectClickActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        addtask.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ScheduleExactAlarm")
            @Override
            public void onClick(View view) {
                Time now = new Time();
                now.setToNow();
                String task = autoCompleteTextView.getText().toString();

                if (!task.isEmpty()) {
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.MONTH, datePicker.getMonth());
                    c.set(Calendar.DATE, datePicker.getDayOfMonth());
                    c.set(Calendar.YEAR, datePicker.getYear());
                    c.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    c.set(Calendar.MINUTE, timePicker.getMinute());
                    c.set(Calendar.SECOND, 0);

                    Date pickedDate = c.getTime();
                    Date currentDate = new Date();

                    if (currentDate.before(pickedDate)) {
                        autoCompleteTextView.setError(null);
                        String date = String.valueOf(pickedDate);

                        // Save task to SharedPreferences
                        SharedPreferences sptask = getApplicationContext().getSharedPreferences(TASK_DATA, MODE_PRIVATE);
                        SharedPreferences.Editor etask = sptask.edit();
                        Gson gson = new Gson();
                        task_list = LoadTaskPrevousList(sptask, gson);
                        task_list.add(new TaskModelClass(subject_text, date, dday, task));
                        String json = gson.toJson(task_list);
                        etask.putString("tasks", json);
                        etask.apply();

                        // Reset input fields
                        autoCompleteTextView.setText("");
                        autoCompleteTextView.setFocusable(false);
                        datePicker.updateDate(now.year, now.month, now.monthDay);
                        timePicker.setHour(now.hour);
                        timePicker.setMinute(now.minute);

                        // Calculate time for notification (12 hours before)
                        long timeForNotification = pickedDate.getTime() - (12 * 60 * 60 * 1000); // 12 hours in milliseconds

                        // Set up the alarm with exact timing
                        int uniqueRequestCode = (subject_text + task + dday + date).hashCode();

                        Intent intent = new Intent(OnSubjectClickActivity.this, ReminderBroadcast.class);
                        intent.putExtra("task_name", subject_text); // Pass task name to notification
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                OnSubjectClickActivity.this,
                                uniqueRequestCode,
                                intent,
                                PendingIntent.FLAG_IMMUTABLE
                        );

                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        if (alarmManager != null) {
                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeForNotification, pendingIntent);
                        }

                    } else {
                        Toast.makeText(OnSubjectClickActivity.this, correct_time_string, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    autoCompleteTextView.setError(enter_task_string);
                }
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (dday){
                    case "monday":
                        SharedPreferences spmonday = getApplicationContext().getSharedPreferences(MONDAY_DATA, MODE_PRIVATE);
                        SharedPreferences.Editor emonday = spmonday.edit();
                        Gson gson = new Gson();
                        monday_list = SelectiveLoadPrevousList("monday", spmonday, gson);
                        String json = gson.toJson(monday_list);
                        emonday.putString("monday", json);
                        emonday.apply();
                        break;
                    case "tuesday":
                        SharedPreferences sptuesday = getApplicationContext().getSharedPreferences(TUESDAY_DATA, MODE_PRIVATE);
                        SharedPreferences.Editor etuesday = sptuesday.edit();
                        Gson gson1 = new Gson();
                        tuesday_list = SelectiveLoadPrevousList("tuesday", sptuesday, gson1);
                        String json1 = gson1.toJson(tuesday_list);
                        etuesday.putString("tuesday", json1);
                        etuesday.apply();
                        break;
                    case "wednesday":
                        SharedPreferences spwednesday = getApplicationContext().getSharedPreferences(WEDNESDAY_DATA, MODE_PRIVATE);
                        SharedPreferences.Editor ewednesday = spwednesday.edit();
                        Gson gson2 = new Gson();
                        wednesday_list = SelectiveLoadPrevousList(dday, spwednesday, gson2);
                        String json2 = gson2.toJson(wednesday_list);
                        ewednesday.putString("wednesday", json2);
                        ewednesday.apply();
                        break;
                    case "thursday":
                        SharedPreferences spthursday = getApplicationContext().getSharedPreferences(THURSDAY_DATA, MODE_PRIVATE);
                        SharedPreferences.Editor ethursday = spthursday.edit();
                        Gson gson3 = new Gson();
                        thursday_list = SelectiveLoadPrevousList("thursday", spthursday, gson3);
                        String json3 = gson3.toJson(thursday_list);
                        ethursday.putString("thursday", json3);
                        ethursday.apply();
                        break;
                    case "friday":
                        SharedPreferences spfriday = getApplicationContext().getSharedPreferences(FRIDAY_DATA, MODE_PRIVATE);
                        SharedPreferences.Editor efriday = spfriday.edit();
                        Gson gson4 = new Gson();
                        friday_list = SelectiveLoadPrevousList("friday", spfriday, gson4);
                        String json4 = gson4.toJson(friday_list);
                        efriday.putString("friday", json4);
                        efriday.apply();
                        break;
                }
                Intent intent = new Intent(OnSubjectClickActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private ArrayList<ListModelClass> SelectiveLoadPrevousList(String day, SharedPreferences sp, Gson gson) {
        ArrayList<ListModelClass> list;
        ArrayList<ListModelClass> rlist = new ArrayList<>();

        String json = sp.getString(day, null);
        Type type = new TypeToken<ArrayList<ListModelClass>>() {

        }.getType();
        list = gson.fromJson(json, type);
        for (int j = 0; j < Objects.requireNonNull(list).size(); j++) {
            if(!list.get(j).subject.equals(subject_text)){
                rlist.add(new ListModelClass(list.get(j).subject, list.get(j).start_time, list.get(j).stop_time,
                        list.get(j).classroom, list.get(j).teacher));
            }
        }

        return rlist;
    }

    private ArrayList<TaskModelClass> LoadTaskPrevousList(SharedPreferences sp, Gson gson) {
        ArrayList<TaskModelClass> list;
        ArrayList<TaskModelClass> rlist = new ArrayList<>();

        String json = sp.getString("tasks", null);
        Type type = new TypeToken<ArrayList<TaskModelClass>>() {

        }.getType();
        list = gson.fromJson(json, type);
        if(list!=null){
            for (int j = 0; j < list.size(); j++) {
                rlist.add(new TaskModelClass(list.get(j).subject, list.get(j).date,
                        list.get(j).clicked_day, list.get(j).task_kind));
            }
        }
        return rlist;
    }
}