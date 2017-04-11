package com.example.islam.movie1.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.islam.movie1.Models.TrailerResult;
import com.example.islam.movie1.R;

import java.util.List;

/**
 * Created by islam on 12/04/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerViewHolder> {


    private List<TrailerResult> list;
    private TrailerListener listener;
    public TrailerAdapter(List<TrailerResult> list , TrailerListener listener){
        this.list=list;
        this.listener=listener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.setView(list.get(position),model -> {
            listener.onTrailerChoosen(model);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
