package com.example.helllo.tim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {
    SeekBar seekFocus, seekRest, seekGoal;
    TextView textFocus, textRest, textGoal;

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

        seekFocus.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress += 1;
                if(progress >= 60) {
                    textFocus.setText(progress/60 + "시간 " + progress%60 + "분");
                } else {
                    textFocus.setText(progress + "분");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekRest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textRest.setText(1+progress + "분");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekGoal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress += 1;
                if(progress == 1) {
                    textGoal.setText("30분");
                } else if (progress%2 == 0){
                    textGoal.setText(progress/2 + "시간");
                } else {
                    textGoal.setText(progress/2 + "시간" + " 30분");
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
    public void onBackBtnClicked (){
        Intent intent = new Intent(SettingActivity.this, TimerActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }
}
