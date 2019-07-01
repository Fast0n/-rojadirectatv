package com.fast0n.rojadirectatvapp.java;

import android.content.Context;

import com.fast0n.rojadirectatvapp.R;
import com.google.android.material.snackbar.Snackbar;

public class SnackbarHelper {

    public static void configSnackbar(Context context, Snackbar snack) {
        setRoundBordersBg(context, snack);
    }


    private static void setRoundBordersBg(Context context, Snackbar snackbar) {
        snackbar.getView().setBackground(context.getDrawable(R.drawable.bg_snackbar));
    }
}