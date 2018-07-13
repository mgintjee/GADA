package com.happybananastudio.gada;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by mgint on 7/11/2018.
 */

public class ActivityClassList extends AppCompatActivity {
    private Context ThisContext;
    private String ClassCode, UserHandle;
    private ListView LV_ClassList;

    private FirebaseDatabase FireBase;
    private DatabaseReference Database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        ThisContext = this;
        FireBase = FirebaseDatabase.getInstance();
        Database = FireBase.getReference();
        InitializeWidgets();
        ExtractIntentInformation();
    }

    private void ExtractIntentInformation() {
        ClassCode = getIntent().getStringExtra("ClassCode");
        UserHandle = getIntent().getStringExtra("UserHandle");

        Log.d("ClassCode", ClassCode);
        Log.d("UserHandle", UserHandle);
        RetrieveUsersFromDatabase();
    }

    private void InitializeWidgets() {
        InitializeButtons();
        InitializeListViews();
    }

    private void InitializeButtons() {
        InitializeButtonGoBack();
        InitializeButtonRefresh();
    }

    private void InitializeButtonGoBack() {
        Button B = findViewById(R.id.ClassListB_GoBack);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void InitializeButtonRefresh() {
        Button B = findViewById(R.id.ClassListB_Refresh);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void InitializeListViews() {
        InitializeListViewUsers();
    }

    private void InitializeListViewUsers() {
        LV_ClassList = findViewById(R.id.ClassListLV_Users);
    }

    private void RetrieveUsersFromDatabase() {
        DatabaseReference UsersDatabase = Database.child(ClassCode);
        UsersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> ChildDataDS = dataSnapshot.getChildren();
                ArrayList<ClassUser> ClassList = new ArrayList<>();
                for (DataSnapshot ChildDS : ChildDataDS) {
                    String UserHandleToView = ChildDS.getKey();
                    String UserName = (String) ChildDS.child("User Name").getValue();
                    String UserType = (String) ChildDS.child("User Type").getValue();
                    String Password = (String) ChildDS.child("Password").getValue();
                }
                LV_ClassList.setAdapter(new ListViewClassList(ThisContext, 0, ClassList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
