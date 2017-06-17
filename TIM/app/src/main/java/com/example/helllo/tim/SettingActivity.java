package com.example.helllo.tim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {
    SeekBar seekFocus, seekRest, seekGoal;
    TextView textFocus, textRest, textGoal;
    Switch mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

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
                if(isChecked == true) {
                    mode.setTextOn("일반");
                    //포도
                } else {
                    mode.setTextOff("뽀모도로");
                    //포모도로
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

            }
        });
    }

    /* 뒤로가기 버튼 클릭 메서드 - timer 액티비티로 이동 */
    public void onBackBtnClicked (View v){
        Intent intent = new Intent(SettingActivity.this, TimerActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }
}