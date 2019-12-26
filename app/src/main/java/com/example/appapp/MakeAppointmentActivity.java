//The user will select a date. If the date is invalid,the user will receive message and
//They will not use the date which they have just picked. when they pick a valid date and
//Clicked the button,then their data will be updated and they will return to the MainActivity.

package com.example.appapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import static com.example.appapp.RegisterActivity.userMap;

public class MakeAppointmentActivity extends AppCompatActivity {
    final private Calendar calendar = Calendar.getInstance();
    private FirebaseFirestore db;//Database
    private FirebaseUser user;
    EditText appointmentDate;
    Button dateButton;
    static boolean isValid =true;
    int day,month,year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);
         appointmentDate=  findViewById(R.id.appointmentDate);
         user = FirebaseAuth.getInstance().getCurrentUser();
         db=FirebaseFirestore.getInstance();
         dateButton = findViewById(R.id.dateButton);
        Calendar currentDate = Calendar.getInstance();
        day= currentDate.get(Calendar.DAY_OF_MONTH);
        month=currentDate.get(Calendar.MONTH);
        year = currentDate.get(Calendar.YEAR);
        appointmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
                if (isValidDate()){
                    updateLabel();
                }
                else if (!(appointmentDate.getText().toString().equals(""))){
                    Toast.makeText(getApplicationContext(),"invalid date",Toast.LENGTH_SHORT).show();
                }

            }
        });



         dateButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                    if (appointmentDate.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext()," You haven't selected a date",Toast.LENGTH_SHORT).show();
                    }
                    else {
                     updateData();
                     Toast.makeText(getApplicationContext(),"Appointment has been made",Toast.LENGTH_LONG).show();
                     Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                     startActivity(intent);
                 }}
         });



    }
    private void updateLabel() {
        //Updates the appointmentDate text with the date.
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        appointmentDate.setText(sdf.format(calendar.getTime()));
  }
    private boolean isValidDate(){
        //Reads data and returns true if the date which is selected by the user is not matches with
        //Other dates. returns false if the date matches.
        db.collection("Dates").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    try {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        System.out.println(document.get("Date").toString());
                        isValid =true;
                        if (!((document.get("Date").toString()).equals(appointmentDate.getText().toString()))){

                            isValid = true;
                        }
                        else {
                            isValid =false;
                            appointmentDate.setText("");
                           Toast.makeText(getApplicationContext(),"The date is already taken",Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }}
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Some problems occurred", Toast.LENGTH_LONG).show();
                    }
                }
                else Toast.makeText(getApplicationContext(),"Some problems have occurred",Toast.LENGTH_SHORT).show();

            }
        });
        return isValid;
    }
    private void updateData(){
        //Updates database using a HashMap.
        Map<String,Object> dateMap = new HashMap<>();
        dateMap.put("Date",appointmentDate.getText().toString());
        db.collection("Dates").document(user.getUid()).set(dateMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful())
                         Toast.makeText(getApplicationContext(),"Some problems occurred",Toast.LENGTH_LONG).show();

                    }
                });
        RegisterActivity.userMap.put("Date",appointmentDate.getText().toString());
        db.collection("users").document(user.getUid()).update(userMap);

    }
    private void showDateTimePicker(){
        //Shows date picker and restrains picking date from the past.
        //Updates the appointment date Text.
        final Calendar currentDate = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear +1;
                String tmp = dayOfMonth+"/"+month+"/" + year;
                if (isValidDate())
                appointmentDate.setText(tmp); }
        };
        DatePickerDialog datePickerDialog = new  DatePickerDialog(MakeAppointmentActivity.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),   currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
}
