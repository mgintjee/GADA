package com.happybananastudio.gada;

import static com.happybananastudio.gada.MyTools.GetFormattedCurrentDate;

public class UserInfo {

    private String Handle, Password, Name, Type, DateCreated;

    UserInfo(String H, String P, String N, String T) {
        super();
        Handle = H;
        Password = P;
        Name = N;
        Type = T;
        DateCreated = GetFormattedCurrentDate();
    }

    // Setters
    public void SetHandle(String H) {
        Handle = H;
    }

    public void SetName(String N) {
        Name = N;
    }

    public void SetPassword(String P) {
        Password = P;
    }

    public void SetType(String T) {
        Type = T;
    }

    // Getters
    String GetHandle() {
        return Handle;
    }

    String GetPassword() {
        return Password;
    }

    String GetName() {
        return Name;
    }

    String GetType() {
        return Type;
    }

    String GetDate() {
        return DateCreated;
    }

}
