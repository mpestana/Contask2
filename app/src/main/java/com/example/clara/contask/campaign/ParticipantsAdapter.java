package com.example.clara.contask.campaign;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clara.contask.R;
import com.example.clara.contask.model.Message;
import com.example.clara.contask.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.MessageViewHolder> {

    private List<User> users;





    public ParticipantsAdapter(List<User> users) {
        this.users = users;
    }



    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_participants, parent, false);
        MessageViewHolder holder = new MessageViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);

    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUserName;

        ImageView imageViewUserPhoto;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName=itemView.findViewById(R.id.textViewUserName);
            imageViewUserPhoto=itemView.findViewById(R.id.photoUser);


        }

        public void bind(User user) {




            textViewUserName.setText(user.getUserName());
            Picasso.get().load(user.getUserPhotoUrl()).into(imageViewUserPhoto);



            }


    }
}
