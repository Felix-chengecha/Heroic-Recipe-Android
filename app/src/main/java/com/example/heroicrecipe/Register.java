package com.example.heroicrecipe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

public class Register extends AppCompatActivity {

    EditText EmailRT, PasswordRT, NameRT;
    RadioGroup RadioGroup;
    Button registerbtn;
    TextView register,LV;
    String level=null;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RadioGroup=findViewById(R.id.radiogroup);
        register= findViewById(R.id.lg);
        NameRT = findViewById(R.id.username);
        EmailRT =  findViewById(R.id.useremail);
        PasswordRT = findViewById(R.id.userpassword);
        registerbtn = findViewById(R.id.buttonlogin);
        LV=findViewById(R.id.lv);


        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkdetails();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.beg:
                        level="beginner";
                        break;
                    case R.id.mid:
                        level="middle-level";
                        break;
                    case R.id.expert:
                        level="master";
                        break;
                }
            }
        });

    }

     //validate username
     private void checkdetails() {
         String name = NameRT.getText().toString().trim();
         String email = EmailRT.getText().toString().trim();
         String pword = PasswordRT.getText().toString().trim();

         //validate username
         if (TextUtils.isEmpty(name)) {
            NameRT.setError("please enter you name");
            NameRT.requestFocus();
          }
         //validate email address
         else if (TextUtils.isEmpty(email)) {
             EmailRT.setError("please enter you email");
             EmailRT.requestFocus();
         }
         //validate email address
         else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
             EmailRT.setError("please enter a valid email address");
             EmailRT.requestFocus();
         }
         //validate password
         else if (TextUtils.isEmpty(pword)) {
             PasswordRT.setError("please enter your password");
             PasswordRT.requestFocus();
         }
         else if(level==null){
             LV.setError("select your expertise level");
         }
         else {
             String PASS = encryptpassword(pword);
             verify(name, email, PASS);
         }
     }



    //authenticate various users
    public void verify(String name, String email,String pword) {
        ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.show();
            progressDialog.setTitle("Registration");
            progressDialog.setMessage("adding new user");

          StringRequest stringRequest=new StringRequest(Request.Method.POST, Base_url.registerurl(), new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                Boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                    EmailRT.setText("");
                                    PasswordRT.setText("");
                                    NameRT.setText("");
                                Toast.makeText(Register.this, "welcome sir ", Toast.LENGTH_SHORT).show();

                                progressDialog.dismiss();
                                    Intent Rintent = new Intent(Register.this, Login.class);
                                    startActivity(Rintent);
                                    }
                            else {
                                progressDialog.dismiss();
                                    Toast.makeText(Register.this, "user already exist ", Toast.LENGTH_SHORT).show();
                                }
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
                        Toast.makeText(Register.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(Register.this, message, Toast.LENGTH_LONG).show();
                }
            })
            {
                @Override
                protected HashMap<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("name",name);
                    map.put("email", email);
                    map.put("level", level);
                    map.put("password", pword);
                    return map;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(
                    new DefaultRetryPolicy(0,-1,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(stringRequest);
    }



    private String encryptpassword(String password){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}



