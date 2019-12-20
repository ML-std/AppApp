//the user needs to fill these blanks = name,surname,ID,mail and password.
// If the user signed with google,then they don't need to fill mail and password.
//When they clicked the button, their data will be added to their database.
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

    Button registerButton;
    EditText idText,nameText,surnameText,mailText,passwordText;
     static   String name,surname,mail,password;
     static Map<String,Object> userMap= new HashMap<>();
    Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerButton = findViewById(R.id.registerButton);
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
            mailText.setEnabled(false);
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = Long.parseLong(idText.getText().toString());
                if (DigitCounter(id)==11){
                System.out.println(id);
                name = nameText.getText().toString();
                surname = surnameText.getText().toString();
                //adding data to a HashMap.
                userMap.put("ID",id);
                userMap.put("Name",name);
                userMap.put("Surname",surname);
                userMap.put("Date","empty date");
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

               else  {
                   //adding HashMap to the database.
                   db.collection("users").document(user.getUid()).set(userMap)
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
    private long DigitCounter(long n){ {
        //method for counting ID number
            int count = 0;
            while (n != 0) {
                n = n / 10;
                ++count;
            }
            return count;
        }
    }
}
