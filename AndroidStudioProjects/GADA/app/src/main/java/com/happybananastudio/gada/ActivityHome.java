package com.happybananastudio.gada;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by mgint on 7/10/2018.
 */

public class ActivityHome extends AppCompatActivity {
    private Context ThisContext;
    private String UserID, UserHandle, ClassCode;

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
        UserID = getIntent().getStringExtra("UserID");
        ExtractUserHandle();
    }

    private void InitializeWidgets() {
        InitializeButtons();
    }

    private void InitializeButtons() {
        InitializeButtonClassList();
        InitializeButtonSubjective();
        InitializeButtonObjective();
        InitializeButtonSchedule();
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
                                     String DialogTitle = "Warning";
                                     String DialogMessage = "Are you sure you want to sign out?";
                                     DialogChoiceSignOut(ThisContext, DialogTitle, DialogMessage);
                                 }
                             }
        );
    }

    private void InitializeButtonClassList() {
        Button B = findViewById(R.id.HomeB_ClassList);
        B.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     Intent intent;
                                     intent = new Intent(ThisContext, ActivityClassList.class);
                                     intent.putExtra("ClassCode", ClassCode);
                                     intent.putExtra("UserID", UserID);
                                     startActivity(intent);
                                 }
                             }
        );
    }

    private void InitializeButtonSubjective() {
        Button B = findViewById(R.id.HomeB_Subjective);
        B.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     Intent intent;
                                     intent = new Intent(ThisContext, ActivityHomeSubjective.class);
                                     intent.putExtra("ClassCode", ClassCode);
                                     intent.putExtra("UserID", UserID);
                                     startActivity(intent);
                                 }
                             }
        );
    }

    private void InitializeButtonObjective() {
        Button B = findViewById(R.id.HomeB_Objective);
        B.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     Intent intent;
                                     intent = new Intent(ThisContext, ActivityHomeObjective.class);
                                     intent.putExtra("ClassCode", ClassCode);
                                     intent.putExtra("UserID", UserID);
                                     startActivity(intent);
                                 }
                             }
        );
    }

    private void InitializeButtonSchedule() {
        Button B = findViewById(R.id.HomeB_Schedule);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(ThisContext, ActivitySchedule.class);
                intent.putExtra("ClassCode", ClassCode);
                intent.putExtra("UserID", UserID);
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
                intent.putExtra("UserID", UserID);
                intent.putExtra("UserIDtoView", UserID);
                startActivity(intent);
            }
        });
    }

    private void ExtractUserHandle() {
        final DatabaseReference UserDatabase = FirebaseDatabase.getInstance().getReference().child("ClassCodes").child(ClassCode).child("ClassList").child(UserID);
        UserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserHandle = (String) dataSnapshot.child("UserHandle").getValue();
                InitializeTextViewWelcome();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void DialogChoiceSignOut(Context ThisContext, String title, String message) {
        AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ThisContext, R.style.AlertDialogStyle);
        } else {
            builder = new AlertDialog.Builder(ThisContext);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.DialogConfirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SignOut();
                    }
                })
                .setNegativeButton(R.string.DialogCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void SignOut() {
        Intent intent;
        intent = new Intent(ThisContext, ActivitySignIn.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
