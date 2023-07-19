package com.daedrii.lembrol.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.daedrii.lembrol.R;
import com.daedrii.lembrol.controller.ReminderAdapter;
import com.daedrii.lembrol.controller.ReminderDataManager;
import com.daedrii.lembrol.model.Reminder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class ReminderViewHolderInstrumentedTest {

    @Mock
    private ReminderDataManager mockDataManager;

    private final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    private final LayoutInflater inflater = LayoutInflater.from(context);
    private final ViewGroup parent = new LinearLayout(context); // Cria um objeto ViewGroup real
    private final int groupPosition = 0;
    private final int childPosition = 0;

    private ArrayList<Reminder> seedData = new ArrayList<>();
    private ReminderAdapter adapterTest;

    final String TXT_REMINDER = "Comprar Frutas";
    final String TXT_DATE = "01/01/2100";
    final String TXT_REMINDER1 = "Comer Frutas";
    final String TXT_DATE1 = "02/01/2100";

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
        mockDataManager = Mockito.mock(ReminderDataManager.class);
        adapterTest = new ReminderAdapter(context, mockDataManager);

    }

    private void setSeedData(){
        seedData.clear();

        ArrayList<Reminder> reminders = new ArrayList<>();
//        reminders.add(new Reminder(TXT_REMINDER1, TXT_DATE1));
        reminders.add(new Reminder(TXT_REMINDER, TXT_DATE));
        adapterTest.setData(reminders);
    }

    @Test
    public void ShouldShowReminderNameInComponentProperly(){

        setSeedData();

        View convertView = inflater.inflate(R.layout.item_list_reminder, null);
        View view = adapterTest.getChildView(groupPosition, childPosition, false, convertView, parent);

        ReminderViewHolder viewHolderTest = (ReminderViewHolder) view.getTag();

        //Chama o Bind para o Reminder do seed
        viewHolderTest.bind(adapterTest.getReminders().get(0));
        String actualText = viewHolderTest.getLblReminder().getText().toString(); //Pega o nome do lembrete definido

        assertEquals(TXT_REMINDER, actualText); //Verifica se o texto exibido corresponde ao nome definido.

    }

    @Test
    public void ShouldRemoveReminderFromGroupWhenHisButtonIsClickedProperly(){

        //Setup para o teste
        setSeedData();

        ArrayList<Reminder> remindersBeforeRemotion = new ArrayList<>(adapterTest.getRemindersInAGroup(0));

        View convertView = inflater.inflate(R.layout.item_list_reminder, null);
        View view = adapterTest.getChildView(groupPosition, childPosition, false, convertView, parent);

        ReminderViewHolder viewHolderTest = (ReminderViewHolder) view.getTag();
        viewHolderTest.bind(adapterTest.getReminders().get(0));

        //Inicio do teste
        viewHolderTest.getLblDelete().performClick();
        ArrayList<Reminder> remindersAfterRemotion = adapterTest.getRemindersInAGroup(0);

        //Verifica se o tamanho da lista diminuiu em 1
        assertEquals(remindersBeforeRemotion.size() - 1, remindersAfterRemotion.size());

        //Verifica se o lembrete removido não está mais presente na lista do grupo
        assertFalse(remindersAfterRemotion.contains(adapterTest.getReminders().get(0)));
    }

    @Test
    public void ShouldPerformRemotionOfAReminder(){
        setSeedData();

        View convertView = inflater.inflate(R.layout.item_list_reminder, null);
        View view = adapterTest.getChildView(groupPosition, childPosition, false, convertView, parent);

        ReminderViewHolder viewHolderTest = (ReminderViewHolder) view.getTag();

        Reminder removedRemidner = viewHolderTest.removeReminder(groupPosition, childPosition);

        //Verifica se o lembrete removido não está mais presente na lista do grupo
        ArrayList<Reminder> remindersInAGroup = adapterTest.getRemindersInAGroup(0);
        assertFalse(remindersInAGroup.contains(removedRemidner));
    }

}
