package com.kartsev.dmitry.githubseeker.model.interfaces;

import com.kartsev.dmitry.githubseeker.model.dto.GitHubSearchAnswerDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiInterface {
    @GET("search/repositories")
    Call<GitHubSearchAnswerDTO> getRepositories(@Query("q") String query);
//    Observable<GitHubSearchAnswerDTO> getRepositories(@Query("q") String query);
}
