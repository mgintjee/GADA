package com.happybananastudio.gada;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by mgint on 7/10/2018.
 */

public class ActivityHome extends AppCompatActivity {
    private Context ThisContext;
    private String UserHandle, ClassCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ThisContext = this;
        ExtractIntentInformation();
        InitializeWidgets();
    }

    private void ExtractIntentInformation() {
        ClassCode = getIntent().getStringExtra("ClassCode");
        UserHandle = getIntent().getStringExtra("UserHandle");
    }

    private void InitializeWidgets() {
        InitializeTextViews();
        InitializeButtons();
    }

    private void InitializeTextViews() {
        InitializeTextViewWelcome();
    }

    private void InitializeButtons() {
        InitializeButtonSignOut();
        InitializeButtonProfile();
    }

    private void InitializeTextViewWelcome() {
        TextView TV = findViewById(R.id.HomeTV_Welcome);
        String Welcome = "Welcome, " + UserHandle + "!";
        TV.setText(Welcome);
    }

    private void InitializeButtonSignOut() {
        Button B = findViewById(R.id.HomeB_SignOut);
        B.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     Intent intent;
                                     intent = new Intent(ThisContext, ActivityInitial.class);
                                     startActivity(intent);
                                 }
                             }
        );
    }

    private void InitializeButtonProfile() {
        Button B = findViewById(R.id.HomeB_MyProfile);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(ThisContext, ActivityUserProfile.class);
                intent.putExtra("ClassCode", ClassCode);
                intent.putExtra("UserHandle", UserHandle);
                intent.putExtra("UserHandleToView", UserHandle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
