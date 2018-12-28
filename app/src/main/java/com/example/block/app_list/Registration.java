package com.example.block.app_list;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registration  extends AppCompatActivity
{
    ProgressDialog pd;
    TextView link_login;
    EditText name,mobile,email=null;
    Button sign_up;
    String name_t ;

    String email_t ;
    String mobile_t ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        link_login=findViewById(R.id.link_login);
        name=findViewById(R.id.input_name);
        mobile=findViewById(R.id.input_mobile);
        email=findViewById(R.id.input_email);
        sign_up=findViewById(R.id.btn_signup);
        initViews();
    }

    public void initViews()
    {
        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    HttpPOSTRequestWithParameters();
                }
            }
        });

    }

    public boolean validate() {
        boolean valid = true;

        name_t = name.getText().toString();

        email_t = email.getText().toString();
        mobile_t =mobile.getText().toString();


        if (name_t.isEmpty()) {
            name.setError("Enter Name");
            valid = false;
        } else {
            name.setError(null);
        }




        if (email_t.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email_t).matches()) {
            email.setError("Enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (mobile_t.isEmpty() || mobile_t.length()!=10) {
            mobile.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            mobile.setError(null);
        }


        return valid;
    }

    public void HttpPOSTRequestWithParameters() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.kuulzz.com/mobileapp/user-registration.php";
        pd = new ProgressDialog(Registration.this);
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
                                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("mobile",mobile_t);
                                editor.apply();
                                startActivity(new Intent(Registration.this,OTP.class));

                            }
                            else
                            {
                                Toast.makeText(Registration.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Registration.this,"Something Went Wrong.Please Try Again!",Toast.LENGTH_SHORT).show();
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> mParams = new HashMap<>();
                mParams.put("method","user_registration");
                mParams.put("mobile",mobile_t);
                mParams.put("name",name_t);
                mParams.put("email",email_t);
                mParams.put("device_id","");
                mParams.put("device_type","");
                mParams.put("device_info","");

                return mParams;
            }
        };
        queue.add(postRequest);
    }

}
