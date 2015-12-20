package com.example.kahye.termproject;

/**
 * Created by kahye on 2015-12-20.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Importance extends AppCompatActivity {

    SQLiteDatabase db;
    String dbName = "project.db"; // name of Database;
    String tableName = "projectTable"; // name of Table;
    int dbMode = Context.MODE_PRIVATE;
    String []str = {"긴급", "중요", "일반", ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = openOrCreateDatabase(dbName,dbMode,null);
        createTable();
        int []count = new int[3];
        float []values = new float[4];

        int max = 0;

        String sql = "select * from " + tableName + ";";
        Cursor result = db.rawQuery(sql, null);

        count[0] = count[1] = count[2] = 0;

        result.moveToFirst();
        while(!result.isAfterLast()){
            String important = result.getString(1);
            Log.d("lab_sqlite", "import="+important);
            switch(important){
                case "긴급":
                    count[0]++;
                    Log.d("lab_sqlite", "asda");
                    break;
                case "중요":
                    count[1]++;
                    break;
                case "일반":
                    count[2]++;
                    break;
                default:
                    Log.d("ERROR","err");
            }
            result.moveToNext();
        }

        for(int i = 0; i < 3; i++) {
            if(count[i] > max) max = count[i];
            values[i] = count[i];
        }

        values[3] =0;

        String []veriables = new String[]{""+max , ""+max/2, "0"};
        GraphView graphView = new GraphView(this, values, "중요도 통계", str, veriables, GraphView.BAR);
        setContentView(graphView);
    }

    public void createTable() {
        try {
            String sql = "create table " + tableName + "(id integer primary key autoincrement, " + "important text not null, groups text not null, subject text not null, memo text not null, month integer not null, day integer not null, lng float not null, lat float not null)";
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("Lab sqlite", "error: " + e);
        }
    }
}