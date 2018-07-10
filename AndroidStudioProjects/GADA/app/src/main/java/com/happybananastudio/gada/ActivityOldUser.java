package com.happybananastudio.gada;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 * Created by mgint on 7/9/2018.
 */

public class ActivityOldUser extends AppCompatActivity {

    Context ThisContext;
    private FirebaseDatabase FireBase;
    private DatabaseReference Database;
    private String ClassCode = "";
    private String UserHandle = "";
    private String Password = "";
    private boolean ValidClassCode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThisContext = this;
        setContentView(R.layout.activity_old_user);
        InitializeWidgets();
        FirebaseApp.initializeApp(ThisContext);
        FireBase = FirebaseDatabase.getInstance();
        Database = FireBase.getReference();

        Database.child("Class Codes").child("4Houses").setValue("07/09/2018");

        /*
        Database.child("User Types").child("Regular User").child("ID").setValue("0");
        Database.child("User Types").child("Moderator User").child("ID").setValue("1");
        Database.child("User Types").child("Admin User").child("ID").setValue("2");
        Database.child("User Types").child("Regular User").child("Functions").setValue("Read, Post");
        Database.child("User Types").child("Moderator User").child("Functions").setValue("Read, Post, Delete posts/comments");
        Database.child("User Types").child("Admin User").child("Functions").setValue("Read, Post, Delete Posts/comments, Change Roster, post in schedule");

        Database.child("Class Code").setValue("string");
        Database.child("Class Code").child("User Handle").setValue("string");
        Database.child("Class Code").child("User Handle").child("Password").setValue("string");
        Database.child("Class Code").child("User Handle").child("User Name").setValue("string");
        Database.child("Class Code").child("User Handle").child("User Type").setValue("int");
        Database.child("Class Code").child("User Handle").child("Created On").setValue("Date:(MM/DD/YYYY)");

        Database.child("4Houses").setValue("string");
        Database.child("4Houses").child("Gintjee").setValue("string");
        Database.child("4Houses").child("Gintjee").child("Password").setValue("NoHelp");
        Database.child("4Houses").child("Gintjee").child("User Name").setValue("Caitlyn Gintjee");
        Database.child("4Houses").child("Gintjee").child("User Type").setValue("2");
        Database.child("4Houses").child("Gintjee").child("Created On").setValue("07/09/2018");
        */
    }

    private void InitializeWidgets() {
        InitializeEditTexts();
        InitializeCheckBoxes();
        InitializeButtons();
    }

    private void InitializeEditTexts() {
        InitializeEditTextClassCode();
        InitializeEditTextUserHandle();
        InitializeEditTextPassword();
    }

    private void InitializeCheckBoxes() {
        InitializeCheckBoxShowPassword();
    }

    private void InitializeButtons() {
        InitializeButtonCancel();
        InitializeButtonSignIn();
    }

    private void InitializeButtonCancel() {
        Button B = findViewById(R.id.OldUserB_Cancel);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }

    private void InitializeButtonSignIn() {

        Button B = findViewById(R.id.OldUserB_SignIn);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Message = "";
                CheckValidClassCode();
                if (ValidClassCode) {
                    Message = ClassCode + " Exists!";
                } else {
                    Message = ClassCode + " No Exists!";
                }
                Log.d("Debug", Message);
            }
        });
    }

    private void InitializeCheckBoxShowPassword() {
        final EditText ET = findViewById(R.id.OldUserET_Password);
        CheckBox CB = findViewById(R.id.OldUserCB_ShowPassword);
        CB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    ET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    private void InitializeEditTextClassCode() {
        EditText ET = findViewById(R.id.OldUserET_ClassCode);
        ET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ClassCode = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }

    private void InitializeEditTextUserHandle() {
        EditText ET = findViewById(R.id.OldUserET_UserHandle);
        ET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UserHandle = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }

    private void InitializeEditTextPassword() {
        EditText ET = findViewById(R.id.OldUserET_Password);
        ET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Password = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }

    private void CheckValidClassCode() {
        DatabaseReference ClassCodesDatabase = Database.child("Class Codes");
        ClassCodesDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ValidClassCode = dataSnapshot.hasChild(ClassCode);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
