package com.example.block.app_list;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

public class PassCodeOTP extends AppCompatActivity
{
    private OtpView otpView;
    SharedPreferences prefs;
    ProgressDialog pd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passcodeotp);
        CommonUtils.getCountryZipCode();
        prefs = getSharedPreferences("user", Context.MODE_PRIVATE);
        otpView = findViewById(R.id.passcode_otp_view);
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String s) {

                callPasscodeOtpVerifyWs(s);

            }
        });
    }
    public void callPasscodeOtpVerifyWs(final String s)
    {

        if(!NetworkConnectivity.isNetworkAvailable(PassCodeOTP.this))
        {
            Toast.makeText(PassCodeOTP.this,"Please Check Your Internet Connection",Toast.LENGTH_SHORT).show();
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.kuulzz.com/mobileapp/passcode-otp-verify.php";
        pd = new ProgressDialog(PassCodeOTP.this);
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
                                Intent i=  new Intent(PassCodeOTP.this, SetPassCode.class);
                                startActivity(i);
                                finish();

                            } else
                            {
                                Toast.makeText(PassCodeOTP.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PassCodeOTP.this, "Something Went Wrong.Please Try Again!", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> mParams = new HashMap<>();
                mParams.put("method", "passcode_otp_verify");
                mParams.put("mobile", prefs.getString("mobile",""));
                mParams.put("otp", s.toString());
                mParams.put("email", prefs.getString("email",""));
                mParams.put("country_zip", CommonUtils.CountryZipCode);
                mParams.put("country_code", CommonUtils.CountryID);

                return mParams;
            }
        };
        queue.add(postRequest);
    }
}
