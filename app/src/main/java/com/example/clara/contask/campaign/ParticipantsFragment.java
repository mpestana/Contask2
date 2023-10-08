package com.example.clara.contask.campaign;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clara.contask.R;
import com.example.clara.contask.chat.ChatsAdapter;
import com.example.clara.contask.model.Campaign;
import com.example.clara.contask.model.Chat;
import com.example.clara.contask.model.Message;
import com.example.clara.contask.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ParticipantsFragment extends Fragment {


    private RecyclerView recyclerView;
    private MutableLiveData<List<String>> usersIdMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<ArrayList<User>> usersMutableLiveData = new MutableLiveData<>();

    private ParticipantsAdapter participantsAdapter;
    private String campaignId;
    public ParticipantsFragment(String campaignId) {
        this.campaignId=campaignId;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_participants, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.list_item);

        usersMutableLiveData.observe(this, new Observer<ArrayList<User>>() {

            @Override
            public void onChanged(ArrayList<User> users) {
                if (usersMutableLiveData.getValue() != null && usersMutableLiveData.getValue().size()==usersIdMutableLiveData.getValue().size() ) {
                    LinearLayoutManager llm = new LinearLayoutManager(recyclerView.getContext());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(llm);
                    participantsAdapter = new ParticipantsAdapter(users);
                    recyclerView.setAdapter(participantsAdapter);
                    recyclerView.scrollToPosition(users.size() - 1);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getUsersIds();
        findUsers();


    }


    public void getUsersIds(){
        FirebaseFirestore.getInstance().document("/campaigns/" + campaignId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("error", error.getMessage(), error);
                }
                Campaign campaign = value.toObject(Campaign.class);

                //campaign.setCampaignId(value.getId());
                usersIdMutableLiveData.setValue(campaign.getUsersIds());

            }
        });
    }

    public void findUsers() {

        usersIdMutableLiveData.observe(this, new Observer<List<String>>() {

            @Override
            public void onChanged(List<String> usersIds) {
                ArrayList<User> auxUsersArray = new ArrayList<>();
                for (int i = 0; i < usersIds.size(); i++) {//para cada chat busca o user da ultima mensagem
                    int finalI = i;

                    String path = "users/"+usersIds.get(finalI);
                    FirebaseFirestore.getInstance().document(path).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            User user= value.toObject(User.class);
                            auxUsersArray.add(user);
                            usersMutableLiveData.setValue(auxUsersArray);
                        }
                    });
                }

            }
        });


    }


}