package com.example.appexpo;

import java.time.LocalDateTime;

public class Session {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
    }

    private static LocalDateTime lastLoginDate;
    private static LocalDateTime profileUpdateDate;

    public static LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }
    public static void setLastLoginDate(LocalDateTime d) {
        lastLoginDate = d;
    }

    public static LocalDateTime getProfileUpdateDate() {
        return profileUpdateDate;
    }
    public static void setProfileUpdateDate(LocalDateTime d) {
        profileUpdateDate = d;
    }

}

