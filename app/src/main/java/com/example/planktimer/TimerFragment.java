package com.example.planktimer;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class    TimerFragment<val> extends Fragment {
    private TextView watchView; /* stopwatch time text view*/private Button startButton, timerButton; /* start, timer button*/private int start_stop; /* start indicator*/private long PlankMillitime, StartTime, MillisecondTime, UpdateTime = 0L; /* Start or resume 버튼 누른 시간, 진행시간,*/private int Planktime, Seconds, Minutes, MilliSeconds, remainSeconds, remainMinutes, remainMilliSeconds; /* 시, 분, 밀리초*/private StopwatchFragment.OnTimePickerSetListener onTimePickerSetListener; /* activity랑 통신 위한 interface*/private ConstraintLayout mcontainer; /* layout 조정용*/RecyclerView mRecyclerView; /* history 출력용*/LinearLayoutManager mLayoutManager; /* history 출력용*/RecyclerViewAdapter mAdapter; /* history 출력용*/Handler handler; /* 이벤트 핸들러*/MediaPlayer mp; /* 사운드 재생용*/ArrayList<Item> items = new ArrayList(); /* plankrecord_list*/int mIndex; /* 횟수*/Date date; /* 레코드 저장용 날짜*/SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); // 날짜

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start_stop = 0; //시작전 = 0, 진행중 = 1, 정지 = 2
        Planktime =60; //default 30초
        Minutes = Seconds = MilliSeconds = remainMilliSeconds = remainMinutes = remainSeconds =0;
        PlankMillitime = 0L;
        handler = new Handler();
        mIndex =1;

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

        View view =  inflater.inflate(R.layout.activity_timer, container, false);watchView = view.findViewById(R.id.watchView);startButton = view.findViewById(R.id.startButton);timerButton = view.findViewById(R.id.timerButton);mcontainer = view.findViewById(R.id.linearLayout);mp = MediaPlayer.create(getContext(), R.raw.whistle);mRecyclerView = view.findViewById(R.id.recyclerView);mLayoutManager = new LinearLayoutManager(getContext());mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);mRecyclerView.setLayoutManager(mLayoutManager);         /* LinearLayout으로 설정*/mAdapter = new RecyclerViewAdapter(items);        /* Adapter 생성*/mRecyclerView.setAdapter(mAdapter);DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLayoutManager.getOrientation());dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.recycler_divider));mRecyclerView.addItemDecoration(dividerItemDecoration);
        startButton.setOnClickListener(new View.OnClickListener() { // 스타트 버튼에 이벤트 리스너 붙이기
            @Override
            public void onClick(View view) {
                if (start_stop == 0) // 최초 시작시 Start 클릭
                {
                    StartTime = SystemClock.uptimeMillis(); // 현재시간 반환
                    PlankMillitime = Planktime * 1000; // 밀리초로 변경
                    handler.postDelayed(runnable, 0);
                    startButton.setText("Stop");
                    timerButton.setVisibility(View.GONE);
                    constraint_setting(0); // 가운데로 버튼 이동
                    start_stop = 1; // 진행중으로 상태 변경
                    onTimePickerSetListener.onTimePickerSet(2,View.INVISIBLE); // stopwatch에서 사용중이라 표시
                } else if (start_stop == 1)   // 진행중 Stop 클릭
                {
                    handler.removeCallbacks(runnable);
                    startButton.setText("Start");
                    timerButton.setVisibility(View.VISIBLE);
                    constraint_setting(1); // 왼쪽으로 버튼 이동
                    start_stop = 0; // 정지 상태로 변경
                    items.add(new Item(String.format("%d",mIndex++), sdf.format(new Date(System.currentTimeMillis())), String.format("%02d:%02d:%02d",Minutes, Seconds, MilliSeconds)));
                    watchView.setText(String.format("%02d:%02d:%02d", Planktime / 60, Planktime%60,0)); // 시간 표시
                    onTimePickerSetListener.onTimePickerSet(2,View.VISIBLE); // stopwatch에서 사용중이라 표시
                }
            }
        });
        timerButton.setOnClickListener(new View.OnClickListener() { // 플랭크 타이머 시간 변경
            @Override
            public void onClick(View view) {
                final String [] items = {"10sec", "20sec", "30sec", "40sec", "50sec", "60sec","1min 10sec","1min 20sec"
                ,"1min 30sec","1min 40sec","1min 50sec","2min","2min 10sec","2min 20sec"
                        ,"2min 30sec","2min 40sec","2min 50sec","3min","3min 10sec","3min 20sec"
                        ,"3min 30sec","3min 40sec","3min 50sec","4min","4min 10sec","4min 20sec"
                        ,"4min 30sec","4min 40sec","4min 50sec","5min"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Plank Time");
                builder.setSingleChoiceItems(items, 5, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        Planktime = (which+1)*10;
                        watchView.setText(String.format("%02d:%02d:%02d", Planktime / 60, Planktime%60,0)); // 시간 표시
                        dialog.dismiss(); // 누르면 바로 닫히는 형태
                    }
                });
                builder.create().show();
            }
        });
        return view;
    }

    public Runnable runnable = new Runnable() { // 시간 업데이트 함수

        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime; // 진행 시간
            Minutes = (int) (MillisecondTime / 1000 / 60); // 분
            Seconds = (int) (MillisecondTime / 1000 % 60); // 초
            MilliSeconds = (int) (MillisecondTime % 100); // 밀리초

            UpdateTime = PlankMillitime - MillisecondTime; // Plank 남은시간
            remainMinutes = (int) (UpdateTime / 1000 / 60); // 분
            remainSeconds = (int) (UpdateTime / 1000 % 60); // 초
            remainMilliSeconds = (int) (UpdateTime % 100); // 밀리초

            watchView.setText(String.format("%02d:%02d:%02d", remainMinutes, remainSeconds,remainMilliSeconds)); // 시간 표시
            if(UpdateTime<=0) // 시간 종료시
            {
                handler.removeCallbacks(runnable);
                startButton.setText("Start");
                start_stop = 0; // 정지 상태로 변경
                timerButton.setVisibility(View.VISIBLE);
                constraint_setting(1); // 왼쪽으로 버튼 이동
                watchView.setText(String.format("%02d:%02d:%02d", Planktime / 60, Planktime%60,0)); // 시간 표시
                onTimePickerSetListener.onTimePickerSet(2,View.VISIBLE); // stopwatch에서 사용중이라 표시
                items.add(new Item(String.format("%d",mIndex++), sdf.format(new Date(System.currentTimeMillis())), String.format("%02d:%02d:%02d",Planktime / 60, Planktime%60,0)));
                mp.start();                // 알람음 재생
                return;
            }
                handler.postDelayed(this, 0);
        }
    };

    public interface OnTimePickerSetListener { // 동작중인지 구분하기 위해서
        void onTimePickerSet(int fragment_num, int visibility); // 보낸 Fragment랑 상태 구분하기 위해 사용
    };
    public void constraint_setting(int i)
    {
        ConstraintSet set = new ConstraintSet(); // constraintset rearrange to center
        set.clone(mcontainer);
        if (i==0) {  // 연결 끊고 가운데로 정렬
            set.connect(R.id.startButton, ConstraintSet.END, R.id.linearLayout, ConstraintSet.END,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30,getResources().getDisplayMetrics()));
        } else if(i==1){ // 연결 다시 해서 왼쪽으로 이동
            set.removeFromHorizontalChain(R.id.startButton);
            set.connect(R.id.startButton, ConstraintSet.START, R.id.guideline_left2, ConstraintSet.END,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,getResources().getDisplayMetrics()));
        }
        set.applyTo(mcontainer);
    }
}
