package com.daedrii.lembrol.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.daedrii.lembrol.R;
import com.daedrii.lembrol.model.Reminder;
import com.daedrii.lembrol.model.exceptions.EmptyFieldException;
import com.daedrii.lembrol.model.exceptions.InvalidDateException;
import com.daedrii.lembrol.view.ReminderViewHolder;
import com.google.android.material.textview.MaterialTextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;


@RunWith(AndroidJUnit4.class)
public class ReminderAdapterInstrumentedTest {

    @Mock
    private ReminderDataManager mockDataManager;

    //Variáveis de tela
    private final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    private final LayoutInflater inflater = LayoutInflater.from(context);
    private final ViewGroup parent = new LinearLayout(context); // Cria um objeto ViewGroup real
    private final int groupPosition = 0;
    private final int childPosition = 0;

    //Utils
    private ArrayList<Reminder> seedData = new ArrayList<>();
    private ReminderAdapter adapterTest;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockDataManager = Mockito.mock(ReminderDataManager.class);
        adapterTest = new ReminderAdapter(context, mockDataManager);

    }

    private void setSeedData(){
        seedData.clear();

        ArrayList<Reminder> reminders = new ArrayList<>();
        reminders.add(new Reminder("Comprar Frutas", "01/01/2100"));
        reminders.add(new Reminder("Comer Frutas", "02/01/2100"));

        adapterTest.setData(reminders);
    }

    @Test
    public void ShouldReturnValidUpdatedConvertView(){
        setSeedData();

        View initialConvertView = inflater.inflate(R.layout.item_list_reminder, null);
        View updatedConvertView = adapterTest.getChildView(groupPosition,childPosition, false,
                                                           initialConvertView, parent);

        //Verificação da atualização das posições do ViewHolder
        ReminderViewHolder viewHolderTest = (ReminderViewHolder) updatedConvertView.getTag();
        assertEquals(groupPosition, viewHolderTest.getGroupPosition());
        assertEquals(childPosition, viewHolderTest.getChildPosition());

        //Verifica se a convertView inicial foi alterada ou é a mesma
        assertNotSame(initialConvertView, updatedConvertView);
    }

    @Test
    public void ShoudlUpdateViewHolderPositionAndBindData(){
        setSeedData();

        View convertView = inflater.inflate(R.layout.item_list_reminder, null);
        View view = adapterTest.getChildView(groupPosition, childPosition, false, convertView, parent);

        //Cria viewHolder e actualReminder de teste
        Reminder actualReminderTest = (Reminder) adapterTest.getChild(groupPosition, childPosition);
        ReminderViewHolder viewHolderTest = (ReminderViewHolder) view.getTag();

        //Verifica se realmente criou o viewHolder e o actualReminder
        assertNotNull(actualReminderTest);
        assertNotNull(viewHolderTest);

        //Verificação da atualização das posições do ViewHolder
        assertEquals(groupPosition, viewHolderTest.getGroupPosition());
        assertEquals(childPosition, viewHolderTest.getChildPosition());

    }

    @Test
    public void ShouldLinkReminderDataToViewHolderProperly(){

        setSeedData();

        View convertView = inflater.inflate(R.layout.item_list_reminder, null);
        View view = adapterTest.getChildView(groupPosition, childPosition, false, convertView, parent);

        //Verifica se realmente criou o viewHolder e o actualReminder
        Reminder actualReminderTest = (Reminder) adapterTest.getChild(groupPosition, childPosition);
        ReminderViewHolder viewHolderTest = (ReminderViewHolder) view.getTag();
        assertNotNull(actualReminderTest);
        assertNotNull(viewHolderTest);

        //Verifica se a atualização foi feita corretamente
        MaterialTextView lblReminder = view.findViewById(R.id.lbl_reminder);
        assertNotNull(lblReminder);
        assertEquals(actualReminderTest.getName(), lblReminder.getText().toString());

    }

    @Test
    public void ShouldCreateConvertViewWhenSentNull(){

        setSeedData();

        View convertView = null;
        View view = adapterTest.getChildView(groupPosition, childPosition, false, convertView, parent);

        assertNotNull(view);

        MaterialTextView lblReminder = view.findViewById(R.id.lbl_reminder);
        assertNotNull(lblReminder);

    }

    @Test
    public void ShouldReuseConvertViewWhenSentNotNull(){

        // Configura os dados do adaptador - TODO- não está funcionando com o seedData, descobrir porquê
        ArrayList<Reminder> testData = new ArrayList<>();
        testData.add(new Reminder("lembrete1", "10/10/2100"));
        adapterTest.setData(testData);

        View convertView = inflater.inflate(R.layout.item_list_reminder, parent, false); // Cria uma instância real de View

        View view = adapterTest.getChildView(groupPosition, childPosition, false, convertView, parent);

        // Verifica se a convertView foi atualizada corretamente com os dados do actualReminder
        MaterialTextView lblReminder = view.findViewById(R.id.lbl_reminder);
        assertNotNull(lblReminder);
        assertEquals(testData.get(childPosition).getName(), lblReminder.getText().toString());

    }

    @Test
    public void ShouldSetGroupTitleProperly() {
        setSeedData();

        View convertView = inflater.inflate(R.layout.date_list, parent, false); // Cria uma instância real de View
        View view = adapterTest.getGroupView(0, false, convertView, parent);

        MaterialTextView listTitle = view.findViewById(R.id.list_title);
        String expectedTitle = adapterTest.getGroup(groupPosition).toString();

        assertEquals(expectedTitle, listTitle.getText().toString()); //Verifica se o titulo do grupo foi definido com sucesso na View;
    }

    @Test
    public void ShouldShowTitleWhenReminderGroupIsNotEmpty() throws InvalidDateException, EmptyFieldException {
        setSeedData();

        View convertView = inflater.inflate(R.layout.date_list, parent, false); //Cria uma nova instancia real de View
        View view = adapterTest.getGroupView(groupPosition, false, convertView, parent);
        MaterialTextView listTitle = view.findViewById(R.id.list_title);

        assertEquals(View.VISIBLE, listTitle.getVisibility());

    }

    @Test
    public void ShouldHideTitleWhenReminderGroupIsEmpty() {

        setSeedData();

        //Remove todos os reminders dos grupos.
        ArrayList<Reminder> reminders = new ArrayList<>(adapterTest.getRemindersInAGroup(groupPosition));
        for (Reminder r : reminders) {
            adapterTest.getRemindersInAGroup(groupPosition).remove(r);
        }

        adapterTest.notifyDataSetChanged();

        View convertView = inflater.inflate(R.layout.date_list, parent, false); //Cria uma nova instancia real de View

        View view = adapterTest.getGroupView(groupPosition, false, convertView, parent);

        MaterialTextView listTitle = view.findViewById(R.id.list_title);

        assertEquals(View.GONE, listTitle.getVisibility());


    }


}
