package com.example.slimyadventure.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.slimyadventure.BackgroundSound;
import com.example.slimyadventure.GlobalVariables;
import com.example.slimyadventure.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);
        if(GlobalVariables.MAX_UPS == 0.0){
            GlobalVariables.MAX_UPS = 60.0;
            GlobalVariables.SENSITIVITY = 1.0f;
        }

        String[] settings = readFromFile(MainMenu.this).split(",");
        if(settings.length == 3 && settings[0] != "0.0" && settings[1] != "0.0" && GlobalVariables.SENSITIVITY == 1.0){
            GlobalVariables.MAX_UPS = Float.valueOf(settings[0]);
            GlobalVariables.SENSITIVITY = Float.valueOf(settings[1]);
            GlobalVariables.GO_MUSIC = Integer.valueOf(settings[2]);
        }
        writeToFile(MainMenu.this);

        if(GlobalVariables.MUSIC == null && GlobalVariables.GO_MUSIC == 1) {
            Intent intent = new Intent(MainMenu.this, BackgroundSound.class);
            startService(intent);
            GlobalVariables.MUSIC = intent;
        }


        configurePlayButton();
        configureScoreButton();
        configureSettingsButton();
        configureExitButton();
        configureAvatarButton();
    }


    private void writeToFile(Context context) {
        String data = GlobalVariables.MAX_UPS + "," + GlobalVariables.SENSITIVITY + "," + GlobalVariables.GO_MUSIC;
        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private void configureAvatarButton(){
        Button AvatarButton = (Button) findViewById(R.id.avatar_button);
        AvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(GlobalVariables.userID != null && GlobalVariables.TOKEN != null)
                    startActivity(new Intent(MainMenu.this, ProfileActivity.class));
                else
                    startActivity(new Intent(MainMenu.this, SignIn.class));

            }
        });
    }
    private void configurePlayButton(){
        Button PlayMenuButton = (Button) findViewById(R.id.play_button);
        PlayMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, GameMenu.class));
            }
        });
    }
    private void configureScoreButton(){
        Button ScoreButton = (Button) findViewById(R.id.score_button);
        ScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, ScoreBoardActivity.class));
            }
        });
    }
    private void configureSettingsButton(){
        Button settingsButton = (Button) findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, Settings.class));
            }
        });
    }
    private void configureExitButton(){
        Button exitButton = (Button) findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                System.exit(0);
            }
        });
    }
}