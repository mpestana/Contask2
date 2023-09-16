package com.example.clara.contask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clara.contask.model.Chat;
import com.example.clara.contask.model.Message;
import com.example.clara.contask.model.MessageSend;

import com.example.clara.contask.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MutableLiveData<Chat> chatMutableLiveData = new MutableLiveData<Chat>(null);
    private MutableLiveData<ArrayList<Message>> messagesWithUser = new MutableLiveData<ArrayList<Message>>(new ArrayList<Message>());
    private EditText editChat;
    private CircleImageView btnChat;
    private MessageAdapter messagesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        editChat = findViewById(R.id.edit_chat);
        btnChat = findViewById(R.id.btnChat);

        String chatId = getIntent().getExtras().getString("chatId", null);

        // getting current chat
        FirebaseFirestore.getInstance().document("/chats/" + chatId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("error", error.getMessage(), error);
                }
                Chat chat = value.toObject(Chat.class);
                chat.setChatId(value.getId());
                chatMutableLiveData.setValue(chat);
            }
        });


        // when get chat, set users in messages
        chatMutableLiveData.observe(this, new Observer<Chat>() {
            @Override
            public void onChanged(Chat chat) {
                if (chat != null) {
                    CircleImageView photo = findViewById(R.id.chatPhotoTitle);
                    TextView titleChat = findViewById(R.id.chatName);
                    titleChat.setText(chat.getNameChat());
                    Picasso.get().load(chat.getChatPhotoUrl()).into(photo);


                    List<Message> messages = chat.getMessages();
                    ArrayList<Message> messagesWithUserTemp = new ArrayList<>();
                    for (int i = 0; i < messages.size(); i++) {//para cada mensagem busca o user
                        int finalI = i;

                        Message mensage = messages.get(finalI);
                        String path = mensage.getUserMessage().getPath();

                        FirebaseFirestore.getInstance().document(path).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                mensage.setUser(value.toObject(User.class));
                                messagesWithUserTemp.add(mensage);
                                messagesWithUser.setValue(messagesWithUserTemp);
                            }
                        });
                    }
                }
            }
        });

        //when set users, add on recycler view
        messagesWithUser.observe(this, new Observer<ArrayList<Message>>() {
            @Override
            public void onChanged(ArrayList<Message> messagesUsers) {
                if (chatMutableLiveData.getValue() != null && messagesUsers != null && messagesUsers.size() == chatMutableLiveData.getValue().getMessages().size() ) {
                    Message lastMessageChat=chatMutableLiveData.getValue().getMessages().get(chatMutableLiveData.getValue().getMessages().size()-1);
                    Message lastMessageWithUser= messagesUsers.get(messagesUsers.size()-1);

                    if(lastMessageChat.getTimeMessage()==lastMessageWithUser.getTimeMessage()){
                        if (messagesAdapter != null) {
                            messagesAdapter.addItem((List<Message>) messagesUsers.clone());

                        } else if (messagesAdapter == null) {

                            recyclerView = findViewById(R.id.recycler_chat);
                            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(llm);
                            messagesAdapter = new MessageAdapter((ArrayList<Message>) messagesUsers.clone());
                            recyclerView.setAdapter(messagesAdapter);
                        }
                    }

                }
            }
        });


        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editChat.getText().toString();
                if (text != null && !text.isEmpty()) {
                    String userId = FirebaseAuth.getInstance().getUid();
                    long time = System.currentTimeMillis();
                    DocumentReference userReference = FirebaseFirestore.getInstance().document("users/" + userId);

                    MessageSend messageSend = new MessageSend(userReference, text, time);

                    List<Message> messagesCopy = chatMutableLiveData.getValue().getMessages();

                    List<MessageSend> messageCopySends = new ArrayList<>();
                    for (Message message :
                            messagesCopy) {
                        messageCopySends.add(new MessageSend(message.getUserMessage(), message.getTextMessage(), message.getTimeMessage()));
                    }

                    messageCopySends.add(messageSend);

                    FirebaseFirestore.getInstance().document("chats/" + chatId).update("messages", messageCopySends);

                }

                editChat.setText(null);
            }
        });
    }


}