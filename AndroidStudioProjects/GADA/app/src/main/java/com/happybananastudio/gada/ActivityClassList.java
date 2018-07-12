package com.happybananastudio.gada;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by mgint on 7/11/2018.
 */

public class ActivityClassList extends AppCompatActivity {
    private Context ThisContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ThisContext = this;
    }

}
