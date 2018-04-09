package com.araia.henock.volve.Utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.araia.henock.volve.R;

public class ITEMVIEWHOLDER extends RecyclerView.ViewHolder{
    public TextView trainerName;
    public TextView trainingTime;
    public ITEMVIEWHOLDER(View itemView) {
        super(itemView);
        trainerName = (TextView)itemView.findViewById(R.id.trainer_name);
        trainingTime = (TextView)itemView.findViewById(R.id.training_time);
    }
}
