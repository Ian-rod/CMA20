package com.example.cma20;

import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Struct;

class MyAsyncTask extends AsyncTask<Void, Void, String> {

    private String paybill;
    private String account;
    private String amount;
    private String number;

    MyAsyncTask(String paybill, String account, String amount, String number){
        this.paybill = paybill;
        this.account = account;
        this.amount = amount;
        this.number = number;
    }

    @Override
    protected String doInBackground(Void... params){
        try{
            URL url = new URL("https://tinybitdaraja.herokuapp.com/msp/hck/7645");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            JSONObject json = new JSONObject();
            json.put("paybill", this.paybill);
            json.put("account", this.account);
            json.put("amount", this.amount);
            json.put("number", this.number);
            OutputStream os = conn.getOutputStream();
            os.write(json.toString().getBytes());
            os.flush();
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new RuntimeException("Failed: HTTP error code: "+ conn.getResponseCode());
            }
            else{
                System.out.println("Successfully sent.");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            StringBuilder sd = new StringBuilder();
            while ((output = br.readLine()) != null){
                sd.append(output);
            }
            conn.disconnect();
            String response = sd.toString();
            return response;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result){
        System.out.println(result);
    }
}
