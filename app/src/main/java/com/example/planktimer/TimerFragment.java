package com.example.planktimer;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class TimerFragment<val> extends Fragment {
    private TextView watchView, recordView; // stopwatch time, record time text view
    private Button startButton, timerButton; // start, timer button
    private int start_stop; // start indicator
    private long PlankMillitime, StartTime, MillisecondTime, TimeBuff, UpdateTime, HandiUpdateTime = 0L; // Start or resume 버튼 누른 시간, 진행시간,
    private int Planktime, Seconds, Minutes, MilliSeconds; // 시, 분, 밀리초
    private StopwatchFragment.OnTimePickerSetListener onTimePickerSetListener; // activity랑 통신 위한 interface
    Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start_stop = 0; //시작전 = 0, 진행중 = 1, 정지 = 2
        Planktime =30; //default 30초
        Minutes = Seconds = MilliSeconds = 0;
        PlankMillitime = 0L;
        handler = new Handler();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof StopwatchFragment.OnTimePickerSetListener) {
            onTimePickerSetListener = (StopwatchFragment.OnTimePickerSetListener) context;
        } else{
            throw new RuntimeException(context.toString()
                    +"must implement OnTimePickerSetListener");
        }
    };

    @Override
    public void onDetach(){
        super.onDetach();
        onTimePickerSetListener = null;
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.activity_timer, container, false);
        watchView = view.findViewById(R.id.watchView);
        recordView = view.findViewById(R.id.recordView);
        startButton = view.findViewById(R.id.startButton);
        timerButton = view.findViewById(R.id.timerButton);

        startButton.setOnClickListener(new View.OnClickListener() { // 스타트 버튼에 이벤트 리스너 붙이기
            @Override
            public void onClick(View view) {
                if (start_stop == 0) // 최초 시작시 Start 클릭
                {
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    startButton.setText("Stop");
                    ConstraintLayout.LayoutParams mlayoutparams = (ConstraintLayout.LayoutParams)startButton.getLayoutParams();
                    timerButton.setVisibility(View.GONE);
                    mlayoutparams = (ConstraintLayout.LayoutParams)startButton.getLayoutParams();


                    start_stop = 1; // 진행중으로 상태 변경
                    PlankMillitime = Planktime * 1000;
                    onTimePickerSetListener.onTimePickerSet(2,View.INVISIBLE); // stopwatch에서 사용중이라 표시
                } else if (start_stop == 1)   // 진행중 Stop 클릭
                {
                    TimeBuff += MillisecondTime;
                    handler.removeCallbacks(runnable);
                    startButton.setText("Start");
                    ConstraintLayout.LayoutParams mlayoutparams = (ConstraintLayout.LayoutParams)startButton.getLayoutParams();
                    timerButton.setVisibility(View.GONE);
                    mlayoutparams = (ConstraintLayout.LayoutParams)startButton.getLayoutParams();
                    timerButton.setVisibility(View.VISIBLE);
                    start_stop = 0; // 정지 상태로 변경

                    watchView.setText(String.format("%02d", Planktime / 60) + ":"
                            + String.format("%02d", Planktime%60) + ":"
                            + String.format("%02d", 0)); // 시간 표시

                    recordView.setVisibility(View.VISIBLE);
                    recordView.setText(String.format("%02d", Minutes) + ":"
                            + String.format("%02d", Seconds) + ":"
                            + String.format("%02d", MilliSeconds)); // 시간 표시
                    onTimePickerSetListener.onTimePickerSet(2,View.VISIBLE); // stopwatch에서 사용중이라 표시
                }
            }
        });
        return view;
    }

    public Runnable runnable = new Runnable() { // 시간 업데이트 함수

        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime; // 진행 시간
            UpdateTime = PlankMillitime - MillisecondTime; // Plank 남은시간

            Minutes = (int) (UpdateTime / 1000 / 60); // 분
            Seconds = (int) (UpdateTime / 1000 % 60); // 초
            MilliSeconds = (int) (UpdateTime % 100); // 밀리초

            watchView.setText(String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%02d", MilliSeconds)); // 시간 표시

            if(UpdateTime<=0) // 시간 종료시
            {
                handler.removeCallbacks(runnable);
                startButton.setText("Start");
                start_stop = 0; // 정지 상태로 변경

                watchView.setText(String.format("%02d", Planktime / 60) + ":"
                        + String.format("%02d", Planktime%60) + ":"
                        + String.format("%02d", 0)); // 시간 표시

                recordView.setVisibility(View.VISIBLE);
                recordView.setText(String.format("%02d", Planktime / 60) + ":"
                        + String.format("%02d", Planktime%60) + ":"
                        + String.format("%02d", 0)); // 시간 표시
                onTimePickerSetListener.onTimePickerSet(2,View.VISIBLE); // stopwatch에서 사용중이라 표시

                // 알람음 재생

                return;
            }
                handler.postDelayed(this, 0);
        }
    };

    public interface OnTimePickerSetListener { // 동작중인지 구분하기 위해서
        void onTimePickerSet(int fragment_num, int visibility); // 보낸 Fragment랑 상태 구분하기 위해 사용
    };
}
