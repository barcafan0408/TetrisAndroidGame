package com.sasha.tetris;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public final class GameMaster {

    public static final int [][][] SHAPES = {
            {{0,0,0,0}, {1,1,1,1}, {0,0,0,0}, {0,0,0,0}, {4, 0xFF00F0F0}}, //I
            {{0,0,0,0}, {0,1,1,0}, {0,1,1,0}, {0,0,0,0}, {4, 0xFFF0F000}}, //O
            {{1,0,0,0}, {1,1,1,0}, {0,0,0,0}, {0,0,0,0}, {3, 0xFF0000F0}}, //J
            {{0,0,1,0}, {1,1,1,0}, {0,0,0,0}, {0,0,0,0}, {3, 0xFFF0A000}}, //L
            {{0,1,1,0}, {1,1,0,0}, {0,0,0,0}, {0,0,0,0}, {3, 0xFF00F000}}, //S
            {{1,1,1,0}, {0,1,0,0}, {0,0,0,0}, {0,0,0,0}, {3, 0xFFA000F0}}, //T
            {{1,1,0,0}, {0,1,1,0}, {0,0,0,0}, {0,0,0,0}, {4, 0xFFF00000}}, //Z
    };

    private static int pointStartX;
    private static int pointStartY;
    private static int BLOCK_SIZE;

    private static int paddingLeft;
    private static int paddingRight;
    private static int paddingTop;
    private static int paddingBottom;
    private static int count_field_height;

    private static int [][] bottom;

    private static int gameScores = 0;
    public static final int [] SCORES = {100, 300, 700, 1500};

    private static int gameSpeed;
    private static boolean displayShadow;

    public static int getPointStartX() {
        return pointStartX;
    }

    public static int getPointStartY() {
        return pointStartY;
    }

    public static void setPointStartY(int pointStartY) {
        GameMaster.pointStartY = pointStartY;
    }

    public static void setPointStartX(int pointStartX) {
        GameMaster.pointStartX = pointStartX;
    }

    public static int getBlockSize() {
        return BLOCK_SIZE;
    }

    public static void setBlockSize(int blockSize) {
        BLOCK_SIZE = blockSize;
    }

    public static int getCountField() {
        return count_field_height;
    }

    public static void setCountField(int height) {
        count_field_height = height;
    }

    public static int getPositionBottom(int height, int width) {
        Log.d("debug", "getPositionBottom: " + "height = " + height + ";   width = " + width);
        return bottom[height][width];
    }

    public static void setPositionBottom(int height, int width, int value){
        bottom[height][width] = value;
    }

    public static void setNewBottom(){
        bottom = new int[count_field_height+1][10];
        for (int i = 0; i < 10; i++){
            bottom[count_field_height][i] = 1;
        }
    }

    public static int [][] getBottom(){
        return bottom;
    }

    public static int getGameScores() {
        return gameScores;
    }

    public static void setGameScores(int countFillRows, boolean gameOver) {
        if (gameOver){
            gameScores = 0;
        } else {
            gameScores += SCORES[countFillRows - 1];
        }
    }

    public static int getGameSpeed() {
        return gameSpeed;
    }

    public static void setGameSpeed(int gameSpeed) {
        GameMaster.gameSpeed = gameSpeed;
    }

    public static boolean isDisplayShadow() {
        return displayShadow;
    }

    public static void setDisplayShadow(boolean displayShadow) {
        GameMaster.displayShadow = displayShadow;
    }

    public static void createArea(Canvas canvas){

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        paddingTop = 0 + height / 40;             //20
        paddingBottom = height - height / 20;

        if (count_field_height==0) {
            int paddingMargin = width / 10;
            paddingLeft = 0 + paddingMargin;
            paddingRight = width - paddingMargin;

            setPointStartX(paddingLeft);
            setPointStartY(paddingTop);

            BLOCK_SIZE = (paddingRight - paddingLeft) / 10;
            paddingRight = paddingLeft + BLOCK_SIZE * 10;

            count_field_height = (paddingBottom - paddingTop) / BLOCK_SIZE;
            paddingBottom = paddingTop + BLOCK_SIZE * count_field_height;

            bottom = new int[count_field_height + 1][10];
            for (int i = 0; i < 10; i++) {
                //bottom[0][i] = 1;
                bottom[count_field_height][i] = 1;
            }
        } else {
            BLOCK_SIZE = (paddingBottom - paddingTop) / count_field_height;
            paddingBottom = paddingTop + BLOCK_SIZE * count_field_height;

            bottom = new int[count_field_height + 1][10];
            for (int i = 0; i < 10; i++) {
                bottom[count_field_height][i] = 1;
            }

            int paddingMargin = (width-BLOCK_SIZE*10)/2;
            paddingLeft = 0 + paddingMargin;
            paddingRight = width - paddingMargin;

            setPointStartX(paddingLeft);
            setPointStartY(paddingTop);
        }

    }

    public static void paintArea(Canvas canvas){

        Paint paint = new Paint();
        Rect rect = new Rect();

        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);

        rect.set(paddingLeft,paddingTop,paddingRight,paddingBottom);
        canvas.drawRect(rect, paint);

        Paint paintLine = new Paint();
        paintLine.setColor(Color.RED);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);

        for(int i=1; i<10; i++){
            canvas.drawLine(paddingLeft+BLOCK_SIZE*i,paddingTop,paddingLeft+BLOCK_SIZE*i,paddingBottom,paintLine);
        }

        for(int i=1; i<count_field_height; i++){
            canvas.drawLine(paddingLeft,paddingTop+BLOCK_SIZE*i,paddingRight,paddingTop+BLOCK_SIZE*i,paintLine);
        }

        Paint paintBottom = new Paint();
        paintBottom.setColor(0xFF778899);
        paintBottom.setStyle(Paint.Style.FILL);
        paintBottom.setStrokeWidth(10);

        for(int i=0; i<count_field_height; i++){
            for (int ii = 0; ii < 10; ii++){
                if (bottom[i][ii] == 1){
                    canvas.drawRect(paddingLeft+BLOCK_SIZE*ii, paddingTop+BLOCK_SIZE*i, paddingLeft+BLOCK_SIZE*(ii+1),  paddingTop+BLOCK_SIZE*(i+1), paintBottom);
                }
            }
        }

    }

}
