package com.kartsev.dmitry.githubseeker.view.interfaces;

import com.kartsev.dmitry.githubseeker.presenter.vo.RepositoryVO;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IView {
    void showList(List<RepositoryVO> RepoList, int totalCount);
    void addToList(List<RepositoryVO> repoList);
    void notCorrectServerAnswer();
    void showError(String error);
    void showRepoDetails(RepositoryVO repo);
    void showEmptyList();
    void closeDetailsScreen();
}
