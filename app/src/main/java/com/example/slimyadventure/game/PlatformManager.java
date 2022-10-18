package com.example.slimyadventure.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.util.Pair;

import com.example.slimyadventure.GlobalVariables;
import com.example.slimyadventure.R;

import java.util.Arrays;
import java.util.Random;

public class PlatformManager {
    private int spawnRate; // Defines how many platform to generate per levelCount
    private int levelCount; // Defines how many level we generate at once
    private int distributionSum; // Defines the sum of all platforms in current generation
    private int platformPerLevel;
    private int[][] generationMatrix;

    public PlatformManager(int levelCount, int platformPerLevel, int spawnRate, int distributionSum){
        this.levelCount = levelCount;
        this.spawnRate = spawnRate;
        this.distributionSum = distributionSum;
        this.generationMatrix = new int[levelCount][platformPerLevel];
        this.platformPerLevel = platformPerLevel;
        for(int i = 0; i < levelCount; i++)
            Arrays.fill(generationMatrix[i], 2);
    }

    public void generateMatrix(int basic, int boost, int trap){
        Random random = new Random();
        for(int i = 0; i < levelCount; i++){
            int row = random.nextInt(platformPerLevel);
            if(generationMatrix[i][row] == 2) {
                generationMatrix[i][row] = 0;
                basic--;
            }else
                i--;
        }
        for(int i = 0; i < basic; i++){
            int col = random.nextInt(levelCount);
            int row = random.nextInt(platformPerLevel);
            if(generationMatrix[col][row] == 2)
                generationMatrix[col][row] = 0;
            else
                i--;
        }
        for(int i = 0; i < boost; i++){
            int col = random.nextInt(levelCount);
            int row = random.nextInt(platformPerLevel);
            if(generationMatrix[col][row] == 2)
                generationMatrix[col][row] = 1;
            else
                i--;
        }
        for(int i = 0; i < trap; i++){
            int col = random.nextInt(levelCount);
            int row = random.nextInt(platformPerLevel);
            if(generationMatrix[col][row] == 2)
                generationMatrix[col][row] = -1;
            else
                i--;
        }
    }

    public Pair<Platform[], Point[]> generate(int currentLevel){
        Platform[] result = new Platform[spawnRate];
        Point[] positions = new Point[spawnRate];
        if(Math.abs(distributionSum) + levelCount > spawnRate){
            Log.i("Error", "wrong parameters");
            return Pair.create(result, positions);
        }
        int iter = 0;
        int trap = 0, boost = 0, basic = 0, size = spawnRate;
        Random random = new Random();
        if(distributionSum < 0){
            trap = Math.abs(distributionSum);
            size -= trap;
        }else if(distributionSum > 0){
            boost = distributionSum;
            size -= boost;
        }
        basic = levelCount;
        size -= basic;
        while(size > 0){
            int number = random.nextInt(99);
            if(size%2 == 0){
                size -= 2;
                if(number < 20){
                    boost++;
                    trap++;
                }else{
                    basic += 2;
                }
            }else{
                basic++;
                size--;
            }
        }
        generateMatrix(basic, boost, trap);
        for(int i = 0; i < levelCount; i++){
            for(int j = 0; j < platformPerLevel; j++){
                Rect temp = new Rect(0,0,(int)(GlobalVariables.SCREEN_WIDTH * 0.9f / (float)platformPerLevel),
                                        (int)(GlobalVariables.SCREEN_WIDTH * 0.3f / (float)platformPerLevel));
                if(i == 0 && j == 0){
                    BitmapFactory bf = new BitmapFactory();
                    GlobalVariables.src_idle = Bitmap.createScaledBitmap(bf.decodeResource(GlobalVariables.CURRENT_CONTEXT.getResources(),
                                    R.drawable.platform),
                            (int)(temp.width() * 1.2f), (int)(temp.height() * 1.7f), false);
                    GlobalVariables.bad_platform = Bitmap.createScaledBitmap(bf.decodeResource(GlobalVariables.CURRENT_CONTEXT.getResources(),
                                    R.drawable.bad_platform),
                            (int)(temp.width() * 1.2f), (int)(temp.height() * 1.7f), false);
                }
                if(generationMatrix[i][j] != 2)
                    positions[iter] = new Point((int)(GlobalVariables.SCREEN_WIDTH * 0.9f / (2 * (float)platformPerLevel)) +
                                                j * ((int)(GlobalVariables.SCREEN_WIDTH * 0.9f / (float)platformPerLevel) +
                                                (int)(GlobalVariables.SCREEN_WIDTH * 0.1f / (float)(platformPerLevel - 1))),
                                                8* GlobalVariables.SCREEN_HEIGHT/9 - (i+1+currentLevel)*(int)(GlobalVariables.SCREEN_HEIGHT* 0.2f));
                if(generationMatrix[i][j] == 0){
                    result[iter++] = new Platform(temp,
                            Color.rgb(0,0,255), 0);
                }
                if(generationMatrix[i][j] == 1){
                    result[iter++] = new Platform(temp,
                            Color.rgb(0,255,0), 1);
                }
                if(generationMatrix[i][j] == -1){
                    result[iter++] = new Platform(temp,
                            Color.rgb(255,0,0), -1);
                }
            }
        }
        this.generationMatrix = new int[levelCount][platformPerLevel];
//        Log.i("result", "basic: " + Integer.toString(basic) + " boost: " + Integer.toString(boost) + " trap: " + Integer.toString(trap));
        return Pair.create(result, positions);
    }

    public int getDistributionSum() {
        return distributionSum;
    }

    public int getLevelCount() {
        return levelCount;
    }

    public int getSpawnRate() {
        return spawnRate;
    }

    public int getPlatformPerLevel() {
        return platformPerLevel;
    }

}
