package com.example.kahye.termproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by kahye on 2015-12-19.
 */
public class Statistics extends AppCompatActivity {

    Button bt_1;
    Button bt_2;
    Button bt_3;
    Button bt_4;
    Intent intent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        bt_1 = (Button)findViewById(R.id.bt_1);
        bt_2 = (Button)findViewById(R.id.bt_2);
        bt_3 = (Button)findViewById(R.id.bt_3);
        bt_4 = (Button)findViewById(R.id.bt_4);

        intent = new Intent(this, Importance.class);

        bt_1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                intent = new Intent(Statistics.this, Importance.class);
                startActivity(intent);


            }
        });

        bt_2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                intent = new Intent(Statistics.this, Events.class);
                startActivity(intent);

            }
        });

        bt_3.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                intent = new Intent(Statistics.this, Months.class);
                startActivity(intent);

            }
        });

        bt_4.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                intent = new Intent(Statistics.this, Daily.class);
                startActivity(intent);

            }
        });

    }
}
