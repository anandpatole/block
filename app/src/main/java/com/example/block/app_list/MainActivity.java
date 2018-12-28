package com.example.block.app_list;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements AppsAdapter.Click {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    public static ArrayList<String> list;
    public static ArrayList<HashMap<String,String>> timelist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences("packagePref", Context.MODE_PRIVATE);
        try {
            list = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString("package", ObjectSerializer.serialize(new ArrayList<String>())));
            timelist= (ArrayList<HashMap<String, String>>)ObjectSerializer.deserialize(prefs.getString("time", ObjectSerializer.serialize(new ArrayList<HashMap<String,String>>())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(list!=null)
        {
            if(list.size()==0)
            {
                list=new ArrayList<>();
            }

        }
        else {
            list = new ArrayList<>();
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // Passing the column number 1 to show online one column in each row.
        //recyclerViewLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new AppsAdapter(MainActivity.this, new ApkInfoExtractor(MainActivity.this).GetAllInstalledApkInfo(),MainActivity.this);
        recyclerView.setAdapter(adapter);
        requestUsageStatsPermission();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent2 = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent2);
            }
        });





    }
    void requestUsageStatsPermission() {
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && !hasUsageStatsPermission(this)) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    boolean hasUsageStatsPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), context.getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;

        return granted;


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId())
        {

            case R.id.logout:
            {
                SharedPreferences prefs = getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.commit();
                Intent i=new Intent(MainActivity.this,Login.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                return true;
            }


            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void checked(String name)
    {
        list.add(name);

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
    @Override
    public void unchecked(String name)
    {
        list.remove(name);
    }
}
