package com.sasha.tetris;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.Arrays;

public class SettingActivity extends Activity {

    final static String HEIGHT = "count_field_height";
    final static String SPEED = "game_speed";
    final static String SHADOW = "display_shape_shadow";

    String[] data = {"default", "15", "16", "17", "18", "19", "20"};
    String[] speed = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerHeight = (Spinner) findViewById(R.id.spinnerHeight);
        spinnerHeight.setAdapter(adapter);
        // заголовок
        spinnerHeight.setPrompt("Title");
        // выделяем элемент
        spinnerHeight.setSelection(loadSetting(HEIGHT, data));
        // устанавливаем обработчик нажатия
        spinnerHeight.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                sPref = getSharedPreferences("SettingPreferences", MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(HEIGHT, data[position]);
                ed.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapterSp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, speed);
        adapterSp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerSpeed = (Spinner) findViewById(R.id.spinnerSpeed);
        spinnerSpeed.setAdapter(adapterSp);
        spinnerSpeed.setPrompt("Game speed");
        spinnerSpeed.setSelection(loadSetting(SPEED, speed));

        spinnerSpeed.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                sPref = getSharedPreferences("SettingPreferences", MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(SPEED, speed[position]);
                ed.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Switch switchShadow = (Switch) findViewById(R.id.switchShadow);
        switchShadow.setChecked(loadSetting(SHADOW));

        switchShadow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sPref = getSharedPreferences("SettingPreferences", MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(SHADOW, isChecked ? "true" : "false");
                ed.commit();
            }
        });

    }

    private int loadSetting(String key, String[] array){
        sPref = getSharedPreferences("SettingPreferences", MODE_PRIVATE);
        String savedValue = sPref.getString(key, "");
        int i = Arrays.asList(array).indexOf(savedValue);
        if (i < 0){
            return 0;
        } else {
            return i;
        }
    }

    private boolean loadSetting(String key){
        sPref = getSharedPreferences("SettingPreferences", MODE_PRIVATE);
        String savedValue = sPref.getString(key, "true");
        if (savedValue == "true"){
            return true;
        } else {
            return false;
        }
    }

}
