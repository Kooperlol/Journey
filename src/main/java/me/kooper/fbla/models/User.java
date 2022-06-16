package me.kooper.fbla.models;

public class User {

    private static String userName;

    public static void setUserName(String name) {
        userName = name;
    }

    public static String getUserName() {
        return userName;
    }

}