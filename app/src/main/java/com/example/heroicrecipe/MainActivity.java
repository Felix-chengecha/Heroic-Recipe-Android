package com.example.heroicrecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    Button nav;
    ImageView pic;

    //    String url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nav=findViewById(R.id.start);
        pic=findViewById(R.id.head_img);

//        url="https://img.buzzfeed.com/thumbnailer-prod-us-east-1/video-api/assets/414312.jpg";
//        Glide.with(this).load(url)
//                .placeholder(R.drawable.ic_launcher_background)
//                .error(R.drawable.ic_launcher_background)
//                .into(pic);

        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Host.class);
                startActivity(intent);
            }
        });
    }
}