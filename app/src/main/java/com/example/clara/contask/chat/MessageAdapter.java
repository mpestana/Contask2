package com.example.clara.contask.chat;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clara.contask.ActivityFullScreenPhoto;
import com.example.clara.contask.campaign.CampaignActivity;
import com.example.clara.contask.FunctionsUtil;
import com.example.clara.contask.R;
import com.example.clara.contask.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messages;


    private String idUser;

    public void addItem(List<Message> newMessages) {
       this.messages.clear();
        notifyDataSetChanged();
        this.messages.addAll(newMessages);
        notifyDataSetChanged();

    }

    public MessageAdapter(ArrayList<Message> messages) {
        this.messages = messages;
        this.idUser = FirebaseAuth.getInstance().getUid();
    }



    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        MessageViewHolder holder = new MessageViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message);

    }

    @Override
    public int getItemCount() {
        return this.messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNameMine;
        TextView textViewNameOther;
        TextView textViewTextMessageMine;
        TextView textViewTextMessageOther;

        ImageView imageViewMessageMine;
        ImageView imageViewMessageOther;
        ImageView imageViewFullScreen;
        CircleImageView photo;
        LinearLayout layoutOther;
        LinearLayout layoutMine;

        TextView layoutSytem;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutMine=itemView.findViewById(R.id.layoutMessageMine);
            layoutOther=itemView.findViewById(R.id.layoutMessageOther);
            layoutSytem=itemView.findViewById(R.id.textViewTextMessageSystem);
            textViewTextMessageMine = itemView.findViewById(R.id.textViewTextMessageMine);
            textViewNameMine = itemView.findViewById(R.id.messageUserNameMine);
            textViewTextMessageOther = itemView.findViewById(R.id.textViewTextMessageOther);
            textViewNameOther = itemView.findViewById(R.id.messageUserNameOther);
            photo = itemView.findViewById(R.id.photoChat);
            imageViewMessageMine= itemView.findViewById(R.id.imageViewTextMessageMine);
            imageViewMessageOther= itemView.findViewById(R.id.imageViewTextMessageOther);
            imageViewFullScreen= itemView.findViewById(R.id.imageFullScreen);

        }

        public void bind(Message message) {


            if (message.getUser().getUserId().equals(idUser)) {//mine
                layoutSytem.setVisibility(View.GONE);
                layoutOther.setVisibility(View.GONE);
                layoutMine.setVisibility(View.VISIBLE);

                textViewNameMine.setText("Você" + " • " + message.getHourMessage() + " • " + message.getDateMessage());
                if(message.getUriPhoto()==null){
                    textViewTextMessageMine.setVisibility(View.VISIBLE);
                    textViewTextMessageMine.setText(message.getTextMessage());
                    imageViewMessageMine.setVisibility(View.GONE);
                }
                else{
                    textViewTextMessageMine.setVisibility(View.GONE);
                    Picasso.get().load(message.getUriPhoto()).into(imageViewMessageMine);
                    imageViewMessageMine.setVisibility(View.VISIBLE);
                    imageViewMessageMine.setOnClickListener(v -> {
                        Intent intent = new Intent(v.getContext(), ActivityFullScreenPhoto.class);
                        intent.putExtra("photoUrl",message.getUriPhoto());
                        v.getContext().startActivity(intent);
                    });
                }

            } else if (message.getUser().getUserId().equals("0")) {//System
                layoutMine.setVisibility(View.GONE);
                layoutOther.setVisibility(View.GONE);
                layoutSytem.setVisibility(View.VISIBLE);

                layoutSytem = itemView.findViewById(R.id.textViewTextMessageSystem);
                   layoutSytem.setText(message.getTextMessage());
            } else {//other
                layoutSytem.setVisibility(View.GONE);
                layoutMine.setVisibility(View.GONE);
                layoutOther.setVisibility(View.VISIBLE);


                if(message.getUriPhoto()==null){
                    textViewTextMessageOther.setVisibility(View.VISIBLE);
                    textViewTextMessageOther.setText(message.getTextMessage());
                    imageViewMessageOther.setVisibility(View.GONE);
                }
                else{
                    textViewTextMessageOther.setVisibility(View.GONE);
                    Picasso.get().load(message.getUriPhoto()).into(imageViewMessageOther);
                    imageViewMessageOther.setVisibility(View.VISIBLE);
                    imageViewMessageOther.setOnClickListener(v -> {
                        Intent intent = new Intent(v.getContext(), ActivityFullScreenPhoto.class);
                        intent.putExtra("photoUrl",message.getUriPhoto());
                        v.getContext().startActivity(intent);
                    });
                }
                textViewNameOther.setText(FunctionsUtil.getFirstAndLastName(message.getUser().getUserName()) + " • " + message.getHourMessage() + " • " + message.getDateMessage());
                Picasso.get().load(message.getUser().getUserPhotoUrl()).into(photo);
                photo.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), ActivityFullScreenPhoto.class);
                    intent.putExtra("photoUrl",message.getUser().getUserPhotoUrl());
                    v.getContext().startActivity(intent);
                });
            }

        }
    }
}
