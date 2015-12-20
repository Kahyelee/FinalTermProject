package com.example.kahye.termproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class EverydayLifeLogger extends AppCompatActivity {

    SQLiteDatabase db;
    String dbName = "project.db"; // name of Database;
    String tableName = "projectTable"; // name of Table;
    int dbMode = Context.MODE_PRIVATE;

    Intent in;
    String lnt, lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = openOrCreateDatabase(dbName,dbMode,null);
        createTable();

        ArrayList<String> list = new ArrayList<String> ();
        list.add("긴급");
        list.add("중요");
        list.add("일반");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);

        Spinner sp = (Spinner) this.findViewById(R.id.importantSpinner);
        sp.setPrompt("중요도를 선택하세요.");
        sp.setAdapter(adapter);

        ArrayList<String> list2 = new ArrayList<String> ();
        list2.add("음식");
        list2.add("여행");
        list2.add("사건");
        list2.add("잡담");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list2);

        Spinner sp2 = (Spinner) this.findViewById(R.id.groupSpinners);
        sp2.setPrompt("분류를 선택하세요.");
        sp2.setAdapter(adapter2);

        Intent temp = this.getIntent();
       // lnt = temp.getStringExtra("lnt");
        //lat = temp.getStringExtra("lat");

        in = new Intent(this, Importance.class);

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sql = "select * from " + tableName + ";";
                Cursor results = db.rawQuery(sql, null);
                results.moveToFirst();

                while (!results.isAfterLast()) {
                    int id = results.getInt(0);
                    String important = results.getString(1);
                    String group = results.getString(2);
                    String subject = results.getString(3);
                    String memo = results.getString(4);
                    int month = results.getInt(5);
                    int day = results.getInt(6);
                    float lnt = results.getInt(7);
                    float lat = results.getInt(8);
                    Log.d("lab_sqlite", "index= " + id + " important=" + important + " group=" + group + " subject=" + subject + " memo=" + memo + " month=" + month + " day=" + day);
                    results.moveToNext();

                    db.close();
                    startActivity(in);
                }
                results.close();

            }
        });

        findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText subjectText = (EditText) findViewById(R.id.subjectText);
                EditText memoText = (EditText) findViewById(R.id.memoText);
                Spinner importantSpinner = (Spinner) findViewById(R.id.importantSpinner);
                Spinner groupSpinner = (Spinner) findViewById(R.id.groupSpinners);

                if (!subjectText.getText().toString().equals("") && (importantSpinner.getSelectedItemPosition() > -1)
                        && (groupSpinner.getSelectedItemPosition() > -1) && !memoText.getText().toString().equals("")) {

                    Calendar c = Calendar.getInstance();


                    String sql = "insert into " + tableName + " values(NULL, '" + (String) importantSpinner.getAdapter().getItem(importantSpinner.getSelectedItemPosition()) + "'";
                    sql += ", '" + (String) groupSpinner.getAdapter().getItem(groupSpinner.getSelectedItemPosition()) + "'";
                    sql += ", '" + subjectText.getText().toString() + "'";
                    sql += ", '" + memoText.getText().toString() + "'";
                    sql += ", " + (c.get(Calendar.MONTH)+1);
                    sql += ", " + (c.get(Calendar.DATE));
                    sql += ", "+ 0.0;
                    sql += ", "+ 0.0;
                    sql += ");";

                    db.execSQL(sql);

                } else {
                    Toast.makeText(getApplicationContext(), "내용을 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Table 생성
    public void createTable() {
        try {
            String sql = "create table " + tableName + "(id integer primary key autoincrement, " + "important text not null, groups text not null, subject text not null, memo text not null, month integer not null, day integer not null, lng float not null, lat float not null)";
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("Lab sqlite", "error: " + e);
        }
    }
}