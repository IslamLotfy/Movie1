package com.example.islam.movie1.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.islam.movie1.Models.ReviewResult;
import com.example.islam.movie1.R;

import java.util.List;

/**
 * Created by islam on 10/04/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    List<ReviewResult> reViewResultList;
    public ReviewAdapter(List<ReviewResult> trailerResults){
        reViewResultList = trailerResults;

    }
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.setView(reViewResultList.get(position));
    }

    @Override
    public int getItemCount() {
        return reViewResultList.size();
    }
}
