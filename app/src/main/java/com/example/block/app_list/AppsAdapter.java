package com.example.block.app_list;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.SwitchCompat;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder>{

    Context context1;
    List<String> stringList;
    Click listner;
    public static ArrayList<String> appChecked;
    public static ArrayList<String> newChecked;
    public interface Click
    {
        void checked(String name);
        void unchecked(String name);
    }

    public AppsAdapter(Context context, List<String> list,Click listener ){

        appChecked=new ArrayList<>();
        newChecked =new ArrayList<>();
        appChecked=MainActivity.list;
        context1 = context;
        listner=listener;
        stringList = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public CardView cardView;
        public ImageView imageView,timer;
        public TextView textView_App_Name;
        public TextView time;
        public SwitchCompat checkBox;

        public ViewHolder (View view){

            super(view);
            timer=view.findViewById(R.id.timer);
            cardView = (CardView) view.findViewById(R.id.card_view);
            imageView = (ImageView) view.findViewById(R.id.imageview);
            textView_App_Name = (TextView) view.findViewById(R.id.Apk_Name);
            checkBox=(SwitchCompat) view.findViewById(R.id.androidCheckBox);
            time=(TextView)view.findViewById(R.id.time);

            //textView_App_Package_Name = (TextView) view.findViewById(R.id.Apk_Package_Name);
        }
    }

    @Override
    public AppsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view2 = LayoutInflater.from(context1).inflate(R.layout.cardview_layout,parent,false);

        ViewHolder viewHolder = new ViewHolder(view2);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position){

        ApkInfoExtractor apkInfoExtractor = new ApkInfoExtractor(context1);

        final String ApplicationPackageName = (String) stringList.get(position);
        String ApplicationLabelName = apkInfoExtractor.GetAppName(ApplicationPackageName);
        Drawable drawable = apkInfoExtractor.getAppIconByPackageName(ApplicationPackageName);
        viewHolder.textView_App_Name.setText(ApplicationLabelName);

        //viewHolder.textView_App_Package_Name.setText(ApplicationPackageName);

        viewHolder.imageView.setImageDrawable(drawable);

        //Adding click listener on CardView to open clicked application directly from here .
        viewHolder.timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(context1, Main2Activity.class);
                context1.startActivity(intent2);
            }
        });
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = context1.getPackageManager().getLaunchIntentForPackage(ApplicationPackageName);
//                if(intent != null){
//
//                    context1.startActivity(intent);
//
//
//                }
//                else {
//
//                    Toast.makeText(context1,ApplicationPackageName + " Error, Please Try Again.", Toast.LENGTH_LONG).show();
//                }
            }
        });
        viewHolder.checkBox.setTag(position);
        viewHolder.time.setTag(position);
        if(MainActivity.list.contains(ApplicationPackageName))

        {
            viewHolder.time.setText("00.00.00");
            for(HashMap<String,ArrayList<Long>> a : MainActivity.timelist) {

                if (a.containsKey(ApplicationPackageName)) {
                    ArrayList<Long> timelist=  a.get(ApplicationPackageName);
                    long starttime=  timelist.get(0);
                    long stoptime= timelist.get(1);
                    long time= System.currentTimeMillis();
                    if(time>starttime && time <stoptime)
                    {
                        long temp=  stoptime-time;
                        String timer = String.format("%02d:%02d:%02d",

                                TimeUnit.MILLISECONDS.toHours(temp),
                                TimeUnit.MILLISECONDS.toMinutes(temp) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(temp)), TimeUnit.MILLISECONDS.toSeconds(temp) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(temp)));

                        viewHolder.time.setText(timer);
                    }
                    else if(time<starttime)
                    {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");

                        // Create a calendar object that will convert the date and time value in milliseconds to date.
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(starttime);
                        viewHolder.time.setText("Start on "+formatter.format(calendar.getTime()));
                    }
//
//                    long millis = Long.valueOf(a.get(ApplicationPackageName));
//                    millis=millis-time;
//
//                    String timer = String.format("%02d:%02d:%02d",
//
//                            TimeUnit.MILLISECONDS.toHours(millis),
//                            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
//
//                    viewHolder.time.setText(timer);
                }
            }
            viewHolder.checkBox.setChecked(true);


        }
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {


                    if(!MainActivity.list.contains(ApplicationPackageName))
                    {
                        newChecked.add(ApplicationPackageName);
                    }
                    appChecked.add(ApplicationPackageName);
                    MainActivity.list=appChecked;
                    //listner.checked(ApplicationPackageName);
                    SharedPreferences prefs = context1.getSharedPreferences("packagePref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    try {
                        editor.putString("package", ObjectSerializer.serialize(appChecked));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    editor.commit();

                }
                else
                {
                    viewHolder.time.setText("");
                    ArrayList<HashMap<String,ArrayList<Long>>> aa =new ArrayList<>();
                    aa.addAll(MainActivity.timelist);
                    for(HashMap<String,ArrayList<Long>> a : MainActivity.timelist)
                    {
                        a.containsKey(ApplicationPackageName);
                        aa.remove(a);

                    }
                    Toast.makeText(context1,"App is Unblocked",Toast.LENGTH_SHORT).show();
                    appChecked.remove(ApplicationPackageName);
                    MainActivity.list=appChecked;
                    SharedPreferences prefs = context1.getSharedPreferences("packagePref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    try {
                        editor.putString("package", ObjectSerializer.serialize(appChecked));
                        editor.putString("time", ObjectSerializer.serialize(aa));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    editor.commit();
                }
            }
        });
    }

    @Override
    public int getItemCount(){

        return stringList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}