package com.example.slimyadventure.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import com.example.slimyadventure.GlobalVariables;

public class Platform implements GameObject{

    private Rect rectangle;
    private int color;
    private int level;
    private int type; //basic = 0, boost = 1, trap = -1
    // TODO: position, theme

    private Animation idle;
    private AnimationManager animationManager;

    public Platform(Rect rectangle, int color, int type){
        this.rectangle = rectangle;
        this.color = color;
        this.type = type;
        Bitmap src_idle;
        if(type == -1)
            src_idle = GlobalVariables.bad_platform;
        else
            src_idle = GlobalVariables.src_idle;

        idle = new Animation(new Bitmap[]{src_idle}, 0.5f);
        animationManager = new AnimationManager(new Animation[]{idle});
    }

    public boolean doesCollide(Player player){
        if(player.getJumpState() == -1){
            return (rectangle.contains(player.getRectangle().left, player.getRectangle().bottom + rectangle.height() / 3)
                || rectangle.contains(player.getRectangle().right, player.getRectangle().bottom + rectangle.height() / 3))
                && (rectangle.contains(player.getRectangle().left, player.getRectangle().bottom)
                || rectangle.contains(player.getRectangle().right, player.getRectangle().bottom));
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
//        Paint paint = new Paint();
//        paint.setColor(color);
//        canvas.drawRect(rectangle, paint);
        animationManager.draw(canvas, rectangle, rectangle.left - (int)(rectangle.width() * 0.1),
                rectangle.top - (int)(rectangle.width() * 0.15));
    }

    @Override
    public void update(Point point) {
        // left, right, top, bottom
        rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2, point.x + rectangle.width()/2, point.y + rectangle.height()/2);
        animationManager.playAnimation(0);
        animationManager.update();
    }

    public Rect getPlatform(){
        return this.rectangle;
    }

    public int getType(){
        return this.type;
    }

    public Point getLocation() {
        // Log.i("location", "x: " + rectangle.centerX() + " y: " + rectangle.centerY());
        return new Point(rectangle.centerX(), rectangle.centerY());
    }
}
