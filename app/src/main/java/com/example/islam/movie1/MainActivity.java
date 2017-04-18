package com.example.islam.movie1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.example.islam.movie1.APIHelper.ApiModule;
import com.example.islam.movie1.APIHelper.MovieApiService;
import com.example.islam.movie1.Adapter.MovieListAdapter;
import com.example.islam.movie1.DataBaseHelper.MovieDbHelper;
import com.example.islam.movie1.Models.MovieModel;
import com.example.islam.movie1.Models.MovieResult;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String MOVIE_LIST = "movie_list";
    RecyclerView recyclerView;
    private List<MovieModel> movies;
    private MovieModel movie;
    private  MovieApiService movieApiService;
    private String sortType;
    private AppPreference appPreference;
    private static final java.lang.String SORT_TYPE = "sort_type";
    private MovieDbHelper dbHelper;
    private MovieListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MovieDbHelper(getApplicationContext());
        recyclerView=(RecyclerView)findViewById(R.id.movielist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        ApiModule apiModule=new ApiModule();
        movieApiService=apiModule.provideApiService();
        appPreference = AppPreference.getInstance(getApplicationContext());
        if(savedInstanceState!=null) {
            sortType = savedInstanceState.getString(SORT_TYPE);
            ArrayList<MovieModel> movies = savedInstanceState.getParcelableArrayList(MOVIE_LIST);
            datafetched(movies);
        }else{
            sortType=appPreference.getSortType();
            retrieveMovies();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String prefSortType = appPreference.getSortType();
        if (prefSortType.equals(getString(R.string.favorites))) {
            loadFavourites();
            this.sortType = prefSortType;
        }
        else if (!sortType.equals(appPreference.getSortType())) {
            sortType=prefSortType;
            retrieveMovies();
        }
        appPreference.setSortType(sortType);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String prefSortType = appPreference.getSortType();
        if (prefSortType.equals(getString(R.string.favorites))) {
            loadFavourites();
            this.sortType = prefSortType;
        }
        else if (!sortType.equals(appPreference.getSortType())) {
            sortType=prefSortType;
            retrieveMovies();
        }
        appPreference.setSortType(sortType);
    }

    private void loadFavourites() {
        List<MovieModel> favouriteMovies = dbHelper.getFavouriteMovies(this);
        datafetched(favouriteMovies);
    }

    private void retrieveMovies() {
        rx.Observable<MovieResult> topRatedMovies=movieApiService.getTopRatedMovies(sortType);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void datafetched(List<MovieModel> movieResult) {
        movies=movieResult;
        adapter=new MovieListAdapter(movies,movieModel -> {
            movie=movieModel;
            Intent intent=new Intent(this,MovieDetail.class);
            intent.putExtra("model", (Serializable) movieModel);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORT_TYPE, sortType);
        outState.putParcelableArrayList(MOVIE_LIST,new ArrayList<>(adapter.getItems()));
    }
}
