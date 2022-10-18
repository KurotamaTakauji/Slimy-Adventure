package com.example.slimyadventure;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIHandler extends Thread{

    private URL urlParam;
    private JSONObject jsonParams;
    private String token, method, bearer, msg, response;

    public APIHandler(URL urlParam, JSONObject jsonParams, String method, String bearer) {
        this.urlParam = urlParam;
        this.jsonParams = jsonParams;
        this.method = method;
        this.bearer = bearer;
        this.token = null;
    }

    public void startThread() {
        start();
    }

    public void stopThread() {
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getToken(){
        return token;
    }

    public String getMsg(){
        return msg;
    }

    public String getResponse(){
        return response;
    }

    @Override
    public void run() {
        super.run();
        try {
            URL url = urlParam;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if(this.method == "POST")
                conn.setRequestMethod("POST");
            if(bearer != "" && bearer != null)
                conn.setRequestProperty("Authorization", "Bearer " + bearer);
            if(this.method == "POST") {
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = jsonParams;

                Log.i("JSON", jsonParam.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());

                os.flush();
                os.close();
            }

            if (conn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();
                String response = sb.toString();
                if (response.contains("token")) {
                    response = response.replace("{","")
                            .replace("}","")
                            .replace("\"token\":", "")
                            .replace("\"","");
                    this.token = response;
                    Log.i("TOKEN" , this.token);
                }else{
                    response = response.replace("[","")
                                .replace("]","")
                                .replace("{","");
                }
                this.response = response;
                Log.i("RESPONSE" , response);
            }
            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());
            this.msg = conn.getResponseMessage();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}