package com.example.attendencemanager.activities;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendencemanager.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.CalendarDayEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

     Toast toast;
     CompactCalendarView compactCalendarView;
     TextView monthHeading;
     DatabaseHelper myDb;
     private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
     private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());

     String subject_name;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_calendar);

          myDb = new DatabaseHelper(this);
          if (getIntent().hasExtra("subject_name")) {
               subject_name = getIntent().getStringExtra("subject_name");
          }

          compactCalendarView = findViewById(R.id.compactcalendar_view);
          compactCalendarView.drawSmallIndicatorForEvents(true);
          compactCalendarView.setUseThreeLetterAbbreviation(true);

          monthHeading = findViewById(R.id.month_heading);
          monthHeading.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

          updateCalendarForSubject();

          compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
               @Override
               public void onDayClick(Date dateClicked) {

                    String selectedDay = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(dateClicked);
                    String status = myDb.getADataFromSubjectTable(subject_name, selectedDay);
                    switch (status) {
                         case "p":
                              displayToast("You were Present that Day");
                              break;
                         case "a":
                              displayToast("You were Absent that Day");
                              break;
                         default:
                              displayToast("No Class that day / not Entered");
                              break;
                    }
               }

               @Override
               public void onMonthScroll(Date firstDayOfNewMonth) {
                    updateCalendarForSubject();
               }
          });
     }


     void updateCalendarForSubject() {
          Date firstDayOfCurrentMonth = compactCalendarView.getFirstDayOfCurrentMonth();
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(firstDayOfCurrentMonth);
          calendar.add(Calendar.MONTH, 1);                     //moved to next month
          calendar.set(Calendar.DAY_OF_MONTH, 1);              //set the first dsy of that month
          calendar.add(Calendar.DATE, -1);                     //subtract 1 so to go to the last day of current month

          String lastDayOfMonth = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(calendar.getTime());
          Cursor res = myDb.getMonthDataFromSubjectTable(subject_name, lastDayOfMonth);


          while (res.moveToNext()) {
               int currDayCursor = Integer.parseInt(Integer.toString(res.getInt(0)).substring(6, 8));
               currentCalender.set(Calendar.DAY_OF_MONTH, currDayCursor);

               String status = res.getString(1);
               setToMidnight(currentCalender);
//               compactCalendarView.addEvent(new CalendarDayEvent(1554163200000L, Color.argb(250, 255, 0, 0)), false);
//               compactCalendarView.addEvent(new CalendarDayEvent(1554249600000L, Color.argb(250, 255, 0, 0)), false);
//               Toast.makeText(this, 1554249600000L + " " + status+ " "+currentCalender.getTimeInMillis(), Toast.LENGTH_LONG).show();

               switch (status) {
                    case "p":
                         compactCalendarView.addEvent(new CalendarDayEvent(currentCalender.getTimeInMillis(), Color.argb(250, 34, 139, 34)), false);
                         break;
                    case "a":
                         compactCalendarView.addEvent(new CalendarDayEvent(currentCalender.getTimeInMillis(), Color.argb(250, 255, 0, 0)), false);
                         break;
                    default:
                         break;
               }
               compactCalendarView.invalidate();
          }
     }

     private void setToMidnight(Calendar calendar) {
          calendar.set(Calendar.HOUR_OF_DAY, 0);
          calendar.set(Calendar.MINUTE, 0);
          calendar.set(Calendar.SECOND, 0);
          calendar.set(Calendar.MILLISECOND, 0);
     }

     public void gotoToday() {

          // Set any date to navigate to particular date
          compactCalendarView.setCurrentDate(Calendar.getInstance(Locale.getDefault()).getTime());

     }

     public final void displayToast(String message) {
          if (toast != null)
               toast.cancel();
          toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
          toast.show();
     }
}