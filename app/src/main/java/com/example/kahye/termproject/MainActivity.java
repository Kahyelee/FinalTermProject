package com.example.kahye.termproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {
    private GoogleMap map;
    static final LatLng SEOUL = new LatLng( 37.56, 126.97);

    Intent intent;
    Intent in;

    SQLiteDatabase db;
    String dbName = "project.db"; // name of Database;
    String tableName = "projectTable"; // name of Table;
    int dbMode = Context.MODE_PRIVATE;

    public void createTable() {
        try {
            String sql = "create table " + tableName + "(id integer primary key autoincrement, " + "important text not null, groups text not null, subject text not null, memo text not null, month integer not null, day integer not null, lng float not null, lat float not null)";
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("Lab sqlite", "error: " + e);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this, EverydayLifeLogger.class);
        intent = new Intent(this, Statistics.class);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFragment.getMap();

        db = openOrCreateDatabase(dbName,dbMode,null);
        createTable();

        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();


        while (!results.isAfterLast()) {
            float lng = results.getFloat(7);
            float lat = results.getFloat(8);

            map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
            results.moveToNext();


        }
        results.close();

        //현재 위치로 가는 버튼 표시
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));//초기 위치...수정필요

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                String msg = "lon: "+location.getLongitude()+" -- lat: "+location.getLatitude();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                drawMarker(location);
            }
        };

        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(getApplicationContext(), locationResult);
    }

    private void drawMarker(Location location) {
        //기존 마커 지우기
        map.clear();

        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

        //currentPosition 위치로 카메라 중심을 옮기고 화면 줌을 조정한다. 줌범위는 2~21, 숫자클수록 확대
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 17));
        map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);

        //마커 추가
        map.addMarker(new MarkerOptions()
                .position(currentPosition)
                .snippet("Lat:" + location.getLatitude() + "Lng:" + location.getLongitude())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("현재위치"));

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "메모 추가하기").setIcon(android.R.drawable.ic_menu_rotate);
        menu.add(0, 1, 0, "통계 보기").setIcon(android.R.drawable.ic_menu_rotate);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                intent = new Intent(MainActivity.this, EverydayLifeLogger.class);
                startActivity(intent);

                break;
            case 1:
                intent = new Intent(MainActivity.this, Statistics.class);
                startActivity(intent);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}