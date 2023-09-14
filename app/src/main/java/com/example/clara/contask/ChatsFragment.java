package com.example.clara.contask;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clara.contask.model.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;

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

        fetchUsers();

    }

    private void fetchUsers() {
        FirebaseFirestore.getInstance().collection("/users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("error", error.getMessage(), error);
                }
                List<DocumentSnapshot> documents = value.getDocuments();
                ArrayList<User> users = new ArrayList<User>();
                for (DocumentSnapshot doc : documents) {
                    User user = doc.toObject(User.class);

                    users.add(user);

                    Log.i("User", user.getUserName());
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                RecyclerView.Adapter chatsAdapter = new ChatsAdapter(users, (MainActivity) getContext());

                recyclerView.setAdapter(chatsAdapter);




            }
        });
    }
}