//the user logs in with their password and mail.
//if they don't have data or it needs to be updated, it will updated.
//when the user logs in, they go to MainActivity.
package com.example.appapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.appapp.RegisterActivity.userMap;

public class LoginActivity extends AppCompatActivity {
    EditText emailText,passwordText;
    Button loginButton;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String email,password;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        loginButton = findViewById(R.id.loginButton);
        mAuth=FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=emailText.getText().toString();
                password=passwordText.getText().toString();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            user=mAuth.getCurrentUser();
                            db.collection("users").document(user.getUid()).set(userMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                              @Override
                                                              public void onSuccess(Void aVoid) {

                                                                  Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                                                                  startActivity(intent);
                                                              }

                                                          }
                                    ).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"Some Problems have occurred",Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }
                });
            }
        });
    }
}
