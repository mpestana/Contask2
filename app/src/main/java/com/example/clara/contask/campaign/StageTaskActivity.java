package com.example.clara.contask.campaign;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

public class StageTaskActivity extends AppCompatActivity {


    private TextView title;
    private TextView beginAndEnd;

    private View status;

    private TextView description;

    private Button deliverStageTask;
    private Button toApply;

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



