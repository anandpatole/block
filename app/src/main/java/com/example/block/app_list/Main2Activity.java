package com.example.block.app_list;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.xw.repo.BubbleSeekBar;

import java.io.IOException;
import java.text.DateFormat;
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
    TextView from_time,to_time;
    CheckBox mon,tue,wed,thu,fri,sat,sun;
    CheckBox[] arrraych;
   public static int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.setFinishOnTouchOutside(false);
        btntest = (Button) findViewById(R.id.startbtn);
        hseek = (BubbleSeekBar)findViewById(R.id.HourSeek);
        mseek = (BubbleSeekBar)findViewById(R.id.MinSeek);
        from_time=(TextView)findViewById(R.id.from_time);
        to_time=(TextView)findViewById(R.id.to_time);

        from_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimepicker(from_time);
            }
        });
        mon=findViewById(R.id.edit_alarm_mon);
        tue=findViewById(R.id.edit_alarm_tues);
        wed=findViewById(R.id.edit_alarm_wed);
        thu=findViewById(R.id.edit_alarm_thurs);
        fri=findViewById(R.id.edit_alarm_fri);
        sat=findViewById(R.id.edit_alarm_sat);
        sun=findViewById(R.id.edit_alarm_sun);
        arrraych = new CheckBox[]{mon, tue, wed, thu, fri, sat, sun};
        to_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(from_time.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(Main2Activity.this,"Please Select from time",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    showTimepicker(to_time);
                }
            }
        });
        // showDatePicker();
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

                if(to_time.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(Main2Activity.this,"Please Select To Time",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(from_time.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(Main2Activity.this,"Please Select From Time",Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<String> days=new ArrayList<>();
                for( CheckBox c :arrraych)
                {
                    if(c.isChecked())
                    {
                        days.add(c.getTag().toString());
                    }
                }
                if(days.size()==0)
                {
                    Toast.makeText(Main2Activity.this,"Please Select Days",Toast.LENGTH_SHORT).show();
                    return;
                }
//                mins= mseek.getProgress();
//                hours=hseek.getProgress();
//
//                hours = hours*3600000;
//                mins = mins*60000;
//                duration = hours+mins;

                ArrayList<Long> aa=new ArrayList<>();


//                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//                Date dates = new Date();
//                date =dateFormat.format(dates);
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                String dateString = from_time.getText().toString();
                String dateString1=to_time.getText().toString();
                try{
                    //formatting the dateString to convert it into a Date
                    Date date = sdf.parse(dateString);
                    Date date1= sdf.parse(dateString1);
                    System.out.println("Given Time in milliseconds : "+date.getTime());

                    Calendar calendar = Calendar.getInstance();
                    //Setting the Calendar date and time to the given date and time
                    calendar.setTime(date);
                    System.out.println("Given Time in milliseconds : "+calendar.getTimeInMillis());
                    aa.add(calendar.getTimeInMillis());
                    calendar.setTime(date1);
                    //  aa.add(calendar.getTimeInMillis()+duration);
                    aa.add(calendar.getTimeInMillis());

                }catch(ParseException e){
                    e.printStackTrace();
                }



                Toast.makeText(Main2Activity.this, "Selected Apps are blocked", LENGTH_LONG).show();
                MainActivity.list=AppsAdapter.appChecked;
                SharedPreferences prefs = getSharedPreferences("packagePref", Context.MODE_PRIVATE);
                ArrayList<HashMap<String, ArrayList<String>>> daysListMap =new ArrayList<>();
                ArrayList<HashMap<String, ArrayList<Long>>> listmap = new ArrayList<>();
                try {
                    listmap.addAll((Collection<? extends HashMap<String, ArrayList<Long>>>) ObjectSerializer.deserialize(prefs.getString("time", ObjectSerializer.serialize(new ArrayList<HashMap<String,ArrayList<Long>>>()))));
                    daysListMap.addAll((Collection<? extends HashMap<String, ArrayList<String>>>) ObjectSerializer.deserialize(prefs.getString("days", ObjectSerializer.serialize(new ArrayList<HashMap<String,ArrayList<String>>>()))));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (String a: MainActivity.list)
                {
                    if(AppsAdapter.newChecked.contains(a))
                    {
                        HashMap<String,ArrayList<String>> ds=new HashMap<>();
                        HashMap<String, ArrayList<Long>> map = new HashMap<>();
                        map.put(a,aa);
                        ds.put(a,days);
                        daysListMap.add(ds);
                        listmap.add(map);
                    }
                }


                SharedPreferences.Editor editor = prefs.edit();
                try {
                    editor.putString("time", ObjectSerializer.serialize(listmap));
                    editor.putString("newChecked",ObjectSerializer.serialize(AppsAdapter.newChecked));
                    editor.putString("days",ObjectSerializer.serialize(daysListMap));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                editor.commit();
                startService(duration);
                if(flag==0) {
                    if (AppsAdapter.newChecked.contains("com.whatsapp")) {
                        Intent intent = new Intent();
                        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
                            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                            intent.putExtra("android.provider.extra.APP_PACKAGE", "com.whatsapp");
                        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                            intent.putExtra("app_package", "com.whatsapp");
                            // intent.putExtra("app_uid",);
                        } else {
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + "com.whatsapp"));

                        }
                        flag=1;
                        startActivityForResult(intent,01);
                        return;
                    }
                }

                startActivity(new Intent(Main2Activity.this,MainActivity.class));
                finish();
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

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            startForegroundService(intent);
        }
        else
        {
            startService(intent);
        }


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
                        showTimepicker(from_time);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        datePickerDialog.setCancelable(false);
    }

    public void showTimepicker(final TextView Tview)
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
                        String AM_PM ;
                        if(hourOfDay < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }
                        boolean isPM = (hourOfDay >= 12);
                        ((TextView)Tview).setText(String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));
                        //((TextView)Tview).setText(hourOfDay + ":" + minute + " " + AM_PM );
                        time =(hourOfDay + ":" + minute + " "+AM_PM);

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
        timePickerDialog.setCancelable(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        startActivity(new Intent(Main2Activity.this,MainActivity.class));
        finish();
    }
}