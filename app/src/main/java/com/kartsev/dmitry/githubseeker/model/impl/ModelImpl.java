package com.kartsev.dmitry.githubseeker.model.impl;

import com.kartsev.dmitry.githubseeker.model.interfaces.ApiInterface;
import com.kartsev.dmitry.githubseeker.model.interfaces.IModel;
import com.kartsev.dmitry.githubseeker.model.dto.GitHubSearchAnswerDTO;

import retrofit2.Call;

public class ModelImpl implements IModel {

    ApiInterface apiInterface = ApiModule.getApiInterface();

    @Override
    public Call<GitHubSearchAnswerDTO> getRepoList(String query, int page) {
        return apiInterface.getRepositories(query, page);
    }
}
