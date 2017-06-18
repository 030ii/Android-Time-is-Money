package com.example.helllo.tim;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.helllo.tim.R.color.progressNomal;
import static com.example.helllo.tim.R.color.progressPomodoro;
import static com.example.helllo.tim.R.color.progressRest;
import static com.example.helllo.tim.R.drawable.pause;
import static com.example.helllo.tim.R.drawable.play;


public class TimerActivity extends AppCompatActivity implements View.OnClickListener {

    final int REQUEST_CODE_SETTING = 1002; // request code of SettingActivity
    final boolean PLAY = true;
    final boolean PAUSE = false;
    private int timerMode = 0;
    private int seconds = 1500;
    private int maxProgress = 1500;
    private boolean pomodoroMode = true; // true = 집중 모드 , false = 쉬는 모드
    private boolean running;
    private boolean wasRunnig;

    TextView timer;
    ImageView startOrPause;

    CircleProgressBar circleProgressBar;


    SharedPreferences setting;
    SharedPreferences.Editor editor;

    /* 알림음을 위한 변수 */
    Uri ringtoneUri;
    Ringtone ringtone;

    /* 날짜를 위한 변수 */
    long now;
    Date date;
    SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");

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

        /* sharedPreferences를 위해 */
        setting = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        editor = setting.edit();

        /* 알림음을 위해 */
        ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(),RingtoneManager.TYPE_NOTIFICATION);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);

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
                startOrPause.setImageResource(play);
                startOrPause.setTag(PLAY);
                onTimerStop();

                if(timerMode == 1) { // 일반 타이머 모드일 경우
                    createDialogBoxForFocus().show(); // 성공/실패 여부
                } else { // 뽀모도로 타이머 모드일 경우
                    createDialogBoxForSetTimer().show(); // 초기화/스킵 여부
                }
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
    public void onTimerReset() {
        wasRunnig = false;
        running = false;
        seconds = maxProgress;
        circleProgressBar.setProgress(0);
    }

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
                        if (seconds == 0) { // 타이머가 끝나면
                            startOrPause.setImageResource(play);
                            startOrPause.setTag(PLAY);
                            onTimerStop(); // 정지시킨다

                            ringtone.play(); // 알림음 재생

                            if(pomodoroMode == true){ // 집중 모드일 경우
                                createDialogBoxForFocus().show(); // 성공/실패 여부
                            } else { // 쉬는 모드일 경우
                                // 자동으로 집중 타이머로 리셋하고 시작
                                setPomodoroTimer("focus", 1500, progressPomodoro);
                                pomodoroMode = true;
                                startOrPause.setImageResource(pause);
                                startOrPause.setTag(PAUSE);
                                onTimerStart();
                            }
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
                pomodoroMode = true;
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

    /* 오늘 날짜 가져오기 */
    private String getTime(){
        now = System.currentTimeMillis();
        date = new Date(now);
        return sdf.format(date);
    }

    // 성공/실패 여부 묻는 대화상자 만들기
    private AlertDialog createDialogBoxForFocus(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("정말 집중하셨나요?");
        builder.setMessage("양심적으로 체크해주세요.\n'네'를 누르면 코인이 적립됩니다.\n'아니오'를 누르면 시간이 기록되지 않고 타이머가 초기화됩니다.");
        builder.setIcon(R.drawable.coin);

        // 성공 버튼 설정
        builder.setPositiveButton("네", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){

                if(timerMode == 1){ // 일반 모드일 경우
                    onTimerReset();
                    seconds = 0;
                } else {
                    setPomodoroTimer("rest", 300, progressRest);
                    pomodoroMode = false;
                    startOrPause.setImageResource(pause);
                    startOrPause.setTag(PAUSE);
                    onTimerStart();
                }
            }
        });
        // 실패 버튼 설정
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
                onTimerReset();
                if (timerMode == 0) {
                    Toast.makeText(getApplicationContext(), "다시 집중하기 도전해봅시다~!", Toast.LENGTH_LONG).show();
                } else {
                    seconds = 0;
                }
            }
        });
        // 창 닫기 버튼 설정
        builder.setNeutralButton("창 닫기", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){

            }
        });

        // 빌더 객체의 create() 메소드를 사용하여 대화상자 객체 생성
        AlertDialog dialog = builder.create();

        return dialog;
    }

    // 타이머 동작을 어떻게 할지 묻는 대화상자 만들기
    private AlertDialog createDialogBoxForSetTimer(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("타이머 동작 설정하기");
        builder.setMessage("'초기화'를 누르면 타이머가 다시 시작됩니다.\n'스킵하기'를 누르면 시간이 기록되지 않고 다음 타이머(집중 모드/쉬는 모드)로 넘어가집니다.");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        // 초기화 버튼 설정
        builder.setPositiveButton("초기화", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                onTimerReset();
            }
        });
        // 아니오 버튼 설정
        builder.setNegativeButton("스킵하기", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                if(pomodoroMode == true){ // 집중 모드였다면, 쉬는 타이머로 바꾸기
                    setPomodoroTimer("rest", 300, progressRest);
                    pomodoroMode = false;
                } else { // 쉬는 모드였다면, 집중 타이머로 바꾸기
                    setPomodoroTimer("focus", 1500, progressPomodoro);
                    pomodoroMode = true;
                }
                onTimerReset();
            }
        });
        // 창 닫기 버튼 설정
        builder.setNeutralButton("창 닫기", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){

            }
        });

        // 빌더 객체의 create() 메소드를 사용하여 대화상자 객체 생성
        AlertDialog dialog = builder.create();

        return dialog;
    }
}