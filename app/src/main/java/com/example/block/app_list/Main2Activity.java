package com.example.block.app_list;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.xw.repo.BubbleSeekBar;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import static android.widget.Toast.LENGTH_LONG;

public class Main2Activity extends AppCompatActivity {

    private Button btntest;
    private TextView timer;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public BubbleSeekBar mseek,hseek;
    public int hours, mins, duration;
    String date="";
    String time="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.setFinishOnTouchOutside(false);
        btntest = (Button) findViewById(R.id.startbtn);
        hseek = (BubbleSeekBar)findViewById(R.id.HourSeek);
        mseek = (BubbleSeekBar)findViewById(R.id.MinSeek);
        showDatePicker();
        hseek.getConfigBuilder()
                .min(0)
                .max(12)
                .sectionCount(12)
                .trackColor(ContextCompat.getColor(this, R.color.black))
                .secondTrackColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .thumbColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .showSectionText()
                .sectionTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .sectionTextSize(18)
                .showThumbText()
                .thumbTextColor(ContextCompat.getColor(this, R.color.white))
                .thumbTextSize(18)
                .bubbleColor(ContextCompat.getColor(this, R.color.green))
                .bubbleTextSize(18)
                .showSectionMark()
                .seekStepSection()
                .touchToSeek()
                .sectionTextPosition(BubbleSeekBar.TextPosition.BELOW_SECTION_MARK)
                .build();

        hseek.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            int shours;
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                shours = progress;
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                hours = shours;
            }
        });

        mseek.getConfigBuilder()
                .min(0)
                .max(60)
                .sectionCount(10)
                .trackColor(ContextCompat.getColor(this, R.color.black))
                .secondTrackColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .thumbColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .showSectionText()
                .sectionTextColor(ContextCompat.getColor(this, R.color.white))
                .sectionTextSize(18)
                .showThumbText()
                .thumbTextColor(ContextCompat.getColor(this, R.color.white))
                .thumbTextSize(18)
                .bubbleColor(ContextCompat.getColor(this, R.color.green))
                .bubbleTextSize(18)
                .showSectionMark()
                .seekStepSection()
                .touchToSeek()
                .sectionTextPosition(BubbleSeekBar.TextPosition.BELOW_SECTION_MARK)
                .build();

        mseek.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            int smins;
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                smins = progress;
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                mins = smins;
            }
        });
//        mseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            int smins;
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                smins = progress;
//            }
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                mins = smins;
//            }
//        });
        //button start
        btntest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mins= mseek.getProgress();
                hours=hseek.getProgress();

                hours = hours*3600000;
                mins = mins*60000;
                duration = hours+mins;

                ArrayList<Long> aa=new ArrayList<>();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                String dateString = date+" "+time;
                try{
                    //formatting the dateString to convert it into a Date
                    Date date = sdf.parse(dateString);
                    System.out.println("Given Time in milliseconds : "+date.getTime());

                    Calendar calendar = Calendar.getInstance();
                    //Setting the Calendar date and time to the given date and time
                    calendar.setTime(date);
                    System.out.println("Given Time in milliseconds : "+calendar.getTimeInMillis());
                    aa.add(calendar.getTimeInMillis());
                    aa.add(calendar.getTimeInMillis()+duration);

                }catch(ParseException e){
                    e.printStackTrace();
                }
                if(duration==0)
                {
                    Toast.makeText(Main2Activity.this, "Please Select Time", LENGTH_LONG).show();
                }

                else {
                    Toast.makeText(Main2Activity.this, "Selected Apps are blocked", LENGTH_LONG).show();
                    MainActivity.list=AppsAdapter.appChecked;
                    SharedPreferences prefs = getSharedPreferences("packagePref", Context.MODE_PRIVATE);

                    ArrayList<HashMap<String, ArrayList<Long>>> listmap = new ArrayList<>();
                    try {
                        listmap.addAll((Collection<? extends HashMap<String, ArrayList<Long>>>) ObjectSerializer.deserialize(prefs.getString("time", ObjectSerializer.serialize(new ArrayList<HashMap<String,ArrayList<Long>>>()))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    for (String a: MainActivity.list)
                    {
                        if(AppsAdapter.newChecked.contains(a))
                        {
                            HashMap<String, ArrayList<Long>> map = new HashMap<>();
                            map.put(a,aa);
                            listmap.add(map);
                        }
                    }


                    SharedPreferences.Editor editor = prefs.edit();
                    try {
                        editor.putString("time", ObjectSerializer.serialize(listmap));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    editor.commit();
                    startService(duration);
                    startActivity(new Intent(Main2Activity.this,MainActivity.class));
                    finish();
                }

            }
        });

        if (!NotificationManagerCompat.getEnabledListenerPackages (getApplicationContext()).contains(getApplicationContext().getPackageName())) {
            Toast.makeText(Main2Activity.this, "Please Enable Notification Access", Toast.LENGTH_LONG).show();
            //service is not enabled try to enabled by calling...
            getApplicationContext().startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {


        }
    }

    //saving data and passing intent to service
    public void startService(int duration)
    {

        int d = duration;
        SharedPreferences sharedPreferences = getSharedPreferences("Timer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("duration", d);
        editor.apply();

        Intent intent = new Intent(this,MyService.class);
        startService(intent);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences prefs = getSharedPreferences("packagePref", Context.MODE_PRIVATE);
        try {
            ArrayList<String>   currentTasks = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString("package", ObjectSerializer.serialize(new ArrayList<String>())));
            for (String a : AppsAdapter.newChecked)
            {
                if(currentTasks.contains(a))
                {
                    currentTasks.remove(a);
                }

            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("package", ObjectSerializer.serialize(currentTasks));
            editor.commit();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showDatePicker()
    {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        date =(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        showTimepicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        datePickerDialog.setCancelable(false);
    }

    public void showTimepicker()
    {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        time =(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
        timePickerDialog.setCancelable(false);
    }
}