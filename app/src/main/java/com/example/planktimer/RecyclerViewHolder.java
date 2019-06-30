package com.example.planktimer;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

class RecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView mIndex;
    public TextView mTimestamp;
    public TextView mPlankrecord;
    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mIndex = (TextView) itemView.findViewById(R.id.index);
        mTimestamp= (TextView) itemView.findViewById(R.id.timestamp);
        mPlankrecord = (TextView) itemView.findViewById(R.id.plankrecord);
    }
}
