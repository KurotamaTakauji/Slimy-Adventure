package com.example.slimyadventure.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.slimyadventure.APIHandler;
import com.example.slimyadventure.GlobalVariables;
import com.example.slimyadventure.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Random;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);

        configureRegisterButton();
        configureBackButton();
        configureLoginButton();
    }

    private void configureLoginButton(){
        Button loginBtn = (Button) findViewById(R.id.loginbtn);
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("username", username.getText());
                    jsonParam.put("password", password.getText());
                    APIHandler Call = new APIHandler(new URL("http://87.229.85.225:42420/users/login"), jsonParam, "POST", "");
                    Call.startThread();
                    Call.stopThread();
                    GlobalVariables.TOKEN = Call.getToken();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && GlobalVariables.TOKEN != null) {
                        String[] chunks = GlobalVariables.TOKEN.split("\\.");
                        Base64.Decoder decoder = Base64.getUrlDecoder();
                        String payload = new String(decoder.decode(chunks[1]));
                        Log.i("token", payload);
                        payload = payload.replace("{","")
                                .replace("}","");
                        String[] payloadData = payload.split(",");
                        GlobalVariables.userID =  payloadData[0].replace("\"id\":","").replace("\"","").trim();
                        GlobalVariables.username =  payloadData[1].replace("\"name\":","").replace("\"","").trim();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(GlobalVariables.TOKEN == null){
                    Toast.makeText(SignIn.this, "invalid login credentials", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SignIn.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignIn.this, MainMenu.class));
                    finish();
                }

                Random random = new Random();
                int number = random.nextInt(99);
                int avatar = 0;
                if(number == 0){
                    avatar = 9;
                }else if(number == 1){
                    avatar = 10;
                }else if(number == 2){
                    avatar = 11;
                }else if(number == 69){
                    avatar = 12;
                }else{
                    avatar = random.nextInt(7) + 1;
                }

                jsonParam = new JSONObject();
                if(GlobalVariables.userID != null &&  GlobalVariables.TOKEN != null){
                    try {
                        jsonParam.put("userID",  GlobalVariables.userID );
                        jsonParam.put("avatar", avatar);
                        APIHandler Call = new APIHandler(new URL("http://87.229.85.225:42420/avatar"), jsonParam, "POST", GlobalVariables.TOKEN);
                        Call.startThread();
                        Call.stopThread();
                        Toast.makeText(SignIn.this, Call.getMsg(), Toast.LENGTH_SHORT).show();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }
                }
            }
        );
    }

    private void configureRegisterButton(){
        Button settingsButton = (Button) findViewById(R.id.registrationbtn);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this, Register.class));
                finish();
            }
        });
    }

    private void configureBackButton(){
        Button settingsButton = (Button) findViewById(R.id.backbtn);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this, MainMenu.class));
                finish();
            }
        });
    }

}