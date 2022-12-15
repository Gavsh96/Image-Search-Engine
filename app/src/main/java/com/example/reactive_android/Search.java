package com.example.reactive_android;

import android.app.Activity;
import android.net.Uri;

import java.net.HttpURLConnection;
import java.util.concurrent.Callable;

public class Search implements Callable<String> {

    private String searchkey;
    private String baseUrl;
    private RemoteUtilities remoteUtilities;
    private Activity uiActivity;

    public Search(Activity uiActivity) {
        this.searchkey = null;
        baseUrl ="https://pixabay.com/api/";
        remoteUtilities = RemoteUtilities.getInstance(uiActivity);
        this.uiActivity = uiActivity;
    }

    @Override
    public String call() throws Exception {
        String response=null;
        String endpoint = getSearchEndpoint();
        HttpURLConnection connection = remoteUtilities.openConnection(endpoint);
        if(connection!=null){
            if(remoteUtilities.isConnectionOkay(connection)==true){
                response = remoteUtilities.getResponseString(connection);
                connection.disconnect();
                try {
                    Thread.sleep(3000);
                }
                catch (Exception e){

                }
            }
        }
        return response;
    }

    private String getSearchEndpoint(){
        String data = null;
        Uri.Builder url = Uri.parse(this.baseUrl).buildUpon();
        url.appendQueryParameter("key","23319229-94b52a4727158e1dc3fd5f2db");
        url.appendQueryParameter("q",this.searchkey);
        String urlString = url.build().toString();
        return urlString;
    }

    public void setSearchkey(String searchkey) {
        this.searchkey = searchkey;
    }
}

