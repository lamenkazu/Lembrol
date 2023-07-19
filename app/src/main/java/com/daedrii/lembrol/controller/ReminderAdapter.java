package com.daedrii.lembrol.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.daedrii.lembrol.R;
import com.daedrii.lembrol.model.Reminder;
import com.daedrii.lembrol.model.ReminderGroup;
import com.daedrii.lembrol.view.ReminderViewHolder;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


//Adaptador dos Lembretes utilizado para a exibição dos mesmos na MainActivity
public class ReminderAdapter extends BaseExpandableListAdapter {

    private ReminderDataManager dataManager;
    private static ArrayList<ReminderGroup> reminderGroups;
    private static HashMap<String, ArrayList<Reminder>> dateList;
    private static ArrayList<Reminder> reminders;

    private Context context;

    public ReminderAdapter(Context context, ReminderDataManager dataManager){
        this.context = context;
        this.dataManager = dataManager;
        this.reminders = new ArrayList<>();
        this.dateList = new HashMap<String, ArrayList<Reminder>>();
        this.reminderGroups = new ArrayList<>();

    }

    public ReminderAdapter() {
        this.dateList = new HashMap<String, ArrayList<Reminder>>();
        this.reminderGroups = new ArrayList<>();
    }

    //Define os dados do lembrete
    public void setData(ArrayList<Reminder> data) {

        // Limpa as listas existentes
        this.reminderGroups.clear();
        this.dateList.clear();
        this.reminders.clear();

        this.reminders.addAll(data);

        // Agrupa os lembretes por data
        for (Reminder reminder : data) {
            String date = reminder.getDate();
            ArrayList<Reminder> reminders = this.dateList.get(date);
            if (reminders == null) {
                reminders = new ArrayList<>();
                this.dateList.put(date, reminders);
            }
            reminders.add(reminder);
        }

        // Cria os grupos a partir dos dados da lista dateList
        for (Map.Entry<String, ArrayList<Reminder>> entry : this.dateList.entrySet()) {
            String date = entry.getKey();
            ArrayList<Reminder> reminders = entry.getValue();
            ReminderGroup group = new ReminderGroup(date, reminders);
            this.reminderGroups.add(group);
        }
    }

    public void orderData(){

        ReminderSorter.sortReminderGroups(this.reminderGroups);

    }


    @Override
    public int getGroupCount() {
        return this.dateList.keySet().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ReminderGroup group = this.reminderGroups.get(groupPosition);
        return group.getReminders().size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        ReminderGroup group = this.reminderGroups.get(groupPosition);
        return group.getDate();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ReminderGroup group = this.reminderGroups.get(groupPosition);
        return group.getReminders().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    //Agrupamento feito por titulos, cujo qual está setado como a própria data no formato dd/MM/yyyy.
    @Override
    public View getGroupView(int groupPosition,
                             boolean isExpanded,
                             View convertView,
                             ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.date_list, null);
        }

        String listTitle = (String) getGroup(groupPosition);
        MaterialTextView listTitleTextView = convertView.findViewById(R.id.list_title);
        listTitleTextView.setText(listTitle);

        //Verificação da lista de reminders para uma determinada data estar vazia, caso esteja, não a mostra mais.
        ArrayList<Reminder> reminders = this.dateList.get(listTitle);
        if(reminders.isEmpty() || reminders == null)
            listTitleTextView.setVisibility(View.GONE);
        else
            listTitleTextView.setVisibility(View.VISIBLE);


        return convertView;
    }

    //Itens da lista, que são Reminders
    @Override
    public View getChildView(int groupPosition,
                             int childPosition,
                             boolean isLastChild,
                             View convertView, ViewGroup parent) {

        ReminderViewHolder viewHolder;

        if(convertView == null || convertView.getTag() == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_list_reminder, null);
            viewHolder = new ReminderViewHolder(convertView,
                                                            ReminderAdapter.this,
                                                                   groupPosition, childPosition);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ReminderViewHolder) convertView.getTag();
            viewHolder.updatePosition(groupPosition, childPosition);
        }

        Reminder actualReminder = (Reminder) getChild(groupPosition, childPosition);

        viewHolder = (ReminderViewHolder) convertView.getTag();
        viewHolder.bind(actualReminder);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public static HashMap<String, ArrayList<Reminder>> getDateList() {
        return dateList;
    }

    public ArrayList<Reminder> getRemindersInAGroup(int groupPosition) {
        ReminderGroup group = this.reminderGroups.get(groupPosition);
        return group.getReminders();
    }



    public ReminderDataManager getDataManager() {
        return dataManager;
    }

    public static ArrayList<ReminderGroup> getReminderGroups() {
        return reminderGroups;
    }

    public Context getContext() {
        return context;
    }


    public void setDataManager(ReminderDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public static void setReminderGroups(ArrayList<ReminderGroup> reminderGroups) {
        ReminderAdapter.reminderGroups = reminderGroups;
    }

    public static void setDateList(HashMap<String, ArrayList<Reminder>> dateList) {
        ReminderAdapter.dateList = dateList;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static ArrayList<Reminder> getReminders() {
        return reminders;
    }
}
