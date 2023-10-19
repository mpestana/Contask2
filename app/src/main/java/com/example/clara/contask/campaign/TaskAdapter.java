package com.example.clara.contask.campaign;

import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clara.contask.R;
import com.example.clara.contask.model.Campaign;
import com.example.clara.contask.model.Task;
import com.google.type.Color;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.CampaignViewHolder> {

    private ArrayList <Task> tasks;


    public TaskAdapter(ArrayList<Task> tasks) {
        this.tasks=tasks;

    }


    @NonNull
    @Override
    public CampaignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_campaign,parent,false);
        CampaignViewHolder holder = new CampaignViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CampaignViewHolder holder, int position) {
        position=holder.getAdapterPosition();
        Task task = tasks.get(position);
        holder.textViewName.setText(task.getTitle());
        holder.bind(task);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), TaskActivity.class);
                intent.putExtra("task",task);
                intent.putParcelableArrayListExtra("stages", (ArrayList<? extends Parcelable>) task.getStageTasks());

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

        public void bind(Task task) {

            textViewName.setText(task.getTitle());
            if(task.getEnd()>0){
                status.setBackground(status.getContext().getResources().getDrawable(R.drawable.circle_status_task_red));

            }

        }
    }
}
