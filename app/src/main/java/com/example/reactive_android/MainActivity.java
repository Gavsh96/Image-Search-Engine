package com.example.reactive_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button imageSearchBt;
    EditText keyword;
    public static ArrayList<Bitmap> imagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keyword = findViewById(R.id.keyword);
        imageSearchBt = findViewById(R.id.scvBT);

        imageSearchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String kW = keyword.getText().toString();
                if(!kW.equals(""))
                {
                    Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                    intent.putExtra("keyword", kW);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Enter a Keyword Please",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}