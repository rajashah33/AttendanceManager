package com.example.attendencemanager.activities;

import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.attendencemanager.R;


public class AddSubjectActivity extends AppCompatActivity {
     Toast toast;
     DatabaseHelper myDb;
     String subjectName, present, absent, total;
     Button okay_butt;
     Vibrator vibrator;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_add_subject);
          vibrator = (Vibrator) getSystemService(MainActivity.VIBRATOR_SERVICE);
          myDb = new DatabaseHelper(this);
          okay_butt = findViewById(R.id.ok);
          okay_butt.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    vibrator.vibrate(50);
                    EditText subjectEdit = findViewById(R.id.subject_enter);
                    // EditText total_class = findViewById(R.id.class_enter);
                    EditText class_present = findViewById(R.id.class_present_enter);
                    EditText class_absent = findViewById(R.id.class_absent_enter);

                    if (subjectEdit.getText().length() == 0) {
                         subjectEdit.setError("Can't be Empty");                     //Subject Field left Empty
                    } else {


//               if (total_class.getText().length() == 0)                    //total class not entered
//                    total = "0";
//               else
//                    total = total_class.getText().toString();


                         if (class_present.getText().length() == 0)                  //present class not entered
                              present = "0";
                         else
                              present = class_present.getText().toString();


                         if (class_absent.getText().length() == 0)                   //absent class not entered
                              absent = "0";
                         else
                              absent = class_absent.getText().toString();


                         subjectName = subjectEdit.getText().toString();
                         AddData();

                    }
               }
          });
     }

     protected void onPause() {
          if (toast != null)
               toast.cancel();
          super.onPause();
     }

     public void AddData() {
          boolean isInserted = myDb.insertData(subjectName, present, absent, total);
          if (isInserted)
               displayToast("Subject Added");
          else
               displayToast("Can't add Subject");
     }

     public void displayToast(String message) {
          if (toast != null)
               toast.cancel();
          toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
          toast.show();
     }
}