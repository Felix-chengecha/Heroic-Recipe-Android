package com.example.heroicrecipe.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class All_foods extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView;
    List<food_model> foodmodels;
    Foods_Adapter foods_adapter;
    Button  Fooods, Fast, Drinks;
    SwipeRefreshLayout foodswipe;
    ImageView srch;

    Toolbar toolbar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_foods, container, false);

        toolbar=view.findViewById(R.id.toolbarr);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);;

        srch=view.findViewById(R.id.search_link);
        Fooods = view.findViewById(R.id.fooods);
        Fast = view.findViewById(R.id.fast);
        Drinks = view.findViewById(R.id.drinks);
        foodswipe=view.findViewById(R.id.FoodSwipe);
        recyclerView = view.findViewById(R.id.foodview);

        Fooods.setOnClickListener(this);
        Fast.setOnClickListener(this);
        Drinks.setOnClickListener(this);

        foodmodels = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        foods_adapter = new Foods_Adapter(getContext(), foodmodels);
        recyclerView.setAdapter(foods_adapter);

         srch.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 frag(new search_food());
             }
         });


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
       StringRequest stringRequest=new StringRequest(Request.Method.POST, Base_url.getallfoods(), new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            foodswipe.setRefreshing(false);

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
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
        }){
           @Nullable
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               HashMap<String,String>map=new HashMap<>();
               map.put("category",category);
               return map;
           }
       };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(0,-1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
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

    private void frag(@NonNull Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ffframelayout1, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}

