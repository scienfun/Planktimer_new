package com.example.planktimer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity
                            implements StopwatchFragment.OnTimePickerSetListener{

    FragmentTransaction fragmentTran;

    StopwatchFragment stopwatchFragment;
    TimerFragment timerFragment;
    SettingFragment settingFragment;


   private Handler mHandler;
   private BottomNavigationView navView; // navigation view
   private FrameLayout mFramelayout;
   private int mfragment_num, mstatus; // fragment에서 Activity로 전달하는 변수
   Intent intent;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_stopwatch:
                    fragmentTran = getSupportFragmentManager().beginTransaction();
                    fragmentTran.replace(R.id.contentFrame, stopwatchFragment);
                    fragmentTran.addToBackStack(null);
                    fragmentTran.commit();
                    return true;
                case R.id.navigation_timer:
                    fragmentTran = getSupportFragmentManager().beginTransaction();
                    fragmentTran.replace(R.id.contentFrame, timerFragment);
                    fragmentTran.addToBackStack(null);
                    fragmentTran.commit();
                    return true;
                case R.id.navigation_setting:
                    fragmentTran = getSupportFragmentManager().beginTransaction();
                    fragmentTran.replace(R.id.contentFrame, settingFragment);
                    fragmentTran.addToBackStack(null);
                    fragmentTran.commit();
                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mfragment_num = 1;// 1이 Stopwatch, 2가 Timer, 3이 Setting
        mstatus = 0; // 0은 정지, 1이 실행중이라 navView 숨김 필요
        stopwatchFragment = new StopwatchFragment();
        timerFragment = new TimerFragment();
        settingFragment = new SettingFragment();

        fragmentTran = getSupportFragmentManager().beginTransaction();
        fragmentTran.replace(R.id.contentFrame, stopwatchFragment);
//        fragmentTran.addToBackStack(null);
        fragmentTran.commit();
        };

    @Override
    public void onTimePickerSet(int fragment_num, int status) {
        mfragment_num = fragment_num;
        navView.setVisibility(status);
    };
}


