package com.readme.app.model.util;

public final class Validation {
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
        return totalPages > 0 && totalPages < 99999;
    }

    public static boolean isActualPageValid(Integer actualPage, Integer totalPages) {
        return actualPage >= 0 && actualPage <= totalPages;
    }
}
