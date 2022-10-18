package com.example.slimyadventure.game;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.slimyadventure.GlobalVariables;
import com.example.slimyadventure.ui.GameOverActivity;

public class GameLoop extends Thread{
    private final double MAX_UPS = GlobalVariables.MAX_UPS;
    private final double UPS_PERIOD = 1E+3/MAX_UPS;
    private float TIMER = 0;
    private SensorHandler sensorHandler;

    private Game game;
    private SurfaceHolder surfaceHolder;

    private boolean isRunning = false;
    private boolean isPaused = false;
    private double averageUPS;
    private double averageFPS;
    private float roll;
    private float pitch;

    public GameLoop(Game game, SurfaceHolder surfaceHolder, SensorHandler sensorHandler) {
        this.game = game;
        this.surfaceHolder = surfaceHolder;
        this.sensorHandler = sensorHandler;
    }

    public double getAverageUPS() {
        return averageUPS;
    }

    public double getAverageFPS() {
        return averageFPS;
    }

    public float getTimer() {
        return TIMER;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getRoll() {
        return this.roll;
    }

    public boolean getIsPaused() { return this.isPaused; }

    public void startLoop() {
        Log.d("GameLoop.java", "startLoop()");
        isRunning = true;
        start();
    }

    public void pauseLoop() {
        Log.d("GameLoop.java", "pauseLoop()");
        isPaused = true;
    }

    public void resumeLoop() {
        Log.d("GameLoop.java", "resumeLoop()");
        isPaused = false;
    }

    @Override
    public void run() {
        Log.d("GameLoop.java", "run()");
        super.run();

        // Declare time and cycle count variables
        int updateCount = 0;
        int frameCount = 0;

        long startTime;
        long elapsedTime;
        long startTimerTime;
        long timerTime;
        long sleepTime;

        // Game loop
        Canvas canvas = null;
        startTime = System.currentTimeMillis();
        startTimerTime = System.currentTimeMillis();
        while(isRunning) {
            if(!isPaused){
                // Try to update and render game
                try {
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        pitch = sensorHandler.getPitch();
                        roll = sensorHandler.getRoll();
                        game.update();
                        updateCount++;
                        game.draw(canvas);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        try {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                            frameCount++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                // Pause game loop to not exceed target UPS
                elapsedTime = System.currentTimeMillis() - startTime;
                timerTime = System.currentTimeMillis() - startTimerTime;
                sleepTime = (long) (updateCount * UPS_PERIOD - elapsedTime);
                if (sleepTime > 0) {
                    try {
                        sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Skip frames to keep up with target UPS
                while (sleepTime < 0 && updateCount < MAX_UPS - 1) {
                    game.update();
                    updateCount++;
                    elapsedTime = System.currentTimeMillis() - startTime;
                    timerTime = System.currentTimeMillis() - startTimerTime;
                    sleepTime = (long) (updateCount * UPS_PERIOD - elapsedTime);
                }

                // Calculate average UPS and FPS
                elapsedTime = System.currentTimeMillis() - startTime;
                timerTime = System.currentTimeMillis() - startTimerTime;
                if (timerTime >= 100) {
                    TIMER += 0.1;
                    startTimerTime = System.currentTimeMillis();
                }
                if (elapsedTime >= 1000) {
                    averageUPS = updateCount / (1E-3 * elapsedTime);
                    averageFPS = frameCount / (1E-3 * elapsedTime);
                    updateCount = 0;
                    frameCount = 0;
                    startTime = System.currentTimeMillis();
                }
            }else{
//                Log.i("Error", "Too much pitch");
            }
        }
    }

    public void stopLoop() {
        Log.d("GameLoop.java", "stopLoop()");
        isRunning = false;
        try {
            join();
            Intent gameOverInt = new Intent(GlobalVariables.CURRENT_CONTEXT, GameOverActivity.class);
            ((Activity)GlobalVariables.CURRENT_CONTEXT).startActivity(gameOverInt);
            ((Activity)GlobalVariables.CURRENT_CONTEXT).finish();
            game.surfaceDestroyed(surfaceHolder);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}