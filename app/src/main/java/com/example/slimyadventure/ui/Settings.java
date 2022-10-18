package com.example.slimyadventure.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.slimyadventure.BackgroundSound;
import com.example.slimyadventure.GlobalVariables;
import com.example.slimyadventure.R;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);


        SeekBar sb = (SeekBar) findViewById(R.id.seekBar);

        sb.setProgress((int)(GlobalVariables.SENSITIVITY * 100));

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                GlobalVariables.SENSITIVITY = progress/(float)100;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(Settings.this, "Seek bar progress is :" + progressChangedValue,
//                        Toast.LENGTH_SHORT).show();
            }
        });


        configureBackButton();
        configureMusicButton();
        configureStopMusicButton();
        configure60fpsButton();
        configure30fpsButton();
    }
    private void configureBackButton(){
        Button settingsButton = (Button) findViewById(R.id.back_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, MainMenu.class));
                finish();
            }
        });
    }

    public void configure60fpsButton(){
        Button MusicButton = (Button) findViewById(R.id.fpsbutton60);
        MusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariables.MAX_UPS = 60.0f;
            }
        });
    }

    public void configure30fpsButton(){
        Button MusicButton = (Button) findViewById(R.id.fpsbutton30);
        MusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariables.MAX_UPS = 30.0f;
            }
        });
    }

    public void configureMusicButton(){
        Button MusicButton = (Button) findViewById(R.id.music_button);
        MusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(GlobalVariables.MUSIC == null) {
                    Intent intent = new Intent(Settings.this, BackgroundSound.class);
                    startService(intent);
                    GlobalVariables.MUSIC = intent;
                    GlobalVariables.GO_MUSIC = 1;
                }
            }
        });
    }
    public void configureStopMusicButton(){
        Button StopMusicButton = (Button) findViewById(R.id.stop_music_button);
        StopMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(GlobalVariables.MUSIC != null)
                    stopService(GlobalVariables.MUSIC);
                GlobalVariables.MUSIC = null;
                GlobalVariables.GO_MUSIC = 0;
            }
        });
    }

}