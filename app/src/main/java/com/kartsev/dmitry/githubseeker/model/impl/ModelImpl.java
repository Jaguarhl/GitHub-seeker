package com.kartsev.dmitry.githubseeker.model.impl;

import com.kartsev.dmitry.githubseeker.model.interfaces.ApiInterface;
import com.kartsev.dmitry.githubseeker.model.interfaces.IModel;
import com.kartsev.dmitry.githubseeker.model.dto.GitHubSearchAnswerDTO;

import retrofit2.Call;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ModelImpl implements IModel {

    ApiInterface apiInterface = ApiModule.getApiInterface();

    @Override
    public Call<GitHubSearchAnswerDTO> getRepoList(String query) {
        return apiInterface.getRepositories(query);
    }

//    @Override
//    public Observable<GitHubSearchAnswerDTO> getRepoList(String query) {
//        return apiInterface.getRepositories(query)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
}
