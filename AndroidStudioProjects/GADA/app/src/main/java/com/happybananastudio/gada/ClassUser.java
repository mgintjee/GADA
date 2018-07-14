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

    @Override
    public boolean equals(Object obj) {
        // TODO
        //  Need to generate the equals and the hash code mapping to allow for easilty pulling the data when making queries. :)
        if (!(obj instanceof ClassUser)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        ClassUser OtherUser = (ClassUser) obj;
        return true;
    }

}
