package com.sasha.tetris;

import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Figure {

    CopyOnWriteArrayList<Block> figure = new CopyOnWriteArrayList<Block>();
    CopyOnWriteArrayList<Block> figureShadow = new CopyOnWriteArrayList<Block>();
    int[][] shape = new int[4][4];
    int type, size, color;
    int x = 3, y = 0;

    Figure() {
        type = new Random().nextInt(GameMaster.SHAPES.length);
        size = GameMaster.SHAPES[type][4][0];
        color = GameMaster.SHAPES[type][4][1];
        if (size == 4) {
            y = - 1;
        }
        for (int i = 0; i < size; i++) {
            System.arraycopy(GameMaster.SHAPES[type][i], 0, shape[i], 0, GameMaster.SHAPES[type][i].length);
        }
        createFromShape();
    }

    void createFromShape() {
        Log.d("debug", "createFromShape");
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (shape[y][x] == 1) {
                    figure.add(new Block(x + this.x, y + this.y));
                }
            }
        }
    }

    void drop(){
        Log.d("debug", "drop");
        while (!isTouchGround()) {
            stepDown();
        }
    }

    void stepDown(){
        Log.d("debug", "stepDown");
        for (Block block : figure) {
            block.setY(block.getY() + 1);
        }
        y++;
    }

    boolean isTouchGround(){
        Log.d("debug", "isTouchGround");
        for (Block block : figure) {
            if (GameMaster.getPositionBottom(block.getY() + 1,block.getX()) > 0 ) {
                return true;
            }
        }
        return false;
    }

    void leaveOnTheGround(){
        Log.d("debug", "leaveOnTheGround");
        for (Block block : figure) {
            GameMaster.setPositionBottom(block.getY(), block.getX(), 1);
        }
    }

    void move(int direction){
        Log.d("debug", "move");
        if (!isTouchWall(direction)) {
            for (Block block : figure) {
                block.setX(block.getX() + direction);
            }
            x += direction;
        }
    }

    boolean isTouchWall(int direction) {
        Log.d("debug", "isTouchWall");
        for (Block block : figure) {
            int blockX= block.getX();
            int blockY= block.getY();
            if (blockY < 0){
                continue;
            }
            if (direction == -1 && (blockX == 0 || GameMaster.getPositionBottom(blockY,blockX-1) > 0)) {
                return true;
            }
            if (direction == 1 && (blockX == 9 || GameMaster.getPositionBottom(blockY,blockX+1) > 0)) {
                return  true;
            }
        }
        return false;
    }

    boolean isCrossGround(){
        Log.d("debug", "isCrossGround");
        for (Block block : figure) {
            if (GameMaster.getPositionBottom(block.getY()+1,block.getX()) > 0){
                return true;
            }
        }
        return false;
    }

    void rotate(){
        Log.d("debug", "rotate");
        for (int i = 0; i < size/2; i++){
            for (int j = i; j < size-1-i; j++){
                int tmp = shape[size-1-j][i];
                shape[size-1-j][i] = shape[size-1-i][size-1-j];
                shape[size-1-i][size-1-j] = shape[j][size-1-i];
                shape[j][size-1-i] = shape[i][j];
                shape[i][j] = tmp;
            }
            if (!isWrongPosition()) {
                figure.clear();
                createFromShape();
            }
        }
    }

    boolean isWrongPosition() {
        Log.d("debug", "isWrongPosition");
        for (int x = 0; x < size; x++){
            for (int y = 0; y < size; y++){
                if (shape[y][x] == 1) {
                    if (y + this.y < 0) {return true;}
                    if (x + this.x < 0 || x + this.x > 9) {return true;}
                    if (GameMaster.getPositionBottom(y + this.y, x + this.x) > 0) {return true;}
                }
            }
        }
        return false;
    }

    void checkFilling(){
        Log.d("debug", "checkFilling");
        int row = GameMaster.getCountField() - 1;
        int countFillRows = 0;
        while (row > 0) {
            int filled = 1;
            for (int col = 0; col < 10; col++){
                filled *= GameMaster.getPositionBottom(row,col);
            }
            if (filled > 0) {
                countFillRows++;
                for (int i = row; i >0; i--){
                    System.arraycopy(GameMaster.getBottom()[i-1],0,GameMaster.getBottom()[i],0,10);
                }
            } else {
                row--;
            }
        }
        if (countFillRows > 0) {
            GameMaster.setGameScores(countFillRows, false);
        }
    }

    void paint(Canvas canvas) {
        Log.d("debug", "paint");
        for (Block block : figure) {
            block.paint(canvas, color);
        }
    }

    void paintShadow(Canvas canvas) {
        Log.d("debug", "paintShadow");
        figureShadow.clear();
        for (Block block : figure){
            figureShadow.add(new Block(block.getX(), block.getY()));
        }
        while (!isTouchGroundShadow() && figureShadow.size()!=0){
            for (Block block : figureShadow) {
                block.setY(block.getY() + 1);
            }
        }
        for (Block block : figureShadow) {
            block.paint(canvas, 0xFFDCDCDC);
        }
    }

    boolean isTouchGroundShadow(){
        Log.d("debug", "isTouchGroundShadow");
        for (Block block : figureShadow) {
            if (GameMaster.getPositionBottom(block.getY() + 1,block.getX()) > 0 ) {
                return true;
            }
        }
        return false;
    }

}
