package com.daedrii.lembrol;

import static org.junit.Assert.assertTrue;

import com.daedrii.lembrol.controller.ReminderAdapter;
import com.daedrii.lembrol.model.Reminder;
import com.daedrii.lembrol.model.ReminderGroup;

import org.junit.Test;
import org.mockito.Mockito;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReminderAdapterTest {

    @Test
    public void ShouldSetRemindersDataProperly(){

        ReminderAdapter adapterTest = Mockito.mock(ReminderAdapter.class);


        ArrayList<Reminder> data = new ArrayList<>();
        data.add(new Reminder("Comprar frutas", "31/12/2100"));
        data.add(new Reminder("Comer frutas", "01/01/2101"));

        //Simula comportamento do setData, que é Void e tem um ArrayList como parâmetro
        Mockito.doNothing().when(adapterTest).setData(Mockito.any(ArrayList.class));

        adapterTest.setData(data);

        Mockito.verify(adapterTest).setData(data); //Verifica se o método chamado anteriormente foi executado com sucesso

    }

    @Test
    public void ShouldOrderDataInsideAReminderGroup(){

        ReminderAdapter adapterTest = new ReminderAdapter();

        ArrayList<ReminderGroup> reminderGroups = new ArrayList<>();
        reminderGroups.add(new ReminderGroup("01/01/2100", new ArrayList<>() ));
        reminderGroups.add(new ReminderGroup("01/01/2099", new ArrayList<>() ));
        reminderGroups.add(new ReminderGroup("01/01/2098", new ArrayList<>() ));


        adapterTest.setReminderGroups(reminderGroups);
        adapterTest.orderData();

        //Verifica se a ordenação foi efetuada corretamente
        List<ReminderGroup> groups = adapterTest.getReminderGroups();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for(int i = 0; i < groups.size() -1 ; i++){
            try {
                Date date1 = sdf.parse(groups.get(i).getDate());
                Date date2 = sdf.parse(groups.get(i+1).getDate());

                assertTrue(date1.before(date2));

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }


    @Test
    public void ShouldSetGroupTitleProperly(){

        //O método RemidnerAdapter.getGroupView faz uso de recursos do Android,
        //não sendo possível realizar o teste sem um ambiente Android Real

        //TODO Realizar os testes dos métodos de ReminderAdapter usando uma estrutura de teste específica para o android

    }


}
