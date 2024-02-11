package com.example.clara.contask.campaign;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clara.contask.R;
import com.example.clara.contask.model.Campaign;
import com.example.clara.contask.model.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class TasksFragment extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView recyclerViewOther;

    private MutableLiveData<Campaign> campaignMutableLiveData = new MutableLiveData<>();

    private TaskAdapter taskAdapter;
    private TaskAdapter taskAdapterOther;

    private String campaignId;

    private TextView empty;
    private TextView emptyOther;


    public TasksFragment(String campaignId) {
        this.campaignId = campaignId;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tasks_campaigns, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.list_item_task_campaign);
        recyclerViewOther = view.findViewById(R.id.list_item_other_task_campaign);
        empty = view.findViewById(R.id.text_empty);
        emptyOther = view.findViewById(R.id.text_empty_other);

    }

    @Override
    public void onStart() {
        super.onStart();
        getCampaigns();
        setRecycleView();
    }

    public MutableLiveData<Campaign> getCampaigns() {
        FirebaseFirestore.getInstance().document("/campaigns/" + campaignId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("error", error.getMessage(), error);
                }

                Campaign campaign = value.toObject(Campaign.class);
                campaign.setCampaignId(value.getId());
                campaignMutableLiveData.setValue(campaign);
            }
        });


        return campaignMutableLiveData;

    }


    public void setRecycleView() {// seta na recyclerview
        campaignMutableLiveData.observe(this, new Observer<Campaign>() {
            @Override
            public void onChanged(Campaign campaign) {


                if (campaign != null) {

                    ArrayList<Task> yourTasks=new ArrayList<>();
                    ArrayList<Task> otherTasks=new ArrayList<>();
                    String id= FirebaseAuth.getInstance().getUid();
                    for (Task t: campaign.getTasks()) {
                        if(t.getUsersIds().contains(id)){
                            yourTasks.add(t);
                        }
                        else{
                            otherTasks.add(t);
                        }
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    taskAdapter = new TaskAdapter(yourTasks, campaignId);
                    recyclerView.setAdapter(taskAdapter);

                    recyclerViewOther.setLayoutManager(new LinearLayoutManager(getContext()));
                    taskAdapterOther = new TaskAdapter(otherTasks,campaignId);
                    recyclerViewOther.setAdapter(taskAdapterOther);

                    if(yourTasks.size()==0){
                        empty.setVisibility(View.VISIBLE);
                    } else if (otherTasks.size()==0) {
                        emptyOther.setVisibility(View.VISIBLE);
                    }

                }


            }
        });
    }
}