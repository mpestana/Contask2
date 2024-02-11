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




}




