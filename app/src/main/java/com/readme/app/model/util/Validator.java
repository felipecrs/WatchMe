package com.readme.app.model.util;

import android.text.TextUtils;
import android.util.Patterns;

public final class Validator {
    private Validator() {
    }

    public static boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public static boolean isTitleValid(String title){
        return title.length() > 2;
    }

    public static boolean isNameValid(String name){
        return name.length() > 2;
    }

    public static boolean isTotalPagesValid(int totalPages){
        return totalPages > 0 && totalPages < 99999;
    }

    public static boolean isActualPageValid(Integer actualPage, Integer totalPages) {
        return actualPage >= 0 && actualPage <= totalPages;
    }
}
