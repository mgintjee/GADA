package com.happybananastudio.gada;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

import static com.happybananastudio.gada.MyTools.DialogSignInError;
import static com.happybananastudio.gada.MyTools.StringIsAlphanumericAndLength;

public class ActivitySignInOldUser extends AppCompatActivity {

    Context ThisContext;
    private FirebaseDatabase FireBase;
    private DatabaseReference Database;
    private String ClassCode = "";
    private String UserHandle = "";
    private String Password = "";
    private String UserType = "";
    private String UserName = "";
    private String UserID = "";

    // Global Bounds
    private int CLASS_CODE_MIN = 5;
    private int CLASS_CODE_MAX = 10;
    private int USER_HANDLE_MIN = 5;
    private int USER_HANDLE_MAX = 25;
    private int USER_PASSWORD_MIN = 8;
    private int USER_PASSWORD_MAX = 20;

    private int ActivityHome = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThisContext = this;
        setContentView(R.layout.activity_signin_old_user);
        FirebaseApp.initializeApp(ThisContext);
        FireBase = FirebaseDatabase.getInstance();
        Database = FireBase.getReference();
        InitializeWidgets();
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

                if (ValidInput()) {
                    AttemptToSignIn();
                }
            }
        });
    }

    private boolean ValidInput() {
        String DialogMessage;
        boolean ValidClassCode = StringIsAlphanumericAndLength(ClassCode, CLASS_CODE_MIN, CLASS_CODE_MAX);
        boolean ValidUserHandle = StringIsAlphanumericAndLength(UserHandle, USER_HANDLE_MIN, USER_HANDLE_MAX);
        boolean ValidPassword1 = StringIsAlphanumericAndLength(Password, USER_PASSWORD_MIN, USER_PASSWORD_MAX);


        if (!ValidClassCode) {
            DialogMessage = "> Class Code must be Alphanumeric\n> Class Code is not Case Sensitive\n> Class Code must be ( " + CLASS_CODE_MIN + " < x < " + CLASS_CODE_MAX + " ) characters";
            DialogSignInError(ThisContext, DialogMessage);
            return false;
        }

        if (!ValidUserHandle) {
            DialogMessage = "> User Handle must be Alphanumeric\n> User Handle is not Case Sensitive\n> User Handle must be ( " + USER_HANDLE_MIN + " < x < " + USER_HANDLE_MAX + " ) characters";
            DialogSignInError(ThisContext, DialogMessage);
            return false;
        }

        if (!ValidPassword1) {
            DialogMessage = "> Password must be Alphanumeric\n> Password is Case Sensitive and \n> Password must be ( " + USER_PASSWORD_MIN + " < x < " + USER_PASSWORD_MAX + " ) characters";
            DialogSignInError(ThisContext, DialogMessage);
            return false;
        }

        return true;
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
                ClassCode = s.toString().toLowerCase();
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
                UserHandle = s.toString().toLowerCase();
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

    private void AttemptToSignIn() {
        DatabaseReference ClassListDatabase = Database.child("ClassCodes").child(ClassCode).child("ClassList");
        ClassListDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean CredentialsExist = false;
                for (DataSnapshot DS_Child : dataSnapshot.getChildren()) {
                    String uID = DS_Child.getKey();
                    String uHandle = (String) DS_Child.child("UserHandle").getValue();
                    String uPassword = (String) DS_Child.child("Password").getValue();

                    if (UserHandle.equals(uHandle) && Password.equals(uPassword)) {
                        UserID = uID;
                        CredentialsExist = true;
                        break;
                    }
                }
                if (CredentialsExist) {
                    LaunchActivityHome();
                } else {
                    DialogSignInError(ThisContext, "Incorrect Credentials");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void LaunchActivityHome() {
        Intent intent;
        intent = new Intent(ThisContext, ActivityHome.class);
        intent.putExtra("ClassCode", ClassCode);
        intent.putExtra("UserID", UserID);
        startActivity(intent);
    }
}
