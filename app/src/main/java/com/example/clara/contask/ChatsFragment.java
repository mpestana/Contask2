package com.example.clara.contask;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clara.contask.model.Chat;
import com.example.clara.contask.model.Message;
import com.example.clara.contask.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;



public class ChatsFragment extends Fragment {


    private RecyclerView recyclerView;
    private ArrayList<Chat> chats;

    private MutableLiveData<ArrayList<Chat>> chatsMutableLiveData = new MutableLiveData<ArrayList<Chat>>();
    private MutableLiveData<ArrayList<Chat>> chatsWithUserLastMessageMutableLiveData = new MutableLiveData<ArrayList<Chat>>(new ArrayList<Chat>());

    public MutableLiveData<ArrayList<Chat>> getChats(){

        FirebaseFirestore.getInstance().collection("/chats").whereArrayContains("usersIds", FirebaseAuth.getInstance().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("error", error.getMessage(), error);
                        }
                        chats= new ArrayList<Chat>();
                        List<DocumentSnapshot> documents = value.getDocuments();

                        for (DocumentSnapshot doc : documents) {

                            Chat chat = doc.toObject(Chat.class);
                            chat.setChatId(doc.getId());
                            chats.add(chat);
                        }

                        chatsMutableLiveData.setValue(chats);

                    }
                });

        return chatsMutableLiveData;

    }

    public ChatsFragment() {
        // Required empty public constructor
    }


    public static ChatsFragment newInstance(String param1, String param2) {



        ChatsFragment fragment = new ChatsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.list_item);


        getChats().observe(this, new Observer<ArrayList<Chat>>() {

            @Override
            public void onChanged(ArrayList<Chat> userChats) {

                for (int i = 0; i < userChats.size(); i++) {//para cada chat busca o user da ultima mensagem
                    int finalI = i;
                    Chat chat= userChats.get(finalI);
                    Message lastMessage= chat.getMessages().get(chat.getMessages().size()-1);
                    String path=lastMessage.getUserMessage().getPath();
                    FirebaseFirestore.getInstance().document(path).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                            lastMessage.setUser(value.toObject(User.class));
                            chat.getMessages().set(chat.getMessages().size()-1,lastMessage);

                            ArrayList<Chat> auxChatArray=chatsWithUserLastMessageMutableLiveData.getValue();
                            auxChatArray.add(chat);
                            chatsWithUserLastMessageMutableLiveData.setValue(auxChatArray);
                        }
                    });
                }
            }
        });


        chatsWithUserLastMessageMutableLiveData.observe(this, new Observer<ArrayList<Chat>>() {
            @Override
            public void onChanged(ArrayList<Chat> newChats) { // checa se todos os users das ultimas mensagens dos chats foram setados
                if( chats!=null  && newChats.size()==chats.size() ){
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    RecyclerView.Adapter chatsAdapter = new ChatsAdapter(newChats, (MainActivity) getContext());
                    recyclerView.setAdapter(chatsAdapter);
                }

            }
        });




    }


}