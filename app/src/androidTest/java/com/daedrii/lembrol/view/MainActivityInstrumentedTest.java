package com.daedrii.lembrol.view;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.daedrii.lembrol.R;
import com.daedrii.lembrol.model.Reminder;
import com.google.android.material.textfield.TextInputEditText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;


@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    final String REMINDER_TEXT = "Lembrete Teste";
    final String REMINDER_DATE = "01/01/2100";
    final String txt_reminder1 = "Comprar Frutas";
    final String txt_reminder2 = "Comer Frutas";
    final String txt_date1 = "01/01/2100";
    final String txt_date2 = "02/01/2100";

    ArrayList<Reminder> seedData = new ArrayList<>();


    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void ShouldUpdateListProperlyWhenRemoveAReminderFromList(){
        ActivityScenario<MainActivity> scenario = activityRule.getScenario();

        seedData.add(new Reminder(txt_reminder1, txt_date1));

        //Cria um novo Reminder pra lista
        scenario.onActivity(activityTest -> {
            activityTest.getReminderAdapter().setData(seedData);
            activityTest.getReminderAdapter().orderData();
            activityTest.getReminderAdapter().notifyDataSetChanged();

            //Verifica se o lembrete foi inserido no grupo
            assertFalse(activityTest.getReminderAdapter().getRemindersInAGroup(0).isEmpty());
        });

        //Abre o novo lembrete criado
        onView(withText(txt_date1))
                .perform(click()); //abre
        onView(withText(txt_reminder1))
                .check(matches(isDisplayed())); //checa

        //Remove o lembrete inserido da lista
        onView(withId(R.id.btn_delete))
                .perform(click());

        //Cria um novo Reminder pra lista
        scenario.onActivity(activityTest -> {

            ArrayList<Reminder> reminders = activityTest.getReminderAdapter().getRemindersInAGroup(0);

            //verifica se a lista de lembretes de um grupo está vazia
            assertTrue(reminders.isEmpty());
        });

        //Se passou, atualizou a lista corretamente quando removeu.
    }

    @Test
    public void ShouldDisplayRemindersListProperlyByDate() throws InterruptedException {
        ActivityScenario<MainActivity> scenario = activityRule.getScenario();

        seedData.add(new Reminder(txt_reminder1, txt_date1));
        seedData.add(new Reminder(txt_reminder2, txt_date2));

        //Cria um cenário de teste com Data Inválida
        scenario.onActivity(activityTest -> {

            activityTest.getReminderAdapter().setData(seedData);
            activityTest.getReminderAdapter().orderData();

            // Atualize o adaptador da ExpandableListView
            activityTest.getReminderAdapter().notifyDataSetChanged();
        });

        // Verifique se os lembretes estão corretamente exibidos na ExpandableListView
        // Abrindo, checando e fechando a lista para não haver confusão da leitura.
        onView(withText(txt_date1))
                .perform(click()); //abre
        onView(withText(txt_reminder1))
                .check(matches(isDisplayed())); //Checa
        onView(withText(txt_date1))
                .perform(click()); //fecha

        //mesmo processo para a data
        onView(withText(txt_date2))
                .perform(click());
        onView(withText(txt_reminder2))
                .check(matches(isDisplayed()));
        onView(withText(txt_date2))
                .perform(click());
    }

    @Test
    public void ShouldOpenDatePickerWhenDateFieldIsFocoused(){
        //Performa o click do campo data
        onView(withId(R.id.txt_date))
                .perform(click());

        onView(withText("Choose date to remind"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void ShouldHandleFieldProperlyWhenDataIsInvalid()  {
        ActivityScenario<MainActivity> scenario = activityRule.getScenario();

        //Cria um cenário de teste com Data Inválida
        scenario.onActivity(activityTest -> {
            TextInputEditText txtReminder = activityTest.findViewById(R.id.txt_reminder);
            txtReminder.setText(REMINDER_TEXT);

            TextInputEditText txtDate = activityTest.findViewById(R.id.txt_date);
            txtDate.setText("27/05/2023");
        });

        //Clica no botoa de criação
        onView(withId(R.id.btn_create))
                .perform(click());

        //Se nada for criado o teste está correto.
        scenario.onActivity(activityTest -> {
            assertTrue(activityTest.isInvalidDate());
        });
    }

    @Test
    public void ShouldHandleFieldsProperlyWhenTheyAreEmpty()  {
        ActivityScenario<MainActivity> scenario = activityRule.getScenario();

        //Clica no botoa de criação
        onView(withId(R.id.btn_create))
                .perform(click());

        //Se nada for criado o teste está correto.
        scenario.onActivity(activityTest -> {
            assertFalse(activityTest.isCreatedReminder());
        });
    }

    @Test
    public void ShouldAddNewReminderWhenButtonCreateIsClicked() {
        ActivityScenario<MainActivity> scenario = activityRule.getScenario();

        //Cria um cenário de teste com dados válidos
        scenario.onActivity(activityTest -> {
            TextInputEditText txtReminder = activityTest.findViewById(R.id.txt_reminder);
            txtReminder.setText(REMINDER_TEXT);

            TextInputEditText txtDate = activityTest.findViewById(R.id.txt_date);
            txtDate.setText(REMINDER_DATE);
        });

        //Clica no botoa de criação
        onView(withId(R.id.btn_create))
                .perform(click());

        //Clica no Titulo do lembrete inserido
        onView(withText(REMINDER_DATE))
                .perform(click());

        // Verifica se o lembrete foi adicionado corretamente
        onView(withText(REMINDER_TEXT))
                .check(matches(isDisplayed()));
    }
}
