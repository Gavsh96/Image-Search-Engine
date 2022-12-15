package com.example.reactive_android;

import static android.widget.GridLayout.VERTICAL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ViewActivity extends AppCompatActivity implements ViewInterface {

    Button SCV;
    Button DCV;
    ProgressBar progressBar;
    RecyclerView RV;
    String keyword;
    String check;
    String successString;
    public static ArrayList<Bitmap> picList = new ArrayList<Bitmap>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        SCV = findViewById(R.id.scvBT);
        DCV = findViewById(R.id.dcvbt);
        progressBar = findViewById(R.id.progressBarId);
        RV = findViewById(R.id.picRecyclerView);
        progressBar.setVisibility(View.INVISIBLE);
        RV.setVisibility(View.INVISIBLE);
        SCV.setEnabled(false);
        DCV.setEnabled(false);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            keyword = extras.getString("keyword");
        }

        searchImage(keyword);

        SCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                RV.setVisibility(View.INVISIBLE);
                loadImage(successString);
                check = "SCV";
            }
        });

        DCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                RV.setVisibility(View.INVISIBLE);
                loadImage(successString);
                check = "DCV";
            }
        });
    }

    public void searchImage(String keyword){
        Toast.makeText(ViewActivity.this, "Searching starts", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
        Search searchTask = new Search(ViewActivity.this);
        searchTask.setSearchkey(keyword);
        Single<String> searchObservable = Single.fromCallable(searchTask);
        searchObservable = searchObservable.subscribeOn(Schedulers.io());
        searchObservable = searchObservable.observeOn(AndroidSchedulers.mainThread());
        searchObservable.subscribe(new SingleObserver<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onSuccess(@NonNull String s) {
                Toast.makeText(ViewActivity.this, "Searching Ends", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                SCV.setEnabled(true);
                DCV.setEnabled(true);
                successString = s;
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(ViewActivity.this, "Searching Error", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }

    public void loadImage(String response){
        getImage getImageTask = new getImage(ViewActivity.this);
        getImageTask.setData(response);

        Toast.makeText(ViewActivity.this, "Image loading starts", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
        Single<ArrayList<Bitmap>> searchObservable = Single.fromCallable(getImageTask);
        searchObservable = searchObservable.subscribeOn(Schedulers.io());
        searchObservable = searchObservable.observeOn(AndroidSchedulers.mainThread());
        searchObservable.subscribe(new SingleObserver<ArrayList<Bitmap>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull ArrayList<Bitmap> bitmaps) {
                Toast.makeText(ViewActivity.this, "Image loading Ends", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                if(check.equals("SCV"))
                {
                    RV.setVisibility(View.VISIBLE);
                    RV.setLayoutManager(new LinearLayoutManager(ViewActivity.this));
                    ViewAdapter adapter = new ViewAdapter(bitmaps, ViewActivity.this);
                    RV.setAdapter(adapter);
                }
                else if(check.equals("DCV"))
                {
                    RV.setVisibility(View.VISIBLE);
                    RV.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));
                    ViewAdapter adapter = new ViewAdapter(bitmaps, ViewActivity.this);
                    RV.setAdapter(adapter);
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(ViewActivity.this, "Image loading error, search again", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ViewActivity.this, UploadActivity.class);
        intent.putExtra("index", position);
        startActivity(intent);
    }
}

