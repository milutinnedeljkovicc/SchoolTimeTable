package com.selicapps.schooltimetable.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.selicapps.schooltimetable.R;

public class AutomaticSortActivity extends AppCompatActivity {
    Button automatic_sort_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatic_sort);
        Toast.makeText(this, "Functions will be available soon!", Toast.LENGTH_SHORT).show();

        automatic_sort_button = findViewById(R.id.backButton);

        automatic_sort_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AutomaticSortActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }
}
