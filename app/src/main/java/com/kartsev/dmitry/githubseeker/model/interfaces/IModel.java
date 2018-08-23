package com.kartsev.dmitry.githubseeker.model.interfaces;

import com.kartsev.dmitry.githubseeker.model.dto.GitHubSearchAnswerDTO;

import java.util.List;

import retrofit2.Call;
import rx.Observable;

public interface IModel {
    Call<GitHubSearchAnswerDTO> getRepoList(String query, int page);
//    Observable<GitHubSearchAnswerDTO> getRepoList(String query);
}
