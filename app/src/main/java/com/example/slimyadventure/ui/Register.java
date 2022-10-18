package com.example.slimyadventure.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.slimyadventure.APIHandler;
import com.example.slimyadventure.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        configureRegisterButton();
        configureBackButton();
    }
    private void configureBackButton(){
        Button settingsButton = (Button) findViewById(R.id.backbtn2);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, SignIn.class));
                finish();
            }
        });
    }

    private void configureRegisterButton(){
        Button loginBtn = (Button) findViewById(R.id.signupbtn);
        EditText username = (EditText) findViewById(R.id.username);
        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("username", username.getText());
                    jsonParam.put("email", email.getText());
                    jsonParam.put("password", password.getText());
                    APIHandler Call = new APIHandler(new URL("http://87.229.85.225:42420/signup"), jsonParam, "POST", "");
                    Call.startThread();
                    Call.stopThread();
                    Toast.makeText(Register.this, Call.getMsg(), Toast.LENGTH_SHORT).show();
                    if(Call.getMsg() == "OK") {
                        Toast.makeText(Register.this, "Successful register!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, SignIn.class));
                        finish();
                    }else
                        Toast.makeText(Register.this, "invalid or missing inputs", Toast.LENGTH_SHORT).show();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}