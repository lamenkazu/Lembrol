package com.daedrii.lembrol.model;

//Classe simples para definir uma Lembrança.
public class Reminder {

    private String name;
    private String date;

    public Reminder(String name, String date){
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
}
