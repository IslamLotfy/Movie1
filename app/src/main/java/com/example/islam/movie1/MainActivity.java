package com.example.islam.movie1;

import android.database.Observable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.islam.movie1.APIHelper.ApiModule;
import com.example.islam.movie1.APIHelper.MovieApiService;
import com.example.islam.movie1.Adapter.MovieListAdapter;
import com.example.islam.movie1.Models.Constants;
import com.example.islam.movie1.Models.MovieModel;
import com.example.islam.movie1.Models.MovieResult;

import java.util.LinkedList;
import java.util.List;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private List<MovieModel> movies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=(RecyclerView)findViewById(R.id.movielist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        ApiModule apiModule=new ApiModule();
        MovieApiService movieApiService=apiModule.provideApiService();
        rx.Observable<MovieResult> topRatedMovies=movieApiService.getTopRatedMovies(Constants.MOST_POPULAR);
        movies=new LinkedList<>();
        topRatedMovies.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(MovieResult :: getResults)
                .subscribe(movieResult -> {
                    Log.e("movies",movieResult.get(0).getTitle());
                    datafetched(movieResult);
                },throwable -> {
                    Log.e("error",throwable.toString());
                });


    }

    private void datafetched(List<MovieModel> movieResult) {
        movies=movieResult;
        MovieListAdapter adapter=new MovieListAdapter(movies,movieModel -> {

        });
        recyclerView.setAdapter(adapter);
    }
}
