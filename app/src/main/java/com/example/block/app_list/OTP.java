package com.example.block.app_list;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
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
import java.util.Objects;

public class OTP extends AppCompatActivity
{
    ProgressDialog pd;
    String mobileNo;
    private OtpView otpView;
    TextView resendOtp;
    String CountryID = "";
    String CountryZipCode = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_layout);

        CommonUtils.getCountryZipCode();
        SharedPreferences prefs = getSharedPreferences("user", Context.MODE_PRIVATE);
        try {
            mobileNo  = prefs.getString("mobile", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        resendOtp=findViewById(R.id.resend_otp);
        otpView = findViewById(R.id.otp_view);
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String s) {

                    callOtpVerificationWs(s);

            }
        });
        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callResendOtpWs();
            }
        });
    }
public void callResendOtpWs()
{
    if(!NetworkConnectivity.isNetworkAvailable(OTP.this))
    {
        Toast.makeText(OTP.this,"Please Check Your Internet Connection",Toast.LENGTH_SHORT).show();
        return;
    }
    RequestQueue queue = Volley.newRequestQueue(this);
    String url = "http://www.kuulzz.com/mobileapp/resend-otp.php";
    pd = new ProgressDialog(OTP.this);
    pd.setMessage("Please Wait......");
    pd.show();
    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        if(pd!=null)
                        {
                            pd.dismiss();
                        }
                        JSONObject jsonObject=new JSONObject(response);
                        String status=  jsonObject.getString("status");
                        if(status.equalsIgnoreCase("1"))
                        {

                        }
                        else
                        {
                            Toast.makeText(OTP.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(pd!=null)
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(OTP.this,"Something Went Wrong.Please Try Again!",Toast.LENGTH_SHORT).show();

                }
            }
    ) {

        @Override
        protected Map<String, String> getParams()
        {

            Map<String, String> mParams = new HashMap<>();
            mParams.put("method","otp_resend");
            mParams.put("mobile",mobileNo);
            mParams.put("country_zip",CommonUtils.CountryZipCode);
            mParams.put("country_code",CommonUtils.CountryID);

            return mParams;
        }
    };
    queue.add(postRequest);
}
    public void callOtpVerificationWs(final String otp)
    {
        if(!NetworkConnectivity.isNetworkAvailable(OTP.this))
        {
            Toast.makeText(OTP.this,"Please Check Your Internet Connection",Toast.LENGTH_SHORT).show();
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.kuulzz.com/mobileapp/otp-verify.php";
        pd = new ProgressDialog(OTP.this);
        pd.setMessage("Please Wait......");
        pd.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        try
                        {
                            if(pd!=null)
                            {
                                pd.dismiss();
                            }
                            JSONObject jsonObject=new JSONObject(response.toString());
                            String status=  jsonObject.getString("status");
                            if(status.equalsIgnoreCase("1"))
                            {
                                JSONObject data=jsonObject.getJSONObject("data");
                                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("mobile",mobileNo);
                                editor.putString("name",data.getString("name"));
                                editor.putString("email",data.getString("email"));
                                editor.putString("id",data.getString("id"));
                                editor.apply();
                                startActivity(new Intent(OTP.this,SetPassCode.class));
                                finishAffinity();
                            }
                            else
                            {
                                Toast.makeText(OTP.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(pd!=null)
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(OTP.this,"Something Went Wrong.Please Try Again!",Toast.LENGTH_SHORT).show();

                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> mParams = new HashMap<>();
                mParams.put("method","otp_verify");
                mParams.put("mobile",mobileNo);
                mParams.put("otp",otp);
                mParams.put("country_zip", CommonUtils.CountryZipCode);
                mParams.put("country_code", CommonUtils.CountryID);

                return mParams;
            }
        };
        queue.add(postRequest);
    }

    /*
    Method to get Country Id and Zip code
     */
    private void getCountryZipCode() {

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = Objects.requireNonNull(manager).getSimCountryIso().toUpperCase();
        String[] rl = this.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                Log.e("CountryID : ZipCode", CountryID + " : " + CountryZipCode);
                break;
            }
        }
    }
}
