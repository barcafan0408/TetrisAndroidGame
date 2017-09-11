package com.sasha.tetris;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RecordActivity extends Activity {

    ListView recordListView;
    ArrayList<HashMap<String, Object>> recordList;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        recordListView = (ListView) findViewById(R.id.recordList);

        recordList = new ArrayList<HashMap<String, Object>>();

        databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.getResults();
        while (cursor.moveToNext()){
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("name", cursor.getString(1));
            hashMap.put("date", new Date(cursor.getLong(2)));
            hashMap.put("result", cursor.getInt(3));
            recordList.add(hashMap);
        }
        databaseHelper.close();

        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), recordList, R.layout.record_list_item,
                new String[]{"name", "date", "result"},
                new int[]{R.id.record_name, R.id.record_date, R.id.record_result});
        recordListView.setAdapter(adapter);
    }
}
