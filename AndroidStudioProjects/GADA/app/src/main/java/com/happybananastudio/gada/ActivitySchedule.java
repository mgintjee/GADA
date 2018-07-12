package com.happybananastudio.gada;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by mgint on 7/11/2018.
 */

public class ActivitySchedule extends AppCompatActivity {
    private Context ThisContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ThisContext = this;
        InitializeWidgets();
    }

    private void InitializeWidgets() {
        InitializeButtons();
    }

    private void InitializeButtons() {
        InitializeButtonGoBack();
    }

    private void InitializeButtonGoBack() {
        Button B = findViewById(R.id.ScheduleB_GoBack);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
