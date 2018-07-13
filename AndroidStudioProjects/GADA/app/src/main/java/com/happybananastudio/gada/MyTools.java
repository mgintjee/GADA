package com.happybananastudio.gada;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by mgint on 7/10/2018.
 */

class MyTools {

    static boolean StringIsAlphanumericAndLength(String S, int Min, int Max) {
        return StringNotEmpty(S) && IsAlphanumeric(S) && StringAtLeastCertainLength(S, Min) && StringAtMostCertainLength(S, Max);
    }

    static boolean StringNotEmpty(String S) {
        return S != null && !S.equals("");
    }

    static boolean IsAlphanumeric(String S) {
        boolean Flag;
        String pattern = "[^a-zA-Z0-9]";
        Pattern p = Pattern.compile(pattern);
        Flag = p.matcher(S).find();
        return !Flag;
    }

    private static boolean StringAtLeastCertainLength(String S, int L) {
        return S.length() >= L;
    }

    private static boolean StringAtMostCertainLength(String S, int L) {
        return S.length() <= L;
    }

    static void DialogSimple(Context ThisContext, String title, String message) {
        AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ThisContext, R.style.AlertDialogStyle);
        } else {
            builder = new AlertDialog.Builder(ThisContext);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.DialogClose, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    static String CapitalizeFirstLetterOfWord(final String word) {
        String temp = word.toLowerCase();
        return Character.toUpperCase(temp.charAt(0)) + temp.substring(1);
    }

    static String GetFormattedCurrentDate() {
        Date CurrentDate = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String FormattedCurrentDate = df.format(CurrentDate);
        System.out.println("Date is " + FormattedCurrentDate);
        return FormattedCurrentDate;
    }

    static String GetUserID(String ClassCode, String UserHandle) {
        FirebaseDatabase Firebase = FirebaseDatabase.getInstance();
        DatabaseReference ClassCodeDatabase = Firebase.getReference().child("");
        return "";
    }

    static void DialogSignInError(Context ThisContext, String DialogMessage) {
        String DialogTitle = "Sign-In Error";
        DialogSimple(ThisContext, DialogTitle, DialogMessage);
    }

    static String GetUserHandle() {
        return "";
    }

    static boolean ClassCodeExists(final String ClassCode) {
        final boolean[] Flag = new boolean[1];
        FirebaseDatabase Firebase = FirebaseDatabase.getInstance();
        final DatabaseReference ClassCodesDatabase = Firebase.getReference().child("Class Codes");
        ClassCodesDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Flag[0] = dataSnapshot.hasChild(ClassCode);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return Flag[0];
    }
}
