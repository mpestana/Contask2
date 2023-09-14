package com.example.clara.contask;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clara.contask.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatViewHolder> {
    private final MainActivity mainActivity;
    private ArrayList <User> users;
    public ChatsAdapter(ArrayList<User> users, MainActivity mainActivity) {
        this.users=users;
        this.mainActivity=mainActivity;
    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        ChatViewHolder holder = new ChatViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        User user = users.get(position);
        holder.textViewName.setText(user.getUserName());
        holder.bind(user);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(position);
                Intent intent = new Intent(mainActivity,ChatActivity.class);
                mainActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    class ChatViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        CircleImageView photo;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName= itemView.findViewById(R.id.textViewChatName);
            photo= itemView.findViewById(R.id.photoChat);
        }

        public void bind(User user) {
            textViewName.setText(FunctionsUtil.getFirstAndLastName(user.getUserName()));

            Picasso.get().load(user.getUserPhotoUrl()).into(photo);



        }
    }
}
