package com.happybananastudio.gada;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by mgint on 7/10/2018.
 */

public class ActivityUserProfile extends AppCompatActivity {
    Context ThisContext;
    private String ClassCode, UserHandle, UserHandleToView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        ThisContext = this;
        ExtractIntentInformation();
    }

    private void InitializeWidgets(){

    }
    private void InitializeTextViews(){

    }

    private void ExtractIntentInformation(){
        ClassCode = getIntent().getStringExtra("ClassCode");
        UserHandleToView = getIntent().getStringExtra("UserHandle");
        UserHandle = getIntent().getStringExtra("UserHandle");
        HandleWhatToDisplay();
    }

    private void HandleWhatToDisplay(){
        if(UserHandle.equals(UserHandleToView)){
            // Viewing Self
        }
        else{
            // Viewing Other
        }
    }
}
