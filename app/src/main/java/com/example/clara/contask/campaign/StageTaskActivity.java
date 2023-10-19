package com.example.clara.contask.campaign;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clara.contask.ActivityFullScreenPhoto;
import com.example.clara.contask.FunctionsUtil;
import com.example.clara.contask.R;
import com.example.clara.contask.model.StageTask;
import com.example.clara.contask.model.Task;
import com.example.clara.contask.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StageTaskActivity extends AppCompatActivity {


    private TextView title;
    private TextView beginAndEnd;

    private View status;

    private TextView description;

    private Button deliverStageTask;
    private Button toApply;
    private LinearLayout layoutDescription;
    private RecyclerView participants;

    private ParticipantsAdapter participantsAdapter;
    private MutableLiveData<ArrayList<User>> usersMutableLiveData = new MutableLiveData<>();
    private StageTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_task);

        title = findViewById(R.id.title);
        beginAndEnd = findViewById(R.id.beginAndEnd);
        status = findViewById(R.id.status_circle);
        description = findViewById(R.id.description);
        participants = findViewById(R.id.list_item_participants);
        toApply= findViewById(R.id.buttonApply);
        deliverStageTask= findViewById(R.id.buttonDeliver);
        task = getIntent().getParcelableExtra("stageTask");
        layoutDescription = findViewById(R.id.layout_description);
        fillInformations();
        usersMutableLiveData.observe(this, new Observer<ArrayList<User>>() {

            @Override
            public void onChanged(ArrayList<User> users) {
                if (usersMutableLiveData.getValue() != null && usersMutableLiveData.getValue().size() == task.getUsersIds().size()) {
                    LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    participants.setLayoutManager(llm);
                    participantsAdapter = new ParticipantsAdapter(users);
                    participants.setAdapter(participantsAdapter);
                }
            }
        });
    }

    private void fillInformations() {
        if (task != null) {
            title.setText(task.getTitle());
            String end = "";
            if (task.getEnd() > 0) {
                end = "| " + getString(R.string.end) + ": " + FunctionsUtil.getDateFormat(task.getEnd());
            }
            beginAndEnd.setText(getString(R.string.begin) + ": " + FunctionsUtil.getDateFormat(task.getBegin())
                    + end);
            if (task.getEnd() > 0) {
                status.setBackground(status.getContext().getResources().getDrawable(R.drawable.circle_status_task_red));
            }
            description.setText(task.getDescription());

            if(task.getUsersIds().contains(FirebaseAuth.getInstance().getUid())){
                toApply.setVisibility(View.GONE);
            }
            else{
                deliverStageTask.setVisibility(View.GONE);
            }

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            int width = displayMetrics.widthPixels;
            for (int i = 0; i < task.getDescriptionImagesUrls().size(); i++) {
                ImageView image= new ImageView(getApplicationContext());
                image.setMinimumWidth((int) (width*0.5));
                image.setMinimumHeight((int) (width*0.5));
                Picasso.get().load(task.getDescriptionImagesUrls().get(i)).into(image);
                int finalI = i;
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(), ActivityFullScreenPhoto.class);
                        intent.putExtra("photoUrl",task.getDescriptionImagesUrls().get(finalI));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        getBaseContext().startActivity(intent);
                    }
                });
                layoutDescription.addView(image);
            }

            ArrayList<User> auxUsersArray = new ArrayList<>();
            List<String> usersIds = task.getUsersIds();
            for (int i = 0; i < usersIds.size(); i++) {//para cada chat busca o user da ultima mensagem
                int finalI = i;

                String path = "users/" + usersIds.get(finalI);
                FirebaseFirestore.getInstance().document(path).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        User user = value.toObject(User.class);
                        auxUsersArray.add(user);
                        usersMutableLiveData.setValue(auxUsersArray);
                    }
                });
            }
        }
    }

}



