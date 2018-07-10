package com.happybananastudio.gada;

/**
 * Created by mgint on 7/9/2018.
 */

public class UserInfo {

    private String Name, ID, Password, Type;

    UserInfo(String N, String I, String P, String T){
        super();
        Name = N;
        ID = I;
        Password = P;
        Type = T;
    }

    // Setters
    public void SetName(String N){Name = N;}
    public void SetID(String I){ID = I;}
    public void SetPassword(String P){Password = P;}
    public void SetType(String T){Type = T;}

    // Getters
    public String GetName(){return Name;}
    public String GetID(){return ID;}
    public String GetPassword(){return Password;}
    public String GetType(){return Type;}

}
