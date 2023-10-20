package com.example.clara.contask;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clara.contask.chat.NotificationChat;
import com.example.clara.contask.model.Campaign;
import com.example.clara.contask.model.Chat;
import com.example.clara.contask.model.Message;
import com.example.clara.contask.model.StageTask;
import com.example.clara.contask.model.Task;
import com.example.clara.contask.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {


    private Uri selectedPhoto;
    private Button buttonRegister;
    private Button buttonPhoto;
    private CircleImageView circlePhoto;
    private EditText editTextName;
    private EditText editTextLogin;
    private EditText editTextPassword;
    private EditText editTextPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonPhoto = (Button) findViewById(R.id.buttonPhoto);
        circlePhoto = findViewById(R.id.circlePhoto);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextLogin = (EditText) findViewById(R.id.editTextEmailAddress);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPasswordConfirm = (EditText) findViewById(R.id.editTextPasswordConfirm);

        clickRegister();
        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }

        });


    }

    private void clickRegister() {
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String login = editTextLogin.getText().toString();
                String password = editTextPassword.getText().toString();
                String passwordConfirm = editTextPasswordConfirm.getText().toString();

                if (selectedPhoto == null || name == null || login == null || password == null
                        || passwordConfirm == null || name.isEmpty() || login.isEmpty()
                        || password.isEmpty() || passwordConfirm.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.check_fields), Toast.LENGTH_SHORT).show();
                } else if (!password.equals(passwordConfirm)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.password_diferent), Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(login, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.sucess_register), Toast.LENGTH_SHORT).show();
                                    System.out.println(authResult.getUser().getUid());
                                    String filename = UUID.randomUUID().toString();
                                    final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + filename);
                                    ref.putFile(selectedPhoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Log.i("Url photo", uri.toString());
                                                    String id = FirebaseAuth.getInstance().getUid();
                                                    User user = new User(id, name, uri.toString());
                                                    FirebaseFirestore.getInstance().collection("users").document(id).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Intent main = new Intent(RegisterActivity.this, MainActivity.class);
                                                            main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                                            startService(new Intent(RegisterActivity.this, AbelhaService.class));
                                                            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                                                                @Override
                                                                public void onSuccess(String s) {
                                                                    user.setUserToken(s);
                                                                    createSimulationData(user);
                                                                }
                                                            });

                                                            startActivity(main);
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.i("Error", e.getMessage(), e);
                                                        }
                                                    });
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
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (e.getMessage().equals("The email address is badly formatted.")) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
                                    } else if (e.getMessage().equals("The given password is invalid. [ Password should be at least 6 characters ]")) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), getString(R.string.error_register), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }


            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            selectedPhoto = data.getData();
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedPhoto);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                circlePhoto.setImageDrawable(bitmapDrawable);
                buttonPhoto.setAlpha(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createSimulationData(User user) {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        Query query = rootRef.collection("/campaigns")
                .orderBy("begin", Query.Direction.DESCENDING)
                .limit(1);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size() == 0) {
                    createCampaignForSimulation(user);
                } else {
                    Campaign lastCampaign = queryDocumentSnapshots.getDocuments().get(0).toObject(Campaign.class);
                    if (lastCampaign.getUsersIds().size()<4){
                        lastCampaign.getUsersIds().add(user.getUserId());
                        lastCampaign.getTasks().get(0).getUsersIds().add(user.getUserId());





                        lastCampaign.getMessagesFeed().add(new Message("users/0", user.getUserName()+" entrou para a campanha", System.currentTimeMillis(), null));

                        String chatCampaignId= lastCampaign.getChatId();
                        String chatTaskId= lastCampaign.getTasks().get(0).getChatId();
                        String chatStageTaskIndvId;
                        String chatStageTaskGroupId;
                        if(lastCampaign.getTasks().get(0).getStageTasks().get(0).getTitle().contains("individual")){
                             chatStageTaskIndvId= lastCampaign.getTasks().get(0).getStageTasks().get(0).getChatId();
                             chatStageTaskGroupId= lastCampaign.getTasks().get(0).getStageTasks().get(1).getChatId();
                            lastCampaign.getTasks().get(0).getStageTasks().get(1).getUsersIds().add(user.getUserId());
                        }
                        else{
                            lastCampaign.getTasks().get(0).getStageTasks().get(0).getUsersIds().add(user.getUserId());
                            chatStageTaskIndvId= lastCampaign.getTasks().get(0).getStageTasks().get(1).getChatId();
                             chatStageTaskGroupId= lastCampaign.getTasks().get(0).getStageTasks().get(0).getChatId();
                        }



                        FirebaseFirestore.getInstance().collection("/chats").document(chatCampaignId).update("usersIds",lastCampaign.getUsersIds());

                        FirebaseFirestore.getInstance().collection("/chats").document(chatTaskId).update("usersIds",lastCampaign.getUsersIds());

                        List<Message> messagesIndv = new ArrayList<>();
                        messagesIndv.add(new Message("users/0", "Bem vindos", System.currentTimeMillis(), null));
                        List<String> usersIds = new ArrayList<>();
                        usersIds.add("TFyOeZY4QXcxWUrxt0qMpV0wMit2");// adm id
                        usersIds.add(user.getUserId());
                        String photoCampaign = "https://firebasestorage.googleapis.com/v0/b/contask-ufba.appspot.com/o/images%2Fc907ddb6-5ed0-4ee3-8978-826e7fc25c1b?alt=media&token=372fd08b-3642-4e9f-ac7f-274a70b6b929";
                        Chat chatStageTask = new Chat("Chat mapear individual", messagesIndv, usersIds, UUID.randomUUID().toString(), lastCampaign.getCampaignId(), "Campaign",
                                photoCampaign,
                                new ArrayList<String>(), new ArrayList<String>());
                        List<String> descriptionIndiviPhotos = new ArrayList<>();
                        descriptionIndiviPhotos.add("https://firebasestorage.googleapis.com/v0/b/contask-ufba.appspot.com/o/images%2Fantes%20a.png?alt=media&token=19b0fe93-c002-4632-a537-c961bb738bfc");
                        descriptionIndiviPhotos.add("https://firebasestorage.googleapis.com/v0/b/contask-ufba.appspot.com/o/images%2Fdepois%20a.png?alt=media&token=b88bbf7d-ff7a-4d8a-bc03-96f784d8152f");
                        descriptionIndiviPhotos.add("https://firebasestorage.googleapis.com/v0/b/contask-ufba.appspot.com/o/images%2Fdesenho.jpg?alt=media&token=5822662c-4fa2-41b0-90f0-30f7fc6b3614");
                        descriptionIndiviPhotos.add("https://firebasestorage.googleapis.com/v0/b/contask-ufba.appspot.com/o/images%2Fsavephoto.jpeg?alt=media&token=fb95ea2a-6c9a-4269-8f8c-d93964ad56a4");

                        lastCampaign.getTasks().get(0).getStageTasks().add(new StageTask("Mapear individualmente",
                                "Levando em conta as categorias de destruição postadas na descrição da tarefa e as imagens de antes e depois do desatres que se encontram abaixo, sinalize na imagem com alguma forma geometrica colorida as areas que sofreram danos, utilize amarelo para dano intermediario e vermelho pra completamente destruido. \n \n " +
                                        "Utilize o link abaixo para realizar a sinalização: \n https://www.canva.com/design/DAFxz25y3wc/aRsKzexYyKqOw6mbrwQbEQ/view?utm_content=DAFxz25y3wc&utm_campaign=designshare&utm_medium=link&utm_source=publishsharelink&mode=preview  \n \n" +
                                        "- Acesse o link, clique em \"Usar em um novo modelo\", caso necessário efetue login, pode ser utilizando conta Google, Facebook etc... \n \n" +
                                        "- Clique no botão de \"+\" e procure a opção \"Desenho\" na barra inferior e realize as sinalizações\n \n" +
                                        "- Após realizar as sinalizações baixe a imagem como demonstrado na imagem abaixo e realize a entrega da etapa.\n", descriptionIndiviPhotos,
                                System.currentTimeMillis(), -1, chatStageTask.getChatId(), 1, usersIds));

                        FirebaseFirestore.getInstance().collection("/campaigns").document(lastCampaign.getCampaignId()).set(lastCampaign);

                        FirebaseFirestore.getInstance().collection("/chats").document(chatStageTask.getChatId()).set(chatStageTask);

                        FirebaseFirestore.getInstance().collection("/chats").document(chatStageTaskGroupId).update("usersIds",lastCampaign.getUsersIds());

                        FirebaseFirestore.getInstance().collection("/chats").document(chatStageTaskGroupId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Chat chatGroup= documentSnapshot.toObject(Chat.class);
                                chatGroup.getAllTokensMessagingUsers().add(user.getUserToken());
                                FirebaseFirestore.getInstance().collection("/chats").document(chatStageTaskGroupId).update("allTokensMessagingUsers",chatGroup.getAllTokensMessagingUsers());
                            }
                        });

                        if(lastCampaign.getUsersIds().size()==4){
                            FirebaseFirestore.getInstance().collection("/chats").document(chatStageTaskGroupId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                 Chat chatGroup= documentSnapshot.toObject(Chat.class);
                                 chatGroup.getMessages().add(new Message("users/0", "O grupo esta completo comecem a tarefa.", System.currentTimeMillis(), null));
                                    FirebaseFirestore.getInstance().collection("/chats").document(chatStageTaskGroupId).update("messages",chatGroup.getMessages());
                                    NotificationChat newNotification = new NotificationChat(chatGroup.getAllTokensMessagingUsers(),
                                            chatGroup.getNameChat(),
                                            "O grupo esta completo comecem a tarefa.", chatGroup.getChatId());
                                    FunctionsUtil.sendNotificationChat(newNotification);

                                }
                            });
                        }

                        FirebaseFirestore.getInstance().collection("/chats").document(chatStageTaskGroupId).update("usersIds",lastCampaign.getUsersIds());


                    }
                    else{
                        createCampaignForSimulation(user);
                    }
                }


            }
        });
    }

    private void createCampaignForSimulation(User user) {
        List<String> usersTokens = new ArrayList<>();
        usersTokens.add(user.getUserToken());

        List<Message> messagesFeed = new ArrayList<>();
        messagesFeed.add(new Message("users/0", "Bem vindos, essa campanha tem como objetivo mapear pontos de destruição " +
                "em fotos de satélite para o treinamento de uma rede neural.", System.currentTimeMillis(), null));
        messagesFeed.add(new Message("users/0", "Na aba de tarefas está disponível a tarefa de mapeamento, ela contem duas etapas, o mapeamento individual, onde cada um deve realizar o mapeamento individualmente e a etapa de mapeamento em grupo onde o grupo deve discutir e comparar os resultados de cada um para aprimorarem o resultado. Mais detalhes estão disponíveis na descrição das etapas. ", System.currentTimeMillis(), null));
        messagesFeed.add(new Message("users/0", user.getUserName()+" entrou para a campanha", System.currentTimeMillis(), null));


        List<Message> messagesChatGroup = new ArrayList<>();
        messagesChatGroup.add(new Message("users/0", "Bem vindos", System.currentTimeMillis(), null));
        messagesChatGroup.add(new Message("users/0", "Aguardem o grupo estar completo para iniciarem a tarefa, quado o grupo estiver completo um aviso aparecerá aqui.", System.currentTimeMillis(), null));

        List<Message> messagesChats = new ArrayList<>();
        messagesChats.add(new Message("users/0", "Bem vindos", System.currentTimeMillis(), null));

        List<String> usersIds = new ArrayList<>();
        usersIds.add("TFyOeZY4QXcxWUrxt0qMpV0wMit2");// adm id
        usersIds.add(user.getUserId());
        UUID idCampaign = UUID.randomUUID();
        String photoCampaign = "https://firebasestorage.googleapis.com/v0/b/contask-ufba.appspot.com/o/images%2Fc907ddb6-5ed0-4ee3-8978-826e7fc25c1b?alt=media&token=372fd08b-3642-4e9f-ac7f-274a70b6b929";

        Chat chatCampaign = new Chat("Chat da campanha",  messagesChats, usersIds, UUID.randomUUID().toString(), idCampaign.toString(), "Campaign",
                photoCampaign,
                new ArrayList<String>(), usersTokens);
        Chat chatTask = new Chat("Chat da tarefa geral",  messagesChats, usersIds, UUID.randomUUID().toString(), idCampaign.toString(), "Campaign",
                photoCampaign,
                new ArrayList<String>(), usersTokens);
        Chat chatStageTask = new Chat("Chat mapear individual",  messagesChats, usersIds, UUID.randomUUID().toString(), idCampaign.toString(), "Campaign",
                photoCampaign,
                new ArrayList<String>(), usersTokens);
        Chat chatStageTaskGroup = new Chat("Chat mapear em grupo", messagesChatGroup, usersIds, UUID.randomUUID().toString(), idCampaign.toString(), "Campaign",
                photoCampaign,
                new ArrayList<String>(), usersTokens);

        List<StageTask> stageTasks = new ArrayList<>();
        List<String> descriptionIndiviPhotos = new ArrayList<>();
        descriptionIndiviPhotos.add("https://firebasestorage.googleapis.com/v0/b/contask-ufba.appspot.com/o/images%2Fantes%20a.png?alt=media&token=19b0fe93-c002-4632-a537-c961bb738bfc");
        descriptionIndiviPhotos.add("https://firebasestorage.googleapis.com/v0/b/contask-ufba.appspot.com/o/images%2Fdepois%20a.png?alt=media&token=b88bbf7d-ff7a-4d8a-bc03-96f784d8152f");
        descriptionIndiviPhotos.add("https://firebasestorage.googleapis.com/v0/b/contask-ufba.appspot.com/o/images%2Fdesenho.jpg?alt=media&token=5822662c-4fa2-41b0-90f0-30f7fc6b3614");
        descriptionIndiviPhotos.add("https://firebasestorage.googleapis.com/v0/b/contask-ufba.appspot.com/o/images%2Fsavephoto.jpeg?alt=media&token=fb95ea2a-6c9a-4269-8f8c-d93964ad56a4");

        List<String> descriptionGroupPhotos = new ArrayList<>();
        descriptionGroupPhotos.add("https://firebasestorage.googleapis.com/v0/b/contask-ufba.appspot.com/o/images%2Fcompartilhar.jpeg?alt=media&token=492f6d83-f968-4bf8-a8a1-49c416e6b85c");
        descriptionGroupPhotos.add("https://firebasestorage.googleapis.com/v0/b/contask-ufba.appspot.com/o/images%2FpodeEditar.jpeg?alt=media&token=ef576c69-16e1-4c21-9f6f-c101f2c553ae");

        stageTasks.add(new StageTask("Mapear individualmente",
                "Levando em conta as categorias de destruição postadas na descrição da tarefa e as imagens de antes e depois do desatres que se encontram abaixo, sinalize na imagem com alguma forma geometrica colorida as areas que sofreram danos, utilize amarelo para dano intermediario e vermelho pra completamente destruido. \n \n " +
                        "Utilize o link abaixo para realizar a sinalização: \n https://www.canva.com/design/DAFxz25y3wc/aRsKzexYyKqOw6mbrwQbEQ/view?utm_content=DAFxz25y3wc&utm_campaign=designshare&utm_medium=link&utm_source=publishsharelink&mode=preview  \n \n" +
                        "- Acesse o link, clique em \"Usar em um novo modelo\", caso necessário efetue login, pode ser utilizando conta Google, Facebook etc... \n \n" +
                        "- Clique no botão de \"+\" e procure a opção \"Desenho\" na barra inferior e realize as sinalizações\n \n" +
                        "- Após realizar as sinalizações baixe a imagem como demonstrado na imagem abaixo e realize a entrega da etapa.\n", descriptionIndiviPhotos,
                System.currentTimeMillis(), -1, chatStageTask.getChatId(), 1, usersIds));
        stageTasks.add(new StageTask("Mapear em grupo",
                "Vocês devem discutir e analisar os resultados individuais de cada para gerarem um novo resultado conjunto, vocês podem postar seus resultados no chat da etapa em grupo e discutirem. \n" +
                        "Semelhante a etapa individual, utilizem o link abaixo para realizar a sinalização em grupo: \n https://www.canva.com/design/DAFxz25y3wc/aRsKzexYyKqOw6mbrwQbEQ/view?utm_content=DAFxz25y3wc&utm_campaign=designshare&utm_medium=link&utm_source=publishsharelink&mode=preview  \n \n" +
                        "É preciso que alguem crie o link para que todos possam editar em conjunto caso necessario, para isso:\n" +
                        "- Acesse o link, clique em \"Usar em um novo modelo\", caso necessário efetue login, pode ser utilizando conta Google, Facebook etc... \n \n" +
                        "- Clique no botão de compartilhar e após isso no botão \"Enviar link\" \n \n" +
                        "- Altere para que qualquer pessoa com o link possa acessar e que qualquer um possa editar, como demonstrado na imagem abaixo \n \n" +
                        "- Após realizar as sinalizações conjuntas, alguem baixe a imagem e realize a entrega da etapa. \n", descriptionGroupPhotos,
                System.currentTimeMillis(), -1, chatStageTaskGroup.getChatId(), 1, usersIds));

        List<Task> tasks = new ArrayList<>();
        List<String> descriptionTaskPhotos = new ArrayList<>();
        descriptionTaskPhotos.add("https://firebasestorage.googleapis.com/v0/b/contask-ufba.appspot.com/o/images%2Fcategorias.png?alt=media&token=0b5ea54f-0ffa-44de-986b-6623a0334070");
        tasks.add(new com.example.clara.contask.model.Task("Mapear Danos",
                "Realizar primeiramente o mapeamento individual e depois o mapeamento em grupo, para tal sigam as instruções de cada etapa. \n " +
                        "Segue abaixo a categoria para o dano em estruturas, estruturas com pouco dano devem ser sinalizadas com amarelo e estruturas destruídas ou com muito dano devem ser sinalizadas em vermelho.", System.currentTimeMillis(), -1,
                chatTask.getChatId(), usersIds, stageTasks, descriptionTaskPhotos));

        Campaign campaign = new Campaign("Mapeamento do desastre", messagesFeed, usersIds, tasks, idCampaign.toString(),
                photoCampaign, System.currentTimeMillis(), -1, chatCampaign.getChatId());
        FirebaseFirestore.getInstance().collection("/campaigns").document(campaign.getCampaignId()).set(campaign);
        FirebaseFirestore.getInstance().collection("/chats").document(chatCampaign.getChatId()).set(chatCampaign);
        FirebaseFirestore.getInstance().collection("/chats").document(chatTask.getChatId()).set(chatTask);
        FirebaseFirestore.getInstance().collection("/chats").document(chatStageTask.getChatId()).set(chatStageTask);
        FirebaseFirestore.getInstance().collection("/chats").document(chatStageTaskGroup.getChatId()).set(chatStageTaskGroup);

    }
}




