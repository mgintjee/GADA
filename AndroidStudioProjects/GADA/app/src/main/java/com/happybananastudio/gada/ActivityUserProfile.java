package com.happybananastudio.gada;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.happybananastudio.gada.MyTools.CapitalizeFirstLetterOfWord;
import static com.happybananastudio.gada.MyTools.DialogSignInError;
import static com.happybananastudio.gada.MyTools.StringIsAlphanumericAndLength;

/**
 * Created by mgint on 7/10/2018.
 */

public class ActivityUserProfile extends AppCompatActivity {
    Context ThisContext;
    private String ClassCode, ActiveUserID;
    private String UserID, UserHandle, CreatedOn, Password1, Password2, UserName, UserTeam, UserType;
    private String NewUserHandle, NewPassword, NewUserName;
    private boolean EditEnabled = false;
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
        Database = FirebaseDatabase.getInstance().getReference();
        ExtractInformation();
        InitializeWidgets();
        HandleEditingFeature();
    }

    private void ExtractInformation() {
        ExtractIntentInformation();
        ExtractCreatedOn();
    }

    private void ExtractIntentInformation() {
        ClassCode = getIntent().getStringExtra("ClassCode");
        UserID = getIntent().getStringExtra("UserID");
        ActiveUserID = getIntent().getStringExtra("ActiveUserID");
    }

    private void InitializeWidgets(){
        InitializeTextViews();
        InitializeSpinners();
        InitializeEditTexts();
        InitializeButtons();
        InitializeCheckBoxes();
    }
    private void InitializeTextViews(){
        InitializeTextViewClassCode();
        InitializeTextViewUserID();
    }

    private void InitializeSpinners() {
        InitializeSpinnerUserTeam();
        InitializeSpinnerUserType();
    }

    private void InitializeButtons(){
        InitializeButtonEdit();
        InitializeButtonGoBack();
        InitializeButtonSaveChanges();
    }

    private void InitializeEditTexts() {
        InitializeEditTextUserName();
        InitializeEditTextUserHandle();
        InitializeEditTextPassword();
    }

    private void InitializeCheckBoxes() {
        InitializeCheckBoxShowPassword();
        InitializeCheckBoxSamePassword();
    }

    private void InitializeTextViewClassCode(){
        TextView TV = findViewById(R.id.ProfileTV_ClassCode);
        TV.setText(ClassCode);
    }

    private void InitializeTextViewUserID() {
        TextView TV = findViewById(R.id.ProfileTV_UserID);
        TV.setText(UserID);
    }

    private void ExtractCreatedOn() {
        final DatabaseReference UserDatabase = Database.child("ClassCodes").child(ClassCode).child("ClassList").child(UserID);
        UserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CreatedOn = (String) dataSnapshot.child("CreatedOn").getValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void InitializeSpinnerUserType() {
        final Spinner S = findViewById(R.id.ProfileS_UserTeam);
        final DatabaseReference UserDatabase = Database.child("ClassCodes").child(ClassCode).child("ClassList").child(UserID);
        UserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserTeam = (String) dataSnapshot.child("UserTeam").getValue();
                int IntUserTeam = Integer.valueOf(UserTeam);
                S.setSelection(IntUserTeam);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void InitializeSpinnerUserTeam() {
        final Spinner S = findViewById(R.id.ProfileS_UserType);
        final DatabaseReference UserDatabase = Database.child("ClassCodes").child(ClassCode).child("ClassList").child(UserID);
        UserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserType = (String) dataSnapshot.child("UserType").getValue();
                int IntUserType = Integer.valueOf(UserType);
                S.setSelection(IntUserType);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void InitializeEditTextUserName() {
        final EditText ET_Name = findViewById(R.id.ProfileET_UserName);
        final DatabaseReference UserDatabase = Database.child("ClassCodes").child(ClassCode).child("ClassList").child(UserID);
        UserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserName = (String) dataSnapshot.child("UserName").getValue();
                ET_Name.setText(UserName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void InitializeEditTextUserHandle() {
        final EditText ET_Name = findViewById(R.id.ProfileET_UserHandle);
        final DatabaseReference UserDatabase = Database.child("ClassCodes").child(ClassCode).child("ClassList").child(UserID);
        UserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserHandle = (String) dataSnapshot.child("UserHandle").getValue();
                ET_Name.setText(UserHandle);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void InitializeEditTextPassword(){
        final EditText ET_Password1 = findViewById(R.id.ProfileET_Password1);
        final EditText ET_Password2 = findViewById(R.id.ProfileET_Password2);
        final DatabaseReference UserDatabase = Database.child("ClassCodes").child(ClassCode).child("ClassList").child(UserID);
        UserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Password1 = (String) dataSnapshot.child("Password").getValue();
                Password2 = Password1;
                ET_Password1.setText(Password1);
                ET_Password2.setText(Password1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void HandleEditingFeature() {
        final boolean UserSame = ActiveUserID.equals(UserID);
        final DatabaseReference ActiveUserDatabase = Database.child("ClassCodes").child(ClassCode).child("ClassList").child(ActiveUserID);
        ActiveUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ActiveUserType = (String) dataSnapshot.child("UserType").getValue();
                boolean UserAbleToEdit = ActiveUserType != null && ActiveUserType.equals("2");
                EditEnabled = UserSame || UserAbleToEdit;
                if (EditEnabled) {
                    EnableEditingProfile();
                } else {
                    DisableEditingProfile();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void DisableEditingProfile() {
        LinearLayout LL = findViewById(R.id.ProfileLL_Edit);
        Space S = findViewById(R.id.Profile_Spacer);
        Button B = findViewById(R.id.ProfileB_Edit);

        LL.setVisibility(View.GONE);
        S.setVisibility(View.VISIBLE);
        B.setVisibility(View.GONE);
    }

    private void EnableEditingProfile() {
        LinearLayout LL = findViewById(R.id.ProfileLL_Edit);
        Space S = findViewById(R.id.Profile_Spacer);
        Button B = findViewById(R.id.ProfileB_Edit);

        LL.setVisibility(View.VISIBLE);
        S.setVisibility(View.GONE);
        B.setVisibility(View.VISIBLE);
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
        EditText ET_Password = findViewById(R.id.ProfileET_Password2);

        ET_Name.setEnabled(false);
        ET_Handle.setEnabled(false);
        ET_Password.setEnabled(false);

        NewUserHandle = UserHandle;
        NewUserName = UserName;

        ET_Name.setText(UserName);
        ET_Handle.setText(UserHandle);

        Button B_Save = findViewById(R.id.ProfileB_SaveChanges);
        B_Save.setVisibility(View.INVISIBLE);
    }
    private void EnableEditingOnEditTexts(){
        EditText ET_Name = findViewById(R.id.ProfileET_UserName);
        EditText ET_Handle = findViewById(R.id.ProfileET_UserHandle);
        EditText ET_Password = findViewById(R.id.ProfileET_Password1);

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
                //if(!UserHandle.equals(NewUserHandle) || !UserName.equals(NewUserName) || !Password.equals(NewPassword)){ }
            }
        });
    }

    private void InitializeCheckBoxSamePassword() {
        CheckBox CB = findViewById(R.id.ProfileCB_SamePassword);
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

    private void InitializeCheckBoxShowPassword() {

        final EditText ET1 = findViewById(R.id.ProfileET_Password1);
        final EditText ET2 = findViewById(R.id.ProfileET_Password2);
        CheckBox CB = findViewById(R.id.ProfileCB_ShowPassword);
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

    private boolean ValidUserName() {
        String[] NameParts = UserName.split(" ");
        StringBuilder NameBuilder = new StringBuilder("");
        for (String NamePart : NameParts) {
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

    private boolean ValidInput() {
        String DialogMessage;
        boolean ValidUserHandle = StringIsAlphanumericAndLength(UserHandle, USER_HANDLE_MIN, USER_HANDLE_MAX);
        boolean ValidUserName = ValidUserName();
        boolean ValidPassword1 = StringIsAlphanumericAndLength(Password1, USER_PASSWORD_MIN, USER_PASSWORD_MAX);
        boolean MatchingPasswords = !Password1.equals("") && Password1.equals(Password2);

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

}
