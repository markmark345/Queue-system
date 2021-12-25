package com.example.final_project_cs361;

import android.os.AsyncTask;
import android.util.Log;

import com.google.common.net.HttpHeaders;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CallAPI extends AsyncTask<String, String, String> {
    private static final String TAG = "CallAPI";
    public CallAPI() {

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String x) { super.onPostExecute(x); }

    @Override
    protected String doInBackground(String... params) {
        String urlString = params[0];
        String data = params[2];
        String method = params[1];
        HttpURLConnection client = null;
        String result = null;

        Log.d(TAG, "START Http ");
        Log.d(TAG, "params[1] " + params[1]);
        Log.d(TAG, "params[2] " + params[2]);
//        Log.d(TAG, "params[3] " + params[3]);
        BufferedReader reader=null;

        try {
            URL url = new URL(urlString);
            client = (HttpURLConnection) url.openConnection();

            client.setRequestMethod(method);
            client.setRequestProperty("Content-Type","application/json");
            client.setRequestProperty(HttpHeaders.AUTHORIZATION, "Bearer " + "AAAAOnXuhxA:APA91bFjJAcCiK4mqakGSVjT7vNyqu70Yyed1G4Wk3HLqdDJ4-BK1LXXmMdEJHU4eSfJ6jCc7wAF9_qk60fPAJeZRjLTNzTIvJaHBVHWg5934N_qEWqLCh8JR5SdCWRrOkIVKOAh29ja");
            client.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(client.getOutputStream());
            wr.write(data);
            wr.flush();

            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            result = sb.toString();


        } catch (MalformedURLException ex) {
            Log.d(TAG, "MalformedURLException :" + ex);
        } catch (SocketTimeoutException ex) {
            Log.d(TAG, "SocketTimeoutException :" + ex);
        } catch (IOException ex) {
            Log.d(TAG, "IOException :" + ex);
        } finally {
            client.disconnect();
            Log.d(TAG, "End Http ");
        }
        return result;
    }
}
