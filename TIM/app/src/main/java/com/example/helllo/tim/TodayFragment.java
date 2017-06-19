package com.example.helllo.tim;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


public class TodayFragment extends Fragment {
    String sql;
    int focusHour, focusMin, focusSec;
    Cursor cursor;
    SQLiteDatabase database;
    int total;
    TextView todayFocus, todayCoin;
    Button button;

    /* 날짜를 위한 변수 */
    long now;
    Date date;
    SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_today, container, false);

        todayFocus = (TextView) rootView.findViewById(R.id.todayFocus);
        todayCoin = (TextView) rootView.findViewById(R.id.todayCoin);

//        openDatabase("Tim.db");
//
//        sql = "select sum(w_time) as sum_time from work where w_date = \"" + getTime() + "\" group by w_date";
//
//        cursor = database.rawQuery(sql, null);
//
//        cursor.moveToLast();
//        total = cursor.getInt(0);
//
//        focusHour = total/3600;
//        focusMin = total%3600/60;
//        focusSec = total%60;
//        todayFocus.setText(focusHour + ":" + focusMin + ":" + focusSec);
//        todayCoin.setText(total/1800 + "코인 획득!");
        return rootView;
    }

    public void openDatabase(String databaseName) {
        database = getActivity().openOrCreateDatabase(databaseName, MODE_PRIVATE, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        openDatabase("Tim.db");

        sql = "select sum(w_time) as sum_time from work where w_date = \"" + getTime() + "\" group by w_date";

        cursor = database.rawQuery(sql, null);

        cursor.moveToLast();
        total = cursor.getInt(0);

        focusHour = total/3600;
        focusMin = total%3600/60;
        focusSec = total%60;
        todayFocus.setText(focusHour + ":" + focusMin + ":" + focusSec);
        todayCoin.setText(total/1800 + "코인 획득!");

    }

    /* 오늘 날짜 가져오기 */
    private String getTime(){
        now = System.currentTimeMillis();
        date = new Date(now);
        return sdf.format(date);
    }
}