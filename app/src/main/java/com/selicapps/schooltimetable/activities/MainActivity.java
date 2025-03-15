package com.selicapps.schooltimetable.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.provider.Settings;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.selicapps.schooltimetable.classes.ListModelClass;
import com.selicapps.schooltimetable.R;
import com.selicapps.schooltimetable.adapters.RCAdapter;
import com.selicapps.schooltimetable.classes.RCModelClass;
import com.selicapps.schooltimetable.interfaces.SelectListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements SelectListener, NavigationView.OnNavigationItemSelectedListener{

    NavigationView navigationView;
    DrawerLayout drawerLayout;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<RCModelClass> rcsubjectList;
    RCAdapter adapter;

    static String classroom;
    static String teacher;
    static String time;
    static String subject;

    InterstitialAd mInterstitialAd;

    public static final String CURRENT_BUTTON = "currentbutton";
    private static final String MONDAY_DATA = "monday_data";
    private static final String TUESDAY_DATA = "tuesday_data";
    private static final String WEDNESDAY_DATA = "wednesday_data";
    private static final String THURSDAY_DATA = "thursday_data";
    private static final String FRIDAY_DATA = "friday_data";
    private static final String TASK_DATA = "task_data";
    public static final String DAY = "day";

    public static final String CHANNEL_ID = "school timetable";
    public static final String CHANNEL_NAME = "school timetable";
    public static final String CHANNEL_DESC = "school timetable";

    Button monday, tuesday, wednesday, thursday, friday;
    ImageView manu, addsubject;

    ArrayList<ListModelClass> subject_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        manu = findViewById(R.id.manu_img);
        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        addsubject = findViewById(R.id.add_subject_img);

        requestNotificationPermission();
        LoadSubjects();

        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData("monday");
                LoadSubjects();
            }
        });

        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData("tuesday");
                LoadSubjects();
            }
        });

        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData("wednesday");
                LoadSubjects();
            }
        });

        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData("thursday");
                LoadSubjects();
            }
        });

        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData("friday");
                LoadSubjects();
            }
        });

        addsubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddSubjectActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        manu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
            }
        }, 2200);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void LoadSubjects() {
        subject_list = new ArrayList<>();
        SharedPreferences spmonday = getSharedPreferences(MONDAY_DATA, MODE_PRIVATE);
        SharedPreferences sptuesday = getSharedPreferences(TUESDAY_DATA, MODE_PRIVATE);
        SharedPreferences spwednesday = getSharedPreferences(WEDNESDAY_DATA, MODE_PRIVATE);
        SharedPreferences spthursday = getSharedPreferences(THURSDAY_DATA, MODE_PRIVATE);
        SharedPreferences spfriday = getSharedPreferences(FRIDAY_DATA, MODE_PRIVATE);
        SharedPreferences spcb = getSharedPreferences(CURRENT_BUTTON, MODE_PRIVATE);
        String dday = spcb.getString(DAY, "monday");

        switch (dday) {
            case "monday":
                loadData(dday, spmonday);
                break;
            case "tuesday":
                loadData(dday, sptuesday);
                break;
            case "wednesday":
                loadData(dday, spwednesday);
                break;
            case "thursday":
                loadData(dday, spthursday);
                break;
            case "friday":
                loadData(dday, spfriday);
                break;
        }
    }

    private void loadData(String dday, SharedPreferences sp) {
        // Initialize RecyclerView and its components
        recyclerView = findViewById(R.id.MainRecView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RCAdapter();
        rcsubjectList = new ArrayList<>();

        // Deserialize JSON data from SharedPreferences
        Gson gson = new Gson();
        String json = sp.getString(dday, null);
        Type type = new TypeToken<ArrayList<ListModelClass>>() {}.getType();
        subject_list = gson.fromJson(json, type);

        if (subject_list == null) {
            subject_list = new ArrayList<>();
        } else {
            // Use a TreeMap to automatically sort by start time (in minutes)
            TreeMap<Integer, ListModelClass> sortedMap = new TreeMap<>();

            for (ListModelClass subject : subject_list) {
                int startTimeInMinutes = parseTimeToMinutes(subject.start_time);

                // Avoid overwriting existing entries for the same start time but different subjects
                while (sortedMap.containsKey(startTimeInMinutes)) {
                    startTimeInMinutes++; // Increment time slightly to maintain uniqueness
                }

                sortedMap.put(startTimeInMinutes, subject);
            }

            // Convert the sorted map back into the RCModelClass list
            for (ListModelClass subject : sortedMap.values()) {
                rcsubjectList.add(new RCModelClass(
                        subject.subject,
                        subject.start_time + "-" + subject.stop_time,
                        subject.classroom,
                        subject.teacher
                ));
            }
        }

        // Update adapter and set it to the RecyclerView
        adapter.updateData(rcsubjectList, this);
        recyclerView.setAdapter(adapter);
    }

    public int parseTimeToMinutes(String time) {
        // Replace any non-numeric separators (like '.' or ':') with a colon ':'
        String normalizedTime = time.replaceAll("[^\\d]", ":");

        // Split time into hours and minutes, default to "00" if minutes are missing
        String[] timeParts = normalizedTime.split(":");

        // Extract hours and minutes, default minutes to "00" if missing
        String hs = timeParts[0];
        String ms = timeParts.length > 1 ? timeParts[1] : "00";

        // Parse hours and minutes as integers
        int hours = Integer.parseInt(hs);
        int minutes = Integer.parseInt(ms);

        // Convert time to total minutes
        return hours * 60 + minutes;
    }

    private void saveData(String text) {
        SharedPreferences sharedPreferences = getSharedPreferences(CURRENT_BUTTON, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DAY, text);
        editor.apply();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Notification Permission Required")
                        .setMessage("This app requires notification permission to send reminders. Please grant it.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            ActivityCompat.requestPermissions(
                                    this,
                                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                                    1001
                            );
                        })
                        .setCancelable(false)
                        .show();
            } else {
                // If already granted, move to the next permission
                requestExactAlarmPermission();
            }
        } else {
            // Skip notification permission for devices below Android 13
            requestExactAlarmPermission();
        }
    }

    private void requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Exact Alarm Permission Required")
                        .setMessage("This app requires exact alarm permission to schedule reminders. Please grant it.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                            startActivity(intent);
                        })
                        .setCancelable(false)
                        .show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1001) { // Notification permission
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Notification permission granted, proceed to the next step
                requestExactAlarmPermission();
            } else {
                // If denied, close the app or inform the user
                Toast.makeText(this, "Notification permission is required.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onItemClicked(RCModelClass rcModelClass) {
        subject= rcModelClass.getSubject_text();
        time = rcModelClass.getTime_text();
        classroom = rcModelClass.getClassroom_text();
        teacher = rcModelClass.getTeacher_text();
        Intent intent = new Intent(MainActivity.this, OnSubjectClickActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public static String getmSubject() {
        return subject;
    }

    public static String getmTime() {
        return time;
    }

    public static String getmClassroom() {
        return classroom;
    }

    public static String getmTeacher() {
        return teacher;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.info:
                Toast.makeText(this, "version 1.2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tasks:
                Intent intent = new Intent(MainActivity.this, TaskLoadActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            case R.id.photo:
                Intent intent1 = new Intent(MainActivity.this, AutomaticSortActivity.class);
                startActivity(intent1);
                overridePendingTransition(0, 0);
                break;
            case R.id.reset:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_delete_all_red)
                        .setTitle("delete")
                        .setMessage("Are you sure you want to delete ALL?")
                        .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences mn = getSharedPreferences(MONDAY_DATA, MODE_PRIVATE);
                                SharedPreferences tu = getSharedPreferences(TUESDAY_DATA, MODE_PRIVATE);
                                SharedPreferences we = getSharedPreferences(WEDNESDAY_DATA, MODE_PRIVATE);
                                SharedPreferences th = getSharedPreferences(THURSDAY_DATA, MODE_PRIVATE);
                                SharedPreferences fr = getSharedPreferences(FRIDAY_DATA, MODE_PRIVATE);
                                SharedPreferences task = getSharedPreferences(TASK_DATA, MODE_PRIVATE);
                                task.edit().clear().apply();
                                mn.edit().clear().apply();
                                tu.edit().clear().apply();
                                we.edit().clear().apply();
                                th.edit().clear().apply();
                                fr.edit().clear().apply();
                                LoadSubjects();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                Toast.makeText(MainActivity.this, "Application reset", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create();
                dialog.show();
                break;
        }
        return true;
    }
}