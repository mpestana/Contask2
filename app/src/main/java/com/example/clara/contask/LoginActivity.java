package com.example.clara.contask;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by clara on 27/11/2016.
 */

public class LoginActivity  extends AppCompatActivity {
    TextView textViewRegister;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);

        textViewRegister=  findViewById(R.id.textRegister);
        Button buttonLogin=  findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EditText edit = (EditText)findViewById(R.id.editText);
                //String content = edit.getText().toString();
                EditText editTextLogin = (EditText)findViewById(R.id.editTextEmailAddress);
                EditText editTextPassword = (EditText)findViewById(R.id.editTextPassword);

                String login = editTextLogin.getText().toString();
                String password = editTextPassword.getText().toString();

                FirebaseAuth.getInstance().signInWithEmailAndPassword(login,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent main = new Intent(LoginActivity.this, MainActivity.class);
                        main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        main.putExtra("name", "Teste User");
                        main.putExtra("surname", "");
                        main.putExtra("imageUrl", "");
                        startService(new Intent(LoginActivity.this, AbelhaService.class));
                        startActivity(main);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.failure_login), Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);


    }
}
