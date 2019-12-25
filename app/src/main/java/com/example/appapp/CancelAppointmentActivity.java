//they can see and cancel the appointment which they made by clicking button.

package com.example.appapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class CancelAppointmentActivity extends AppCompatActivity {
    private FirebaseFirestore db;//Database
    private FirebaseUser user;
    TextView dateInfoText;
    Button cancelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_appointment);
        user= FirebaseAuth.getInstance().getCurrentUser();
        db=FirebaseFirestore.getInstance();
        dateInfoText = findViewById(R.id.dateInfoText);
        cancelButton = findViewById(R.id.cancelButton);
        dateInfoText.setText( "Your appointment date is : "  + MainActivity.date);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Dates").document(user.getUid()).delete();
                DocumentReference docRef =db.collection("users").document(user.getUid());
                RegisterActivity.userMap.put("Date","empty date");
                docRef.update(RegisterActivity.userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"deletion completed",Toast.LENGTH_SHORT).show();
                            if (MainActivity.willPostponed){
                            Intent intent = new Intent(getApplicationContext(),MakeAppointmentActivity.class);
                            startActivity(intent);
                        }
                            else{
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                            }
                        }
                        else Toast.makeText(getApplicationContext(),"some problems occurred",Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}
