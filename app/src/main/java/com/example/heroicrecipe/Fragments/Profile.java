package com.example.heroicrecipe.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.heroicrecipe.Login;
import com.example.heroicrecipe.R;
import com.example.heroicrecipe.Utils.Base_url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Profile extends Fragment {
    TextView name,email,link,level;

    private String url_details="https://0a83-105-165-94-243.in.ngrok.io/Recipe/user_details.php";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);

        name=view.findViewById(R.id.pname);
        email=view.findViewById(R.id.pemail);
        link=view.findViewById(R.id.plink);
        level=view.findViewById(R.id.plevel);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_info", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        String user_id = sharedPreferences.getString("user_id", null);



        if(token!=null)
        {
            link.setText(" your Personal details");
        }
        if(user_id!=null){
            fetchuser(user_id);
        }

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Login.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void fetchuser(String user_id) {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Base_url.userdetails(), new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetC hanged")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String Name = object.getString("name");
                        String Email = object.getString("email");
                        String Level = object.getString("level");
                        name.setText(Name);
                        email.setText(Email);
                        level.setText(Level);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            protected HashMap<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("user_id", user_id);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}
