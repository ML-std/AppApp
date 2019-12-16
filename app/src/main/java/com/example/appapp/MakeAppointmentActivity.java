package com.example.appapp;
//so far we have done validation of the date,but we want to not see if it is a invalid date in the text

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

import static com.example.appapp.RegisterActivity.userMap;

public class MakeAppointmentActivity extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    EditText appointmentDate;
    FirebaseUser user;
    FirebaseFirestore db;
    Button dateButton;
    static boolean flag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);
         appointmentDate=  findViewById(R.id.appointmentDate);
         user = FirebaseAuth.getInstance().getCurrentUser();
         db=FirebaseFirestore.getInstance();
         dateButton = findViewById(R.id.dateButton);

         dateButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (readData()) {
                     updateData();
                     Toast.makeText(getApplicationContext(),"Appointment has been made",Toast.LENGTH_LONG).show();
                     Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                     startActivity(intent);
                 }
             //    else   Toast.makeText(getApplicationContext(),"invalid date",Toast.LENGTH_LONG).show();

             }
         });

       final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (readData())
                updateLabel();


            }

        };
        appointmentDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MakeAppointmentActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        appointmentDate.setText(sdf.format(myCalendar.getTime()));
    }
    private boolean readData(){

        db.collection("Dates").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    try {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        System.out.println(document.get("Date").toString());
                        flag=true;
                        if (!((document.get("Date").toString()).equals(appointmentDate.getText().toString()))){
                            flag= true;
                        }
                        else {
                            flag=false;
                            appointmentDate.setText(" ");
                          //  Toast.makeText(getApplicationContext(),"invalid date",Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }}
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(),"some problems occurred", Toast.LENGTH_LONG).show();
                    }
                }
                else Toast.makeText(getApplicationContext(),"omg",Toast.LENGTH_SHORT).show();

            }
        });
        return flag;
    }
    private void updateData(){
        Map<String,Object> dateMap = new HashMap<>();
        dateMap.put("Date",appointmentDate.getText().toString());
        db.collection("Dates").document(user.getUid()).set(dateMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"we have updated",Toast.LENGTH_LONG).show();
                        }
                        else Toast.makeText(getApplicationContext(),"Some problems occurred",Toast.LENGTH_LONG).show();

                    }
                });
        RegisterActivity.userMap.put("Date",appointmentDate.getText().toString());
        db.collection("users").document(user.getUid()).update(userMap);
    }
}
