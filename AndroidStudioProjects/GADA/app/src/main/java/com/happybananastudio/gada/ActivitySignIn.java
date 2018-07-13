package com.happybananastudio.gada;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class ActivitySignIn extends AppCompatActivity {

    Context ThisContext;
    private static int ActivityNewUser = 1;
    private static int ActivityOldUser = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThisContext = this;
        setContentView(R.layout.activity_signin);
        //GenerateFrameworkInDatabase();
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
        Button B = findViewById(R.id.SignInB_NewUser);
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
        Button B = findViewById(R.id.SignInB_OldUser);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(ThisContext, ActivityOldUser.class);
                startActivityForResult(intent, ActivityOldUser);
            }
        });
    }

    // Used to show how i want the db to be organized in the future
    private void GenerateFrameworkInDatabase() {
        FirebaseDatabase FireBase = FirebaseDatabase.getInstance();
        DatabaseReference Database = FireBase.getReference();
        ArrayList<String> ObjectiveTopics = new ArrayList<>(
                Arrays.asList("Arts", "Economics", "Literature", "Math", "Music", "Science", "Social Science"));
        ArrayList<String> SubjectiveTopics = new ArrayList<>(
                Arrays.asList("Essay", "Interview", "Impromptu"));
        DatabaseReference DataStructure = Database.child("Class Codes");
        DataStructure.child("temp").setValue("asdf");
        DatabaseReference D = DataStructure.child("4houses").child("Class List");

        DataStructure = Database.child("Data Structure");
        DataStructure.child("Created On").setValue("MM-DD-YYYY");

        DatabaseReference ClassList = DataStructure.child("Class List");
        DatabaseReference UserID = ClassList.child("User ID");
        UserID.child("Created On").setValue("MM-DD-YYYY");
        UserID.child("User Name").setValue("String");
        UserID.child("User Handle").setValue("String");
        UserID.child("Password").setValue("String");
        UserID.child("User type").setValue("String");

        DatabaseReference ClassSchedule = DataStructure.child("Class Schedule");
        DatabaseReference Day = ClassSchedule.child("Day");
        Day.child("Date").setValue("MM-DD-YYYY");
        Day.child("Info").setValue("String");
        Day.child("OP").setValue("UserID");

        DatabaseReference Prompts;

        for (int i = 0; i < SubjectiveTopics.size(); ++i) {
            String Subject = SubjectiveTopics.get(i);
            if (i == 2) {
                Subject = "Speech";
            }
            DatabaseReference SubjectiveDB = DataStructure.child("Subjective Material");
            DatabaseReference SubjectDB = SubjectiveDB.child(Subject + " Materials");
            Subject = SubjectiveTopics.get(i);
            Prompts = SubjectDB.child(Subject + " Prompts");
            Prompts.child("Prompt ID").child("Prompt").setValue("String");
            Prompts.child("Prompt ID").child("Rating").child("UserID").setValue("String");
            Prompts.child("Prompt ID").child("Rating").child("Vote").setValue("String");
            if (i == 2) {
                Prompts = SubjectDB.child("User Speeches");
                Prompts.child("User ID").setValue("String");
            }
        }

        for (int i = 0; i < ObjectiveTopics.size(); ++i) {
            String Subject = ObjectiveTopics.get(i);
            DatabaseReference ObjectiveDB = DataStructure.child("Objective Material");
            DatabaseReference SubjectDB = ObjectiveDB.child(Subject + " Materials");
            Prompts = SubjectDB.child(Subject + " Questions");
            Prompts.child("Question ID").child("Question").child("Question").setValue("String");
            Prompts.child("Question ID").child("Question").child("Correct Answer").setValue("String");
            Prompts.child("Question ID").child("Question").child("False Answers").setValue("String");
            Prompts.child("Question ID").child("Rating").child("UserID").setValue("String");
            Prompts.child("Question ID").child("Rating").child("Vote").setValue("String");
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
