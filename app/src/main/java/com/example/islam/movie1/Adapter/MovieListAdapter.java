package com.example.islam.movie1.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.islam.movie1.Models.MovieModel;
import com.example.islam.movie1.R;

import java.util.List;

/**
 * Created by islam on 08/04/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    MovieSelectedListener listener;
    private List<MovieModel> movieModels;
    public MovieListAdapter(List<MovieModel> movies,MovieSelectedListener listener){
        movieModels=movies;
        this.listener=listener;
    }
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_view_item, parent, false);
        return new MovieViewHolder(view,parent.getContext());
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.setView(movieModels.get(position),movieModel -> {
            listener.onMovieSelected(movieModel);
        });
    }

    @Override
    public int getItemCount() {
        return movieModels.size();
    }
}
