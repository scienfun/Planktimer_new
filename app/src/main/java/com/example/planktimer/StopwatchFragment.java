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

import androidx.fragment.app.Fragment;

public class StopwatchFragment extends Fragment {
    private TextView watchView; // stopwatch time text view
    private Button startButton, resetButton; // start, reset button
    private int start_stop; // start indicator
    private long StartTime, MillisecondTime, TimeBuff, UpdateTime, HandiUpdateTime = 0L; // Start or resume 버튼 누른 시간, 진행시간,
    private int Seconds, Minutes, MilliSeconds; // 시, 분, 밀리초
    private OnTimePickerSetListener onTimePickerSetListener; // activity랑 통신 위한 interface
    Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start_stop = 0; //시작전 = 0, 진행중 = 1, 정지 = 2
        Minutes = Seconds = MilliSeconds = 0;
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

        View view =  inflater.inflate(R.layout.activity_stopwatch, container, false);
        watchView = view.findViewById(R.id.watchView);
        startButton = view.findViewById(R.id.startButton);
        resetButton = view.findViewById(R.id.resetButton);
        resetButton.setEnabled(false);

        startButton.setOnClickListener(new View.OnClickListener() { // 스타트 버튼에 이벤트 리스너 붙이기
            @Override
            public void onClick(View view) {
                if (start_stop == 0) // 최초 시작시 Start 클릭
                {
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    resetButton.setEnabled(false);
                    startButton.setText("Pause");
                    start_stop = 1; // 진행중으로 상태 변경
                    onTimePickerSetListener.onTimePickerSet(1,View.INVISIBLE); // stopwatch에서 사용중이라 표시
                } else if (start_stop == 1)   // 진행중 Pause 클릭
                {
                    TimeBuff += MillisecondTime;
                    handler.removeCallbacks(runnable);
                    resetButton.setEnabled(true);
                    startButton.setText("Resume");
                    start_stop = 2; // 정지 상태로 변경
                    onTimePickerSetListener.onTimePickerSet(1,View.INVISIBLE); // stopwatch에서 사용중이라 표시
                } else  if (start_stop == 2) // Pause 중 Resume 클릭
                {
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    resetButton.setEnabled(false);
                    startButton.setText("Pause");
                    start_stop = 1; // 진행중으로 상태 변경
                    onTimePickerSetListener.onTimePickerSet(1,View.INVISIBLE); // stopwatch에서 사용중이라 표시
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() { // 리셋 버튼에 이벤트 리스너 붙이기
            @Override
            public void onClick(View view) {

                MillisecondTime = 0L;
                StartTime = 0L;
                TimeBuff = 0L;
                UpdateTime = 0L;
                Seconds = 0;
                Minutes = 0;
                MilliSeconds = 0;
                watchView.setText("00:00:00");
                startButton.setText("Start");
                onTimePickerSetListener.onTimePickerSet(1,View.VISIBLE); // stopwatch에서 정지 상태로 표시
            }
        });
        return view;
    }

    public Runnable runnable = new Runnable() { // 시간 업데이트 함수

        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime; // 진행 시간
            UpdateTime = TimeBuff + MillisecondTime; // Handicap 모드용

            Minutes = (int) (UpdateTime / 1000 / 60); // 분
            Seconds = (int) (UpdateTime / 1000 % 60); // 초
            MilliSeconds = (int) (UpdateTime % 100); // 밀리초

            watchView.setText(String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%02d", MilliSeconds)); // 시간 표시

            handler.postDelayed(this, 0);
        }
    };

    public interface OnTimePickerSetListener { // 동작중인지 구분하기 위해서
        void onTimePickerSet(int fragment_num, int visibility); // 보낸 Fragment랑 상태 구분하기 위해 사용
    };
}

