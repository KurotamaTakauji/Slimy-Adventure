package com.example.slimyadventure.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.slimyadventure.APIHandler;
import com.example.slimyadventure.GlobalVariables;
import com.example.slimyadventure.R;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScoreBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard_layout);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        List<Item> Items = new ArrayList<Item>();

        JSONObject jsonParam = new JSONObject();
        try {
            APIHandler Call = new APIHandler(new URL("http://87.229.85.225:42420/game"), jsonParam, "GET", GlobalVariables.TOKEN);
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
                Integer[] scores = new Integer[items.length];
                Integer[] tempScores = new Integer[items.length];
                List<Item> temp = new ArrayList<Item>();
                for (int i = 0; i < items.length; i++) {
                    String[] subItems = items[i].split(",");
                    String username = subItems[0].replace("\"username\":", "").replace("\"","");
                    String userID = subItems[1].replace("\"userID\":", "").replace("\"","");
                    String preScore = subItems[2].replace("\"score\":", "").replace("\"","");
                    Log.i("num", preScore);
                    Integer score = Integer.parseInt(preScore.trim());
                    int avatar = getAvatar(userID);
                    switch (avatar){
                        case 1:
                            temp.add(new Item(username, "Score: "+ score, R.drawable.ava01));
                            break;
                        case 2:
                            temp.add(new Item(username, "Score: "+ score, R.drawable.ava02));
                            break;
                        case 3:
                            temp.add(new Item(username, "Score: "+ score, R.drawable.ava03));
                            break;
                        case 4:
                            temp.add(new Item(username, "Score: "+ score, R.drawable.ava04));
                            break;
                        case 5:
                            temp.add(new Item(username, "Score: "+ score, R.drawable.ava05));
                            break;
                        case 6:
                            temp.add(new Item(username, "Score: "+ score, R.drawable.ava06));
                            break;
                        case 7:
                            temp.add(new Item(username, "Score: "+ score, R.drawable.ava07));
                            break;
                        case 8:
                            temp.add(new Item(username, "Score: "+ score, R.drawable.ava08));
                            break;
                        case 9:
                            temp.add(new Item(username, "Score: "+ score, R.drawable.ava09));
                            break;
                        case 10:
                            temp.add(new Item(username, "Score: "+ score, R.drawable.ava010));
                            break;
                        case 11:
                            temp.add(new Item(username, "Score: "+ score, R.drawable.ava011));
                            break;
                        case 12:
                            temp.add(new Item(username, "Score: "+ score, R.drawable.ava012));
                            break;
                        default:
                            temp.add(new Item(username, "Score: "+ score, R.drawable.ava01));
                            break;
                    }
                    scores[i] = score;
                    tempScores[i] = score;
                }
                Arrays.sort(tempScores, Collections.reverseOrder());
                for(int i = 0; i < tempScores.length; i++){
                    for(int j = 0; j < scores.length; j++){
                        if(tempScores[i] == scores[j])
                            Items.add(temp.get(j));
                    }
                }
            }
            Toast.makeText(ScoreBoardActivity.this, Call.getMsg(), Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(),Items));

    }

    public int getAvatar(String userID){
        JSONObject jsonParam = new JSONObject();
        try {
            APIHandler Call = new APIHandler(new URL("http://87.229.85.225:42420/user/profile/"+ userID), jsonParam, "GET", GlobalVariables.TOKEN);
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
                    return avatar;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return 1;
    }
}