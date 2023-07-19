package com.daedrii.lembrol;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.util.Log;

import com.daedrii.lembrol.controller.ReminderDataManager;
import com.daedrii.lembrol.model.Reminder;
import com.daedrii.lembrol.model.ReminderGroup;
import com.daedrii.lembrol.model.exceptions.EmptyFieldException;
import com.daedrii.lembrol.model.exceptions.InvalidDateException;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;


public class ReminderDataManagerTest {

    final String INVALID_DATE = "25/05/2023";
    final String VALID_DATE = "01/01/2100";
    final String VALID_DATE1 = "02/01/2100";

    final Reminder VALID_REMINDER = new Reminder("Lembrrte", VALID_DATE);

    ReminderDataManager dataManagerTest = new ReminderDataManager();

    @Test
    public void ShouldThrowExceptionWhenSomeFieldIsEmpty(){

        Reminder newReminderTest = new Reminder("", "");

        try{
            dataManagerTest.addList(newReminderTest);
            fail("EmptyField: Esperava uma exception EmptyFieldException - Reminder deveria ser criado vazio");

        }catch (EmptyFieldException e) {

            assertEquals("Os campos não podem estar vazios para a inserção.", e.getMessage());

        }catch (InvalidDateException e) { e.printStackTrace(); }

    }

    @Test
    public void ShouldThrowExceptionIfDateIsNotValid(){


        Reminder newReminderTest = new Reminder("Lembrete", INVALID_DATE);

        try{
            dataManagerTest.addList(newReminderTest);
            fail("InvalidDate: Esperava uma exception InvalidDateException - Data: " + INVALID_DATE);

        }  catch (InvalidDateException e) {

            assertEquals("A data informada precisa estar no futuro para ser inserida", e.getMessage());

        }catch (EmptyFieldException e) {e.printStackTrace();}


    }

    @Test
    public void ShouldAddAValidReminder(){

        final String VALID_REMINDER_MESSAGE = "Deveria ter adcionado um lembrete corretamente";


        try {
            dataManagerTest.addList(VALID_REMINDER);

            ArrayList<Reminder> reminders = dataManagerTest.getReminders();

            assertTrue(reminders.contains(VALID_REMINDER));

        }
        catch (EmptyFieldException e) {fail(VALID_REMINDER_MESSAGE);}
        catch (InvalidDateException e) {fail(VALID_REMINDER_MESSAGE);}

    }

    @Test
    public void ShouldAddANewReminderGroup(){

        final String VALID_GROUP_MESSAGE = "Deveria ter adcionado um grupo corretamente";

        ArrayList<ReminderGroup> groups = dataManagerTest.getReminderGroups();
        try {
            dataManagerTest.addList(VALID_REMINDER);

            Boolean groupFound = false;

            for(ReminderGroup group: groups){
                if(group.getDate().equals(VALID_REMINDER.getDate()))
                    groupFound = true;
            }

            assertTrue(groupFound);
        }
        catch (EmptyFieldException e) {fail(VALID_GROUP_MESSAGE);}
        catch (InvalidDateException e) {fail(VALID_GROUP_MESSAGE);}

    }

    @Test
    public void ShouldContainExpectedRemindersInDateList(){

        String value1 = "Lembrete 1";
        String value2 = "Lembrete 2";

        Reminder valid1 = new Reminder(value1, VALID_DATE);
        Reminder valid2 = new Reminder(value2, VALID_DATE);


        try{
            dataManagerTest.addList(valid1);
            dataManagerTest.addList(valid2);

            HashMap<String, ArrayList<Reminder>> dateListTest = dataManagerTest.getDateList();


            assertTrue(dateListTest.containsKey(VALID_DATE));

            ArrayList<Reminder> reminders = dateListTest.get(VALID_DATE);
            assertTrue(reminders.contains(valid1));
            assertTrue(reminders.contains(valid2));


        } catch (InvalidDateException e) {
            e.printStackTrace();
        } catch (EmptyFieldException e) {
            e.printStackTrace();
        }
    }

    /*O teste ShouldLoadReminderList depende dos valores da Seed loadList() em ReminderDataManager,
       que a depender de quando o teste é rodado deverá ser trocado para receber datas válidas adequadamente.*/
    @Test
    public void ShouldLoadReminderList(){

        dataManagerTest.loadList();

        ArrayList<Reminder> reminders = dataManagerTest.getReminders();
        System.out.println("isso:" + reminders.toString());
        assertEquals(3, reminders.size());

        //Checa os detalhes de cada lembrete adicionado
        //TODO lidar com variáveis estáticas no lugar de rawValues
        Reminder reminder1 = reminders.get(0);
        assertEquals("Aniversario da Taís", reminder1.getName());
        assertEquals("07/06/2024", reminder1.getDate());


        Reminder reminder2 = reminders.get(1);
        assertEquals("Limpar caixa de areia", reminder2.getName());
        assertEquals("30/05/2024", reminder2.getDate());

        Reminder reminder3 = reminders.get(2);
        assertEquals("Colocar ração", reminder3.getName());
        assertEquals("29/05/2024", reminder3.getDate());

    }




}
