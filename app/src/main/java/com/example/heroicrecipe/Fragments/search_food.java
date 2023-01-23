package com.example.heroicrecipe.Fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.heroicrecipe.Adapters.Foods_Adapter;
import com.example.heroicrecipe.Fragments.All_foods;
import com.example.heroicrecipe.Fragments.Food_details;
import com.example.heroicrecipe.Models.food_model;
import com.example.heroicrecipe.R;
import com.example.heroicrecipe.Utils.Base_url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class search_food extends Fragment {
    RecyclerView recyclerView;
    ImageView Back;
    List<food_model> foodmodels;
    Foods_Adapter foods_adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_food, container, false);

        Back=view.findViewById(R.id.simg);
        foodmodels = new ArrayList<>();
        recyclerView = view.findViewById(R.id.searchview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        foods_adapter = new Foods_Adapter(getContext(),foodmodels);
        recyclerView.setAdapter(foods_adapter);

        String food=getArguments().getString("foodid");
        searchfood(food);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repfrag(new All_foods());
            }
        });
        return view;
    }
    private void searchfood(String food){
        ProgressDialog progressDialog= new ProgressDialog(getContext());
        progressDialog.setTitle("fetching data");
        progressDialog.setMessage("wait");
        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Base_url.searchfood(food), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                    JSONArray jsonArray = response.getJSONArray("data");
                        progressDialog.dismiss();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String foodid = object.getString("foodid");
                            String foodname = object.getString("name");
                            String foodcateg = object.getString("category");
                            String imageurl = object.getString("image");
                            String level = object.getString("level");

                            food_model foods_models = new food_model(foodid, foodname, foodcateg, imageurl,level);
                            foodmodels.add(foods_models);
                            foods_adapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "exception message", Toast.LENGTH_SHORT).show();
                }
                foods_adapter.Layclick(new Foods_Adapter.Layclicklistener() {
                    @Override
                    public void onclicking(View v, String fid) {
                        replacefrag(new Food_details(),fid);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        jsonObjectRequest.setRetryPolicy(
                new DefaultRetryPolicy(0,
                        -1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }

    private void replacefrag(@NonNull Fragment fragment, String fid) {
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ffframelayout1,fragment);
        Bundle args = new Bundle();
        args.putString("foodid", fid);
        fragment.setArguments(args);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void repfrag(@NonNull Fragment fragment) {
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ffframelayout1,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
