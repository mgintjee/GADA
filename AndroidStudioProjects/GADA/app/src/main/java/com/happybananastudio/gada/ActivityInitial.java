package com.happybananastudio.gada;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ActivityInitial extends AppCompatActivity {

    Context ThisContext;
    private static int ActivityNewUser  = 1;
    private static int ActivityOldUser  = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThisContext = this;
        setContentView(R.layout.activity_initial);
        InitializeWidgets();
    }

    private void InitializeWidgets() {
        InitializeButtons();
    }

    private void InitializeButtons() {
        InitializeNewUserButton();
        InitializeOldUserButton();
    }

    private void InitializeNewUserButton() {
        Button B = findViewById(R.id.InitialB_NewUser);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(ThisContext, ActivityNewUser.class);
                startActivityForResult(intent, ActivityNewUser);
            }
        });
    }

    private void InitializeOldUserButton() {
        Button B = findViewById(R.id.InitialB_OldUser);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(ThisContext, ActivityOldUser.class);
                startActivityForResult(intent, ActivityOldUser);
            }
        });
    }
}
