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
                intent = new Intent(ThisContext, ActivitySignInNewUser.class);
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
                intent = new Intent(ThisContext, ActivitySignInOldUser.class);
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
        DatabaseReference DataStructure = Database.child("ClassCodes");
        DataStructure.child("temp").setValue("asdf");
        DatabaseReference D = DataStructure.child("4houses").child("ClassList");

        DataStructure = Database.child("ExampleOfDataOrganization");
        DataStructure.child("CreatedOn").setValue("MM-DD-YYYY");

        DatabaseReference ClassList = DataStructure.child("ClassList");
        DatabaseReference UserID = ClassList.child("UserID");
        UserID.child("CreatedOn").setValue("MM-DD-YYYY");
        UserID.child("UserName").setValue("String");
        UserID.child("UserHandle").setValue("String");
        UserID.child("Password").setValue("String");
        UserID.child("Usertype").setValue("String");

        DatabaseReference ClassSchedule = DataStructure.child("ClassSchedule");
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
            DatabaseReference SubjectiveDB = DataStructure.child("SubjectiveMaterial");
            DatabaseReference SubjectDB = SubjectiveDB.child(Subject + "Materials");
            Subject = SubjectiveTopics.get(i);
            Prompts = SubjectDB.child(Subject + " Prompts");
            Prompts.child("PromptID").child("Prompt").setValue("String");
            Prompts.child("PromptID").child("Rating").child("UserID").setValue("String");
            Prompts.child("PromptID").child("Rating").child("Vote").setValue("String");
            if (i == 2) {
                Prompts = SubjectDB.child("UserSpeeches");
                Prompts.child("UserID").setValue("String");
            }
        }

        for (int i = 0; i < ObjectiveTopics.size(); ++i) {
            String Subject = ObjectiveTopics.get(i);
            DatabaseReference ObjectiveDB = DataStructure.child("ObjectiveMaterial");
            DatabaseReference SubjectDB = ObjectiveDB.child(Subject + "Materials");
            Prompts = SubjectDB.child(Subject + " Questions");
            Prompts.child("QuestionID").child("Question").child("Question").setValue("String");
            Prompts.child("QuestionID").child("Question").child("CorrectAnswer").setValue("String");
            Prompts.child("QuestionID").child("Question").child("FalseAnswers").setValue("String");
            Prompts.child("QuestionID").child("Rating").child("UserID").setValue("String");
            Prompts.child("QuestionID").child("Rating").child("Vote").setValue("String");
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
