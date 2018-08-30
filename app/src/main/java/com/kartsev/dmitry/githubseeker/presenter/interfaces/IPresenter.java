package com.kartsev.dmitry.githubseeker.presenter.interfaces;

import com.kartsev.dmitry.githubseeker.presenter.vo.RepositoryVO;
import com.kartsev.dmitry.githubseeker.view.interfaces.IView;

public interface IPresenter {
    void attachView(IView view);
    void detachView();

    void onSearchClick(String query);
    void onLoadMore(String query, int page);
    void onRepoChosen(RepositoryVO repo);
    void onCloseDetailsScreen();
    void onStop();
}
