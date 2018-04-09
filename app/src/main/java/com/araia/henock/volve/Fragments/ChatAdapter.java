package com.araia.henock.volve.Fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.araia.henock.volve.R;
import com.araia.henock.volve.Utils.MyApplication;

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private static final int CHAT_END = 1;
    private static final int CHAT_START = 2;

    MyApplication myApplication;

    private List<com.araia.henock.volve.Objects.Chat> mDataSet;
    private String mId;

    ChatAdapter(List<com.araia.henock.volve.Objects.Chat> dataSet, String id, Context context) {
        mDataSet = dataSet;
        mId = id;
        myApplication = (MyApplication) context.getApplicationContext();
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == CHAT_END) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_end, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_start, parent, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataSet.get(position).getEmail().equals(myApplication.getLoginSession().getToken().getEmail())) {
            return CHAT_END;
        }
        return CHAT_START;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        com.araia.henock.volve.Objects.Chat chat = mDataSet.get(position);
        holder.Message.setText(chat.getMessage());
        holder.Time.setText(getDate(Long.parseLong(mDataSet.get(position).getTime())));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**
     * Inner Class for a recycler view
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView Message, Time;

        ViewHolder(View v) {
            super(v);
            Message = (TextView) itemView.findViewById(R.id.Message);
            Time = (TextView) itemView.findViewById(R.id.Time);
        }
    }

    private String getDate(long time) {
        Date date = new Date(time); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); // the format of your dat

        return sdf.format(date);

    }

}
