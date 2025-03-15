package com.selicapps.schooltimetable.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.selicapps.schooltimetable.R;
import com.selicapps.schooltimetable.classes.ReminderBroadcast;
import com.selicapps.schooltimetable.interfaces.TaskSelectListener;
import com.selicapps.schooltimetable.adapters.RCTaskAdapter;
import com.selicapps.schooltimetable.classes.RCTaskModelClass;
import com.selicapps.schooltimetable.classes.TaskModelClass;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TaskLoadActivity extends AppCompatActivity implements TaskSelectListener {

    public static final String TASK_DATA = "task_data";

    Button addTask;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<RCTaskModelClass> rcTaskList;
    RCTaskAdapter adapter;

    String subject1, date1, task1, day1;

    ArrayList<TaskModelClass> task_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_load);

        addTask = findViewById(R.id.back_button_task);
        addTask.setOnClickListener(v -> {
            Intent intent = new Intent(TaskLoadActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        SharedPreferences tasksp = getSharedPreferences(TASK_DATA, MODE_PRIVATE);
        loadData(tasksp);
    }

    private void loadData(SharedPreferences sp) {
        task_list.clear();
        recyclerView = findViewById(R.id.TaskRecView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RCTaskAdapter();
        rcTaskList = new ArrayList<>();

        Gson gson = new Gson();
        String json = sp.getString("tasks", null);
        Type type = new TypeToken<ArrayList<TaskModelClass>>() {

        }.getType();
        task_list = gson.fromJson(json, type);
        if (task_list == null) {
            task_list = new ArrayList<>();
        } else {
            for (int j = 0; j < task_list.size(); j++) {
                rcTaskList.add(new RCTaskModelClass(task_list.get(j).clicked_day ,task_list.get(j).date, task_list.get(j).subject,
                        task_list.get(j).task_kind));
            }
        }

        adapter.updateData(rcTaskList, this);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<TaskModelClass> deleteElementLoadPrevousList(SharedPreferences sp, Gson gson) {
        ArrayList<TaskModelClass> list;
        ArrayList<TaskModelClass> rlist = new ArrayList<>();

        String json = sp.getString("tasks", null);
        Type type = new TypeToken<ArrayList<TaskModelClass>>() {

        }.getType();
        list = gson.fromJson(json, type);
        if(list!=null){
            int lock = 0;
            for (int j = 0; j < list.size(); j++) {
                if(list.get(j).clicked_day.equals(day1)){
                    if(list.get(j).subject.equals(subject1)){
                        if(list.get(j).date.equals(date1)){
                            if(list.get(j).task_kind.equals(task1)){
                                lock=1;
                                cancelScheduledNotification(list.get(j));
                            }
                        }
                    }
                }
                if(lock==0){
                    rlist.add(new TaskModelClass(list.get(j).subject, list.get(j).date,
                            list.get(j).clicked_day, list.get(j).task_kind));
                }
                lock=0;
            }
        }

        return rlist;
    }

    private void cancelScheduledNotification(TaskModelClass task) {
        // Create the same Intent with the unique task extras used for scheduling
        Intent intent = new Intent(this, ReminderBroadcast.class);
        intent.putExtra("task_name" , task.subject);

        // Use the same unique request code based on task details
        int uniqueRequestCode = (task.subject + task.task_kind + task.clicked_day + task.date).hashCode();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                uniqueRequestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        // Cancel the alarm
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        // Optional: Cancel any active notifications (if needed)
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(uniqueRequestCode);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onItemClicked(RCTaskModelClass rcTaskModelClass) {
        SharedPreferences tsp = getSharedPreferences(TASK_DATA, MODE_PRIVATE);
        SharedPreferences.Editor edt = tsp.edit();
        subject1 = rcTaskModelClass.getSubject();
        date1 = rcTaskModelClass.getDate();
        task1 = rcTaskModelClass.getTask_kind();
        day1 = rcTaskModelClass.getClicked_day();
        task_list.clear();
        Gson gsont = new Gson();
        task_list = deleteElementLoadPrevousList(tsp, gsont);
        String jsont = gsont.toJson(task_list);
        edt.putString("tasks", jsont);
        edt.apply();
        loadData(tsp);
    }
}