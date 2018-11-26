package com.readme.app.model.entity;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

public class UserWithEmails {
    @Embedded
    private User user = new User();

    @Relation(entity = Email.class, parentColumn = User.ID, entityColumn = Email.USER_ID, projection = Email.ADDRESS)
    private List<String> emailAdresses = new ArrayList<>();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getEmailAdresses() {
        return emailAdresses;
    }

    public void setEmailAdresses(List<String> emailAdresses) {
        this.emailAdresses = emailAdresses;
    }
}
