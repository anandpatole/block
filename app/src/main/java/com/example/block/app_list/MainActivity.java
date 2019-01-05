package com.example.block.app_list;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements AppsAdapter.Click {

    private static final int LOCATION_REQUEST_CODE_PERMISSION = 1001;
    public static ArrayList<String> list;
    public static ArrayList<HashMap<String,ArrayList<Long>>> timelist;
    RecyclerView recyclerView;
    public AppsAdapter adapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    String mobileNo = "", id = "", email = "";
    private String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private GPSTracker gps;
    private ProgressDialog pd;
    String CountryID = "";
    String CountryZipCode = "";
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CommonUtils.getCountryZipCode();
        SharedPreferences prefs = getSharedPreferences("packagePref", Context.MODE_PRIVATE);
        try {
            list = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString("package", ObjectSerializer.serialize(new ArrayList<String>())));
            timelist= (ArrayList<HashMap<String, ArrayList<Long>>>)ObjectSerializer.deserialize(prefs.getString("time", ObjectSerializer.serialize(new ArrayList<HashMap<String,ArrayList<Long>>>())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            if (list.size() == 0) {
                list = new ArrayList<>();
            }

        } else {
            list = new ArrayList<>();
        }

        SharedPreferences prefs1 = getSharedPreferences("user", Context.MODE_PRIVATE);
        try {
            mobileNo = prefs1.getString("mobile", "");
            email = prefs1.getString("email", "");
            id = prefs1.getString("id", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        whiteNotificationBar(recyclerView);
        // Passing the column number 1 to show online one column in each row.
        //recyclerViewLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        List<String> list= new ApkInfoExtractor(MainActivity.this).GetAllInstalledApkInfo();
        adapter = new AppsAdapter(MainActivity.this,list , MainActivity.this);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onStart() {
        super.onStart();
        try {
            getPermission();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void requestUsageStatsPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
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

    private void getGps() {
        // create class object
        gps = new GPSTracker(this);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            Log.e("latitude+longitude", +latitude + " : " + longitude);
            callSendLatLong(latitude, longitude);
        } else {
            gps.showSettingsAlert();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getPermission() {
        if (ActivityCompat.checkSelfPermission(this, mPermission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{mPermission}, LOCATION_REQUEST_CODE_PERMISSION);
        } else if (ActivityCompat.checkSelfPermission(this, mPermission) == PackageManager.PERMISSION_GRANTED) {
            getGps();
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.str_allow_location_permission), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getGps();
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.str_allow_location_permission), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.logout:
                SharedPreferences prefs = getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.commit();
                SharedPreferences prefs1 = getSharedPreferences("pin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = prefs1.edit();
                editor1.clear();
                editor1.commit();
                Intent i = new Intent(MainActivity.this, Login.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                return true;

            case R.id.action_search :
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void checked(String name) {
        list.add(name);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        SharedPreferences prefs = getSharedPreferences("packagePref", Context.MODE_PRIVATE);
        try {
            ArrayList<String> currentTasks = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString("package", ObjectSerializer.serialize(new ArrayList<String>())));
            for (String a : AppsAdapter.newChecked) {
                if (currentTasks.contains(a)) {
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
    public void unchecked(String name) {
        list.remove(name);
    }

    public void callSendLatLong(final double sLat, final double sLong) {

        if(!NetworkConnectivity.isNetworkAvailable(MainActivity.this))
        {
            Toast.makeText(MainActivity.this,"Please Check Your Internet Connection",Toast.LENGTH_SHORT).show();
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.kuulzz.com/mobileapp/update-lat-long.php";
        pd = new ProgressDialog(MainActivity.this);
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
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("1")) {
                                Log.e("status", status);
                            } else {
                                Toast.makeText(MainActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainActivity.this, "Something Went Wrong.Please Try Again!", Toast.LENGTH_SHORT).show();

                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> mParams = new HashMap<>();
                mParams.put("method", "update_lat_long");
                mParams.put("id", id);
                mParams.put("latitude", String.valueOf(sLat));
                mParams.put("longitude", String.valueOf(sLong));
                mParams.put("mobile", mobileNo);
                mParams.put("email", email);
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

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }
}
