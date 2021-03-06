package com.example.islam.movie1.APIHelper;

import com.example.islam.movie1.Models.Constants;
import com.example.islam.movie1.Models.MovieModel;
import com.example.islam.movie1.Models.MovieResult;
import com.example.islam.movie1.Models.Reviews;
import com.example.islam.movie1.Models.Trailers;

import java.util.List;


import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by islam on 05/04/17.
 */


public interface MovieApiService {

    @GET("movie/{sort}?")
    Observable<MovieResult> getTopRatedMovies(@Path("sort") String sort);

    @GET("movie/{id}/videos?")
    Observable<Trailers> getMovieTrailers(@Path("id") int id);

    @GET("movie/{id}/reviews?")
    Observable<Reviews> getMovieReviews(@Path("id") int id);



}