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
    private String ClassCode, ActiveUserID;
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
        ActiveUserID = getIntent().getStringExtra("UserID");

        Log.d("ClassCode", ClassCode);
        Log.d("UserID", ActiveUserID);
        RetrieveUsersFromDatabase();
    }

    private void InitializeWidgets() {
        InitializeButtons();
        InitializeListViews();
    }

    private void InitializeButtons() {
        InitializeButtonGoBack();
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

    private void InitializeListViews() {
        InitializeListViewUsers();
    }

    private void InitializeListViewUsers() {
        LV_ClassList = findViewById(R.id.ClassListLV_Users);
    }

    private void RetrieveUsersFromDatabase() {
        DatabaseReference UsersDatabase = Database.child("ClassCodes").child(ClassCode).child("ClassList");
        UsersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> ChildDataDS = dataSnapshot.getChildren();
                ArrayList<ClassUser> ClassList = new ArrayList<>();
                for (DataSnapshot ChildDS : ChildDataDS) {
                    ClassUser User = new ClassUser();
                    String UserID = ChildDS.getKey();
                    String UserHandle = (String) ChildDS.child("UserHandle").getValue();
                    String UserTeam = (String) ChildDS.child("UserTeam").getValue();
                    User.UserID = UserID;
                    User.UserHandle = UserHandle;
                    User.UserTeam = UserTeam;
                    ClassList.add(User);
                }
                LV_ClassList.setAdapter(new ListViewClassList(ThisContext, 0, ClassList, ClassCode, ActiveUserID));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
