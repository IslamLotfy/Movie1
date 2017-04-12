package com.example.islam.movie1.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.islam.movie1.Models.TrailerResult;
import com.example.islam.movie1.R;

/**
 * Created by islam on 12/04/17.
 */

class TrailerViewHolder extends RecyclerView.ViewHolder {

    private Button trailer;
    private View view;
    public TrailerViewHolder(View itemView) {
        super(itemView);
        view=itemView;
    }

    public void setView(TrailerResult model,TrailerListener listener){
        trailer=(Button)view.findViewById(R.id.btnTrailers);
        trailer.setText(model.getName());
        trailer.setOnClickListener(v -> {
            listener.onTrailerChoosen(model);
        });
    }
}
