package com.example.clara.contask.campaign;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clara.contask.ActivityFullScreenPhoto;
import com.example.clara.contask.R;
import com.example.clara.contask.chat.OpenChatActivity;
import com.example.clara.contask.model.Campaign;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CampaignsAdapter extends RecyclerView.Adapter<CampaignsAdapter.CampaignViewHolder> {

    private ArrayList <Campaign> campaigns;

    public void redraw(ArrayList<Campaign> campaignsNew) {
        this.campaigns.clear();
        notifyDataSetChanged();
        this.campaigns.addAll(campaignsNew);
        notifyDataSetChanged();
    }
    public CampaignsAdapter(ArrayList<Campaign> campaigns) {
        this.campaigns=campaigns;

    }


    @NonNull
    @Override
    public CampaignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_campaign,parent,false);
        CampaignViewHolder holder = new CampaignViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CampaignViewHolder holder, int position) {
        position=holder.getAdapterPosition();
        Campaign campaign = campaigns.get(position);
        holder.textViewName.setText(campaign.getName());
        holder.bind(campaign);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), CampaignActivity.class);
                intent.putExtra("campaignId",campaign.getCampaignId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return campaigns.size();
    }
    class CampaignViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        CircleImageView photo;
        public CampaignViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName= itemView.findViewById(R.id.textViewChatName);
            photo= itemView.findViewById(R.id.photoChat);

        }

        public void bind(Campaign campaign) {

            textViewName.setText(campaign.getName());

            Picasso.get().load(campaign.getCampaignPhotoUrl()).into(photo);
            photo.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), ActivityFullScreenPhoto.class);
                intent.putExtra("photoUrl",campaign.getCampaignPhotoUrl());
                v.getContext().startActivity(intent);
            });
            System.out.println(System.currentTimeMillis());

        }
    }
}
