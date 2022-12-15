package com.example.reactive_android;


import static com.example.reactive_android.MainActivity.imagesList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;

public class getImage implements Callable<ArrayList<Bitmap>> {
    private Activity uiActivity;
    private String data;
    private RemoteUtilities remoteUtilities;
    private Iterator<String> it;

    public getImage(Activity uiActivity) {
        remoteUtilities = RemoteUtilities.getInstance(uiActivity);
        this.uiActivity=uiActivity;
        this.data = null;
    }

    @Override
    public ArrayList<Bitmap> call() throws Exception {
        ArrayList<Bitmap> imageList = null;
        ArrayList<String> endpoint = getEndpoint(this.data);
        if(endpoint==null){
            uiActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(uiActivity,"No image found",Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            imageList = getImageFromUrl(endpoint);
            imagesList = imageList;
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
            }

        }
        return imageList;
    }

    private ArrayList<String> getEndpoint(String data){
        ArrayList<String> urlList = new ArrayList<>();
        try {
            JSONObject jBase = new JSONObject(data);
            JSONArray jHits = jBase.getJSONArray("hits");
            if(jHits.length()>0){

                if(jHits.length() > 15)
                {
                    for(int i =0; i < 15; i++)
                    {
                        JSONObject jHitsItem = jHits.getJSONObject(i);
                        urlList.add(jHitsItem.getString("largeImageURL"));
                    }
                }
                else if (jHits.length() < 15)
                {
                    for(int i =0; i < jHits.length(); i++)
                    {
                        JSONObject jHitsItem = jHits.getJSONObject(i);
                        urlList.add(jHitsItem.getString("largeImageURL"));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return urlList;
    }

    private ArrayList<Bitmap> getImageFromUrl(ArrayList<String> urlList){
        ArrayList<Bitmap> images = new ArrayList<>();
        itURL(urlList,images);
        return images;
    }

    public Bitmap getBitmapFromConnection(HttpURLConnection conn){
        Bitmap data = null;
        try {
            InputStream inputStream = conn.getInputStream();
            byte[] byteData = getByteArrayFromInputStream(inputStream);
            data = BitmapFactory.decodeByteArray(byteData,0,byteData.length);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return data;
    }

    private byte[] getByteArrayFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[4096];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }

    public void setData(String data) {
        this.data = data;
    }

    private void itURL(ArrayList<String> urlList, ArrayList<Bitmap> images)
    {
        it = urlList.iterator();

        while (it.hasNext())
        {
            String imageUrl = it.next();
            Uri.Builder url = Uri.parse(imageUrl).buildUpon();
            String urlString = url.build().toString();
            HttpURLConnection connection = remoteUtilities.openConnection(urlString);
            if(connection!=null){
                if(remoteUtilities.isConnectionOkay(connection)==true){
                    images.add(getBitmapFromConnection(connection));
                    connection.disconnect();
                }
            }
        }
    }
}

