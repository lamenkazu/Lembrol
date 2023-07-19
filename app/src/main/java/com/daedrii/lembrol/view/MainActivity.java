package com.daedrii.lembrol.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daedrii.lembrol.R;
import com.daedrii.lembrol.controller.ReminderAdapter;
import com.daedrii.lembrol.controller.ReminderDataManager;
import com.daedrii.lembrol.model.Reminder;
import com.daedrii.lembrol.model.exceptions.EmptyFieldException;
import com.daedrii.lembrol.model.exceptions.InvalidDateException;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //Variaveis para validação de testes, não precisam ser modificadas para o funcionamento da feature,
    // não se preocupar com essas variáveis.
    private boolean createdReminder = false;
    private boolean invalidDate = false;
    //////////////////////////////////////////////////////////////////////////////////////////////////

    private ReminderAdapter reminderAdapter;
    private ReminderDataManager dataManager;

    private TextInputEditText txtReminder, txtDate;
    private MaterialDatePicker<Long> materialDatePicker;
    private MaterialButton btnCreate;
    private ExpandableListView dateListView;


    public void initComponents(){
        txtReminder = findViewById(R.id.txt_reminder);
        txtDate = findViewById(R.id.txt_date);
        btnCreate = findViewById(R.id.btn_create);
        dateListView = findViewById(R.id.reminder_list);
        dataManager = new ReminderDataManager();

        reminderAdapter = new ReminderAdapter(MainActivity.this, dataManager);
        dateListView.setAdapter(reminderAdapter); // A lista agora se adapta ao ReminderAdapter

        //Criação do DatePicker
        materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Choose date to remind")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds() - 10800000)
                .build();

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {

                String data = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(selection));
                txtDate.setText(data);

            }

        });



    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();

        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(!materialDatePicker.isAdded())
                        materialDatePicker.show(getSupportFragmentManager(), "tag");
                }
            }
        });

        txtDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!materialDatePicker.isAdded())
                    materialDatePicker.show(getSupportFragmentManager(), "tag");
                return false;
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Esconde o teclado do txtReminder quando clica no botao
                InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(txtReminder.getWindowToken(), 0);

                try{
                    String textReminder = txtReminder.getText().toString();
                    String textDate = txtDate.getText().toString();

                    dataManager.addList(new Reminder(textReminder, textDate));

                    reminderAdapter.setData(dataManager.getReminders());

                    createdReminder = true; //variável para teste

                    reminderAdapter.orderData();
                    reminderAdapter.notifyDataSetChanged();

                    txtReminder.setText("");
                    txtDate.setText("");


                }catch(EmptyFieldException e){
                    Log.w("EmptyFieldException", e.getMessage());
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }catch (InvalidDateException e){
                    Log.w("InvalidDateException", e.getMessage());
                    invalidDate = true;
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }                

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Carrega a lista de lembretes predefinidos
        //dataManager.loadList();


        // Atualiza os dados do adapter apenas se houver dados no ReminderDataManager
        if (dataManager != null && !dataManager.getReminders().isEmpty()) {
            reminderAdapter.setData(dataManager.getReminders());
            reminderAdapter.orderData();
            reminderAdapter.notifyDataSetChanged(); // Notifica o adapter para atualizar a exibição
        }
    }

    public ReminderAdapter getReminderAdapter() {
        ExpandableListView expandableListView = findViewById(R.id.reminder_list);
        ReminderAdapter expandableListAdapter = (ReminderAdapter) expandableListView.getExpandableListAdapter();

        if (expandableListAdapter instanceof ReminderAdapter) {
            return (ReminderAdapter) expandableListAdapter;
        }

        return null; //TODO tratar nulo ou criar exceção
    }

    public boolean isCreatedReminder() {
        return createdReminder;
    }

    public boolean isInvalidDate() {
        return invalidDate;
    }

    public ReminderDataManager getDataManager() {
        return dataManager;
    }
}