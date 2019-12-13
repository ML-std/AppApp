package com.example.appapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Button idButton;
    EditText idText,nameText,surnameText,mailText,passwordText;
    String name,surname;
    int id;

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
        if (user!=null){
            mailText.setText(user.getEmail());
            passwordText.setVisibility(View.INVISIBLE);
        }

        idButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = Integer.parseInt(idText.getText().toString());
                name = nameText.getText().toString();
                surname = surnameText.getText().toString();
                Map<String,Object> userMap= new HashMap<>();
                userMap.put("ID",id);
                userMap.put("Name",name);
                userMap.put("Surname",surname);

                db.collection("users").document(user.getUid()+ " lol").set(userMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Some Problems have occurred",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });



    }
}
