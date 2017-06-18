package com.example.helllo.tim;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/* 아이템을 위한 XML 레이아웃은 LinearLayout과 같은
   레이아웃 클래스를 상속하는 클래스를 만들어 설정함 */
public class RecordLayout extends LinearLayout {
    Context mContext;
    LayoutInflater inflater;

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
    }//end of init()

    public void setTime(int time) {
        this.time.setHeight(time/60);
        this.time.setBackgroundColor(Color.parseColor("#4C545A"));
    }

    public void setDate(String date) {
        this.date.setText(date);
    }
}
