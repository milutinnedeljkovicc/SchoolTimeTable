package com.selicapps.schooltimetable.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.selicapps.schooltimetable.R;
import com.selicapps.schooltimetable.interfaces.TaskSelectListener;
import com.selicapps.schooltimetable.classes.RCTaskModelClass;

import java.util.ArrayList;
import java.util.List;

public class RCTaskAdapter extends RecyclerView.Adapter<RCTaskAdapter.ViewHolder> {

    private final List<RCTaskModelClass> rctaskList;
    private TaskSelectListener listener2;
    ImageView delete;

    public RCTaskAdapter (){rctaskList = new ArrayList<>();}

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<RCTaskModelClass> rctaskList, TaskSelectListener listener2){
        this.listener2 = listener2;
        this.rctaskList.clear();
        this.rctaskList.addAll(rctaskList);
        this.notifyDataSetChanged();}

    @NonNull
    @Override
    public RCTaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_design, parent, false);
        return new RCTaskAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RCTaskAdapter.ViewHolder holder, int position) {
        String date = rctaskList.get(position).getDate();
        String subject = rctaskList.get(position).getSubject();
        String task = rctaskList.get(position).getTask_kind();

        holder.setData(subject, date, task);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener2.onItemClicked(rctaskList.get(holder.getAbsoluteAdapterPosition()));
            }
        });
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener2.onItemClicked(rctaskList.get(holder.getAbsoluteAdapterPosition()));
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return rctaskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView subject_view;
        private final TextView date_view;
        private final TextView task_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            delete = itemView.findViewById(R.id.deleteTaskView);
            subject_view = itemView.findViewById(R.id.task_subject_text);
            date_view = itemView.findViewById(R.id.task_date_text);
            task_view = itemView.findViewById(R.id.task_text);
        }

        public void setData(String subject, String date, String task) {
            subject_view.setText(subject);
            date_view.setText(date);
            task_view.setText(task);
        }
    }
}
