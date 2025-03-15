package com.selicapps.schooltimetable.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.selicapps.schooltimetable.R;
import com.selicapps.schooltimetable.interfaces.SelectListener;
import com.selicapps.schooltimetable.classes.RCModelClass;

import java.util.ArrayList;
import java.util.List;

public class RCAdapter extends RecyclerView.Adapter<RCAdapter.ViewHolder> {

    private final List<RCModelClass> rcsubjectList;
    private SelectListener listener;

    public RCAdapter (){rcsubjectList = new ArrayList<>();}

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<RCModelClass> rcsubjectList, SelectListener listener){
        this.listener = listener;
        this.rcsubjectList.clear();
        this.rcsubjectList.addAll(rcsubjectList);
        this.notifyDataSetChanged();}

    @NonNull
    @Override
    public RCAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RCAdapter.ViewHolder holder, int position) {
        String subject = rcsubjectList.get(position).getSubject_text();
        String time = rcsubjectList.get(position).getTime_text();
        String classroom = rcsubjectList.get(position).getClassroom_text();

        holder.setData(subject, time, classroom);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(rcsubjectList.get(holder.getAbsoluteAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return rcsubjectList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView subject_view;
        private final TextView time_view;
        private final TextView classroom_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subject_view = itemView.findViewById(R.id.subject_text);
            time_view = itemView.findViewById(R.id.time_text);
            classroom_view = itemView.findViewById(R.id.classroom_text);
        }

        public void setData(String subject, String time, String classroom) {
            subject_view.setText(subject);
            time_view.setText(time);
            classroom_view.setText(classroom);
        }
    }
}
