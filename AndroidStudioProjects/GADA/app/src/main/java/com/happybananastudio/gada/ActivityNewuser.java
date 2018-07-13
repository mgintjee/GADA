package com.happybananastudio.gada;

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
import static com.happybananastudio.gada.MyTools.DialogSignInError;
import static com.happybananastudio.gada.MyTools.StringIsAlphanumericAndLength;

public class ActivityNewUser extends AppCompatActivity {

    Context ThisContext;
    private DatabaseReference Database;
    private String ClassCode = "";
    private int UserID = -1;
    private String UserHandle = "";
    private String UserName = "";
    private String Password1 = "";
    private String Password2 = "";
    private String UserType = "0";

    // Global Bounds
    private int CLASS_CODE_MIN = 5;
    private int CLASS_CODE_MAX = 10;
    private int USER_HANDLE_MIN = 5;
    private int USER_HANDLE_MAX = 25;
    private int USER_NAME_MIN = 1;
    private int USER_NAME_MAX = 15;
    private int USER_PASSWORD_MIN = 8;
    private int USER_PASSWORD_MAX = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThisContext = this;
        setContentView(R.layout.activity_user_new);
        FirebaseApp.initializeApp(ThisContext);
        Database = FirebaseDatabase.getInstance().getReference();
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
                finish();
            }
        });
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
                FB_GetNewUserID();
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
        EditText ET = findViewById(R.id.NewUserET_UserHandle);
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
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }

    private void CheckBoxSamePassword() {
        CheckBox CB = findViewById(R.id.NewUserCB_SamePassword);
        boolean MatchingPasswords = !Password1.equals("") && Password1.equals(Password2);
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

    private boolean ValidUserName() {
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

    private void InitializeButtonSignIn() {

        Button B = findViewById(R.id.NewUserB_SignIn);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidInput()) {
                    String CreatedOn = MyTools.GetFormattedCurrentDate();
                    ClassUser User = new ClassUser(CreatedOn, Password1, UserHandle, UserName, UserType);
                    FB_SigningIntoClass(User);
                }
            }
        });
    }

    private void FB_SigningIntoClass(final ClassUser User) {
        final DatabaseReference ClassCodesDatabase = Database.child("ClassCodes");
        ClassCodesDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(ClassCode)) {
                    // Existing ClassCode
                    String DialogTitle = "Class Code: " + ClassCode + " Doesn\'t Exist";
                    String DialogMessage = "Would you like to proceed and create this new class code?";
                    DialogChoiceNewClass(ThisContext, DialogTitle, DialogMessage, User);
                } else {
                    // Existing ClassCode
                    String DialogTitle = "Is the Following Correct?";
                    String DialogMessage = "> Class Code: " + ClassCode + "\n> User Name: " + User.UserName + "\n> User Handle: " + User.UserHandle + "\n> Password: " + User.Password + "\n> User Type: " + User.UserType;
                    DialogChoiceNewUser(ThisContext, DialogTitle, DialogMessage, User);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void FB_GetNewUserID() {
        final DatabaseReference ClassDatabase = Database.child("ClassCodes").child(ClassCode);
        ClassDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Class List")) {
                    // There is a class listing for this class code
                    Log.d("DEBUG", "Class List Exists");
                    FB_GetNextAvailableUserID();
                } else {
                    Log.d("DEBUG", "Class List Does Not Exists");
                    UserID = 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void FB_GetNextAvailableUserID() {
        final DatabaseReference ClassListDatabase = Database.child("ClassCodes").child(ClassCode).child("ClassList");
        ClassListDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserID = 1;
                for (DataSnapshot DS_Child : dataSnapshot.getChildren()) {
                    int DS_Key = Integer.valueOf(DS_Child.getKey());
                    if (UserID != DS_Key) {
                        break;
                    } else {
                        UserID++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private boolean ValidInput() {
        String DialogMessage;
        boolean ValidClassCode = StringIsAlphanumericAndLength(ClassCode, CLASS_CODE_MIN, CLASS_CODE_MAX);
        boolean ValidUserHandle = StringIsAlphanumericAndLength(UserHandle, USER_HANDLE_MIN, USER_HANDLE_MAX);
        boolean ValidUserName = ValidUserName();
        boolean ValidPassword1 = StringIsAlphanumericAndLength(Password1, USER_PASSWORD_MIN, USER_PASSWORD_MAX);
        boolean MatchingPasswords = !Password1.equals("") && Password1.equals(Password2);


        if (!ValidClassCode) {
            DialogMessage = "> Class Code must be Alphanumeric\n> Class Code is not Case Sensitive\n> Class Code must be ( " + CLASS_CODE_MIN + " < x < " + CLASS_CODE_MAX + " ) characters";
            DialogSignInError(ThisContext, DialogMessage);
            return false;
        }

        if (!ValidUserName) {
            DialogMessage = "> User Name must be Alphanumeric\n> User Name must be ( " + USER_NAME_MIN + " < x < " + USER_NAME_MAX + " ) characters per word";
            DialogSignInError(ThisContext, DialogMessage);
            return false;
        }

        if (!ValidUserHandle) {
            DialogMessage = "> User Handle must be Alphanumeric\n> User Handle is not Case Sensitive\n> User Handle must be ( " + USER_HANDLE_MIN + " < x < " + USER_HANDLE_MAX + " ) characters";
            DialogSignInError(ThisContext, DialogMessage);
            return false;
        }

        if (!MatchingPasswords) {
            DialogMessage = "> Passwords must be the same";
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

    private void FB_CreateNewClassCode() {
        DatabaseReference ClassCodesDatabase = Database.child("ClassCodes");
        String CurrentDate = MyTools.GetFormattedCurrentDate();
        ClassCodesDatabase.child(ClassCode).child("Created On").setValue(CurrentDate);
    }

    private void RegisterNewUser(ClassUser User) {

        Log.d("Registering", User.UserHandle);
        String StringUserID = String.valueOf(UserID);
        DatabaseReference UserDatabase = Database.child("ClassCodes").child(ClassCode).child("ClassList").child(StringUserID);
        UserDatabase.setValue(User);
    }

    void DialogChoiceNewClass(final Context ThisContext, String title, String message, final ClassUser User) {
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
                        FB_CreateNewClassCode();
                        User.UserType = "2";
                        String DialogTitle = "Is the Following Correct?";
                        String DialogMessage = "> Class Code: " + ClassCode + "\n> User Name: " + User.UserName + "\n> User Handle: " + User.UserHandle + "\n> Password: " + User.Password + "\n> User Type: " + User.UserType;
                        DialogChoiceNewUser(ThisContext, DialogTitle, DialogMessage, User);

                    }
                })
                .setNegativeButton(R.string.DialogCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    void DialogChoiceNewUser(Context ThisContext, String title, String message, final ClassUser User) {
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
    }

    private void LaunchActivityHome() {
        Intent intent;
        intent = new Intent(ThisContext, ActivityHome.class);
        intent.putExtra("ClassCode", ClassCode);
        intent.putExtra("UserID", UserID);
        startActivity(intent);
    }
}