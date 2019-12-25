//this is the activity that the user needs to choose what they want.
//they can make appointment,cancel or postpone if they want by clicking the buttons.

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
import com.google.firebase.firestore.FirebaseFirestore;
public class MainActivity extends AppCompatActivity {
    TextView infoText, dateText;
    Button makeButton,cancelButton,postponeButton,logoutButton;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    String info;
    static boolean willPostponed;
    static String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoText = findViewById(R.id.infoText);
        makeButton = findViewById(R.id.makeButton);
        cancelButton = findViewById(R.id.cancelButton);
        postponeButton = findViewById(R.id.postponeButton);
        logoutButton = findViewById(R.id.logoutButton);
        dateText = findViewById(R.id.dateText);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        willPostponed = false;


        DocumentReference documentReference = db.collection("users").document(user.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        String str = " ";
                        try {
                            str = document.get("Name").toString();
                            date=document.get("Date").toString();
                        }
                        catch (Exception e){
                        date = " empty date";
                        }

                        info= "Welcome, " +str;
                        infoText.setText(info);
                        if (!(date.equals("empty date")))
                        dateText.setText("Claimed Appointment Date : " + date);
                        else dateText.setText("You don't have an appointment");

                    } else {
                        Toast.makeText(getApplicationContext(),"Some problems have occurred!",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Some problems have occurred at task",Toast.LENGTH_LONG).show();
                }
            }
        });

        makeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (date.equals("empty date"))
                startActivity(MakeAppointmentActivity.class);
                else Toast.makeText(getApplicationContext(),"You already have an appointment",Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(date.equals("empty date")))
                startActivity(CancelAppointmentActivity.class);
                else Toast.makeText(getApplicationContext(),"You don't have an appointment",Toast.LENGTH_SHORT).show();
            }
        });
        postponeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                willPostponed=true;
                if (!(date.equals("empty date")))
                    startActivity(CancelAppointmentActivity.class);
                else Toast.makeText(getApplicationContext(),"You don't have an appointment",Toast.LENGTH_SHORT).show();
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(StartingActivity.class);
            }
        });

}
    void startActivity(Class c){
        //to go to another activity.
        Intent intent = new Intent(getApplicationContext(),c);
        startActivity(intent);
    }
}