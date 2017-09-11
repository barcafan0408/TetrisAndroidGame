package com.sasha.tetris;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends Activity {

    private Context thisContext = this;
    private Figure figure;
    DrawView drawView;
    private TextView gameScores;
    private boolean pauseActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        figure = new Figure();

        gameScores = (TextView) findViewById(R.id.gameScores);

        LinearLayout surfaceViewLayout = (LinearLayout) findViewById(R.id.surfaceViewLayout);
        drawView = new DrawView(this);
        surfaceViewLayout.addView(drawView);

        Button buttonLeft = (Button) findViewById(R.id.buttonLeft);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                figure.move(-1);
            }
        });

        Button buttonRight = (Button) findViewById(R.id.buttonRight);
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                figure.move(1);
            }
        });

        Button buttonRotate = (Button) findViewById(R.id.buttonRotate);
        buttonRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                figure.rotate();
            }
        });

        Button buttonDrop = (Button) findViewById(R.id.buttonDrop);
        buttonDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                figure.drop();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseActivity = true;
        drawView.stopGame();
        //drawView.drawThread.setRunning(false);
    }

    private void showGameOverDialog(){
        // Объект DialogFragment для вывода статистики и начала новой игры
        final DialogFragment gameResult = new DialogFragment(){
            // Метод создает объект AlertDialog и возвращает его
            @Override
            public Dialog onCreateDialog(Bundle bundle){
                // Создание диалогового окна с выводом строки messageId
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Game over");
                // Вывод количества выстрелов и затраченного времени
                //builder.setMessage("Scores - " + GameMaster.getGameScores());
                // создаем view из dialog.xml
                LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog, null);
                // устанавливаем ее, как содержимое тела диалога
                builder.setView(view);
                // находим TexView для отображения результатов
                TextView dialogResultText = (TextView) view.findViewById(R.id.dialogResultText);
                dialogResultText.setText("Scores - " + GameMaster.getGameScores());
                // находим EditText для ввода имени рекорда
                final EditText dialogRecordName = (EditText) view.findViewById(R.id.dialogRecordName);
                builder.setPositiveButton("restart", new DialogInterface.OnClickListener(){
                            // Вызывается при нажатии кнопки "Reset Game"
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                //dialogIsDisplayed = false;
                                GameMaster.setGameScores(0, true);
                                GameMaster.setNewBottom();
                                drawView.newGame();
                                //drawView.drawThread.setRunning(true);
                                //drawView.drawThread.start();
                                //drawView = new DrawView(context);
                                //childLinearLayout1.addView(drawView);
                                //newGame(); // Создание и начало новой партии
                            }
                        } // Конец анонимного внутреннего класса
                ); // Конец вызова setPositiveButton
                builder.setNegativeButton("save record", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                if (dialogRecordName.getText().length() != 0){
                                    //сохранение рекорда в БД
                                    DatabaseHelper databaseHelper = new DatabaseHelper(thisContext);
                                    databaseHelper.insertResult(dialogRecordName.getText().toString(), GameMaster.getGameScores());
                                    databaseHelper.close();
                                }
                                GameMaster.setGameScores(0, true);
                                GameMaster.setNewBottom();
                                drawView.newGame();
                            }
                        } // Конец анонимного внутреннего класса
                );
                return builder.create(); // Вернуть AlertDialog
            } // Конец метода onCreateDialog
        }; // Конец анонимного внутреннего класса DialogFragment
        // В UI-потоке FragmentManager используется для вывода DialogFragment
        runOnUiThread (new Runnable() {
                           public void run(){
                               //dialogIsDisplayed = true;
                               gameResult.setCancelable(false); // Модальное окно
                               gameResult.show(GameActivity.this.getFragmentManager(), "results");
                           }
                       } // Конец Runnable
        ); // Конец вызова runOnUiThread
    }

    private void updatePositions(){
        if (figure.isTouchGround()){
            figure.leaveOnTheGround();
            figure.checkFilling();
            figure = new Figure();
            boolean gameOver = figure.isCrossGround();
            if (gameOver) {
                drawView.drawThread.setRunning(false);
                //сохранение рекорда в БД
                /*DatabaseHelper databaseHelper = new DatabaseHelper(this);
                databaseHelper.insertResult("Sasha", GameMaster.getGameScores());
                databaseHelper.close();*/
                showGameOverDialog();
            }
        } else {
            figure.stepDown();
        }
    }

    public void drawGameElements(Canvas canvas){
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);

        GameMaster.paintArea(canvas);

        figure.paintShadow(canvas);
        figure.paint(canvas);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameScores.setText("Scores - " + GameMaster.getGameScores());
            }
        });
    }

    class DrawView extends SurfaceView implements SurfaceHolder.Callback {

        private DrawThread drawThread;
        Canvas canvasTest;

        public DrawView(Context context) {
            super(context);
            getHolder().addCallback(this);
            //test();
        }
        private void createArea(SurfaceHolder surfaceHolder){
            Canvas canvas;
            canvas = surfaceHolder.lockCanvas(null);
            //GameArea.paintArea(canvas);
            GameMaster.createArea(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (!pauseActivity) {
                createArea(getHolder());
                pauseActivity = false;
            }
            newGame();
            //drawThread = new DrawThread(getHolder());
            //drawThread.setRunning(true);
            //drawThread.start();
        }

        private void newGame() {
            drawThread = new DrawThread(getHolder());
            drawThread.setRunning(true);
            drawThread.start();
        }

        public void stopGame() {
            if (drawThread != null)
                drawThread.setRunning(false); // Приказываем потоку завершиться
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;
            drawThread.setRunning(false);
            while (retry) {
                try {
                    drawThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                }
            }
        }

        class DrawThread extends Thread {

            private long prevTime;

            private boolean running = false;
            private SurfaceHolder surfaceHolder;

            Paint paint = new Paint();
            Rect rect = new Rect();

            public DrawThread(SurfaceHolder surfaceHolder) {
                this.surfaceHolder = surfaceHolder;
                prevTime = System.currentTimeMillis();
            }

            public void setRunning(boolean running) {
                this.running = running;
            }

            @Override
            public void run() {
                //runOnUiThread(new Runnable() {
                //    @Override
                //    public void run() {
                Canvas canvas;
                paint.setColor(Color.GREEN);
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(10);
                while (running) {
                    canvas = null;
                    try {
                        //synchronized(surfaceHolder) {
                        canvas = surfaceHolder.lockCanvas(null);
                        if (canvas == null)
                            continue;
                        //canvas.drawColor(Color.GREEN);
                        //rect.set(500,500,700,600);
                        //canvas.drawRect(rect, paint);
                        try {
                            canvasTest = canvas;
                            //DrawThread.sleep(300);
                            //canvasTest = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        long now = System.currentTimeMillis();
                        long elapsedTime = now - prevTime;
                        if (elapsedTime > 500) {
                            prevTime = now;
                            updatePositions();
                        }
                        drawGameElements(canvas);
                        //}
                    } finally {
                        if (canvas != null) {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
                //
                //}
                //});
                //
            }
        }

    }

}
