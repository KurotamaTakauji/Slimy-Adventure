package com.example.slimyadventure.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import com.example.slimyadventure.GlobalVariables;
import com.example.slimyadventure.R;


public class Player implements GameObject {

    private final Rect rectangle;
    private int color;
    private Point startJump = null;
    private float jumpState = 0;
    private float jumpSpeed = 0;
    private int jumpLength;

    private Animation idle;
    private Animation jump;
    private Animation lose;
    private AnimationManager animationManager;


    public Player(Rect rectangle, int color, int jumpLength){
        this.rectangle = rectangle;
        this.color = color;
        this.jumpLength = jumpLength;

        BitmapFactory bf = new BitmapFactory();
        Bitmap src_idle = Bitmap.createScaledBitmap(bf.decodeResource(GlobalVariables.CURRENT_CONTEXT.getResources(), R.drawable.slime_happy), (int)(rectangle.width() * 1.7f), (int)(rectangle.height() * 1.7f), false);
        Bitmap src_jump = bf.decodeResource(GlobalVariables.CURRENT_CONTEXT.getResources(), R.drawable.slime_jump);
        Bitmap src_lose = bf.decodeResource(GlobalVariables.CURRENT_CONTEXT.getResources(), R.drawable.slime_sad);

        idle = new Animation(new Bitmap[]{src_idle}, 0.5f);
        jump = new Animation(new Bitmap[]{src_jump}, 0.5f);
        lose = new Animation(new Bitmap[]{src_lose}, 0.5f);


        animationManager = new AnimationManager(new Animation[]{idle});
    }

    public Rect getRectangle(){
        return this.rectangle;
    }

    public float getJumpState(){
        return this.jumpState;
    }

    @Override
    public void draw(Canvas canvas) {
//        Paint paint = new Paint();
//        paint.setColor(color);
//        canvas.drawRect(rectangle, paint);
        animationManager.draw(canvas, rectangle, rectangle.left - (int)(rectangle.width() * 0.35),
                rectangle.top - (int)(rectangle.width() * 0.65));
    }

    public void resetJump(){
        startJump = null;
        jumpState = 0;
    }

    public float jump(int Camera) {
        if(startJump == null) {
            startJump = this.getLocation();
            jumpState = 0;
            return 0;
        }
        startJump.y += Camera;
        if(this.jumpState == 1 && this.getLocation().y <= (startJump.y - jumpLength)){
            jumpState = -1;
            jumpSpeed = -0.5f;
            return jumpSpeed;
        }
//        if(this.jumpState == -1 && this.getLocation().y >= startJump.y){
//            jumpState = 1;
//            jumpSpeed = 1;
//            return jumpSpeed;
//        }
        if(this.jumpState == 1 && this.getLocation().y <= (startJump.y - jumpLength + (15 * jumpLength/100))){
            jumpSpeed = 0.5f;
            return jumpSpeed;
        }
        if(this.jumpState == -1 && this.getLocation().y >= (startJump.y - jumpLength + (15 * jumpLength/100))){
            jumpSpeed = -1;
            return jumpSpeed;
        }
        if(this.jumpState == 0){
            jumpState = 1;
            jumpSpeed = 1;
            return jumpSpeed;
        }
        return jumpSpeed;
    }

    public Point getLocation() {
//        Log.i("location", "x: " + rectangle.centerX() + " y: " + rectangle.centerY());
        return new Point(rectangle.centerX(), rectangle.centerY());
    }

    public void update(Point point){
        // left, right, top, bottom
        rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2, point.x + rectangle.width()/2, point.y + rectangle.height()/2);

        float oldBottom = rectangle.bottom;

        int state = 0;
        if(rectangle.left - oldBottom > 5)
            state = 0;
        else if(rectangle.left - oldBottom < -5)
            state = 0;

        animationManager.playAnimation(state);
        animationManager.update();
    }

}

