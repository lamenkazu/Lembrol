package com.daedrii.lembrol.model;

import java.util.ArrayList;

public class ReminderGroup {

    private String date;
    private ArrayList<Reminder> reminders;

    public ReminderGroup(String date, ArrayList<Reminder> reminders){
        this.date = date;
        this.reminders = reminders;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<Reminder> getReminders() {
        return reminders;
    }
}
