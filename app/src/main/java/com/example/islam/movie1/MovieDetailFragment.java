package com.example.islam.movie1;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.islam.movie1.Models.MovieModel;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {



    private ImageView movieImage;
    private ImageView posterImage;
    private TextView movieTitle;
    private TextView movieDate;
    private TextView movieVoteAverage;
    private TextView movieDescription;
    private MovieModel movieModel;
    View view;
    public MovieDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Intent intent=getActivity().getIntent();
        if(intent.hasExtra("model"))
            movieModel= (MovieModel) intent.getSerializableExtra("model");
        initView();
        bindDataToViews();


        return view;
    }

    private void initView() {
        movieImage= (ImageView) view.findViewById(R.id.movie_cover);
        posterImage=(ImageView)view.findViewById(R.id.movie_image);
        movieTitle=(TextView)view.findViewById(R.id.movie_title);
        movieDate=(TextView)view.findViewById(R.id.movie_date);
        movieVoteAverage=(TextView)view.findViewById(R.id.movie_vote_average);
        movieDescription=(TextView)view.findViewById(R.id.movie_description);
    }

    private void bindDataToViews() {
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w780/"+movieModel.getBackdropPath()).into(movieImage);
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w780/"+movieModel.getPosterPath()).into(posterImage);
        movieTitle.setText(movieModel.getOriginalTitle());
        movieDate.setText(movieModel.getReleaseDate());
        movieVoteAverage.setText(movieModel.getVoteAverage()+" / 10");
        movieDescription.setText(movieModel.getOverview());
    }
}
