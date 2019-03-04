package com.example.block.app_list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.widget.SwitchCompat;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import android.widget.Filter;
import android.widget.Filterable;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> implements Filterable {

    Context context1;
    List<String> stringList;
    List<String> filteredList;

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
        this. listner=listener;
        this.stringList = list;
        this.filteredList=list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public CardView cardView;
        public ImageView imageView,timer;
        public TextView textView_App_Name;
        public TextView time,days;
        public SwitchCompat checkBox;

        public ViewHolder (View view){

            super(view);
            timer=view.findViewById(R.id.timer);
            cardView = (CardView) view.findViewById(R.id.card_view);
            imageView = (ImageView) view.findViewById(R.id.imageview);
            textView_App_Name = (TextView) view.findViewById(R.id.Apk_Name);
            checkBox=(SwitchCompat) view.findViewById(R.id.androidCheckBox);
            time=(TextView)view.findViewById(R.id.time);
            days=view.findViewById(R.id.days);
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

        final String ApplicationPackageName = (String) filteredList.get(position);
        String ApplicationLabelName = apkInfoExtractor.GetAppName(ApplicationPackageName);
        Drawable drawable = apkInfoExtractor.getAppIconByPackageName(ApplicationPackageName);


        viewHolder.textView_App_Name.setText(ApplicationLabelName);

        //viewHolder.textView_App_Package_Name.setText(ApplicationPackageName);

        viewHolder.imageView.setImageDrawable(drawable);

        //Adding click listener on CardView to open clicked application directly from here .
        viewHolder.timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(viewHolder.checkBox.isChecked())
                {
                    Intent intent2 = new Intent(context1, Main2Activity.class);
                    context1.startActivity(intent2);
                    ((Activity)context1).finish();
                }
                else
                {
                    Toast.makeText(context1,"Please Activate Switch",Toast.LENGTH_SHORT).show();
                }
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
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String todaysDay= new SimpleDateFormat("EEE", Locale.ENGLISH).format(date.getTime());
        if(MainActivity.list.contains(ApplicationPackageName))

        {
            viewHolder.time.setText("00.00.00");
            for(HashMap<String,ArrayList<String>> a : MainActivity.dayslist)
            {
                if (a.containsKey(ApplicationPackageName)) {
                    ArrayList<String> dayslist=  a.get(ApplicationPackageName);
                    String daystext ="";
                    String todayinList="N";
                    for(String ach : dayslist)
                    {

                        if(ach.contains(todaysDay))
                        {
                            todayinList="Y";
                            for(HashMap<String,ArrayList<Long>> ab : MainActivity.timelist) {

                                if (ab.containsKey(ApplicationPackageName)) {
                                    ArrayList<Long> timelist=  ab.get(ApplicationPackageName);
                                    long starttime=  timelist.get(0);
                                    long stoptime= timelist.get(1);

                                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

                                    String start =sdf.format(new  Date(starttime));
                                    String end = sdf.format(new  Date(stoptime));
                                    String current = sdf.format(new Date());
                                    try {
                                        java.util.Date currentTime = new SimpleDateFormat("hh:mm")
                                                .parse(current);
                                        Calendar currentCalendar = Calendar.getInstance();
                                        currentCalendar.setTime(currentTime);

                                        long currentTme= currentCalendar.getTimeInMillis();

                                      //  long currentTme= currentTime.getTime();

                                        if(isTimeBetweenTwoTime(start,end,current))
                                        {
                                            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
                                            // Create a calendar object that will convert the date and time value in milliseconds to date.
                                            Calendar calendar1 = Calendar.getInstance();
                                            Calendar calendar21 =Calendar.getInstance();
                                            calendar1.setTimeInMillis(starttime);
                                            calendar21.setTimeInMillis(stoptime);
                                            viewHolder.time.setVisibility(View.VISIBLE);
                                            viewHolder.time.setText("Start on "+formatter.format(calendar1.getTime())+" and End on "+formatter.format(calendar21.getTime()));
                                        }
                                        else
                                        {
                                            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
                                            // Create a calendar object that will convert the date and time value in milliseconds to date.
                                            Calendar calendar1 = Calendar.getInstance();
                                            Calendar calendar21 =Calendar.getInstance();
                                            calendar1.setTimeInMillis(starttime);
                                            calendar21.setTimeInMillis(stoptime);
                                            viewHolder.time.setVisibility(View.VISIBLE);
                                            viewHolder.time.setText("Start on "+formatter.format(calendar1.getTime())+" and End on "+formatter.format(calendar21.getTime()));
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }
                        else {
                            if(todayinList.equalsIgnoreCase("N"))
                            {
                                for (HashMap<String, ArrayList<Long>> ab : MainActivity.timelist) {

                                    if (ab.containsKey(ApplicationPackageName)) {
                                        ArrayList<Long> timelist = ab.get(ApplicationPackageName);
                                        long starttime = timelist.get(0);
                                        long stoptime = timelist.get(1);
                                        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
                                        // Create a calendar object that will convert the date and time value in milliseconds to date.
                                        Calendar calendar1 = Calendar.getInstance();
                                        Calendar calendar21 = Calendar.getInstance();
                                        calendar1.setTimeInMillis(starttime);
                                        calendar21.setTimeInMillis(stoptime);
                                        viewHolder.time.setVisibility(View.VISIBLE);
                                        viewHolder.time.setText("Start on " + formatter.format(calendar1.getTime()) + " and End on " + formatter.format(calendar21.getTime()));

                                    }
                                }
                            }
                        }
                        daystext= daystext + ach + " ";

                    }
                    viewHolder.days.setVisibility(View.VISIBLE);
                    viewHolder.days.setText(" Days: "+daystext);
                    //////

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
                    int flag=0;
                    viewHolder.time.setText("");
                    viewHolder.time.setVisibility(View.GONE);
                    viewHolder.days.setVisibility(View.GONE);
                    ArrayList<HashMap<String,ArrayList<Long>>> aa =new ArrayList<>();
                    ArrayList<HashMap<String,ArrayList<String>>> days =new ArrayList<>();
                    days.addAll(MainActivity.dayslist);
                    aa.addAll(MainActivity.timelist);

                    for(HashMap<String,ArrayList<Long>> a : MainActivity.timelist)
                    {

                        a.containsKey(ApplicationPackageName);
                        aa.remove(a);

                    }
                    for(HashMap<String,ArrayList<String>> a : MainActivity.dayslist)
                    {
                        a.containsKey(ApplicationPackageName);
                        days.remove(a);

                    }



                    Toast.makeText(context1,"App is Unblocked",Toast.LENGTH_SHORT).show();
                    appChecked.remove(ApplicationPackageName);
                    MainActivity.list=appChecked;
                    SharedPreferences prefs = context1.getSharedPreferences("packagePref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    try {
                        editor.putString("package", ObjectSerializer.serialize(appChecked));
                        editor.putString("time", ObjectSerializer.serialize(aa));
                        editor.putString("days",ObjectSerializer.serialize(days));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    editor.commit();

                    if(ApplicationPackageName.equalsIgnoreCase("com.whatsapp")) {
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

                        context1.startActivity(intent);
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount(){

        return filteredList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList=(ArrayList<String>)results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<String> FilteredList= new ArrayList<String>();
                if (constraint == null || constraint.length() == 0) {
                    // No filter implemented we return all the list
                    results.values = stringList;
                    results.count = stringList.size();
                }
                else {
                    for (int i = 0; i < stringList.size(); i++) {
                        ApkInfoExtractor apkInfoExtractor = new ApkInfoExtractor(context1);

                        final String ApplicationPackageName = (String) stringList.get(i);
                        String ApplicationLabelName = apkInfoExtractor.GetAppName(ApplicationPackageName);

                        if (ApplicationLabelName.toLowerCase().contains(constraint.toString()))  {
                            FilteredList.add(ApplicationPackageName);
                        }
                    }
                    results.values = FilteredList;
                    results.count = FilteredList.size();
                }
                return results;
            }
        };
        return filter;
    }

    public  boolean isTimeBetweenTwoTime(String argStartTime,
                                         String argEndTime, String argCurrentTime) throws ParseException {
        String reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9])$";
        //
        if (argStartTime.matches(reg) && argEndTime.matches(reg)
                && argCurrentTime.matches(reg)) {
            boolean valid = false;
            // Start Time
            java.util.Date startTime = new SimpleDateFormat("HH:mm")
                    .parse(argStartTime);
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startTime);

            // Current Time
            java.util.Date currentTime = new SimpleDateFormat("HH:mm")
                    .parse(argCurrentTime);
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(currentTime);

            // End Time
            java.util.Date endTime = new SimpleDateFormat("HH:mm")
                    .parse(argEndTime);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endTime);

            //
            if (currentTime.compareTo(endTime) < 0) {

                currentCalendar.add(Calendar.DATE, 1);
                currentTime = currentCalendar.getTime();

            }

            if (startTime.compareTo(endTime) < 0) {

                startCalendar.add(Calendar.DATE, 1);
                startTime = startCalendar.getTime();

            }
            //
            if (currentTime.before(startTime)) {

                System.out.println(" Time is Lesser ");

                valid = false;
            } else {

                if (currentTime.after(endTime)) {
                    endCalendar.add(Calendar.DATE, 1);
                    endTime = endCalendar.getTime();

                }

                System.out.println("Comparing , Start Time /n " + startTime);
                System.out.println("Comparing , End Time /n " + endTime);
                System.out
                        .println("Comparing , Current Time /n " + currentTime);

                if (currentTime.before(endTime)) {
                    System.out.println("RESULT, Time lies b/w");
                    valid = true;
                } else {
                    valid = false;
                    System.out.println("RESULT, Time does not lies b/w");
                }

            }
            return valid;

        } else {
            throw new IllegalArgumentException(
                    "Not a valid time, expecting HH:MM:SS format");
        }

    }
}