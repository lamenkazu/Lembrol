package com.daedrii.lembrol;

import static org.junit.Assert.assertTrue;

import com.daedrii.lembrol.controller.ReminderSorter;
import com.daedrii.lembrol.model.ReminderGroup;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReminderSorterTest {



    @Test
    public void ShouldDealWithEmptyReminderGroups(){

        ArrayList<ReminderGroup> reminderGroups = new ArrayList<>();

        ReminderSorter.sortReminderGroups(reminderGroups);

        assertTrue(reminderGroups.isEmpty());

    }

    @Test
    public void ShouldSortReminderGroupsInChronologicalOrder(){
        ArrayList<ReminderGroup> reminderGroups = new ArrayList<>();

        reminderGroups.add(new ReminderGroup("01/01/2102", new ArrayList<>()));
        reminderGroups.add(new ReminderGroup("01/01/2100", new ArrayList<>()));
        reminderGroups.add(new ReminderGroup("01/01/2101", new ArrayList<>()));

        ReminderSorter.sortReminderGroups(reminderGroups);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        for(int i = 0; i < reminderGroups.size() -1; i++){

            try {
                Date date1 = dateFormat.parse(reminderGroups.get(i).getDate());
                Date date2 = dateFormat.parse(reminderGroups.get(i+1).getDate());
                assertTrue(date1.compareTo(date2) <= 0);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    }

}
