package com.kartsev.dmitry.githubseeker.view.listeners;

import com.kartsev.dmitry.githubseeker.presenter.vo.RepositoryVO;

public interface IItemClickListener {
    void onItemControlClicked(int position, RepositoryVO repository, int conrol);
}

