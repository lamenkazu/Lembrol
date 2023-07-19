package com.daedrii.lembrol.view;

import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daedrii.lembrol.R;
import com.daedrii.lembrol.controller.ReminderAdapter;
import com.daedrii.lembrol.model.Reminder;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

//Classe que lida com is componentes do item reminder
public class ReminderViewHolder extends RecyclerView.ViewHolder{

    private MaterialTextView lblReminder;
    private ImageButton lblDelete;

    private ReminderAdapter adapter;
    private int groupPosition;
    private int childPosition;

    public ReminderViewHolder(View itemView, ReminderAdapter adapter, int groupPosition, int childPosition) {
        super(itemView);
        this.adapter = adapter;
        this.groupPosition = groupPosition;
        this.childPosition = childPosition;

        this.lblReminder = itemView.findViewById(R.id.lbl_reminder);
        this.lblDelete = itemView.findViewById(R.id.btn_delete);
    }

    //Vincula cada Reminder com sua View na tela, o alinhando com seus componentes
    public void bind(@NonNull Reminder reminder) {
        lblReminder.setText(reminder.getName()); //Define o campo texto do componente

        lblDelete.setOnClickListener(new View.OnClickListener() { //Define a ação do botão de remover do componente
            @Override
            public void onClick(View v) {

                removeReminder(groupPosition, childPosition);

            }
        });
    }

    //Remove um lembrete
    public Reminder removeReminder(int groupPosition, int childPosition){
        ArrayList<Reminder> remindersInAGroup = adapter.getRemindersInAGroup(groupPosition);

        Reminder removedReminder = remindersInAGroup.remove(childPosition); //Remove reminder do grupo
        adapter.getDataManager().getReminders().remove(removedReminder); //Remove Reminder da lista de lembretes

        adapter.notifyDataSetChanged();

        return removedReminder;
    }

    public void updatePosition(int groupPosition, int childPosition) {
        this.groupPosition = groupPosition;
        this.childPosition = childPosition;
    }

    public int getGroupPosition() {
        return groupPosition;
    }

    public int getChildPosition() {
        return childPosition;
    }

    public MaterialTextView getLblReminder() {
        return lblReminder;
    }

    public ImageButton getLblDelete() {
        return lblDelete;
    }

    public ReminderAdapter getAdapter() {
        return adapter;
    }
}
