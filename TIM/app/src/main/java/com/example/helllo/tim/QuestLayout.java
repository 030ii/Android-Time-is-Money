package com.example.helllo.tim;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuestLayout extends LinearLayout {
    Context mContext;
    LayoutInflater inflater;

    ImageView imageView;
    TextView nameTextView;

    //생성자-1
    public QuestLayout(Context context) {
        super(context);
        mContext = context;

        //객체가 생성될 때 초기화
        init();
    }

    //생성자-2
    public QuestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        //객체가 생성될 때 초기화
        init();
    }

    //초기화 메서드
    private void init() {
        // 아이템의 화면을 구성한 XML 레이아웃(singer_item.xml)을 인플레이션
        inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.quest_item, this, true);

        //부분화면 레이아웃에 정의된 객체 참조
        imageView = (ImageView) findViewById(R.id.imageView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
    }//end of init()

    public void setImage(int resId) {//이미지 리소스 id 설정
        imageView.setImageResource(resId);
    }

    public void setNameText(String name) {//이름 설정
        nameTextView.setText(name);
    }
}