package com.kartsev.dmitry.githubseeker.view.ui.adapters.listeners;

import com.kartsev.dmitry.githubseeker.presenter.vo.RepositoryVO;

public interface IItemClickListener {
    void onItemControlClicked(int position, RepositoryVO repository, int conrol);
}

