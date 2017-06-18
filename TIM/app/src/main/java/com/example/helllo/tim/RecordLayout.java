package com.example.helllo.tim;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RecordLayout extends LinearLayout {
    Context mContext;
    LayoutInflater inflater;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    TextView date, time;

    //생성자-1
    public RecordLayout(Context context) {
        super(context);
        mContext = context;

        //객체가 생성될 때 초기화
        init();
    }

    //생성자-2
    public RecordLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        //객체가 생성될 때 초기화
        init();
    }

    //초기화 메서드
    private void init() {
        // 아이템의 화면을 구성한 XML 레이아웃(singer_item.xml)을 인플레이션
        inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.record_item, this, true);

        //부분화면 레이아웃에 정의된 객체 참조
        time = (TextView) findViewById(R.id.graph);
        date = (TextView) findViewById(R.id.date);

        setting = getContext().getSharedPreferences("setting", Activity.MODE_PRIVATE);
        editor = setting.edit();
    }//end of init()

    public void setTime(int time) {
        int goal = setting.getInt("goal", 7200);
        this.time.setHeight(time/60); // 초 단위를 분 단위로 바꾸어 픽셀값으로 사용.
        if (time >= goal) {
            this.time.setBackgroundColor(Color.parseColor("#328C8B")); //#4C545A
        } else {
            this.time.setBackgroundColor(Color.parseColor("#242D37")); //#4C545A
        }
    }

    public void setDate(String date) {
        this.date.setText(date);
    }
}