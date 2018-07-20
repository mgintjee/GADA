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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import static com.happybananastudio.gada.MyTools.UserTeamTitle;
import static com.happybananastudio.gada.MyTools.UserTypeTitle;

/**
 * Created by mgint on 7/10/2018.
 */

public class ActivityUserProfile extends AppCompatActivity {
    Context ThisContext;
    private String ClassCode, ActiveUserID;
    private String UserID, UserHandle, CreatedOn, Password, UserName, UserTeam, UserType;
    private String NewUserHandle, NewPassword, NewUserName, NewUserTeam, NewUserType;
    private boolean EditEnabled = false;
    private boolean EnableEditPassword = false, EnableEditTeamAndType = false;
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
    }

    // Extracting the Info given from the intent and stored in the database
    private void ExtractInformation() {
        ExtractIntentInformation();
        ExtractEditingPower();
    }

    private void ExtractIntentInformation() {
        ClassCode = getIntent().getStringExtra("ClassCode");
        UserID = getIntent().getStringExtra("UserID");
        ActiveUserID = getIntent().getStringExtra("ActiveUserID");
    }

    private void ExtractEditingPower() {
        final boolean UserSame = ActiveUserID.equals(UserID);
        final DatabaseReference ActiveUserDatabase = Database.child("ClassCodes").child(ClassCode).child("ClassList").child(ActiveUserID);
        ActiveUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ActiveUserType = (String) dataSnapshot.child("UserType").getValue();
                boolean UserAdmin = ActiveUserType != null && ActiveUserType.equals("2");
                EnableEditPassword = UserSame;
                EnableEditTeamAndType = UserAdmin;
                if (UserAdmin || UserSame) {
                    EnableViewingProfilePassword();
                } else {
                    DisableViewingProfilePassword();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void EnableViewingProfilePassword() {
        LinearLayout LL = findViewById(R.id.ProfileLL_EditPassword);
        Space S = findViewById(R.id.Profile_Spacer);
        Button B = findViewById(R.id.ProfileB_Edit);

        LL.setVisibility(View.VISIBLE);
        S.setVisibility(View.GONE);
        B.setVisibility(View.VISIBLE);
    }

    private void DisableViewingProfilePassword() {
        LinearLayout LL = findViewById(R.id.ProfileLL_EditPassword);
        Space S = findViewById(R.id.Profile_Spacer);
        Button B = findViewById(R.id.ProfileB_Edit);

        LL.setVisibility(View.GONE);
        S.setVisibility(View.VISIBLE);
        B.setVisibility(View.GONE);
    }

    // Handling Editing Features
    private void EnableButtonSaveChanges() {
        LinearLayout LL = findViewById(R.id.ProfileLL_SaveChanges);
        LL.setVisibility(View.VISIBLE);
    }

    private void DisableButtonSaveChanges() {
        LinearLayout LL = findViewById(R.id.ProfileLL_SaveChanges);
        LL.setVisibility(View.GONE);
    }

    private void EnableEditTexts() {
        EnableEditTextUserName();
        EnableEditTextUserHandle();
        if (EnableEditPassword) {
            EnableEditTextPassword();
        }
    }

    private void DisableEditTexts() {
        DisableEditTextUserName();
        DisableEditTextUserHandle();
        if (EnableEditPassword) {
            DisableEditTextPassword();
        }
    }

    private void EnableSpinners() {
        if (EnableEditTeamAndType) {
            EnableSpinnerUserTeam();
            EnableSpinnerUserType();
        }
    }

    private void DisableSpinners() {
        if (EnableEditTeamAndType) {
            DisableSpinnerUserTeam();
            DisableSpinnerUserType();
        }
    }

    private void EnableEditTextUserName() {
        EditText ET = findViewById(R.id.ProfileET_UserName);
        ET.setEnabled(true);
    }

    private void EnableEditTextUserHandle() {
        EditText ET = findViewById(R.id.ProfileET_UserHandle);
        ET.setEnabled(true);
    }

    private void EnableEditTextPassword() {
        EditText ET = findViewById(R.id.ProfileET_Password);
        ET.setEnabled(true);
    }

    private void DisableEditTextUserName() {
        EditText ET = findViewById(R.id.ProfileET_UserName);
        NewUserName = UserName;
        ET.setText(UserName);
        ET.setEnabled(false);
    }

    private void DisableEditTextUserHandle() {
        EditText ET = findViewById(R.id.ProfileET_UserHandle);
        NewUserHandle = UserHandle;
        ET.setText(UserHandle);
        ET.setEnabled(false);
    }

    private void DisableEditTextPassword() {
        EditText ET = findViewById(R.id.ProfileET_Password);
        NewPassword = Password;
        ET.setText(Password);
        ET.setEnabled(false);
    }

    private void EnableSpinnerUserTeam() {
        Spinner S = findViewById(R.id.ProfileS_UserTeam);
        S.setSelection(Integer.valueOf(UserTeam));
        S.setEnabled(true);
        S.setClickable(true);
    }

    private void EnableSpinnerUserType() {
        Spinner S = findViewById(R.id.ProfileS_UserType);
        S.setSelection(Integer.valueOf(UserType));
        S.setEnabled(true);
        S.setClickable(true);
    }

    private void DisableSpinnerUserTeam() {
        Spinner S = findViewById(R.id.ProfileS_UserTeam);
        NewUserTeam = UserTeam;
        S.setSelection(Integer.valueOf(UserTeam));
        S.setEnabled(false);
        S.setClickable(false);
    }

    private void DisableSpinnerUserType() {
        Spinner S = findViewById(R.id.ProfileS_UserType);
        NewUserType = UserType;
        S.setSelection(Integer.valueOf(UserType));
        S.setEnabled(false);
        S.setClickable(false);
    }


    private boolean ValidUserName() {
        String[] NameParts = NewUserName.split(" ");
        StringBuilder NameBuilder = new StringBuilder("");
        for (String NamePart : NameParts) {
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

    private boolean ValidInput() {
        String DialogMessage;
        boolean ValidUserHandle = StringIsAlphanumericAndLength(NewUserHandle, USER_HANDLE_MIN, USER_HANDLE_MAX);
        boolean ValidUserName = ValidUserName();
        boolean ValidPassword = StringIsAlphanumericAndLength(NewPassword, USER_PASSWORD_MIN, USER_PASSWORD_MAX);

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

        if (!ValidPassword) {
            DialogMessage = "> Password must be Alphanumeric\n> Password is Case Sensitive and \n> Password must be ( " + USER_PASSWORD_MIN + " < x < " + USER_PASSWORD_MAX + " ) characters";
            DialogSignInError(ThisContext, DialogMessage);
            return false;
        }

        return true;
    }

    // Widget Handling
    private void InitializeWidgets() {
        InitializeTextViews();
        InitializeSpinners();
        InitializeEditTexts();
        InitializeButtons();
    }

    private void InitializeTextViews() {
        InitializeTextViewClassCode();
        InitializeTextViewUserID();
        InitializeTextViewCreatedOn();
    }

    private void InitializeSpinners() {
        InitializeSpinnerUserTeam();
        InitializeSpinnerUserType();
    }

    private void InitializeButtons() {
        InitializeButtonEdit();
        InitializeButtonGoBack();
        InitializeButtonSaveChanges();
    }

    private void InitializeEditTexts() {
        InitializeEditTextUserName();
        InitializeEditTextUserHandle();
        InitializeEditTextPassword();
    }

    private void InitializeTextViewClassCode() {
        TextView TV = findViewById(R.id.ProfileTV_ClassCode);
        TV.setText(ClassCode);
    }

    private void InitializeTextViewUserID() {
        TextView TV = findViewById(R.id.ProfileTV_UserID);
        TV.setText(UserID);
    }

    private void InitializeTextViewCreatedOn() {
        final TextView TV = findViewById(R.id.ProfileTV_CreatedOn);
        final DatabaseReference UserDatabase = Database.child("ClassCodes").child(ClassCode).child("ClassList").child(UserID);
        UserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CreatedOn = (String) dataSnapshot.child("CreatedOn").getValue();
                TV.setText(CreatedOn);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void InitializeSpinnerUserTeam() {
        final Spinner S = findViewById(R.id.ProfileS_UserTeam);
        final String[] TeamArray = getResources().getStringArray(R.array.SpinnerArrayUserTeam);
        final DatabaseReference UserDatabase = Database.child("ClassCodes").child(ClassCode).child("ClassList").child(UserID);
        UserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserTeam = (String) dataSnapshot.child("UserTeam").getValue();
                int IntUserTeam = Integer.valueOf(UserTeam);
                S.setAdapter(new ArrayAdapter<>(ThisContext, R.layout.spinner_item, TeamArray));
                S.setSelection(IntUserTeam);
                S.setClickable(false);
                S.setEnabled(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        S.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NewUserTeam = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void InitializeSpinnerUserType() {
        final Spinner S = findViewById(R.id.ProfileS_UserType);
        final String[] TypeArray = getResources().getStringArray(R.array.SpinnerArrayUserType);
        final DatabaseReference UserDatabase = Database.child("ClassCodes").child(ClassCode).child("ClassList").child(UserID);
        UserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserType = (String) dataSnapshot.child("UserType").getValue();
                int IntUserType = Integer.valueOf(UserType);
                S.setAdapter(new ArrayAdapter<>(ThisContext, R.layout.spinner_item, TypeArray));
                S.setSelection(IntUserType);
                S.setClickable(false);
                S.setEnabled(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        S.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NewUserType = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void InitializeButtonGoBack() {
        Button B = findViewById(R.id.ProfileB_GoBack);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void InitializeButtonEdit() {
        final Button B = findViewById(R.id.ProfileB_Edit);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PreType", UserType);
                Log.d("PreTeam", UserTeam);
                Log.d("PreEditEnabled", String.valueOf(EditEnabled));
                if (EditEnabled) {
                    EditEnabled = false;
                    DisableEditTexts();
                    DisableSpinners();
                    DisableButtonSaveChanges();
                    B.setText(R.string.ProfileButtonEnableEdit);
                } else {
                    EditEnabled = true;
                    EnableEditTexts();
                    EnableSpinners();
                    EnableButtonSaveChanges();
                    B.setText(R.string.ProfileButtonDisableEdit);
                }
                Log.d("PostType", UserType);
                Log.d("PostTeam", UserTeam);
                Log.d("PostEditEnabled", String.valueOf(EditEnabled));
            }
        });
    }

    private void InitializeButtonSaveChanges() {
        Button B = findViewById(R.id.ProfileB_SaveChanges);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidInput()) {
                    ClassUser User = new ClassUser(CreatedOn, NewPassword, NewUserHandle, NewUserName, NewUserTeam, NewUserType);
                    String DialogTitle = "Is the Following Correct?";
                    String DialogMessage = "\n> User Name: " + User.UserName + "\n> User Handle: " + User.UserHandle + "\n> Password: " + User.Password + "\n>UserTeam: " + UserTeamTitle(User.UserTeam) + "\n>UserType: " + UserTypeTitle(User.UserType);
                    DialogChoiceNewUser(ThisContext, DialogTitle, DialogMessage, User);
                }
            }
        });
    }

    private void InitializeEditTextUserName() {
        final EditText ET = findViewById(R.id.ProfileET_UserName);
        final DatabaseReference UserDatabase = Database.child("ClassCodes").child(ClassCode).child("ClassList").child(UserID);
        UserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserName = (String) dataSnapshot.child("UserName").getValue();
                NewUserName = UserName;
                ET.setText(UserName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                NewUserName = s.toString().toLowerCase();
            }
        });
    }

    private void InitializeEditTextUserHandle() {
        final EditText ET = findViewById(R.id.ProfileET_UserHandle);
        final DatabaseReference UserDatabase = Database.child("ClassCodes").child(ClassCode).child("ClassList").child(UserID);
        UserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserHandle = (String) dataSnapshot.child("UserHandle").getValue();
                NewUserHandle = UserHandle;
                ET.setText(UserHandle);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                NewUserHandle = s.toString().toLowerCase();
            }
        });
    }

    private void InitializeEditTextPassword() {
        final EditText ET = findViewById(R.id.ProfileET_Password);
        final DatabaseReference UserDatabase = Database.child("ClassCodes").child(ClassCode).child("ClassList").child(UserID);
        UserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Password = (String) dataSnapshot.child("Password").getValue();
                NewPassword = Password;
                ET.setText(Password);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
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
                        UpdateUserInfo(User);
                    }
                })
                .setNegativeButton(R.string.DialogCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void UpdateUserInfo(ClassUser User) {
        DatabaseReference UserDatabase = Database.child("ClassCodes").child(ClassCode).child("ClassList").child(UserID);
        UserDatabase.setValue(User);
        UserName = NewUserName;
        UserHandle = NewUserHandle;
        Password = NewPassword;
        UserTeam = NewUserTeam;
        UserType = NewUserType;
    }
}
