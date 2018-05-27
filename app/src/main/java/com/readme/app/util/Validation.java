package com.readme.app.util;

public class Validation {
    private Validation() {
    }

    public static boolean isEmailValid(String email) {
        return email.contains("@");
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
        return totalPages > 10 && totalPages < 99999;
    }

}
