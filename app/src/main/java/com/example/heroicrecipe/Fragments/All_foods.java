package com.example.heroicrecipe.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.heroicrecipe.Models.food_model;
import com.example.heroicrecipe.R;
import com.example.heroicrecipe.Utils.Base_url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class All_foods extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView;
    List<food_model> foodmodels;
    Foods_Adapter foods_adapter;
    EditText search;
    Button search_btn, Fooods, Fast, Drinks;
    SwipeRefreshLayout foodswipe;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_foods, container, false);

        Fooods = view.findViewById(R.id.fooods);
        Fast = view.findViewById(R.id.fast);
        Drinks = view.findViewById(R.id.drinks);
        foodswipe=view.findViewById(R.id.FoodSwipe);

        search = view.findViewById(R.id.Recipe_name);
        search_btn = view.findViewById(R.id.search_btn);
        recyclerView = view.findViewById(R.id.foodview);

        foodmodels = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        foods_adapter = new Foods_Adapter(getContext(), foodmodels);
        recyclerView.setAdapter(foods_adapter);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String food = search.getText().toString().trim();
                replacefrag(new search_food(), food);
            }
        });
        Fooods.setOnClickListener(this);
        Fast.setOnClickListener(this);
        Drinks.setOnClickListener(this);
        fetchfoods("1");

        return view;
    }


    @Override
    public void onClick(View v) {
        String category=" ";
        switch (v.getId()) {
            case R.id.fooods:
                category="1";
                fetchfoods(category);
                break;
            case R.id.fast:
                category="2";
                fetchfoods(category);
                break;
            case R.id.drinks:
                category="3";
                fetchfoods(category);
                break;
        }
    }

    private void fetchfoods(String category) {
        foodmodels.clear();
        foodswipe.setRefreshing(true);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, Base_url.getallfoods(category), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String foodid = object.getString("foodid");
                        String foodname = object.getString("name");
                        String foodcateg = object.getString("category");
                        String imageurl = object.getString("image");
                        String level = object.getString("level");

                        food_model foods_models = new food_model(foodid, foodname, foodcateg, imageurl, level);
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
                        replacefrag(new Food_details(), fid);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                foodswipe.setRefreshing(true);
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
                new DefaultRetryPolicy(0,-1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }

    private void replacefrag(@NonNull Fragment fragment, String fid) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ffframelayout1, fragment);
        Bundle args = new Bundle();
        args.putString("foodid", fid);
        fragment.setArguments(args);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}

