package com.example.clara.contask.campaign;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.example.clara.contask.model.StageTasksAnswers;
import com.example.clara.contask.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StageTaskActivity extends AppCompatActivity {


    private TextView title;
    private TextView beginAndEnd;

    private View status;

    private TextView description;

    private Button deliverStageTask;
    private Button toApply;
    private Button addPhoto;
    private ArrayList<String> addedPhotosUrls = new ArrayList<>();
    private EditText deliveryText;
    private TextView photosCount;
    private LinearLayout layoutDescription;
    private RecyclerView participants;

    private ParticipantsAdapter participantsAdapter;
    private MutableLiveData<ArrayList<User>> usersMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<StageTasksAnswers> stageTaskAnswer = new MutableLiveData<>();
    private StageTask task;
    private LinearLayout layoutDelivery;

    private LinearLayout layoutDelivered;

    private TextView textDelivered;
    private String campaignId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_task);

        title = findViewById(R.id.title);
        beginAndEnd = findViewById(R.id.beginAndEnd);
        status = findViewById(R.id.status_circle);
        description = findViewById(R.id.description);
        participants = findViewById(R.id.list_item_participants);
        toApply = findViewById(R.id.buttonApply);
        deliverStageTask = findViewById(R.id.buttonDeliver);
        addPhoto = findViewById(R.id.buttonAddPhoto);
        deliveryText = findViewById(R.id.deliveryText);
        photosCount = findViewById(R.id.photosNames);
        task = getIntent().getParcelableExtra("stageTask");
        layoutDescription = findViewById(R.id.layout_description);
        layoutDelivery = findViewById(R.id.layout_delivery);
        layoutDelivered = findViewById(R.id.layout_delivered);
        textDelivered = findViewById(R.id.text_delivered);
        campaignId = getIntent().getParcelableExtra("campaignId");
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

        stageTaskAnswer.observe(this, new Observer<StageTasksAnswers>() {

            @Override
            public void onChanged(StageTasksAnswers answer) {
                if (answer!=null) {
                    textDelivered.setText(answer.getDeliveryText());
                    beginAndEnd.setText(getString(R.string.begin) + ": " + FunctionsUtil.getDateFormat(task.getBegin())+" | "
                            +getString(R.string.end)+": "+FunctionsUtil.getDateFormat(answer.getEnd()));
                    status.setBackground(status.getContext().getResources().getDrawable(R.drawable.circle_status_task_red));
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                    int width = displayMetrics.widthPixels;
                    for (int i = 0; i < answer.getDeliveryImagesUrls().size(); i++) {
                        ImageView image = new ImageView(getApplicationContext());
                        image.setMinimumWidth((int) (width * 0.5));
                        image.setMinimumHeight((int) (width * 0.5));
                        Picasso.get().load(answer.getDeliveryImagesUrls().get(i)).into(image);
                        int finalI = i;
                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getBaseContext(), ActivityFullScreenPhoto.class);
                                intent.putExtra("photoUrl", task.getDescriptionImagesUrls().get(finalI));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                getBaseContext().startActivity(intent);
                            }
                        });
                        layoutDelivered.addView(image);
                    }
                    layoutDelivered.setVisibility(View.VISIBLE);
                }
            }
        });

        deliverStageTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (addedPhotosUrls.size() < task.getRequirePhoto()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.request_photos) + " " + task.getRequirePhoto() + " " + getString(R.string.photos), Toast.LENGTH_SHORT).show();
                } else if (deliveryText.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.hint_delivery), Toast.LENGTH_SHORT).show();
                } else {
                    StageTasksAnswers answer = new StageTasksAnswers(addedPhotosUrls, deliveryText.getText().toString(),System.currentTimeMillis());

                    FirebaseFirestore.getInstance().collection("/stageTasksAnswers").document(task.getChatId()).set(answer).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            layoutDelivery.setVisibility(View.GONE);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), getString(R.string.faliure_delivery), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });


        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (data != null) {
                Uri selectedPhoto = data.getData();
                if (selectedPhoto != null)
                    uploadPhoto(selectedPhoto);
            }
        }
    }


    private void fillInformations() {

        if (task != null) {
            title.setText(task.getTitle());

            beginAndEnd.setText(getString(R.string.begin) + ": " + FunctionsUtil.getDateFormat(task.getBegin()));

            description.setText(task.getDescription());

            if (task.getUsersIds().contains(FirebaseAuth.getInstance().getUid())) {
                toApply.setVisibility(View.GONE);
            } else {
                deliverStageTask.setVisibility(View.GONE);
            }

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            int width = displayMetrics.widthPixels;
            for (int i = 0; i < task.getDescriptionImagesUrls().size(); i++) {
                ImageView image = new ImageView(getApplicationContext());
                image.setMinimumWidth((int) (width * 0.5));
                image.setMinimumHeight((int) (width * 0.5));
                Picasso.get().load(task.getDescriptionImagesUrls().get(i)).into(image);
                int finalI = i;
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(), ActivityFullScreenPhoto.class);
                        intent.putExtra("photoUrl", task.getDescriptionImagesUrls().get(finalI));
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

        FirebaseFirestore.getInstance().document("/stageTasksAnswers/"+task.getChatId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value!= null){
                    StageTasksAnswers a= value.toObject(StageTasksAnswers.class);
                    stageTaskAnswer.setValue(a);
                }
            }
        });


    }

    private void uploadPhoto(Uri selectedPhoto) {

        String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + filename);
        ref.putFile(selectedPhoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        addedPhotosUrls.add(uri.toString());
                        photosCount.setVisibility(View.VISIBLE);
                        photosCount.setText("Imagens Adicionadas: " + addedPhotosUrls.size());


                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Error", e.getMessage(), e);
            }
        });
    }
}



