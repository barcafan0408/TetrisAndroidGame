package com.sasha.tetris;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Block {

    private int x, y;
    private Paint paint;
    public Block(int x, int y) {
        setX(x);
        setY(y);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(10);
    }
    void setX(int x) {this.x = x;}
    void setY(int y) {this.y = y;}
    int getX() {return x;}
    int getY() {return y;}
    void paint(Canvas canvas, int color) {
        paint.setColor(color);
        int blockSize = GameMaster.getBlockSize();
        int startX = GameMaster.getPointStartX();
        int startY = GameMaster.getPointStartY();
        canvas.drawRect(startX+blockSize*x, startY+blockSize*y, startX+blockSize*(x+1), startY+blockSize*(y+1), paint);
    }

}
