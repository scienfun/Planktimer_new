package com.example.planktimer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment {
    TextView email_text;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.activity_setting, container, false);
        email_text = view.findViewById(R.id.email_text);
        email_text.setOnClickListener(new View.OnClickListener() { // 스타트 버튼에 이벤트 리스너 붙이기
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("mailto:scienfun@hotmail.com");
                Intent email = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(email);
            }
        }
        );
        return view;


    }

}
