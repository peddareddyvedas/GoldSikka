package com.goldsikka.goldsikka.Utils;

import android.content.Context;

import com.goldsikka.goldsikka.R;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;





public class ErrorSnackBar {

    public static void onBackExit(Context context, View view) {
        Snackbar.make(view, context.getString(R.string.press_again_to_exit), Snackbar.LENGTH_LONG).show();
    }

    public static void onInternetConnection(Context context, View view) {
        Snackbar.make(view, context.getString(R.string.error_no_internet_connection), Snackbar.LENGTH_LONG).show();
    }

    public static void onConnectionFailed(Context context, View view) {
        Snackbar.make(view, context.getString(R.string.error_connection_request_failed), Snackbar.LENGTH_LONG).show();
    }

    public static void onInvalidJson(Context context, View view) {
        Snackbar.make(view, context.getString(R.string.error_invalid_data), Snackbar.LENGTH_LONG).show();
    }

    public static void onCustomMessage(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }
}
