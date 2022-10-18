package com.example.slimyadventure;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

public class GlobalVariables {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static Context CURRENT_CONTEXT;
    public static Bitmap src_idle = null;
    public static Bitmap bad_platform = null;
    public static String TOKEN;
    public static String userID;
    public static String username;
    // Settings
    public static double MAX_UPS;
    public static float SENSITIVITY;
    public static Intent MUSIC;
    public static int GO_MUSIC = 1;
}
