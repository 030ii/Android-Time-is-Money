package com.example.helllo.tim;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class TimerActivity extends AppCompatActivity {

    private int seconds = 0;
    private boolean running;
    private boolean wasRunnig;
    TextView timer;
//    ImageView startBtn;
    CircleProgressBar circleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(savedInstanceState!=null){
            running = savedInstanceState.getBoolean("runningState");
            seconds = savedInstanceState.getInt("secondsState");
            wasRunnig = savedInstanceState.getBoolean("wasRunningState");
        }

        setContentView(R.layout.activity_timer);
        timer = (TextView)findViewById(R.id.timer);
        runTimer();

        circleProgressBar = (CircleProgressBar) findViewById(R.id.custom_progressBar);
        circleProgressBar.setProgressWithAnimation(50);

//        startBtn = (ImageView) findViewById(R.id.start);
//        startBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Your code.
//            }
//        });
    }

    public void onBackBtnClicked (View v){
        Intent intent = new Intent(TimerActivity.this, MainActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }



    public void onTimerStart(View view) {
        running = true;

//        circleProgressBar.setProgressWithAnimation(100);
    }

    public void onTimerStop(View view) {
        wasRunnig = running;
        running = false;
    }

    public void onTimerReset(View view) {
        wasRunnig = false;
        running = false;
        seconds = 0;
    }

    private void runTimer(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time = String.format("%d:%02d:%02d",
                        hours, minutes, secs);
                timer.setText(time);
                if (running) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
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


}
