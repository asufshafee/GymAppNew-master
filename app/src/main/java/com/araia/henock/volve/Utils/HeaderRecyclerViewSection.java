package com.araia.henock.volve.Utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.araia.henock.volve.R;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class HeaderRecyclerViewSection extends StatelessSection{
    private static final String TAG = HeaderRecyclerViewSection.class.getSimpleName();
    private String title;
    String Data;
    private List<ItemObject> list;
    public HeaderRecyclerViewSection(String title, List<ItemObject> list,String Date) {
        super(R.layout.header_layout, R.layout.item_layout);
        this.title = title;
        this.Data=Date;
        this.list = list;
    }
    @Override
    public int getContentItemsTotal() {
        return list.size();
    }
    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ITEMVIEWHOLDER(view);
    }
    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ITEMVIEWHOLDER iHolder = (ITEMVIEWHOLDER) holder;
        iHolder.trainerName.setText(list.get(position).getTrainee_name());
        iHolder.trainingTime.setText(list.get(position).getTime());
    }
    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HEADERVIEWHOLDER(view);
    }
    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HEADERVIEWHOLDER hHolder = (HEADERVIEWHOLDER) holder;
        hHolder.headerTitle.setText(title);
        hHolder.headerDesciption.setText(Data);
    }
}
