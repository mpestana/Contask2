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
import com.example.clara.contask.model.Campaign;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class CampaignsFragment extends Fragment {


    private RecyclerView recyclerView;
    private ArrayList<Campaign> campaigns;
    private MutableLiveData<ArrayList<Campaign>> campaignsMutableLiveData = new MutableLiveData<ArrayList<Campaign>>();

    private CampaignsAdapter campaignsAdapter;

    public CampaignsFragment() {
        // Required empty public constructor
    }


    public static CampaignsFragment newInstance(String param1, String param2) {
        CampaignsFragment fragment = new CampaignsFragment();
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

        return inflater.inflate(R.layout.fragment_campaigns, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.list_item_campaign);


    }

    @Override
    public void onStart() {
        super.onStart();
        getCampaigns();
        setRecycleView();
    }

    public MutableLiveData<ArrayList<Campaign>> getCampaigns() {

        FirebaseFirestore.getInstance().collection("/campaigns").whereArrayContains("usersIds", FirebaseAuth.getInstance().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("error", error.getMessage(), error);
                        }
                        campaigns = new ArrayList<Campaign>();
                        List<DocumentSnapshot> documents = value.getDocuments();

                        for (DocumentSnapshot doc : documents) {

                            Campaign campaign = doc.toObject(Campaign.class);
                            campaign.setCampaignId(doc.getId());
                            campaigns.add(campaign);
                        }

                        campaignsMutableLiveData.setValue(campaigns);
                    }
                });

        return campaignsMutableLiveData;

    }



    public void setRecycleView() {// seta na recyclerview
        campaignsMutableLiveData.observe(this, new Observer<ArrayList<Campaign>>() {
            @Override
            public void onChanged(ArrayList<Campaign> newCampaigns) {


                if (campaigns != null) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    campaignsAdapter = new CampaignsAdapter((ArrayList<Campaign>) newCampaigns.clone());
                    recyclerView.setAdapter(campaignsAdapter);

                } else if (campaignsAdapter != null) {
                    campaignsAdapter.redraw((ArrayList<Campaign>) newCampaigns.clone());
                }


            }
        });
    }
}