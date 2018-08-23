package com.kartsev.dmitry.githubseeker.view.interfaces;

import com.kartsev.dmitry.githubseeker.presenter.vo.RepositoryVO;

import java.util.List;

public interface IView {
    void showList(List<RepositoryVO> RepoList);
    void showError(String error);
    void showRepoDetails(RepositoryVO repo);
    void showEmptyList();
    void closeDetailsScreen();
}
