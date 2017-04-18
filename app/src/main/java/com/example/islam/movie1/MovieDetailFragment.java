package com.example.islam.movie1;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.islam.movie1.APIHelper.ApiModule;
import com.example.islam.movie1.APIHelper.MovieApiService;
import com.example.islam.movie1.Adapter.ReviewAdapter;
import com.example.islam.movie1.Adapter.TrailerAdapter;
import com.example.islam.movie1.DataBaseHelper.MovieDbHelper;
import com.example.islam.movie1.Models.MovieModel;
import com.example.islam.movie1.Models.ReviewResult;
import com.example.islam.movie1.Models.Reviews;
import com.example.islam.movie1.Models.TrailerResult;
import com.example.islam.movie1.Models.Trailers;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {


    FloatingActionButton favouriteBtn;
    private MovieApiService movieApiService;
    private ImageView movieImage;
    private ImageView posterImage;
    private TextView movieTitle;
    private TextView movieDate;
    private TextView movieVoteAverage;
    private TextView movieDescription;
    private MovieModel movieModel;
    private Button reviews;
    private RecyclerView recyclerView;
    private RecyclerView trailrRecyclerView;
    private MovieDbHelper dbHelper;

    View view;
    public MovieDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Intent intent=getActivity().getIntent();
        dbHelper= new MovieDbHelper(getActivity().getApplicationContext());
        if(intent.hasExtra("model"))
            movieModel= (MovieModel) intent.getSerializableExtra("model");
        movieModel.setFavourite(dbHelper.isFavourite(getActivity(),movieModel.getId()));
        ApiModule apiModule=new ApiModule();
        movieApiService=apiModule.provideApiService();
        initView();
        bindDataToViews();
        getTrailers();


        reviews.setOnClickListener(v -> {
            getReviews();
        });

        favouriteBtn.setOnClickListener(view -> {
            String message="";
            if (movieModel.isFavourite()) {
                dbHelper.deleteFavourite(getActivity(), movieModel.getId());
                movieModel.setFavourite(false);
                message+="Movie removed from favourites ";
            }
            else {
                dbHelper.insertFavourite(getActivity(), movieModel);
                movieModel.setFavourite(true);
                message+="Movie added to favourites ";
            }
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
            view.setSelected(!view.isSelected());

        });

        return view;
    }
    public void watchYoutubeVideo(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    private void getReviews() {
        rx.Observable<Reviews> trailersObservable=movieApiService.getMovieReviews(movieModel.getId());
        trailersObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(Reviews ::getReViewResults)
                .subscribe(results -> {
                    recyclerView.setAdapter(new ReviewAdapter(results));
                    if(results.size()>0){
                        Toast.makeText(getActivity(),"please scroll to see the reviews",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(),"thist movie doesn't have any reviews",Toast.LENGTH_SHORT).show();
                    }

                    },throwable -> {
                    Log.e("error",throwable.toString());
                });
    }


    private void getTrailers() {
        rx.Observable<Trailers> trailersObservable=movieApiService.getMovieTrailers(movieModel.getId());
        trailersObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(Trailers ::getResults)
                .subscribe(results -> {
                    trailrRecyclerView.setAdapter(new TrailerAdapter(results,model -> {
                        watchYoutubeVideo(model.getKey());
                    }));
                    if(results.size()<=0){
                        Toast.makeText(getActivity(),"thist movie doesn't have any trailers",Toast.LENGTH_SHORT).show();
                    }
                },throwable -> {
                    Log.e("error",throwable.toString());
                });
    }

    private void initView() {
        movieImage= (ImageView) view.findViewById(R.id.movie_cover);
        posterImage=(ImageView)view.findViewById(R.id.movie_image);
        movieTitle=(TextView)view.findViewById(R.id.movie_title);
        movieDate=(TextView)view.findViewById(R.id.movie_date);
        movieVoteAverage=(TextView)view.findViewById(R.id.movie_vote_average);
        movieDescription=(TextView)view.findViewById(R.id.movie_description);
        reviews=(Button)view.findViewById(R.id.btnReviews);
        recyclerView=(RecyclerView)view.findViewById(R.id.reviews);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        trailrRecyclerView=(RecyclerView)view.findViewById(R.id.trailers);
        trailrRecyclerView.setHasFixedSize(true);
        trailrRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,true));
        favouriteBtn= (FloatingActionButton) view.findViewById(R.id.favorite_fab);
        favouriteBtn.setSelected(movieModel.isFavourite());
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
