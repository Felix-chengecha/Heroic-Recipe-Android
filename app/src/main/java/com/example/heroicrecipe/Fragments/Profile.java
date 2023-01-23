package com.example.heroicrecipe.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.example.heroicrecipe.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Profile extends Fragment {
    TextView name,email,link;

    private String url_details="https://0a83-105-165-94-243.in.ngrok.io/Recipe/user_details.php";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);

        name=view.findViewById(R.id.pname);
        email=view.findViewById(R.id.pemail);
        link=view.findViewById(R.id.plink);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_info", MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user-id", null);

        fetchuser(user_id);
        return view;
    }

    private void fetchuser(String user_id) {
        ProgressDialog progressDialog= new ProgressDialog(getContext());
        progressDialog.setTitle("fetching data");
        progressDialog.setMessage("wait");
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_details, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetC hanged")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (success.equalsIgnoreCase("1")) {
                        progressDialog.dismiss();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("id");
                            String Name = object.getString("name");
                            String Email = object.getString("email");
                                name.setText(Name);
                                email.setText(Email);
                                link.setVisibility(View.GONE);
                        }
                    }
                    else if (success.equalsIgnoreCase("0")) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "you don`t have an account", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "you don`t have an account", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "you don`t have an account", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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
