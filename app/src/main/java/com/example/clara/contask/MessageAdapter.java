package com.example.clara.contask;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        List<Message> newList = new ArrayList<>();
        newList.addAll(newMessages);
        messages.clear();
        notifyDataSetChanged();
        messages.addAll(newMessages);
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

        Log.i("add msg 2", String.valueOf(getItemCount()));
        Log.i("msg", String.valueOf(position));
        holder.bind(message);

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewTextMessage;
        CircleImageView photo;

        LinearLayout linearLayout;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Message message) {


            if (message.getUser().getUserId().equals(idUser)) {//mine
                itemView.findViewById(R.id.textViewTextMessageSystem).setVisibility(View.GONE);
                itemView.findViewById(R.id.layoutMessageOther).setVisibility(View.GONE);

                textViewTextMessage = itemView.findViewById(R.id.textViewTextMessageMine);
                textViewName = itemView.findViewById(R.id.messageUserNameMine);
                textViewName.setText("Você" + " • " + message.getHourMessage() + " • " + message.getDateMessage());

            } else if (message.getUser().getUserId().equals("0")) {//System
                itemView.findViewById(R.id.layoutMessageMine).setVisibility(View.GONE);
                itemView.findViewById(R.id.layoutMessageOther).setVisibility(View.GONE);

                textViewTextMessage = itemView.findViewById(R.id.textViewTextMessageSystem);

            } else {//other
                itemView.findViewById(R.id.textViewTextMessageSystem).setVisibility(View.GONE);
                itemView.findViewById(R.id.layoutMessageMine).setVisibility(View.GONE);


                textViewTextMessage = itemView.findViewById(R.id.textViewTextMessageOther);
                textViewName = itemView.findViewById(R.id.messageUserNameOther);
                photo = itemView.findViewById(R.id.photoChat);
                textViewName.setText(FunctionsUtil.getFirstAndLastName(message.getUser().getUserName()) + " • " + message.getHourMessage() + " • " + message.getDateMessage());
                Picasso.get().load(message.getUser().getUserPhotoUrl()).into(photo);
            }

            textViewTextMessage.setText(message.getTextMessage());


        }
    }
}
