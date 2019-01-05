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
import java.util.Objects;

public class Login extends AppCompatActivity {
    TextView link_signup;
    Button login_in;
    EditText mobile;
    String mobile_t;
    ProgressDialog pd;
    String CountryID = "";
    String CountryZipCode = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        link_signup = (TextView) findViewById(R.id.link_signup);
        login_in = (Button) findViewById(R.id.btn_login);
        mobile = (EditText) findViewById(R.id.input_mobile);
        initViews();
    }

    public void initViews() {

        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registration.class));
            }
        });

        login_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    CommonUtils.getCountryZipCode();
                    HttpPOSTRequestWithParameters();
                }

            }
        });

    }

    public void HttpPOSTRequestWithParameters() {
        if(!NetworkConnectivity.isNetworkAvailable(Login.this))
        {
            Toast.makeText(Login.this,"Please Check Your Internet Connection",Toast.LENGTH_SHORT).show();
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.kuulzz.com/mobileapp/login.php";
        pd = new ProgressDialog(Login.this);
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
                            if (status.equalsIgnoreCase("1")) {
                                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("mobile", mobile_t);
                                editor.apply();
                                Intent i=    new Intent(Login.this, OTP.class);
                                i.putExtra("from","login");
                                startActivity(i);
                            } else {
                                Toast.makeText(Login.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Login.this, "Something Went Wrong.Please Try Again!", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> mParams = new HashMap<>();
                mParams.put("method", "user_login");
                mParams.put("mobile", mobile_t);
                mParams.put("country_zip", CommonUtils.CountryZipCode);
                mParams.put("country_code", CommonUtils.CountryID);

                return mParams;
            }
        };
        queue.add(postRequest);
    }

    public boolean validate() {
        boolean valid = true;

        mobile_t = mobile.getText().toString();


        if (mobile_t.isEmpty() || mobile_t.length() != 10) {
            mobile.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            mobile.setError(null);
        }


        return valid;
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
