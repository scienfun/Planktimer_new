package com.example.planktimer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private ArrayList<Item> mItems;
    Context mContext;
    public RecyclerViewAdapter(ArrayList itemList) {
        mItems = itemList;
    }
    // 필수 오버라이드 : View 생성, ViewHolder 호출
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        mContext = parent.getContext();
        RecyclerViewHolder holder = new RecyclerViewHolder(v);
        return holder;
    }
    // 필수 오버라이드 : 재활용되는 View 가 호출, Adapter 가 해당 position 에 해당하는 데이터를 결합
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        // 해당 position 에 해당하는 데이터 결합
        holder.mIndex.setText(mItems.get(position).index);
        holder.mTimestamp.setText(mItems.get(position).timestamp);
        holder.mPlankrecord.setText(mItems.get(position).plankrecord);

        // 이벤트처리 : 생성된 List 중 선택된 목록번호를 Toast로 출력
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(mContext, String.format("%d 선택", position + 1), Toast.LENGTH_SHORT).show();
//            }
//        });
    }
    // 필수 오버라이드 : 데이터 갯수 반환
    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
