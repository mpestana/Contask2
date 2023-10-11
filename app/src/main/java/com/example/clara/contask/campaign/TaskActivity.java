package com.example.clara.contask.campaign;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clara.contask.FunctionsUtil;
import com.example.clara.contask.LoginActivity;
import com.example.clara.contask.R;
import com.example.clara.contask.chat.MessageAdapter;
import com.example.clara.contask.model.Message;
import com.example.clara.contask.model.StageTask;
import com.example.clara.contask.model.Task;
import com.example.clara.contask.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TaskActivity extends AppCompatActivity {


    private TextView title;
    private TextView beginAndEnd;

    private View status;

    private TextView description;

    private RecyclerView stageTasks;
    private StageTaskAdapter stageTaskAdapter;

    private RecyclerView stageTasksOther;
    private StageTaskAdapter stageTaskAdapterOther;

    private RecyclerView participants;

    private ParticipantsAdapter participantsAdapter;
    private MutableLiveData<ArrayList<User>> usersMutableLiveData = new MutableLiveData<>();
    private Task task;
    private ArrayList<StageTask> arrayStageTasks;
    private TextView empty;
    private TextView emptyOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        title = findViewById(R.id.title);
        beginAndEnd = findViewById(R.id.beginAndEnd);
        status = findViewById(R.id.status_circle);
        description = findViewById(R.id.description);
        stageTasks = findViewById(R.id.list_item_stage_task_campaign);
        stageTasksOther = findViewById(R.id.list_item_other_stage_task_campaign);
        participants = findViewById(R.id.list_item_participants);
        empty = findViewById(R.id.text_empty);
        emptyOther = findViewById(R.id.text_empty_other);

        task = getIntent().getParcelableExtra("task");
        task.setStageTasks(getIntent().getParcelableArrayListExtra("stages"));

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

            if (task != null) {
                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                LinearLayoutManager llmOther = new LinearLayoutManager(getApplicationContext());
                llmOther.setOrientation(LinearLayoutManager.VERTICAL);

                ArrayList<StageTask> yourTasks=new ArrayList<>();
                ArrayList<StageTask> otherTasks=new ArrayList<>();
                String id= FirebaseAuth.getInstance().getUid();
                for (StageTask t: task.getStageTasks()) {
                    if(t.getUsersIds().contains(id)){
                        yourTasks.add(t);
                    }
                    else{
                        otherTasks.add(t);
                    }
                }
                stageTasks.setLayoutManager(llm);
                stageTaskAdapter = new StageTaskAdapter(yourTasks);
                stageTasks.setAdapter(stageTaskAdapter);

                stageTasksOther.setLayoutManager(llmOther);
                stageTaskAdapterOther = new StageTaskAdapter(otherTasks);
                stageTasksOther.setAdapter(stageTaskAdapterOther);

                if(yourTasks.size()==0){
                    empty.setVisibility(View.VISIBLE);
                } else if (otherTasks.size()==0) {
                    emptyOther.setVisibility(View.VISIBLE);
                }

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



