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
    TextView infoText;
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
                        System.out.println(document.getData());
                       String str = document.get("ID").toString();
                       date=document.get("Date").toString();
                        System.out.println(str);
                        info= "welcome " +str;
                        infoText.setText(info);

                    } else {
                        Toast.makeText(getApplicationContext(),"some problems has occurred!",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"some problems has occurred at task.",Toast.LENGTH_LONG).show();
                }
            }
        });

        makeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MakeAppointmentActivity.class);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CancelAppointmentActivity.class);
            }
        });
        postponeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                willPostponed=true;
                startActivity(CancelAppointmentActivity.class);
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
   private void startActivity(Class c){
        //to go to another activity.
        Intent intent = new Intent(getApplicationContext(),c);
        startActivity(intent);

    }
}