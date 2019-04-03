package com.example.attendencemanager.activities;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendencemanager.R;
import com.example.attendencemanager.activities.adapter.MyAdapter;
import com.example.attendencemanager.activities.model.Model;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnRawClickListener, MyAdapter.OnButtonClickListener, MyAdapter.OnLongHoldListener {

     FloatingActionButton fab;
     private RecyclerView recyclerView;
     Toast toast;
     DatabaseHelper myDb;
     Vibrator vibrator;
     public ArrayList<Model> arrayList = new ArrayList<>();

     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);
          vibrator = (Vibrator) getSystemService(MainActivity.VIBRATOR_SERVICE);
          //
          fab = findViewById(R.id.fab);
          fab.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    vibrator.vibrate(50);
                    Intent i = new Intent(v.getContext(), AddSubjectActivity.class);
                    startActivity(i);
               }
          });
          //

          recyclerView = findViewById(R.id.recycler_view);
          myDb = new DatabaseHelper(this);

          addDataInRaw();
     }

     @Override
     public void onRestart() {
          arrayList.clear();
          addDataInRaw();
          super.onRestart();
          //When BACK BUTTON is pressed, the activity on the stack is restarted
          //Do what you want on the refresh procedure here
     }

     @Override
     public void onRawClick(int position) {
//          Open calender for each raw

          Cursor res = myDb.getAData(position + 1);
          String subject_name;

          if (res.moveToNext()) {
               subject_name = res.getString(1);

               Intent intent = new Intent(this, CalendarActivity.class);
               intent.putExtra("subject_name", subject_name);
               startActivity(intent);
          }


     }

     @Override
     public void onButtonClick(int position, int PorA) {
          vibrator.vibrate(50);
          Cursor res = myDb.getAData(position + 1);
          String pos = "" + (position + 1);

          //check present or absent button clicked--- PorA=0/1:absent/present
          if (PorA == 1) {
               if (res.moveToNext()) {
                    //increment the database value of the desired ( present or absent )
                    String p = Integer.toString(Integer.parseInt(res.getString(2)) + 1);
                    if (!myDb.updateData("p", pos, res.getString(1), p, res.getString(3), res.getString(4)))
                         displayToast("Error in updating 1st or 2nd table");
               } else
                    displayToast("Error");
          } else {
               if (res.moveToNext()) {
                    String a = Integer.toString(Integer.parseInt(res.getString(3)) + 1);
                    if (!myDb.updateData("a", pos, res.getString(1), res.getString(2), a, res.getString(4)))
                         displayToast("Error in updating 1st or 2nd table");
               } else
                    displayToast("Error");
          }
          //called this to make update of putting the data from database to each raw again
          onRestart();
     }

     @Override
     public void onLongHold(final int position) {
          final Dialog dialog = new Dialog(this);
          dialog.setContentView(R.layout.dialog);
          TextView edit = dialog.findViewById(R.id.text1);
          edit.setText("Edit");
          TextView delete = dialog.findViewById(R.id.text2);
          delete.setText("Delete");


          edit.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    vibrator.vibrate(50);
                    dialog.dismiss();
                    dialog.setContentView(R.layout.activity_add_subject);
                    dialog.show();
                    final Cursor res = myDb.getAData(position + 1);

                    final EditText subject = dialog.findViewById(R.id.subject_enter);
                    final EditText present = dialog.findViewById(R.id.class_present_enter);
                    final EditText absent = dialog.findViewById(R.id.class_absent_enter);
                    final Button ok = dialog.findViewById(R.id.ok);

                    if (res.moveToNext()) {
                         subject.setText(res.getString(1));
                         present.setText(res.getString(2));
                         absent.setText(res.getString(3));
                         ok.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                   myDb.updateData("no", Integer.toString(position + 1),
                                           subject.getText().toString(),
                                           present.getText().toString(),
                                           absent.getText().toString(),
                                           res.getString(4)
                                   );
                                   displayToast("Done");
                                   dialog.dismiss();
                                   onRestart();
                              }

                         });

                    } else
                         displayToast("Error");
               }
          });
          delete.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    vibrator.vibrate(50);
                    dialog.dismiss();
                    final Cursor res = myDb.getAData(position + 1);
                    if (res.moveToNext()) {
                         String pos = "" + (position + 1);
                         myDb.deleteData(pos);
                         onRestart();
                    } else
                         displayToast("Error");

               }
          });
          dialog.show();

     }


     public void addDataInRaw() {
          Cursor res = myDb.getAllData();
          if (res.getCount() == 0) {
               //No data present in database
               //showMessage("Error", "Nothing found");

          }
          while (res.moveToNext()) {
               int id = res.getInt(0);
               String sub = res.getString(1);
               String p = res.getString(2);
               String a = res.getString(3);
               String t = res.getString(4);
               Model bean1 = new Model(id, sub, p, a, t);
               arrayList.add(bean1);
          }
          MyAdapter myAdapter = new MyAdapter(arrayList, this, this, this);
          RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
          recyclerView.setLayoutManager(layoutManager);
          recyclerView.setItemAnimator(new DefaultItemAnimator());
          recyclerView.setAdapter(myAdapter);

     }

     public final void displayToast(String message) {
          if (toast != null)
               toast.cancel();
          toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
          toast.show();
     }
}