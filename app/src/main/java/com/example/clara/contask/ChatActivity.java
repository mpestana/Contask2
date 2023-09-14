package com.example.clara.contask;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clara.contask.model.Message;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    ArrayList<Message> messages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messages=new ArrayList<Message>();

        Message auxMessage= new Message(FirebaseAuth.getInstance().getUid(),"Testando","01/01/2022","9:07");
        messages.add(auxMessage);
        Message auxMessage2= new Message("1","Testando","01/01/2022","9:07");
        messages.add(auxMessage2);

        recyclerView=findViewById(R.id.recycler_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter messagesAdapter = new MessageAdapter(messages);

        recyclerView.setAdapter(messagesAdapter);


    }
}
