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

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;

    Button idButton;
    EditText idText,nameText,surnameText,mailText,passwordText;
     static   String name,surname,mail,password;
       static Map<String,Object> userMap;
    Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        idButton = findViewById(R.id.idButton);
        nameText = findViewById(R.id.nameText);
        surnameText = findViewById(R.id.surnameText);
        idText = findViewById(R.id.idText);
        mailText = findViewById(R.id.mailText);
        passwordText = findViewById(R.id.passwordText);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        user= mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user==null){
                   user= mAuth.getCurrentUser();
                }
            }
        };
        if (user!=null){
            mailText.setText(user.getEmail());
            passwordText.setVisibility(View.INVISIBLE);
            mailText.setEnabled(false);
        }

        idButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = Long.parseLong(idText.getText().toString());
                if (DigitCounter(id)==11){
                System.out.println(id);
                name = nameText.getText().toString();
                surname = surnameText.getText().toString();
                   userMap= new HashMap<>();
                userMap.put("ID",id);
                userMap.put("Name",name);
                userMap.put("Surname",surname);
                if (user==null) {
                        mail = mailText.getText().toString();
                        password = passwordText.getText().toString();
                        mAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                    startActivity(intent);

                                }
                            }
                        });
                }

//

               else  {db.collection("users").document(user.getUid()).set(userMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);}

                            }
                        ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Some Problems have occurred",Toast.LENGTH_LONG).show();
                    }
                });

            }}
            else{
                Toast.makeText(getApplicationContext(),"ID number should be 11 numbers",Toast.LENGTH_LONG).show();
                }
            }

        });



    }
    private long DigitCounter(long n){
        {
            int count = 0;
            while (n != 0) {
                n = n / 10;
                ++count;
            }
            return count;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
