package com.example.christmasapp.ui.pois.Event_Detailed;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.christmasapp.R;
import com.example.christmasapp.data.model.AgendaInstance;
import java.util.List;

public class EventSchedulesListAdapter extends RecyclerView.Adapter<EventSchedulesListAdapter.EventScheduleListViewHolder> {

    private final List<AgendaInstance> agendaInstanceList;

    public EventSchedulesListAdapter(List<AgendaInstance> agendaInstanceList) {
        this.agendaInstanceList = agendaInstanceList;
    }

    @NonNull
    @Override
    public EventScheduleListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new EventScheduleListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventScheduleListViewHolder holder, int index) {

        holder.tvAgendaInstanceName.setText(agendaInstanceList.get(index).getTitle());
        holder.tvAgendaInstanceDate.setText(agendaInstanceList.get(index).getDate().toString());
        holder.tvAgendaInstanceTime.setText(agendaInstanceList.get(index).getStartTime().toString() + "h - "
                                            + agendaInstanceList.get(index).getEndTime() + "h");
    }

    @Override
    public int getItemCount() {
        return agendaInstanceList.size();
    }

    @Override
    public void onViewRecycled(EventScheduleListViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public static class EventScheduleListViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private final TextView tvAgendaInstanceName;
        private final TextView tvAgendaInstanceDate;
        private final TextView tvAgendaInstanceTime;

        public EventScheduleListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAgendaInstanceName = itemView.findViewById(R.id.agenda_instance_name);
            tvAgendaInstanceDate = itemView.findViewById(R.id.agenda_instance_date);
            tvAgendaInstanceTime = itemView.findViewById(R.id.agenda_instance_time);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        }
    }
}
