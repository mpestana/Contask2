package com.example.clara.contask.chat;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.clara.contask.FunctionsUtil;
import com.example.clara.contask.MainActivity;
import com.example.clara.contask.R;
import com.example.clara.contask.model.Chat;
import com.example.clara.contask.model.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatViewHolder> {

    private ArrayList <Chat> chats;

    public void redraw(ArrayList<Chat> chatsNew) {
        this.chats.clear();
        notifyDataSetChanged();
        this.chats.addAll(chatsNew);
        notifyDataSetChanged();
    }
    public ChatsAdapter(ArrayList<Chat> chats) {
        this.chats=chats;

    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);
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

                Intent intent = new Intent(v.getContext(), OpenChatActivity.class);
                intent.putExtra("chatId",chat.getChatId());
                v.getContext().startActivity(intent);
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
            String userNameLastMessage= FunctionsUtil.getFirstAndLastName(lastMessage.getUser().getUserName());
            String textLastMessage= lastMessage.getTextMessage();
            textViewName.setText(chat.getNameChat());
            System.out.println("teste "+ chat.getTypeChat());
            if(chat.getTypeChat().equals("Campaign")){
                textViewName.setTextColor(ResourcesCompat.getColor(textViewName.getResources(),R.color.colorCampaign,null));
            } else if (chat.getTypeChat().equals("Task")) {
                textViewName.setTextColor(ResourcesCompat.getColor(textViewName.getResources(),R.color.colorChatTask,null));
            } else  if (chat.getTypeChat().equals("Stage Task") || chat.getTypeChat().equals("StageTask")) {
                textViewName.setTextColor(ResourcesCompat.getColor(textViewName.getResources(),R.color.colorChatStageTask,null));
            }

            if(lastMessage.getUriPhoto()==null){
                textViewLastMessage.setText(userNameLastMessage+": "+textLastMessage);
            }
            else{
                textViewLastMessage.setText(userNameLastMessage+" "+ textViewLastMessage.getContext().getString(R.string.send_photo));
            }
            textViewTimeLastMessage.setText(lastMessage.getHourMessage()+ " • "+ lastMessage.getDateMessage());
            Picasso.get().load(chat.getChatPhotoUrl()).into(photo);
            photo.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), ActivityFullScreenPhoto.class);
                intent.putExtra("photoUrl",chat.getChatPhotoUrl());
                v.getContext().startActivity(intent);
            });
            System.out.println(System.currentTimeMillis());

        }
    }
}
