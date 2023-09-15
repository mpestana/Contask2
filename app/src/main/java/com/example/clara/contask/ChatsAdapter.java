package com.example.clara.contask;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.clara.contask.model.Chat;
import com.example.clara.contask.model.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatViewHolder> {
    private final MainActivity mainActivity;
    private ArrayList <Chat> chats;
    public ChatsAdapter(ArrayList<Chat> chats, MainActivity mainActivity) {

        this.chats=chats;
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
        position=holder.getAdapterPosition();
        Chat chat = chats.get(position);
        holder.textViewName.setText(chat.getNameChat());
        holder.bind(chat);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mainActivity,ChatActivity.class);
                intent.putExtra("chatId",chat.getChatId());
                mainActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
    class ChatViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        TextView textViewLastMessage;
        TextView textViewTimeLastMessage;
        CircleImageView photo;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName= itemView.findViewById(R.id.textViewChatName);
            photo= itemView.findViewById(R.id.photoChat);
            textViewLastMessage= itemView.findViewById(R.id.textViewLastMessage);
            textViewTimeLastMessage= itemView.findViewById(R.id.textViewTimeMessage);
        }

        public void bind(Chat chat) {
            Message lastMessage= chat.getMessages().get(chat.getMessages().size()-1);
            String userNameLastMessage=FunctionsUtil.getFirstAndLastName(lastMessage.getUser().getUserName());
            String textLastMessage= lastMessage.getTextMessage();


            textViewName.setText(chat.getNameChat());
            textViewLastMessage.setText(userNameLastMessage+": "+textLastMessage);
            textViewTimeLastMessage.setText(lastMessage.getHourMessage()+ " • "+ lastMessage.getDateMessage());

            Picasso.get().load(chat.getChatPhotoUrl()).into(photo);
            System.out.println(System.currentTimeMillis());


        }
    }
}
