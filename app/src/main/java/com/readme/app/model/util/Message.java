package com.readme.app.model.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

import com.readme.app.R;

public final class Message {

    private Message() {
    }

    public static void show(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static AlertDialog.Builder showConfirmation(Context context, String title, String message, DialogInterface.OnClickListener onPositiveClickListener, EditText editText) {
        return new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setView(editText)
            .setPositiveButton(context.getString(R.string.dialog_button_confirm), onPositiveClickListener)
            .setNegativeButton(context.getString(R.string.dialog_button_cancel), null);
    }
}
