package com.example.slimyadventure.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slimyadventure.APIHandler;
import com.example.slimyadventure.GlobalVariables;
import com.example.slimyadventure.R;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class ProfileActivity extends AppCompatActivity {
    private ImageView image;
    private TextView username, avg, max, played;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        image = findViewById(R.id.imageView);
        username = findViewById(R.id.UserName);
        avg = findViewById(R.id.AllTimeAverage);
        max = findViewById(R.id.MaxScoreView);
        played = findViewById(R.id.PlayedGames);

        configureBackButton();
        configureLogoutButton();

        JSONObject jsonParam = new JSONObject();
        try {
            APIHandler Call = new APIHandler(new URL("http://87.229.85.225:42420/user/profile/"+ GlobalVariables.userID), jsonParam, "GET", GlobalVariables.TOKEN);
            Call.startThread();
            Call.stopThread();
            String[] items;
            if(Call.getResponse() != null){
                String response = Call.getResponse();
                if (response.contains("},")) {
                    items = response.split("\\},");
                    items[items.length-1] = items[items.length-1].replace("}","");
                } else {
                    items = new String[1];
                    items[0] = response.replace("}","");
                }
                for (int i = 0; i < items.length; i++) {
                    String[] subItems = items[i].split(",");
                    Integer avatar = Integer.valueOf(subItems[0].replace("\"avatar\":", "").replace("\"","").trim());
                    Float highscore = Float.valueOf(subItems[1].replace("\"highscore\":", "").replace("\"","").trim());
                    Float averagescore = Float.valueOf(subItems[2].replace("\"averagescore\":", "").replace("\"","").trim());
                    Float gamesplayed = Float.valueOf(subItems[3].replace("\"gamesplayed\":", "").replace("\"","").trim());
                    Log.i("num", ""+avatar+" "+highscore+" "+averagescore+" "+gamesplayed);
                    switch (avatar){
                        case 1:
                            this.image.setImageResource(R.drawable.ava01);
                            break;
                        case 2:
                            this.image.setImageResource(R.drawable.ava02);
                            break;
                        case 3:
                            this.image.setImageResource(R.drawable.ava03);
                            break;
                        case 4:
                            this.image.setImageResource(R.drawable.ava04);
                            break;
                        case 5:
                            this.image.setImageResource(R.drawable.ava05);
                            break;
                        case 6:
                            this.image.setImageResource(R.drawable.ava06);
                            break;
                        case 7:
                            this.image.setImageResource(R.drawable.ava07);
                            break;
                        case 8:
                            this.image.setImageResource(R.drawable.ava08);
                            break;
                        case 9:
                            this.image.setImageResource(R.drawable.ava09);
                            break;
                        case 10:
                            this.image.setImageResource(R.drawable.ava010);
                            break;
                        case 11:
                            this.image.setImageResource(R.drawable.ava011);
                            break;
                        case 12:
                            this.image.setImageResource(R.drawable.ava012);
                            break;
                        default:
                            this.image.setImageResource(R.drawable.ava01);
                            break;
                    }
                    this.username.setText(GlobalVariables.username);
                    this.max.setText("Highscore: "+Float.toString(highscore));
                    this.avg.setText("Average score: "+Float.toString(averagescore));
                    this.played.setText("Games played: "+Float.toString(gamesplayed));
                }
            }
            Toast.makeText(ProfileActivity.this, Call.getMsg(), Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void configureBackButton(){
        Button PlayMenuButton = (Button) findViewById(R.id.backbtn);
        PlayMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, MainMenu.class));
            }
        });
    }

    private void configureLogoutButton(){
        Button PlayMenuButton = (Button) findViewById(R.id.logoutbtn);
        PlayMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariables.TOKEN = null;
                GlobalVariables.userID = null;
                GlobalVariables.username = null;
                startActivity(new Intent(ProfileActivity.this, SignIn.class));
            }
        });
    }
}