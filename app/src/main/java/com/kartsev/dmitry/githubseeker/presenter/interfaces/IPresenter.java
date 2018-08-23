package com.kartsev.dmitry.githubseeker.presenter.interfaces;

import com.kartsev.dmitry.githubseeker.presenter.vo.RepositoryVO;

public interface IPresenter {
    void onSearchClick(String query);
    void onLoadMore(String query, int page);
    void onRepoChosen(RepositoryVO repo);
    void onCloseDetailsScreen();
    void onStop();
}
