package br.pucminas.arthur.lddm.tp01;

import android.provider.ContactsContract;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by glori on 08/04/2018.
 */

public class Contact {
    private String name;
    private String email;
    private String phone;
    private Calendar birthday;

    public Contact(String name, String email, String phone, Calendar birthday){
        this.name = name;
        this.email = email;
        this. phone = phone;
        this.birthday = birthday;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public void setBirthday(Calendar birthday){
        this.birthday = birthday;
    }

    public String getName(){
        return this.name;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPhone(){
        return this.phone;
    }

    public Calendar getBirthday(){
        return this.birthday;
    }





}
