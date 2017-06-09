package com.example.helllo.tim;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;
    Button button;
    final int REQUEST_CODE_TIMER = 1001; // 타이머 엑티비티
    final int REQUEST_CODE_SETTING = 1002; // 세팅 엑티비티


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        button = (Button) findViewById(R.id.button);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();

        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment1).commit();

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("오늘 기록"));
        tabs.addTab(tabs.newTab().setText("전체 기록"));
        tabs.addTab(tabs.newTab().setText("업적"));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;
                if (position == 0) {
                    selected = fragment1;
                } else if (position == 1) {
                    selected = fragment2;
                } else if (position == 2) {
                    selected = fragment3;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // 다른 탭 눌렀을 때
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // 현재 탭 눌렀을 때
            }
        });




    }

    public void onTimerStartClicked (View v){
        Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
        startActivityForResult(intent, REQUEST_CODE_TIMER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(resultCode){
            case REQUEST_CODE_TIMER:
                Log.e("asdf", "timer에서 돌아옴");
                break;
            case REQUEST_CODE_SETTING:
                Log.e("asdf", "setting에서 돌아옴");
                break;
        }
    }
}
