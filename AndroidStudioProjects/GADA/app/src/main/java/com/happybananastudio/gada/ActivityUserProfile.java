package com.happybananastudio.gada;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.happybananastudio.gada.MyTools.CapitalizeFirstLetterOfWord;
import static com.happybananastudio.gada.MyTools.DialogSimple;
import static com.happybananastudio.gada.MyTools.StringIsAlphanumericAndLength;

/**
 * Created by mgint on 7/10/2018.
 */

public class ActivityUserProfile extends AppCompatActivity {
    Context ThisContext;
    private String ClassCode, UserHandle, UserType;
    private String UserHandleToView, CreatedOn, Password, Speech, UserName, UserTypeToView;
    private String NewUserHandle, NewPassword, NewUserName;
    private boolean EditEnabled = false;
    private boolean UserHandleExists = false;
    private FirebaseDatabase FireBase;
    private DatabaseReference Database;

    private int USER_NAME_MIN = 2;
    private int USER_NAME_MAX = 15;
    private int USER_PASSWORD_MIN = 8;
    private int USER_PASSWORD_MAX = 20;
    private int USER_HANDLE_MIN = 5;
    private int USER_HANDLE_MAX = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ThisContext = this;
        FireBase = FirebaseDatabase.getInstance();
        Database = FireBase.getReference();
        ExtractIntentInformation();
    }

    private void InitializeWidgets(){
        InitializeTextViews();
        InitializeButtons();
        InitializeEditTexts();
    }
    private void InitializeTextViews(){
        InitializeTextViewClassCode();
        InitializeTextViewUserType();
    }

    private void InitializeButtons(){
        InitializeButtonEdit();
        InitializeButtonGoBack();
        InitializeButtonSaveChanges();
    }

    private void InitializeTextViewClassCode(){
        TextView TV = findViewById(R.id.ProfileTV_ClassCode);
        TV.setText(ClassCode);
    }

    private void InitializeTextViewUserType(){
        TextView TV = findViewById(R.id.ProfileTV_UserType);
        String UserTypeAsString = UserTypeAsString(UserTypeToView);
        TV.setText(UserTypeAsString);
    }

    private void InitializeEditTexts(){
        InitializeEditTextUserName();
        InitializeEditTextUserHandle();
        InitializeEditTextPassword();
    }
    private void InitializeEditTextUserName(){
        EditText ET_Name = findViewById(R.id.ProfileET_UserName);
        ET_Name.setText(UserName);
        ET_Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NewUserName = s.toString().toLowerCase();
                ValidName();
            }

            @Override
            public void afterTextChanged(Editable s) {
                NewUserName = s.toString().toLowerCase();
                ValidName();
            }
        });
    }
    private void InitializeEditTextUserHandle(){
        EditText ET_Name = findViewById(R.id.ProfileET_UserHandle);
        ET_Name.setText(UserHandleToView);
        ET_Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NewUserHandle = s.toString().toLowerCase();
                CheckIfUserCredentialsExist();
            }

            @Override
            public void afterTextChanged(Editable s) {
                NewUserHandle = s.toString().toLowerCase();
                CheckIfUserCredentialsExist();
            }
        });
    }
    private void InitializeEditTextPassword(){
        EditText ET_Name = findViewById(R.id.ProfileET_Password);
        ET_Name.setText(Password);
        ET_Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NewPassword = s.toString().toLowerCase();
            }

            @Override
            public void afterTextChanged(Editable s) {
                NewPassword = s.toString().toLowerCase();
            }
        });
    }

    private void InitializeButtonEdit(){
        final Button B = findViewById(R.id.ProfileB_Edit);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EditEnabled){
                    EditEnabled = false;
                    DisableEditingOnEditTexts();
                    B.setText(R.string.ProfileButtonEnableEdit);
                }
                else{
                    EditEnabled = true;
                    EnableEditingOnEditTexts();
                    B.setText(R.string.ProfileButtonDisableEdit);
                }
            }
        });
    }

    private void DisableEditingOnEditTexts(){
        EditText ET_Name = findViewById(R.id.ProfileET_UserName);
        EditText ET_Handle = findViewById(R.id.ProfileET_UserHandle);
        EditText ET_Password = findViewById(R.id.ProfileET_Password);

        ET_Name.setEnabled(false);
        ET_Handle.setEnabled(false);
        ET_Password.setEnabled(false);

        NewPassword = Password;
        NewUserHandle = UserHandle;
        NewUserName = UserName;

        ET_Name.setText(UserName);
        ET_Handle.setText(UserHandle);
        ET_Password.setText(Password);

        Button B_Save = findViewById(R.id.ProfileB_SaveChanges);
        B_Save.setVisibility(View.INVISIBLE);
    }
    private void EnableEditingOnEditTexts(){
        EditText ET_Name = findViewById(R.id.ProfileET_UserName);
        EditText ET_Handle = findViewById(R.id.ProfileET_UserHandle);
        EditText ET_Password = findViewById(R.id.ProfileET_Password);

        ET_Name.setEnabled(true);
        ET_Handle.setEnabled(true);
        ET_Password.setEnabled(true);

        Button B_Save = findViewById(R.id.ProfileB_SaveChanges);
        B_Save.setVisibility(View.VISIBLE);
    }

    private void InitializeButtonGoBack(){
        Button B = findViewById(R.id.ProfileB_GoBack);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void InitializeButtonSaveChanges(){
        Button B = findViewById(R.id.ProfileB_SaveChanges);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!UserHandle.equals(NewUserHandle) || !UserName.equals(NewUserName) || !Password.equals(NewPassword)){
                    GateUserName();
                }
            }
        });
    }

    private void ExtractIntentInformation(){
        ClassCode = getIntent().getStringExtra("ClassCode");
        UserHandleToView = getIntent().getStringExtra("UserHandleToView");
        UserHandle = getIntent().getStringExtra("UserHandle");
        RetrieveUserInfoFromDatabase();
        HandleWhatToDisplay();
    }

    private void HandleWhatToDisplay(){
        LinearLayout LL_Password = findViewById(R.id.ProfileLL_SelfProfilePassword);
        LinearLayout LL_Mods = findViewById(R.id.ProfileLL_SelfProfileMods);
        if(UserHandle.equals(UserHandleToView)){
            // Viewing Self
            LL_Password.setVisibility(View.VISIBLE);
            LL_Mods.setVisibility(View.VISIBLE);
        }
        else{
            // Viewing Other
            LL_Password.setVisibility(View.GONE);
            LL_Mods.setVisibility(View.GONE);
        }
    }

    private void RetrieveUserInfoFromDatabase(){
        final DatabaseReference UserInfoDatabase = Database.child(ClassCode).child(UserHandleToView);
        UserInfoDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CreatedOn = (String) dataSnapshot.child("Created on").getValue();
                Password = (String) dataSnapshot.child("Password").getValue();
                Speech = (String) dataSnapshot.child("Speech").getValue();
                UserName = (String) dataSnapshot.child("User Name").getValue();
                UserTypeToView = (String) dataSnapshot.child("User Type").getValue();

                NewPassword = Password;
                NewUserHandle = UserHandleToView;
                NewUserName = UserName;

                InitializeWidgets();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void CheckIfUserCredentialsExist() {
        DatabaseReference ClassCodeDatabase = Database.child(ClassCode);
        ClassCodeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserHandleExists = dataSnapshot.hasChild(NewUserHandle);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private String UserTypeAsString(String S){
        if(S!= null) {
            switch (S) {
                case "0":
                    return "User";
                case "1":
                    return "Moderator";
                case "2":
                    return "Coach";
                default:
                    return "Error Loading";
            }
        }
        else{
            return "Error Loading";
        }
    }
    private void GateUserName() {
        String DialogTitle = "Sign-In Error";
        String DialogMessage;

        boolean SameUserName = UserName.equals(NewUserName);
        if (SameUserName || ValidName()) {
            GatePassword();
        } else {
            DialogMessage = "> User Name must be Alphanumeric\n> User Name must be ( " + USER_NAME_MIN + " < x < " + USER_NAME_MAX + " ) characters";
            DialogSimple(ThisContext, DialogTitle, DialogMessage);
        }
    }

    private void GatePassword(){
        String DialogTitle = "Sign-In Error";
        String DialogMessage;

        boolean ValidPassword = StringIsAlphanumericAndLength(UserHandle, USER_HANDLE_MIN, USER_HANDLE_MAX);

        boolean SamePassword = Password.equals(NewPassword);
        if (SamePassword || ValidPassword) {
            GateUserHandle();
        } else {
            DialogMessage = "> Password is not Case Sensitive\n> Password must be ( " + USER_HANDLE_MIN + " < x < " + USER_HANDLE_MAX + " ) characters";
            DialogSimple(ThisContext, DialogTitle, DialogMessage);
        }

    }

    private void GateUserHandle(){

        String DialogTitle = "Sign-In Error";
        String DialogMessage;
        boolean ValidUserHandle = StringIsAlphanumericAndLength(UserHandle, USER_HANDLE_MIN, USER_HANDLE_MAX);
        boolean SameUserHandle = UserHandleToView.equals(NewUserHandle);
        if (SameUserHandle || ValidUserHandle) {
            if (SameUserHandle || !UserHandleExists) {
                SignInSuccess();
            } else {
                DialogMessage = "> User Handle is Already Taken";
                DialogSimple(ThisContext, DialogTitle, DialogMessage);
            }
        }
        else{

            DialogMessage = "> User Handle is not Case Sensitive\n> User Handle must be ( " + USER_HANDLE_MIN + " < x < " + USER_HANDLE_MAX + " ) characters";
            DialogSimple(ThisContext, DialogTitle, DialogMessage);
        }
    }

    private void SignInSuccess(){
        String DialogTitle = "Are You Sure You Want To Save These Changes";
        String DialogMessage = "> User Name: " + NewUserName + "\n> User Handle: " + NewUserHandle + "\n> Password: " + NewPassword;
        DialogConfirmCancel(ThisContext, DialogTitle, DialogMessage);
    }

    private boolean ValidName() {
        String[] NameParts = NewUserName.split(" ");
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
        NewUserName = NameBuilder.toString();
        return true;
    }

    void DialogConfirmCancel(Context ThisContext, String title, String message) {
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
                        HandleSavingNewUserInfo();
                    }
                })
                .setNegativeButton(R.string.DialogCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void HandleSavingNewUserInfo(){

        DatabaseReference ClassCodeDatabase = Database.child(ClassCode);
        if(UserHandleToView.equals(NewUserHandle)){
            // just update old entry
            DatabaseReference UserInfoDatabase = ClassCodeDatabase.child(UserHandleToView);
            if(!Password.equals(NewPassword)){
                // Update the password if the new password is different
                UserInfoDatabase.child("Password").setValue(NewPassword);
            }
            if(!UserName.equals(NewUserName)){
                // Update the user name if the new user name is different
                UserInfoDatabase.child("User Name").setValue(NewUserName);
            }
        }
        else{
            // Have to make new entry and delete old
            DatabaseReference NewUserInfoDatabase = ClassCodeDatabase.child(NewUserHandle);
            NewUserInfoDatabase.child("User Name").setValue(NewUserName);
            NewUserInfoDatabase.child("Password").setValue(NewPassword);
            NewUserInfoDatabase.child("User Type").setValue(UserTypeToView);
            NewUserInfoDatabase.child("Created on").setValue(CreatedOn);
            NewUserInfoDatabase.child("Speech").setValue(Speech);

            DatabaseReference OldUserInfoDatabase = ClassCodeDatabase.child(UserHandleToView);
            OldUserInfoDatabase.removeValue();
        }

        // update this activity with the "new" handle, name, and password
        UserHandleToView = NewUserHandle;
        UserName = NewUserName;
        Password = NewPassword;
    }
}
