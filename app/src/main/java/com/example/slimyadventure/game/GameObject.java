package com.example.slimyadventure.game;

import android.graphics.Canvas;
import android.graphics.Point;


public interface GameObject {
    abstract void draw(Canvas canvas);
    public void update(Point point);
}
