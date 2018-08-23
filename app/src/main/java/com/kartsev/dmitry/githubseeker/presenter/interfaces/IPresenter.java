package com.kartsev.dmitry.githubseeker.presenter.interfaces;

import com.kartsev.dmitry.githubseeker.presenter.vo.RepositoryVO;

public interface IPresenter {
    void onSearchClick(String toString);
    void onRepoChosen(RepositoryVO repo);
    void onCloseDetailsScreen();
    void onStop();
}
