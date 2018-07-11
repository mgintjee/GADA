package com.happybananastudio.gada;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by mgint on 7/10/2018.
 */

public class ActivityHome extends AppCompatActivity {
    Context ThisContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ThisContext = this;
    }
}
