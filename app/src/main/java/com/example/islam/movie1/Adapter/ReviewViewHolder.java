package com.example.islam.movie1.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.islam.movie1.Models.ReviewResult;
import com.example.islam.movie1.R;

/**
 * Created by islam on 10/04/17.
 */

public class ReviewViewHolder extends RecyclerView.ViewHolder {

    private TextView author;
    private TextView content;
    private View view;
    public ReviewViewHolder(View itemView) {
        super(itemView);
        view=itemView;
    }



    public void setView(ReviewResult model){
        author=(TextView)view.findViewById(R.id.author);
        content=(TextView)view.findViewById(R.id.content);
        author.setText(model.getAuthor());
        content.setText(model.getContent());
    }
}
