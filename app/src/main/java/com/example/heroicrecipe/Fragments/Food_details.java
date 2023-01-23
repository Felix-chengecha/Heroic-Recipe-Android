package com.example.heroicrecipe.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.heroicrecipe.Adapters.Ingridients_Adapter;
import com.example.heroicrecipe.Adapters.Img_Adapter;
import com.example.heroicrecipe.Login;
import com.example.heroicrecipe.Models.Img_model;
import com.example.heroicrecipe.Models.Ingridients_model;
import com.example.heroicrecipe.R;
import com.example.heroicrecipe.Utils.Base_url;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Food_details  extends Fragment {
    TextView fname;
    TextView fcategory;
    ImageView Back;
    Button bookmak, Time,People;
    RecyclerView recyclerView,foodimages;
    List<Ingridients_model> ingmodels;
    Ingridients_Adapter ingridients_adapter;
    List<Img_model> img_models;
    Img_Adapter img_adapter;
    FloatingActionButton cook;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food_details, container, false);

        cook=view.findViewById(R.id.cooking);
        Time=view.findViewById(R.id.time);
        People=view.findViewById(R.id.people);
        bookmak=view.findViewById(R.id.bookmark);
        Back=view.findViewById(R.id.simg);
        foodimages=view.findViewById(R.id.fimages);
        fname=view.findViewById(R.id.food_name2);
        fcategory=view.findViewById(R.id.food_category2);
        recyclerView = view.findViewById(R.id.ingridients);

        img_models = new ArrayList<>();
        foodimages.setHasFixedSize(true);
        foodimages.setLayoutManager(new LinearLayoutManager(getContext()));
        img_adapter = new Img_Adapter(getContext(),img_models);
        foodimages.setAdapter(img_adapter);

        ingmodels = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ingridients_adapter = new Ingridients_Adapter(getContext(),ingmodels);
        recyclerView.setAdapter(ingridients_adapter);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_info", MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user-id", null);

        String food_id=getArguments().getString("foodid");

        ingridients(food_id);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repfrag(new All_foods());
            }
        });

        bookmak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_id==null) {
                    showalertdialog();
                }
                else{
                    addbookmark(food_id,user_id);
                }
            }
        });

        cook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cooking_process(food_id);
            }
        });

        return view;
    }

    private void showBottomSheetDialog(String cookprocess) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.process);
        TextView process = bottomSheetDialog.findViewById(R.id.process);
        process.setText(cookprocess);
        bottomSheetDialog.show();
    }

    private void addbookmark(String food_id, String user_id) {
        ProgressDialog progressDialog= new ProgressDialog(getContext());
        progressDialog.setTitle("adding to bookmarks");
        progressDialog.setMessage("wait");
        progressDialog.show();

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, Base_url.addbookmarks(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                    String success = response.getString("success");
                    if (success.equalsIgnoreCase("1")) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "food added to bookmarks ", Toast.LENGTH_SHORT).show();
                    }
                    else if (success.equalsIgnoreCase("0")) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "you have already added this food to bookmarks ", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

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
        })
        {
            @Override
            protected HashMap<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("food_id", food_id);
                map.put("user_id", user_id);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        jsonObjectRequest.setRetryPolicy(
                new DefaultRetryPolicy(0,-1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }

    private void ingridients(String food_id) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Base_url.getingridients(food_id), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                    JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String foodid =object.getString("foodid");
                            String foodname =object.getString("name");
                            String foodcateg = object.getString("category");
                            String imageurl = object.getString("image");
                            String ingridients = object.getString("ingridients");
                            String process = object.getString("process");
                            String time = object.getString("time");
                            String people = object.getString("people");

                            Time.setText(time);
                            People.setText(String.format("serve%s", people));
                            fname.setText(foodname);
                            fcategory.setText(foodcateg);

                            Ingridients_model ing_model= new Ingridients_model(ingridients);
                            ingmodels.add(ing_model);
                            ingridients_adapter.notifyDataSetChanged();

                            Img_model mg_model=new Img_model(imageurl);
                            img_models.add(mg_model);
                            img_adapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void repfrag(@NonNull Fragment fragment) {
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ffframelayout1,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void showalertdialog() {
        final AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setTitle("login");
        builder.setMessage("login to add this food to bookmark");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(getContext(), Login.class);
                startActivity(intent);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void cooking_process(String food_id){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Base_url.getcookingprocess(food_id), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                    JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                                    String process =object.getString("process");
                            showBottomSheetDialog(process);
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

}

