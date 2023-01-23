package com.example.heroicrecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.heroicrecipe.Utils.Base_url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    String idNoo,NAMe,email;
    EditText EmailET, PasswordET;
    Button loginbtn;
    TextView register;

    public static final String url_alllogin ="https://0a83-105-165-94-243.in.ngrok.io/Recipe/login.php";

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register= findViewById(R.id.rg);
        EmailET =  findViewById(R.id.useremail);
        PasswordET = findViewById(R.id.userpassword);
        loginbtn = findViewById(R.id.buttonlogin);

        //various buttons for clicking
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }


    //validate username
    private boolean checkusername(String uname) {
        if (TextUtils.isEmpty(uname)) {
            EmailET.setError("please enter you name");
            EmailET.requestFocus();
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(uname).matches())
        {
            EmailET.setError("please enter a valid email address");
            EmailET.requestFocus();
            return false;
        }
        return true;
    }

    //validate password
    private boolean checkpassword(String pword) {
        if (TextUtils.isEmpty(pword)) {
            PasswordET.setError("please enter your password");
            PasswordET.requestFocus();
            return false;
        }
        return true;
    }

    //authenticate various users
    public void verify() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        email = EmailET.getText().toString().trim();
        String Pword = PasswordET.getText().toString().trim();
        if (checkusername(email) && checkpassword(Pword)) {
            progressDialog.show();
            progressDialog.setTitle("Authenticating");
            progressDialog.setMessage("Authenticating");

            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, Base_url.loginurl(), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String success = response.getString("MSG");
                                if (success.equalsIgnoreCase("user registration successful")) {
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        idNoo = obj.getString("user-id");
                                        NAMe = obj.getString("name");

                                        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("user-id", obj.getString("user-id"));
                                        editor.putString("name", obj.getString("name"));
                                        editor.putBoolean("user_logged_in", true);
                                        editor.commit();
                                    }
                                }
                                    EmailET.setText("");
                                    PasswordET.setText("");
                                    progressDialog.dismiss();

                                    Intent Rintent = new Intent(Login.this, Host.class);
                                    startActivity(Rintent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String message = null;
                    if (error instanceof NetworkError) {
                        message = getString(R.string.network_error);
                    } else if (error instanceof ServerError) {
                        message = getString(R.string.server_error);
                    } else if (error instanceof AuthFailureError) {
                        message = getString(R.string.auth_error);
                    } else if (error instanceof ParseError) {
                        message = getString(R.string.parse_error);
                    } else if (error instanceof TimeoutError) {
                        message = getString(R.string.timeout_error);
                    } else {
                        Toast.makeText(Login.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
                }
            })
            {
                @Override
                protected HashMap<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("email", email);
                    map.put("password", Pword);
                    return map;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(this);
            jsonObjectRequest.setRetryPolicy(
                    new DefaultRetryPolicy(0,-1,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonObjectRequest);

        }

    }
}



