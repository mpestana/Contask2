package com.example.clara.contask.campaign;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clara.contask.FunctionsUtil;
import com.example.clara.contask.R;
import com.example.clara.contask.chat.MessageAdapter;
import com.example.clara.contask.chat.NotificationChat;
import com.example.clara.contask.model.Campaign;
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

public class FeedFragment extends Fragment {

    private RecyclerView recyclerView;
    private MutableLiveData<List<Message>> messagesFeedMutableLiveData = new MutableLiveData<>(null);
    private EditText editChat;
    private CircleImageView btnChat;

    private ImageView btnPhoto;
    private FeedAdapter feedAdapter;
    private DocumentReference chatReference;
    private String campaignId;


    private boolean inOnlineList;

    public FeedFragment(String id) {
        campaignId=id;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_feed, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messagesFeedMutableLiveData.observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                if (messagesFeedMutableLiveData.getValue() != null ) {
                    recyclerView = view.findViewById(R.id.recycler_feed);
                    LinearLayoutManager llm = new LinearLayoutManager(recyclerView.getContext());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(llm);
                    feedAdapter = new FeedAdapter((ArrayList<Message>) messages);
                    recyclerView.setAdapter(feedAdapter);
                    recyclerView.scrollToPosition(messages.size() - 1);
                }
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        getFeed();

    }

    public void getFeed(){
        FirebaseFirestore.getInstance().document("/campaigns/" + campaignId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("error", error.getMessage(), error);
                }
                Campaign campaign = value.toObject(Campaign.class);

                //campaign.setCampaignId(value.getId());
                messagesFeedMutableLiveData.setValue(campaign.getMessagesFeed());

            }
        });
    }
}