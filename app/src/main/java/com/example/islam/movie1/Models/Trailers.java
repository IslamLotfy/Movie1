package com.example.islam.movie1.Models;

/**
 * Created by islam on 10/04/17.
 */
 import com.google.gson.annotations.Expose;
 import com.google.gson.annotations.SerializedName;
 import java.util.List;


public class Trailers {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}