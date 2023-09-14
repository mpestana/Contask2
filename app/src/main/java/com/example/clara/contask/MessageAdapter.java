package com.example.clara.contask;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clara.contask.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private ArrayList <Message> messages;


    String idUser;
    public MessageAdapter(ArrayList<Message> messages) {
        this.messages=messages;

        this.idUser=FirebaseAuth.getInstance().getUid();
    }
    int currentMessage=0;

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Message message = messages.get(currentMessage);
        View view;
        if(message.getUserId().equals(idUser)){
             view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_me_message,parent,false);
        }
        else{
            view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_from_message,parent,false);
        }
        MessageViewHolder holder = new MessageViewHolder(view);
        currentMessage++;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        Message message = messages.get(position);


        holder.bind(message);

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
    class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        CircleImageView photo;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Message message) {
//            textViewName.setText(FunctionsUtil.getFirstAndLastName(message.getUserName()));
//
//            Picasso.get().load(user.getUserPhotoUrl()).into(photo);



        }
    }
}
