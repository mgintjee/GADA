package com.happybananastudio.gada;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

import static com.happybananastudio.gada.MyTools.CapitalizeFirstLetterOfWord;
import static com.happybananastudio.gada.MyTools.DialogSimple;
import static com.happybananastudio.gada.MyTools.StringIsAlphanumericAndLength;

public class ActivityNewUser extends AppCompatActivity {

    Context ThisContext;
    private FirebaseDatabase FireBase;
    private DatabaseReference Database;
    private String ClassCode = "";
    private String UserHandle = "";
    private String UserName = "";
    private String Password1 = "";
    private String Password2 = "";
    private boolean ClassCodeExists = false;
    private boolean UserHandleExists = false;
    private boolean MatchingPasswords = false;

    // Global Bounds
    private int CLASS_CODE_MIN = 5;
    private int CLASS_CODE_MAX = 10;
    private int USER_HANDLE_MIN = 5;
    private int USER_HANDLE_MAX = 25;
    private int USER_NAME_MIN = 2;
    private int USER_NAME_MAX = 15;
    private int USER_PASSWORD_MIN = 8;
    private int USER_PASSWORD_MAX = 20;

    private int ActivityHome = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThisContext = this;
        setContentView(R.layout.activity_new_user);
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
        InitializeEditTextUserName();
        InitializeEditTextPassword1();
        InitializeEditTextPassword2();
    }

    private void InitializeCheckBoxes() {
        InitializeCheckBoxShowPassword();
    }

    private void InitializeButtons() {
        InitializeButtonCancel();
        InitializeButtonSignIn();
    }

    private void InitializeButtonCancel() {
        Button B = findViewById(R.id.NewUserB_Cancel);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }

    private void InitializeButtonSignIn() {

        Button B = findViewById(R.id.NewUserB_SignIn);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassCodeGate();
            }
        });
    }

    private void ClassCodeGate() {

        String DialogTitle = "Sign-In Error";
        String DialogMessage;

        boolean ValidClassCode = StringIsAlphanumericAndLength(ClassCode, CLASS_CODE_MIN, CLASS_CODE_MAX);

        if (ValidClassCode) {
            if (ClassCodeExists) {
                ValidUserName();
            } else {
                DialogMessage = "> Class Code Doesn\'t Exist";
                DialogSimple(ThisContext, DialogTitle, DialogMessage);

            }
        } else {
            DialogMessage = "> Class Code is not Case Sensitive\n> Class Code must be ( " + CLASS_CODE_MIN + " < x < " + CLASS_CODE_MAX + " ) characters";
            DialogSimple(ThisContext, DialogTitle, DialogMessage);
        }
    }

    private void ValidUserName() {

        String DialogTitle = "Sign-In Error";
        String DialogMessage;

        if (ValidName()) {
            ValidUserCredentialsGate();
        } else {
            DialogMessage = "> User Name must be Alphanumeric\n> User Name must be ( " + USER_NAME_MIN + " < x < " + USER_NAME_MAX + " ) characters";
            DialogSimple(ThisContext, DialogTitle, DialogMessage);
        }
    }

    private void ValidUserCredentialsGate() {
        String DialogTitle = "Sign-In Error";
        String DialogMessage;

        boolean ValidUserHandle = StringIsAlphanumericAndLength(UserHandle, USER_HANDLE_MIN, USER_HANDLE_MAX);
        boolean ValidPassword1 = StringIsAlphanumericAndLength(Password1, USER_PASSWORD_MIN, USER_PASSWORD_MAX);
        boolean ValidPassword2 = StringIsAlphanumericAndLength(Password2, USER_PASSWORD_MIN, USER_PASSWORD_MAX);

        if (ValidUserHandle) {
            if (ValidPassword1 && ValidPassword2) {
                if (MatchingPasswords) {
                    UniqueUserHandleGate();
                } else {
                    DialogMessage = "> Passwords must Match";
                    DialogSimple(ThisContext, DialogTitle, DialogMessage);
                }
            } else {
                DialogMessage = "> Password is Case Sensitive and \n> Password must be ( " + USER_PASSWORD_MIN + " < x < " + USER_PASSWORD_MAX + " ) characters";
                DialogSimple(ThisContext, DialogTitle, DialogMessage);
            }
        } else {
            DialogMessage = "> User Handle is not Case Sensitive\n> User Handle must be ( " + USER_HANDLE_MIN + " < x < " + USER_HANDLE_MAX + " ) characters";
            DialogSimple(ThisContext, DialogTitle, DialogMessage);
        }
    }

    private void UniqueUserHandleGate() {
        String DialogTitle = "Sign-In Error";
        String DialogMessage;

        if (!UserHandleExists) {
            SignInSuccess();
        } else {
            DialogMessage = "> User Handle is Already Taken";
            DialogSimple(ThisContext, DialogTitle, DialogMessage);
        }
    }

    private void SignInSuccess() {
        String DialogTitle = "Registration Success";
        String DialogMessage = "> Class Code: " + ClassCode + "\n> User Name: " + UserName + "\n> User Handle: " + UserHandle + "\n> Password: " + Password1;
        String DefaultUserType = "0";
        UserInfo User = new UserInfo(UserHandle, Password1, UserName, DefaultUserType);
        DialogConfirmCancel(ThisContext, DialogTitle, DialogMessage, User);
    }

    private void InitializeCheckBoxShowPassword() {
        final EditText ET1 = findViewById(R.id.NewUserET_Password1);
        final EditText ET2 = findViewById(R.id.NewUserET_Password2);
        CheckBox CB = findViewById(R.id.NewUserCB_ShowPassword);
        CB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ET1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ET2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    ET1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ET2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    private void InitializeEditTextClassCode() {
        EditText ET = findViewById(R.id.NewUserET_ClassCode);
        ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ClassCode = s.toString().toLowerCase();
                CheckIfClassCodeExists();
            }

            @Override
            public void afterTextChanged(Editable s) {
                ClassCode = s.toString().toLowerCase();
                CheckIfClassCodeExists();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }

    private void InitializeEditTextUserHandle() {
        EditText ET = findViewById(R.id.NewUserET_UserHandle);
        ET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UserHandle = s.toString().toLowerCase();
                CheckIfUserCredentialsExist();
            }

            @Override
            public void afterTextChanged(Editable s) {
                UserHandle = s.toString().toLowerCase();
                CheckIfUserCredentialsExist();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }

    private void InitializeEditTextUserName() {
        EditText ET = findViewById(R.id.NewUserET_UserName);
        ET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UserName = s.toString().toLowerCase();
            }

            @Override
            public void afterTextChanged(Editable s) {
                UserName = s.toString().toLowerCase();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }

    private void InitializeEditTextPassword1() {
        EditText ET = findViewById(R.id.NewUserET_Password1);
        ET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Password1 = s.toString();
                CheckBoxSamePassword();
            }

            @Override
            public void afterTextChanged(Editable s) {
                Password1 = s.toString();
                CheckBoxSamePassword();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }

    private void InitializeEditTextPassword2() {
        EditText ET = findViewById(R.id.NewUserET_Password2);
        ET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Password2 = s.toString();
                CheckBoxSamePassword();
            }

            @Override
            public void afterTextChanged(Editable s) {
                Password2 = s.toString();
                CheckBoxSamePassword();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }

    private void CheckBoxSamePassword() {
        CheckBox CB = findViewById(R.id.NewUserCB_SamePassword);
        MatchingPasswords = !Password1.equals("") && Password1.equals(Password2);
        if (MatchingPasswords) {
            if (!CB.isChecked()) {
                CB.toggle();
            }
        } else {
            if (CB.isChecked()) {
                CB.toggle();
            }
        }
    }

    private void CheckIfClassCodeExists() {
        final DatabaseReference ClassCodesDatabase = Database.child("Class Codes");
        if (!ClassCode.equals("")) {
            ClassCodesDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ClassCodeExists = dataSnapshot.hasChild(ClassCode);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {
            ClassCodeExists = false;
        }
    }

    private void CheckIfUserCredentialsExist() {
        DatabaseReference ClassCodeDatabase = Database.child(ClassCode);
        ClassCodeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserHandleExists = dataSnapshot.hasChild(UserHandle);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private boolean ValidName() {
        String[] NameParts = UserName.split(" ");
        StringBuilder NameBuilder = new StringBuilder("");
        for (int i = 0; i < NameParts.length; ++i) {
            String NamePart = NameParts[i];
            if (StringIsAlphanumericAndLength(NamePart, USER_NAME_MIN, USER_NAME_MAX)) {
                String FormattedNamePart = CapitalizeFirstLetterOfWord(NamePart);
                NameBuilder.append(FormattedNamePart).append(" ");
            } else {
                return false;
            }
        }
        UserName = NameBuilder.toString();
        return true;
    }

    private void RegisterNewUser(UserInfo User) {
        Log.d("Registering", User.GetName());
        DatabaseReference ClassCodeDatabase = Database.child(ClassCode);
        DatabaseReference UserInfoDatabase = ClassCodeDatabase.child(User.GetHandle());

        UserInfoDatabase.child("User Name").setValue(User.GetName());
        UserInfoDatabase.child("Password").setValue(User.GetPassword());
        UserInfoDatabase.child("User Type").setValue(User.GetType());
        UserInfoDatabase.child("Created on").setValue(User.GetDate());
        UserInfoDatabase.child("Speech").setValue("");
    }

    void DialogConfirmCancel(Context ThisContext, String title, String message, final UserInfo User) {
        AlertDialog.Builder builder;
        final int[] choice = {-1};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ThisContext, R.style.AlertDialogStyle);
        } else {
            builder = new AlertDialog.Builder(ThisContext);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.DialogConfirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RegisterNewUser(User);
                        LaunchActivityHome();
                    }
                })
                .setNegativeButton(R.string.DialogCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
        Log.d("Choice", String.valueOf(choice[0]));
    }

    private void LaunchActivityHome() {
        Intent intent;
        intent = new Intent(ThisContext, ActivityHome.class);
        intent.putExtra("ClassCode", ClassCode);
        intent.putExtra("UserHandle", UserHandle);
        startActivity(intent);
    }
}
