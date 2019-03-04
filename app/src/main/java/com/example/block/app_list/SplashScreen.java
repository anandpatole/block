package com.example.block.app_list;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {
    String check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        check = getIntent().getStringExtra("launch");

        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        if (!TextUtils.isEmpty(check)) {
            if (check.equalsIgnoreCase("Y")) {
                startActivity(new Intent(SplashScreen.this, demo.class));
            }
        } else {
            //splash screen
            Thread myThread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(1000);
                        SharedPreferences prefs = getSharedPreferences("user", Context.MODE_PRIVATE);
                        if (prefs != null) {
                            String mobileNo = prefs.getString("mobile", "");
                            if (mobileNo.equalsIgnoreCase("")) {
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                                finish();
                            } else {
                                SharedPreferences prefs1 = getSharedPreferences("pin", Context.MODE_PRIVATE);
                                if(prefs1!=null) {
                                    String id=prefs.getString("id","");
                                    if(id!=null) {
                                        if(id.equalsIgnoreCase(""))
                                        {
                                            Intent intent = new Intent(getApplicationContext(),Login.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            String pin = prefs1.getString("pin", "");
                                            if (pin.equalsIgnoreCase("")) {
                                                Intent intent = new Intent(getApplicationContext(), SetPassCode.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Intent intent = new Intent(getApplicationContext(), PassCode.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }

                                }
                                else
                                {
                                    Intent intent = new Intent(getApplicationContext(),Login.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }

//                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                                        startActivity(intent);
//                                       finish();
                        }
                        else
                        {
                            Intent intent = new Intent(getApplicationContext(),Login.class);
                            startActivity(intent);
                            finish();
                        }


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
            myThread.start();
        }
    }
}
