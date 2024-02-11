package com.example.clara.contask.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clara.contask.ActivityFullScreenPhoto;
import com.example.clara.contask.campaign.CampaignActivity;
import com.example.clara.contask.FunctionsUtil;
import com.example.clara.contask.R;
import com.example.clara.contask.model.Chat;
import com.example.clara.contask.model.Message;
import com.example.clara.contask.model.MessageSend;

import com.example.clara.contask.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class OpenChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MutableLiveData<Chat> chatMutableLiveData = new MutableLiveData<Chat>(null);
    private MutableLiveData<ArrayList<Message>> messagesWithUser = new MutableLiveData<ArrayList<Message>>(new ArrayList<Message>());
    private EditText editChat;
    private CircleImageView btnChat;

    private ImageView btnPhoto;
    private MessageAdapter messagesAdapter;
    private DocumentReference chatReference;
    private String chatId;


    private boolean inOnlineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        editChat = findViewById(R.id.edit_chat);
        btnChat = findViewById(R.id.btnChat);
        btnPhoto = findViewById(R.id.btn_photo);

        chatId = getIntent().getExtras().getString("chatId", null);
        inOnlineList = false;
        // getting current chat
        chatReference = FirebaseFirestore.getInstance().document("/chats/" + chatId);
        FirebaseFirestore.getInstance().document("/chats/" + chatId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("error", error.getMessage(), error);
                }
                Chat chat = value.toObject(Chat.class);
                //geting user token messaging and setting user online
                if (inOnlineList == false) {
                    changeStatus(chat);
                }
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
                    photo.setOnClickListener(v -> {
                        Intent intent = new Intent(v.getContext(), ActivityFullScreenPhoto.class);
                        intent.putExtra("photoUrl",chat.getChatPhotoUrl());
                        v.getContext().startActivity(intent);
                    });

                    List<Message> messages = chat.getMessages();
                    ArrayList<Message> messagesWithUserTemp = new ArrayList<>();
                    for (int i = 0; i < messages.size(); i++) {//para cada mensagem busca o user
                        int finalI = i;

                        Message mensage = messages.get(finalI);
                        String path = mensage.getUserReference();

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
                if (chatMutableLiveData.getValue() != null && messagesUsers != null && messagesUsers.size() == chatMutableLiveData.getValue().getMessages().size()) {
                    Message lastMessageChat = chatMutableLiveData.getValue().getMessages().get(chatMutableLiveData.getValue().getMessages().size() - 1);
                    Message lastMessageWithUser = messagesUsers.get(messagesUsers.size() - 1);

                    if (lastMessageChat.getTimeMessage() == lastMessageWithUser.getTimeMessage()) {
                        if (messagesAdapter != null) {
                            messagesAdapter.addItem((List<Message>) messagesUsers.clone());
                            recyclerView.scrollToPosition(messagesUsers.size() - 1);

                        } else if (messagesAdapter == null) {

                            recyclerView = findViewById(R.id.recycler_chat);
                            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(llm);
                            messagesAdapter = new MessageAdapter((ArrayList<Message>) messagesUsers.clone());
                            recyclerView.setAdapter(messagesAdapter);
                            recyclerView.scrollToPosition(messagesUsers.size() - 1);

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
                    String userReference = FirebaseFirestore.getInstance().document("users/" + userId).getPath();


                    MessageSend messageSend = new MessageSend(userReference, text, time, null);

                    List<Message> messagesCopy = chatMutableLiveData.getValue().getMessages();

                    List<MessageSend> messageCopySends = new ArrayList<>();
                    for (Message message :
                            messagesCopy) {
                        messageCopySends.add(new MessageSend(message.getUserReference(), message.getTextMessage(), message.getTimeMessage(), message.getUriPhoto()));
                    }

                    messageCopySends.add(messageSend);

                    FirebaseFirestore.getInstance().document("chats/" + chatId).update("messages", messageCopySends);

                    //enviando as notificacoes
                    FirebaseFirestore.getInstance().document(userReference).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user= documentSnapshot.toObject(User.class);
                            List<String> recipientsOfflines=  chatMutableLiveData.getValue().getAllTokensMessagingUsers();
                            recipientsOfflines.removeAll(chatMutableLiveData.getValue().getUsersOnline());
                            String textNotification= user.getUserName()+": "+messageSend.getTextMessage();
                            NotificationChat newNotification = new NotificationChat(recipientsOfflines,
                                    chatMutableLiveData.getValue().getNameChat(),
                                    textNotification, chatId);
                            FunctionsUtil.sendNotificationChat(newNotification);

                        }
                    });



                }

                editChat.setText(null);
            }
        });

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }

        });
    }

    private void changeStatus(Chat chat) {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String token) {
                if (chat.getAllTokensMessagingUsers().contains(token) == false) {
                    chat.getAllTokensMessagingUsers().add(token);
                    chatReference.update("allTokensMessagingUsers", chat.getAllTokensMessagingUsers());
                }
                if (chat.getUsersOnline().contains(token) == false) {
                    chat.getUsersOnline().add(token);
                    chatReference.update("usersOnline", chat.getUsersOnline());
                    inOnlineList=true;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if(data!=null){
                Uri selectedPhoto = data.getData();
                if(selectedPhoto!=null)
                    uploadPhoto(selectedPhoto);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Chat chat = chatMutableLiveData.getValue();
        inOnlineList = true;
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String token) {
                if (chat.getUsersOnline().contains(token)) {
                    chat.getUsersOnline().remove(token);
                    chatReference.update("usersOnline", chat.getUsersOnline());
                }
            }
        });

    }

    private void uploadPhoto(Uri selectedPhoto){

        String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + filename);
        ref.putFile(selectedPhoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.i("Url photo", uri.toString());



                            String userId = FirebaseAuth.getInstance().getUid();
                            long time = System.currentTimeMillis();
                            String userReference = FirebaseFirestore.getInstance().document("users/" + userId).getPath();


                            MessageSend messageSend = new MessageSend(userReference, null, time, uri.toString());

                            List<Message> messagesCopy = chatMutableLiveData.getValue().getMessages();

                            List<MessageSend> messageCopySends = new ArrayList<>();
                            for (Message message :
                                    messagesCopy) {
                                messageCopySends.add(new MessageSend(message.getUserReference(), message.getTextMessage(), message.getTimeMessage(), message.getUriPhoto()));
                            }

                            messageCopySends.add(messageSend);

                            FirebaseFirestore.getInstance().document("chats/" + chatId).update("messages", messageCopySends);

                            //enviando as notificacoes
                            FirebaseFirestore.getInstance().document(userReference).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    User user= documentSnapshot.toObject(User.class);
                                    List<String> recipientsOfflines=  chatMutableLiveData.getValue().getAllTokensMessagingUsers();
                                    recipientsOfflines.removeAll(chatMutableLiveData.getValue().getUsersOnline());
                                    String textNotification= user.getUserName()+ " "+ getString(R.string.send_photo);
                                    NotificationChat newNotification = new NotificationChat(recipientsOfflines,
                                            chatMutableLiveData.getValue().getNameChat(),
                                            textNotification, chatId);
                                    FunctionsUtil.sendNotificationChat(newNotification);

                                }
                            });







                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Error", e.getMessage(), e);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Chat chat= chatMutableLiveData.getValue();
        if (chat!=null) {
            changeStatus(chat);
        }
    }
}