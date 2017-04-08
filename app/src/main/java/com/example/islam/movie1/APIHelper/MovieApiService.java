package com.example.islam.movie1.APIHelper;

import com.example.islam.movie1.Models.Constants;
import com.example.islam.movie1.Models.MovieModel;
import com.example.islam.movie1.Models.MovieResult;

import java.util.List;


import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by islam on 05/04/17.
 */


public interface MovieApiService {

    @GET("discover/movie")
    Observable<MovieResult> getTopRatedMovies(@Query("sort_by") String sort);
}