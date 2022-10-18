package com.example.slimyadventure.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slimyadventure.APIHandler;
import com.example.slimyadventure.GlobalVariables;
import com.example.slimyadventure.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class GameOverActivity extends AppCompatActivity {
    private float score, timer;
    private boolean win;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        score = -1;
        timer = -1;
        win = false;
        if(b != null){
            score = b.getFloat("score");
            timer = b.getFloat("timer");
            win = b.getBoolean("win");
            Log.i("s", ""+score + " " + timer + " " + win);
            if(win) {
                setContentView(R.layout.game_over_stage_win_layout);
                TextView scoreTxt = findViewById(R.id.scoreView);
                scoreTxt.setText("You finished in " + Integer.toString((int)timer) + " seconds!");
            }else{
                if(score == 0.0)
                    setContentView(R.layout.game_over_stage_layout);
                else {
                    setContentView(R.layout.game_over_layout);
                    TextView scoreTxt = findViewById(R.id.scoreView);
                    scoreTxt.setText("You have " + Float.toString(score) + " points!");
                    JSONObject jsonParam = new JSONObject();
                    try {
                        jsonParam.put("userID", GlobalVariables.userID);
                        jsonParam.put("score", score);
                        APIHandler Call = new APIHandler(new URL("http://87.229.85.225:42420/game"), jsonParam, "POST", GlobalVariables.TOKEN);
                        Call.startThread();
                        Call.stopThread();
                        Toast.makeText(GameOverActivity.this, "ඞඞඞ", Toast.LENGTH_SHORT).show();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        configureBackButton();
    }

    private void configureBackButton(){
        Button settingsButton = (Button) findViewById(R.id.button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GameOverActivity.this, GameMenu.class));
                finish();
            }
        });
    }

}