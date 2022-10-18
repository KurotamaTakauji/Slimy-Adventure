package com.example.slimyadventure.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.slimyadventure.R;

public class GameMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_menu_layout);

        configureBackButton();
        configureEndlessButton();
        configureStage1Button();
        configureStage2Button();
        configureStage3Button();
    }

    private void configureBackButton(){
        Button settingsButton = (Button) findViewById(R.id.back_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GameMenu.this, MainMenu.class));
                finish();
            }
        });
    }
    private void configureEndlessButton(){
        Button settingsButton = (Button) findViewById(R.id.endless_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameInt = new Intent(GameMenu.this, GameStarterActivity.class);
                Bundle b = new Bundle();
                b.putInt("gameMode", 0);
                gameInt.putExtras(b);
                startActivity(gameInt);
                finish();
            }
        });
    }

    private void configureStage1Button(){
        Button settingsButton = (Button) findViewById(R.id.stages1_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameInt = new Intent(GameMenu.this, GameStarterActivity.class);
                Bundle b = new Bundle();
                b.putInt("gameMode", 1);
                gameInt.putExtras(b);
                startActivity(gameInt);
                finish();
            }
        });
    }

    private void configureStage2Button(){
        Button settingsButton = (Button) findViewById(R.id.stages2_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameInt = new Intent(GameMenu.this, GameStarterActivity.class);
                Bundle b = new Bundle();
                b.putInt("gameMode", 2);
                gameInt.putExtras(b);
                startActivity(gameInt);
                finish();
            }
        });
    }

    private void configureStage3Button(){
        Button settingsButton = (Button) findViewById(R.id.stages3_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameInt = new Intent(GameMenu.this, GameStarterActivity.class);
                Bundle b = new Bundle();
                b.putInt("gameMode", 2);
                gameInt.putExtras(b);
                startActivity(gameInt);
                finish();
            }
        });
    }
}