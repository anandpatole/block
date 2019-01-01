package com.example.block.app_list;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //splash screen
        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1000);
                    SharedPreferences prefs = getSharedPreferences("user", Context.MODE_PRIVATE);
                  if(prefs!=null) {
                      String mobileNo = prefs.getString("mobile", "");
                      if(mobileNo.equalsIgnoreCase(""))
                      {
                          Intent intent = new Intent(getApplicationContext(),Login.class);
                          startActivity(intent);
                          finish();
                      }
                      else
                      {
                          Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                          startActivity(intent);
                          finish();
                      }
                  }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        myThread.start();
    }
}
