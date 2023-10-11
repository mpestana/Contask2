package com.example.clara.contask.campaign;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clara.contask.R;
import com.example.clara.contask.model.StageTask;
import com.example.clara.contask.model.Task;

import java.util.ArrayList;

public class StageTaskAdapter extends RecyclerView.Adapter<StageTaskAdapter.CampaignViewHolder> {

    private ArrayList <StageTask> tasks;


    public StageTaskAdapter(ArrayList<StageTask> tasks) {
        this.tasks=tasks;

    }


    @NonNull
    @Override
    public CampaignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stage_task_campaign,parent,false);
        CampaignViewHolder holder = new CampaignViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CampaignViewHolder holder, int position) {
        position=holder.getAdapterPosition();
        StageTask task = tasks.get(position);
        holder.textViewName.setText(task.getTitle());
        holder.bind(task);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), StageTaskActivity.class);
                intent.putExtra("stageTask",task);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
    class CampaignViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        View status;
        public CampaignViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName= itemView.findViewById(R.id.textViewTaskName);
            status= itemView.findViewById(R.id.status_circle);

        }

        public void bind(StageTask task) {

            textViewName.setText(task.getTitle());
            if(task.getEnd()>0){
                status.setBackground(status.getContext().getResources().getDrawable(R.drawable.circle_status_task_red));
            }

        }
    }
}
