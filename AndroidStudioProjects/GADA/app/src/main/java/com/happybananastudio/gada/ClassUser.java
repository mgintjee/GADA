package com.happybananastudio.gada;

public class ClassUser {

    public String CreatedOn, Password, UserHandle, UserName, UserTeam, UserType;

    ClassUser() {
    }

    ClassUser(String date, String password, String handle, String name, String team, String type) {
        super();
        CreatedOn = date;
        Password = password;
        UserHandle = handle;
        UserName = name;
        UserTeam = team;
        UserType = type;
    }
}
