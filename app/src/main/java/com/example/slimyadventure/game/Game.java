package com.example.slimyadventure.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Pair;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.slimyadventure.ui.GameOverActivity;
import com.example.slimyadventure.GlobalVariables;
import com.example.slimyadventure.R;

import java.util.Arrays;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private GameLoop gameLoop;
    private Context context;
    private SensorHandler sensorHandler;
    private PlatformManager[] managers;
    private Player player;
    private Platform platform;
    private SurfaceHolder surfaceHolder;
    private Point playerPoint, platformPoint;
    private int Camera, total_shift, currLvl, currGen, minusPoint;
    private float score;
    private boolean gameOver, isEndless, win;
    private Pair<Platform[], Point[]> platforms;
    private float[] waitCalls = new float[1];
    private long frameTime = 0;

    public Game(Context context, PlatformManager[] managers, boolean isEndless){
        super(context);
        // Get surface holder and add callback
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        this.context = context;
        GlobalVariables.CURRENT_CONTEXT = context;
        sensorHandler = new SensorHandler(this.context);
        gameLoop = new GameLoop(this, surfaceHolder, this.sensorHandler);
        sensorHandler.onResume();
        this.isEndless = isEndless;
        this.managers = managers;
        PlatformManager platformManager = managers[0];
        player = new Player(new Rect(0, 0,
                (int)(GlobalVariables.SCREEN_WIDTH  / (6)), (int)(GlobalVariables.SCREEN_WIDTH / (6))),
                Color.rgb(255,0,255), (int)(GlobalVariables.SCREEN_HEIGHT * 0.25f));
        platforms = platformManager.generate(0);
        currLvl = managers[0].getLevelCount();
        currGen = 0;
        platform = new Platform(new Rect(0,0, GlobalVariables.SCREEN_WIDTH,1* GlobalVariables.SCREEN_HEIGHT/4), Color.rgb(255,255,0), 0);
        Camera = 10;
        total_shift = 0;
        score = 0;
        minusPoint = 0;
        gameOver = false;
        win = false;
        playerPoint = new Point(GlobalVariables.SCREEN_WIDTH/2, 3* GlobalVariables.SCREEN_HEIGHT/4 + (int)(GlobalVariables.SCREEN_WIDTH * 0.1f));
        platformPoint = new Point(GlobalVariables.SCREEN_WIDTH/2, GlobalVariables.SCREEN_HEIGHT);
        player.update(playerPoint);
        Arrays.fill(waitCalls, 0);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        if(gameLoop.getState().equals(Thread.State.TERMINATED)) {
            SurfaceHolder surfaceHolder2 = getHolder();
            surfaceHolder2.addCallback(this);
            gameLoop = new GameLoop(this, surfaceHolder2, this.sensorHandler);
        }
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        gameLoop.stopLoop();
        sensorHandler.onPause();
        gameLoop = null;
        context = null;
        sensorHandler = null;
        managers = null;
        player = null;
        platform = null;
        this.surfaceHolder = null;
        playerPoint = null;
        platformPoint = null;
        platforms = null;
    }

    public void update() {
        if(gameOver)
            this.surfaceDestroyed(surfaceHolder);
        int elapsedTime = (int)(System.currentTimeMillis() - frameTime);
        frameTime = System.currentTimeMillis();
//        float pitch = gameLoop.getPitch();
        float roll = gameLoop.getRoll();
        float xSpeed = GlobalVariables.SENSITIVITY * 1/10f * roll * GlobalVariables.SCREEN_WIDTH/1000f;
        float ySpeed = 0;
//        float ySpeed = pitch * GlobalVariables.SCREEN_HEIGHT/10000f;
        playerPoint.x += Math.abs(xSpeed * elapsedTime) > 5 ? xSpeed * elapsedTime : 0;
        ySpeed = player.jump(0) * 0.7f * GlobalVariables.SCREEN_HEIGHT/1000f;

        if(playerPoint.y < GlobalVariables.SCREEN_HEIGHT/2){
            total_shift += Camera;
            platformPoint.y += Camera;
            for(int i = 0; i < platforms.second.length; i++){
                if(platforms.first[i] != null){
                    platforms.second[i].y += Camera;
                }
            }
            ySpeed = player.jump(0) * 0.7f * GlobalVariables.SCREEN_HEIGHT/1000f;
        }

        if(playerPoint.y < GlobalVariables.SCREEN_HEIGHT/2 && player.getJumpState() == 1){
            ySpeed = player.jump(Camera) * 0.7f * GlobalVariables.SCREEN_HEIGHT/1000f;
            platformPoint.y += Camera;
            total_shift += Camera;
            for(int i = 0; i < platforms.second.length; i++){
                if(platforms.first[i] != null){
                    platforms.second[i].y += Camera;
                }
            }
        }

        if(platforms.second[platforms.first.length -1].y >= GlobalVariables.SCREEN_HEIGHT/4){
            PlatformManager platformManager = (isEndless && currGen == managers.length - 1) ? managers[currGen] : currGen < managers.length - 1 ? managers[++currGen] : null;
            if(platformManager != null){
                Pair<Platform[], Point[]> temp = platformManager.generate(currLvl);
                currLvl += managers[currGen].getLevelCount();
                for(int i = 0; i < temp.second.length; i++)
                    temp.second[i].y += total_shift;
                Platform[] first = Arrays.copyOf(platforms.first, platforms.first.length + temp.first.length);
                Point[] second = Arrays.copyOf(platforms.second, platforms.second.length + temp.second.length);
                System.arraycopy(temp.first, 0, first, platforms.first.length, temp.first.length);
                System.arraycopy(temp.second, 0, second, platforms.second.length, temp.second.length);
                platforms = Pair.create(first, second);
            }
        }

        playerPoint.y -= Math.abs(ySpeed * elapsedTime) > 5 ? ySpeed * elapsedTime : 0;

        if (playerPoint.x < 0)
            playerPoint.x = GlobalVariables.SCREEN_WIDTH;
        else if (playerPoint.x > GlobalVariables.SCREEN_WIDTH)
            playerPoint.x = 0;
        if (playerPoint.y >= GlobalVariables.SCREEN_HEIGHT)
            gameOver = true;

        player.update(playerPoint);
        platform.update(platformPoint);
        if(platform.doesCollide(player)){
            player.resetJump();
        }

        for(int i = 0; i < platforms.second.length; i++){
            if(platforms.first[i] != null){
                if(platforms.second[i].y >= GlobalVariables.SCREEN_HEIGHT) {
                    platforms.first[i] = null;
                    platforms.second[i] = null;
                    continue;
                }
                platforms.first[i].update(platforms.second[i]);
                if(platforms.first[i].doesCollide(player)){
                    if(platforms.first[i].getType() == -1)
                        minusPoint += 10;
                    if(platforms.first[i].getType() == -1 && isEndless == false)
                        gameOver = true;
                    player.resetJump();
                }
            }
        }

        if(platforms.second[platforms.second.length-1].y >= GlobalVariables.SCREEN_HEIGHT / 2){
            gameOver = true;
            win = true;
        }

        score = total_shift/100 - minusPoint;

        if(score < 0){
            gameOver = true;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.GRAY);
        platform.draw(canvas);
        for(int i = 0; i < platforms.first.length; i++){
            if(platforms.first[i] != null){
                if(platforms.second[i].y >= 0){
                    platforms.first[i].draw(canvas);
                }
            }
        }
        player.draw(canvas);
        drawTimer(canvas);
        drawUPS(canvas);
        drawFPS(canvas);
        drawPitchAndRoll(canvas);
        drawScore(canvas);
        if(gameOver){
            Intent gameOverInt = new Intent(context, GameOverActivity.class);
            Bundle b = new Bundle();
            if(isEndless){
                b.putFloat("score", score);
            }else{
                b.putFloat("timer", gameLoop.getTimer());
            }
            if(win)
                b.putBoolean("win", true);
            gameOverInt.putExtras(b);
            ((Activity)context).startActivity(gameOverInt);
            ((Activity)context).finish();
            surfaceHolder.removeCallback(this);
        }
    }

    public void drawUPS(Canvas canvas) {
        String averageUPS = String.format("%.2f", gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS:" + averageUPS, 50, 80, paint);
    }

    public void drawFPS(Canvas canvas) {
        String averageFPS = String.format("%.2f", gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS:" + averageFPS, 50, 200, paint);
    }


    public void drawTimer(Canvas canvas) {
        String averageFPS = String.format("%.1f", gameLoop.getTimer());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Timer:" + averageFPS, GlobalVariables.SCREEN_WIDTH/2 - 80, 200, paint);
    }

    public void drawScore(Canvas canvas) {
        String score = Float.toString(this.score);
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Score:" + score, GlobalVariables.SCREEN_WIDTH/2 - 80, 80, paint);
    }

    public void drawPitchAndRoll(Canvas canvas) {
        String pitch = String.format("%.2f", gameLoop.getPitch());
        String roll = String.format("%.2f", gameLoop.getRoll());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Pitch:" + pitch, 3* GlobalVariables.SCREEN_WIDTH/4, 80, paint);
        canvas.drawText("Roll:" + roll, 3* GlobalVariables.SCREEN_WIDTH/4, 200, paint);
    }
}
