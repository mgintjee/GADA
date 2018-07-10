package com.happybananastudio.gada;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

/**
 * Created by mgint on 7/9/2018.
 */

public class ActivityNewUser extends AppCompatActivity {

    Context ThisContext;
    private FirebaseDatabase FireBase;
    private DatabaseReference Database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThisContext = this;
        setContentView(R.layout.activity_new_user);
        FirebaseApp.initializeApp(ThisContext);
        FireBase = FirebaseDatabase.getInstance();
        Database = FireBase.getReference();
    }
}
