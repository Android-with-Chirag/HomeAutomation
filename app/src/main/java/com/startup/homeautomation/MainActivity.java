package com.startup.homeautomation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Switch aSwitch1, aSwitch2, aSwitch3, aSwitch4;
    Switch[] switches;

    Button signIn;

    TextView jsonStatus;

    private int reqNumber = 0;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aSwitch1 = findViewById(R.id.switch1);
        aSwitch2 = findViewById(R.id.switch2);
        aSwitch3 = findViewById(R.id.switch3);
        aSwitch4 = findViewById(R.id.switch4);

        switches = new Switch[]{aSwitch1, aSwitch2, aSwitch3, aSwitch4};

        signIn = findViewById(R.id.button);
        jsonStatus = findViewById(R.id.textView);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jsonStatus.setText("");
                Toast.makeText(getApplicationContext(), "Button is tapped", Toast.LENGTH_SHORT).show();

                requestQueue = Volley.newRequestQueue(MainActivity.this);
                jsonParse();
            }
        });

        aSwitch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestQueue = Volley.newRequestQueue(MainActivity.this);
                ON_OFF(1, 1, aSwitch1.isChecked() ? "ON" : "OFF");
            }
        });
        aSwitch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestQueue = Volley.newRequestQueue(MainActivity.this);
                ON_OFF(1, 2, aSwitch2.isChecked() ? "ON" : "OFF");
            }
        });
        aSwitch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestQueue = Volley.newRequestQueue(MainActivity.this);
                ON_OFF(1, 3, aSwitch3.isChecked() ? "ON" : "OFF");
            }
        });
        aSwitch4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestQueue = Volley.newRequestQueue(MainActivity.this);
                ON_OFF(1, 4, aSwitch4.isChecked() ? "ON" : "OFF");
            }
        });
    }

    private void ON_OFF(int roomNo, int ApplianceNumber, String status) {
        String url = "https://homeautomationapi-sutharsolutions2.azurewebsites.net/" + status.toUpperCase() + "/5cd7a9bb-611b-424d-bde2-e440f8a92044/" + roomNo + "/" + ApplianceNumber + "/";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("Response is: ", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error", "That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        requestQueue.add(stringRequest);


    }

    private void jsonParse() {

        String url = "https://homeautomationapi-sutharsolutions2.azurewebsites.net/status/all/5cd7a9bb-611b-424d-bde2-e440f8a92044/1/"; // + token.getText().toString() + "/1";
//                String url = "https://192.168.1.100/status/all/17ba5242-e6af-4bc0-82ab-413dc42366f2/1/"; // + token.getText().toString() + "/1";

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        Log.i("Success Request", "chal rha h");
                        try {
                            JSONArray jsonArray = response.getJSONArray("Statuses");

                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Status = jsonArray.getJSONObject(i);

                                int applianceNumber = Status.getInt("applianceNumber");
                                boolean applianceStatus = Status.getBoolean("status");

                                switches[applianceNumber - 1].setChecked(applianceStatus);

                                jsonStatus.append(applianceNumber + " : " + applianceStatus + "\n");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                                jsonStatus.setText("Response is: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Message", "nhi hua");
                jsonStatus.append("That didn't work! " + reqNumber++ + "\n");
            }
        });

        requestQueue.add(stringRequest);
    }
}