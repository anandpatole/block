package com.example.block.app_list;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PassCode extends AppCompatActivity
{
    private OtpView otpView;
    String pin;
    TextView forgot_pin;
    ProgressDialog pd;
    SharedPreferences prefs;
    SharedPreferences prefs1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passcode);
        CommonUtils.getCountryZipCode();
        otpView = findViewById(R.id.pin_check);
        forgot_pin=findViewById(R.id.forgot_pin);
        prefs = getSharedPreferences("user", Context.MODE_PRIVATE);
        prefs1 = getSharedPreferences("pin", Context.MODE_PRIVATE);
        if (prefs1 != null) {
            pin = prefs1.getString("pin", "");
        }
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String s) {
                if(s.equalsIgnoreCase(pin))
                {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(PassCode.this,"Incorrect Pin",Toast.LENGTH_SHORT).show();
                }
            }
        });

        forgot_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callForgotPinWs();
            }
        });
    }

    public void callForgotPinWs()
    {

        if(!NetworkConnectivity.isNetworkAvailable(PassCode.this))
        {
            Toast.makeText(PassCode.this,"Please Check Your Internet Connection",Toast.LENGTH_SHORT).show();
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.kuulzz.com/mobileapp/forgot-passcode.php";
        pd = new ProgressDialog(PassCode.this);
        pd.setMessage("Please Wait......");
        pd.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            if (pd != null) {
                                pd.dismiss();
                            }
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("1"))
                            {
                                Intent i=  new Intent(PassCode.this, PassCodeOTP.class);
                                startActivity(i);
                                finish();

                            } else
                            {
                                Toast.makeText(PassCode.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (pd != null) {
                            pd.dismiss();
                        }
                        Toast.makeText(PassCode.this, "Something Went Wrong.Please Try Again!", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> mParams = new HashMap<>();
                mParams.put("method", "forgot_passcode");
                mParams.put("mobile", prefs.getString("mobile",""));
                mParams.put("id", prefs.getString("id",""));
                mParams.put("email", prefs.getString("email",""));
                mParams.put("country_zip", CommonUtils.CountryZipCode);
                mParams.put("country_code", CommonUtils.CountryID);

                return mParams;
            }
        };
        queue.add(postRequest);

    }
}
