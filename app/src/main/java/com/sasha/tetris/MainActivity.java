package com.sasha.tetris;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonPlay = (Button) findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        Button buttonRecordList = (Button) findViewById(R.id.buttonRecordList);
        buttonRecordList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RecordActivity.class);
                startActivity(intent);
            }
        });

    }

    private void startGame(){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

}
