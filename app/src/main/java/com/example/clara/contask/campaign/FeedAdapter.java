package com.example.clara.contask.campaign;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clara.contask.FunctionsUtil;
import com.example.clara.contask.R;
import com.example.clara.contask.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MessageViewHolder> {

    private List<Message> messages;



    public void addItem(List<Message> newMessages) {
       this.messages.clear();
        notifyDataSetChanged();
        this.messages.addAll(newMessages);
        notifyDataSetChanged();

    }

    public FeedAdapter(ArrayList<Message> messages) {
        this.messages = messages;
    }



    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_feed, parent, false);
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
        TextView textViewStatus;
        TextView textViewFeed;
        ImageView imageViewMessage;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStatus=itemView.findViewById(R.id.messageStatusFeed);
            textViewFeed=itemView.findViewById(R.id.textViewMessageFeed);
            imageViewMessage=itemView.findViewById(R.id.imageViewMessage);


        }

        public void bind(Message message) {




            textViewStatus.setText(message.getHourMessage() + " • " + message.getDateMessage());
                if(message.getUriPhoto()==null){
                    textViewFeed.setVisibility(View.VISIBLE);
                    textViewFeed.setText(message.getTextMessage());
                    imageViewMessage.setVisibility(View.GONE);
                }
                else{
                    textViewFeed.setVisibility(View.GONE);
                    Picasso.get().load(message.getUriPhoto()).into(imageViewMessage);
                    imageViewMessage.setVisibility(View.VISIBLE);
                    imageViewMessage.setOnClickListener(v -> {
                        Intent intent = new Intent(v.getContext(), CampaignActivity.ActivityFullScreenPhoto.class);
                        intent.putExtra("photoUrl",message.getUriPhoto());
                        v.getContext().startActivity(intent);
                    });
                }

            }


    }
}
