package com.example.helllo.tim;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.helllo.tim.R.color.progressNomal;
import static com.example.helllo.tim.R.color.progressPomodoro;
import static com.example.helllo.tim.R.drawable.pause;
import static com.example.helllo.tim.R.drawable.play;


public class TimerActivity extends AppCompatActivity implements View.OnClickListener {

    final int REQUEST_CODE_SETTING = 1002; // request code of SettingActivity
    final boolean PLAY = true;
    final boolean PAUSE = false;
    private int timerMode = 0;
    private int seconds = 1500;
    private int maxProgress = 1500;
    private boolean running;
    private boolean wasRunnig;

    TextView timer;
    ImageView startOrPause;

    CircleProgressBar circleProgressBar;


    SharedPreferences setting;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* for timer */
        if(savedInstanceState!=null){
            running = savedInstanceState.getBoolean("runningState");
            seconds = savedInstanceState.getInt("secondsState");
            wasRunnig = savedInstanceState.getBoolean("wasRunningState");
        }

        setContentView(R.layout.activity_timer);

        setting = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        editor = setting.edit();

        /* timer 연동 */
        timer = (TextView)findViewById(R.id.timer);
        runTimer();

        /* 커스텀할 프로그레스바 */
        circleProgressBar = (CircleProgressBar) findViewById(R.id.custom_progressBar);

        /* 이미지뷰 참조 */
        ImageView back = (ImageView) findViewById(R.id.back);
        ImageView setting = (ImageView) findViewById(R.id.setting);
        startOrPause = (ImageView) findViewById(R.id.startOrPause);
        ImageView stop = (ImageView) findViewById(R.id.stop);

        /* startOrPause 태그값 세팅 */
        startOrPause.setTag(PLAY); // default로는 play image로 세팅했으므로

        /* 이미지뷰에 클릭리스너 등록 */
        back.setOnClickListener(this);
        setting.setOnClickListener(this);
        startOrPause.setOnClickListener(this);
        stop.setOnClickListener(this);

//        circleProgressBar.setProgressWithAnimation(10);
    }

    /* 각 이미지뷰 클릭 이벤트 처리 */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                onBackBtnClicked();
                break;
            case R.id.setting:
                onSettingBtnClicked();
                break;
            case R.id.startOrPause:
                if((boolean)startOrPause.getTag()){ // 재생 버튼이 보이는 상태에서 클릭했다면, 일시정지 버튼으로 바꿔주고 타이머 실행
                    startOrPause.setImageResource(pause);
                    startOrPause.setTag(PAUSE);
//                    circleProgressBar.setProgressWithAnimation(10);
                    onTimerStart();
                }else{ // 일시정지 버튼이 보이는 상태에서 클릭했다면, 재생 버튼으로 바꿔주고 타이머 멈춤
                    startOrPause.setImageResource(play);
                    startOrPause.setTag(PLAY);
//                    circleProgressBar.pauseAnimation();
                    onTimerStop();
                }
                break;
            case R.id.stop:
                onTimerStop();
                break;
        }
    }

    /* 뒤로가기 버튼 클릭 메서드 - main 액티비티로 이동 */
    public void onBackBtnClicked (){
        Intent intent = new Intent(TimerActivity.this, MainActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }

    /* 설정 버튼 클릭 메서드 - setting 액티비티로 이동 */
    public void onSettingBtnClicked (){
        Intent intent = new Intent(TimerActivity.this, SettingActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SETTING);
        // @TODO: SharedPreferences -> settingActivity에서 설정값
    }

    /* setting 액티비티에서 timer 액티비티로 돌아왔을 때에 대한 Activity Result 메서드 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // @TODO: 세팅 코드 완료되는거 보고 결정
    }

    /* 타이머 시작 메서드 */
    public void onTimerStart() {
        running = true;
    }

    /* 타이머 정지 메서드 */
    public void onTimerStop() {
        wasRunnig = running;
        running = false;
    }

    /* 타이머 리셋 메서드 */
//    public void onTimerReset(View view) {
//        wasRunnig = false;
//        running = false;
//        seconds = 0;
//    }

    /* 타이머 동작시키는 메서드 */
    private void runTimer(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (running) {
                    if(timerMode == 1){ // 일반 타이머 모드일 경우
                        seconds++;
                        if (seconds%maxProgress == 0) circleProgressBar.setProgress(maxProgress);
                        else circleProgressBar.setProgress(seconds%maxProgress);
                    } else { // 뽀모도로 타이머 모드일 경우
                        seconds--;
                        circleProgressBar.setProgress(maxProgress - seconds);
                        if (seconds == 0) {
                            startOrPause.setImageResource(play);
                            startOrPause.setTag(PLAY);
                            onTimerStop();

                            // @TODO
                            // 성공/실패 묻기
                            // - 성공 시 -> 디비 저장
                            // - 실패 시 -> 디비 저장 하지 않음.
                            // 쉬는 타이머 시작
                            // setPomodoroTimer("rest", 300, progressRest);

                        }
                    }
                }
                handler.postDelayed(this, 1000);

                setTimerTextView(seconds);
            }
        });
    }

    private void setTimerTextView(int seconds){
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        String time = String.format("%d:%02d:%02d", hours, minutes, secs); // 타이머 형태
        timer.setText(time); // 화면에 표시
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putBoolean("runningState", running);
        savedInstanceState.putInt("secondsState", seconds);
        savedInstanceState.putBoolean("wasRunningState", running);
    }

    @Override
    public void onStart(){
        super.onStart();
        if(wasRunnig){
            running = true;
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        wasRunnig = running;
        running = false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        restoreState();
    }

    /* 설정에서 설정한 값으로 타이머 세팅값 불러오기 */
    protected void restoreState(){
        if(setting != null){
            timerMode = setting.getInt("mode",0);
            if(timerMode == 1){ // 일반 모드 = 1
                seconds = 0;
                maxProgress = 3600; // 1시간(3600초)
                setTimerTextView(seconds);
                circleProgressBar.setMax(maxProgress); // maxProgress초마다 프로그래스바 1바퀴 돌도록
                circleProgressBar.setColor(getResources().getColor(progressNomal)); // 일반모드 타이머 색상
            } else { // 일반 모드로 설정되지 않았다면 기본적으로 뽀모도로 타이머로 동작
                setPomodoroTimer("focus", 1500, progressPomodoro);
            }
        }
    }

    // 뽀모도로 타이머는 집중 타이머와 쉬는 타이머를 번갈아가며 세팅해줘야하므로
    private void setPomodoroTimer(String key, int setDefault, int color){
        seconds = setting.getInt(key, setDefault);
        maxProgress = seconds;
        setTimerTextView(seconds);
        circleProgressBar.setMax(maxProgress); // maxProgress초마다 프로그래스바 1바퀴 돌도록
        circleProgressBar.setColor(getResources().getColor(color)); // 타이머 색상
    }
}