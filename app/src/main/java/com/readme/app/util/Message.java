package com.readme.app.util;

import android.content.Context;
import android.widget.Toast;

public final class Message {

    private Message() {
    }

    public static void show(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
