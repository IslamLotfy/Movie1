package com.example.islam.movie1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.islam.movie1.Models.MovieModel;
import com.example.islam.movie1.R;
import com.squareup.picasso.Picasso;

/**
 * Created by islam on 08/04/17.
 */

public class MovieViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private ImageView poster;
    private View view;
    public MovieViewHolder(View itemView ,Context context) {
        super(itemView);
        view=itemView;
        this.context=context;
    }

    public void setView(MovieModel model,MovieSelectedListener listener){
        poster= (ImageView) view.findViewById(R.id.poster);
        Picasso.with(context).load("http://image.tmdb.org/t/p/w780/"+model.getPosterPath()).into(poster);
        poster.setOnClickListener(v -> {
            listener.onMovieSelected(model);
        });
    }
}
