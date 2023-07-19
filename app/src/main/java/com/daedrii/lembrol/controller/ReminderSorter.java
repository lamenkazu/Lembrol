package com.daedrii.lembrol.controller;

import com.daedrii.lembrol.model.ReminderGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class ReminderSorter {

    public static void sortReminderGroups(ArrayList<ReminderGroup> reminderGroups) {
        Collections.sort(reminderGroups, new Comparator<ReminderGroup>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            @Override
            public int compare(ReminderGroup group1, ReminderGroup group2) {
                try {
                    Date date1 = dateFormat.parse(group1.getDate());
                    Date date2 = dateFormat.parse(group2.getDate());
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });


    }
}
