package com.happybananastudio.gada;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;

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
}
