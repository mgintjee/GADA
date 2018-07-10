package com.happybananastudio.gada;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InitialActivity extends AppCompatActivity {

    Context ThisContext;
    private FirebaseDatabase FireBase;
    private DatabaseReference Database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThisContext = this;
        setContentView(R.layout.activity_initial);
        FirebaseApp.initializeApp(ThisContext);
        FireBase = FirebaseDatabase.getInstance();
        Database = FireBase.getReference("Temp");
        Database.setValue("Hello, WorldA dirld!");
    }
}
