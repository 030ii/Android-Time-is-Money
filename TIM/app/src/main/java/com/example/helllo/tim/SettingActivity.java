package com.example.helllo.tim;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {
    SeekBar seekFocus, seekRest, seekGoal;
    TextView textFocus, textRest, textGoal;
    Switch mode;

    SharedPreferences setting;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 상태 바 제거 */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_setting);

        setting = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        editor = setting.edit();

        seekFocus = (SeekBar) findViewById(R.id.seekFocus);
        seekRest = (SeekBar) findViewById(R.id.seekRest);
        seekGoal = (SeekBar) findViewById(R.id.seekGoal);

        textFocus = (TextView) findViewById(R.id.textFocus);
        textRest = (TextView) findViewById(R.id.textRest);
        textGoal = (TextView) findViewById(R.id.textGoal);

        mode = (Switch) findViewById(R.id.switch1);

        // 타이머 모드에 관한 리스너
        mode.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) { // 일반 타이머 모드
                    mode.setTextOn("일반");
                    putIntProgress("mode", 1); // mode 값이 2이면 일반 타이머 모드
                } else { // 뽀모도로 타이머 모드
                    mode.setTextOff("뽀모도로");
                    putIntProgress("mode", 0); // mode 값이 1이면 뽀모도로 타이머 모드

                }
            }
        });

        // 집중 시간에 관한 리스너
        seekFocus.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress += 60;
                if(progress >= 3600) {
                    textFocus.setText(progress/3600 + "시간 " + (progress%3600)/60 + "분");
                } else {
                    textFocus.setText(progress/60 + "분");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                int time = (progress/60 + 1) * 60; // progress값이 초 단위이므로 분 단위로 바꾸면 초 단위가 버림으로 되므로 + 1분 해준후 다시 초단위로 바꿈
                putIntProgress("focus", time);
            }
        });

        // 쉬는 시간에 관한 리스너
        seekRest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress += 60;
                textRest.setText(progress/60 + "분");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                int time = (progress/60 + 1) * 60;
                putIntProgress("rest", time);
            }
        });

        // 목표 시간에 관한 리스너
        seekGoal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress += 1800;
                if(progress == 1800) {
                    textGoal.setText("30분");
                } else if (progress%3600 < 1800){
                    textGoal.setText(progress/3600 + "시간");
                } else if (progress/3600 > 0) {
                    textGoal.setText(progress/3600 + "시간" + " 30분");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                int time = (progress/60 + 1) * 60;
                putIntProgress("goal", time);
            }
        });
    }

    /* 뒤로가기 버튼 클릭 메서드 - timer 액티비티로 이동 */
    public void onBackBtnClicked (View v){
        Intent intent = new Intent(SettingActivity.this, TimerActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoreState();
    }

    /* 이전에 설정한 값이 있다면 값 불러오기 */
    protected void restoreState(){
        if(setting != null){
            if(setting.getInt("mode",0) == 1){ // 일반 모드 = 1
                mode.setChecked(true);
            } else { // 뽀모도로 모드
                mode.setChecked(false);
            }

            if(setting.contains("focus")){
                setProgressTextView("focus", seekFocus, textFocus, 1500);
            }
            if(setting.contains("rest")){
                setProgressTextView("rest", seekRest, textRest, 300);
            }
            if(setting.contains("goal")){
                setProgressTextView("goal", seekGoal, textGoal, 7200);
            }
        }
    }

    private void setProgressTextView(String key, SeekBar seekBar, TextView textView, int setDefault){
        int progress = setting.getInt(key, setDefault);
        seekBar.setProgress(progress);

        if(progress >= 3600) {
            textView.setText(progress/3600 + "시간 " + (progress%3600)/60 + "분");
        } else {
            textView.setText(progress/60 + "분");
        }
    }

    public void putIntProgress(String key, int value){
        editor.putInt(key, value);
        editor.commit();
    }

}