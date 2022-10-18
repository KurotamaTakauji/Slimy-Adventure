package com.example.slimyadventure.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.slimyadventure.GlobalVariables;
import com.example.slimyadventure.game.Game;
import com.example.slimyadventure.game.PlatformManager;

public class GameStarterActivity extends AppCompatActivity {

    private Game game;
    private int gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        gameMode = -1;
        if(b != null){
            gameMode = b.getInt("gameMode");
        }

        // Set orientation to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Set window to fullscreen
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e) {

        }


        // Hide system bars
        hideSystemBars();

        //Logging and Actions
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        GlobalVariables.SCREEN_WIDTH = dm.widthPixels;
        GlobalVariables.SCREEN_HEIGHT = dm.heightPixels;
        if(gameMode == 0){
            PlatformManager[] managers =
                    {
                            new PlatformManager(30, 3, 50, -5),
                            new PlatformManager(30, 4, 50, -5),
                            new PlatformManager(30, 4, 40, -5),
                            new PlatformManager(30, 5, 50, -5),
                            new PlatformManager(30, 5, 40, -5)
                    };
            game = new Game(this, managers, true);
        }else if(gameMode == 1){
            PlatformManager[] managers =
                    {
                            new PlatformManager(25, 3, 38, 0),
                            new PlatformManager(25, 3, 25, 0),
                    };
            game = new Game(this, managers, false);
        }else if(gameMode == 2){
            PlatformManager[] managers =
                    {
                            new PlatformManager(30, 3, 45, -5),
                            new PlatformManager(30, 4, 45, -5),
                            new PlatformManager(30, 5, 45, -5)
                    };
            game = new Game(this, managers, false);
        }else if(gameMode == 3){
            PlatformManager[] managers =
                    {
                            new PlatformManager(30, 3, 45, -8),
                            new PlatformManager(30, 4, 60, -8),
                            new PlatformManager(30, 5, 75, -8)
                    };
            game = new Game(this, managers, false);
        }
        setContentView(game);
        Log.i("Main", "Created!");

    }

    private void hideSystemBars() {
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if (windowInsetsController == null) {
            Log.i("error", "No controller :C!");
            return;
        }
        // Configure the behavior of the hidden system bars
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
    }

}