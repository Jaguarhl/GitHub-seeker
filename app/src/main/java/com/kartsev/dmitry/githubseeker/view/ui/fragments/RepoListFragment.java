package com.kartsev.dmitry.githubseeker.view.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kartsev.dmitry.githubseeker.R;
import com.kartsev.dmitry.githubseeker.presenter.vo.RepositoryVO;
import com.kartsev.dmitry.githubseeker.utils.LogUtils;
import com.kartsev.dmitry.githubseeker.view.listeners.IItemClickListener;
import com.kartsev.dmitry.githubseeker.view.listeners.ILoadMoreListener;
import com.kartsev.dmitry.githubseeker.view.ui.adapters.recycler.RepoListAdapter;
import com.kartsev.dmitry.githubseeker.view.ui.adapters.recycler.pagination.PaginationScrollListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RepoListFragment extends Fragment implements IItemClickListener {
    private String TAG = this.getClass().getSimpleName();
    private RepoListAdapter mAdapter;
    private List<RepositoryVO> mRepoList = Collections.emptyList();
    private RecyclerView recyclerRepoList;
    private IItemClickListener clickListener;
    private ILoadMoreListener eventListener;
    private String LIST_REPOS = "RepoList";

    /*
        Pagination vals
     */
    private int LIST_OFFSET = 30;
    private static final int PAGE_START = 1;
    // Indicates if footer ProgressBar is shown (i.e. next page is loading)
    private boolean isLoading;
    // If current page is the last page (Pagination will stop after this page load)
    private boolean isLastPage = false;
    // total no. of pages to load. Initial load is page 0, after which 2 more pages will load.
    private int TOTAL_PAGES = 0;
    // total no. of items to load
    private int TOTAL_ITEM_COUNT = 0;
    // indicates the current page which Pagination is fetching.
    private int currentPage = PAGE_START;

    public RepoListFragment() {
    }

    public static RepoListFragment newInstance() {
        return new RepoListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.setAsAllowedTag(TAG);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repo_list, container, false);
        recyclerRepoList = view.findViewById(R.id.recyclerRepoList);
        initRecyclerView();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IItemClickListener)
            clickListener = (IItemClickListener) context;
        if (context instanceof ILoadMoreListener)
            eventListener = (ILoadMoreListener) context;
    }

    private void initRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerRepoList.setLayoutManager(llm);
        recyclerRepoList.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new RepoListAdapter(new ArrayList<RepositoryVO>(), this);
        recyclerRepoList.setAdapter(mAdapter);
        recyclerRepoList.addOnScrollListener(new PaginationScrollListener(llm) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1; //Increment page index to load the next one
                TOTAL_PAGES = getTotalPageCount();
                eventListener.onLoadMore(currentPage);
                 LogUtils.LOGD(TAG, "loadMoreItems: for page " + currentPage + " from " + TOTAL_PAGES
                        + " total items " + TOTAL_ITEM_COUNT + " from item (" + (currentPage * LIST_OFFSET) + ")"
                        + " to (" + ((currentPage * LIST_OFFSET) + LIST_OFFSET) + ")");
            }

            @Override
            public int getTotalPageCount() {
                return (int) Math.ceil(TOTAL_ITEM_COUNT / LIST_OFFSET);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    @Override
    public void onItemControlClicked(int position, RepositoryVO repository, int control) {
        if (repository != null) {
            clickListener.onItemControlClicked(position, repository, control);
        }
    }

    public void showRepoList(List<RepositoryVO> repoList, int totalCount) {
        if (repoList != null) {
            LogUtils.LOGD(TAG, "showRepoList(" + totalCount + "): " + repoList);
            this.mRepoList = repoList;
            this.TOTAL_ITEM_COUNT = totalCount;
            recyclerRepoList.scrollToPosition(0);
            mAdapter.setRepoList(repoList);
        }
    }

    public void addToList(List<RepositoryVO> repoList) {
        LogUtils.LOGD(TAG, "addToList(): " + repoList);
        if (repoList != null) {
            mAdapter.removeLoadingFooter();
            isLoading = false;
            mRepoList.addAll(repoList);
            mAdapter.addAll(repoList);

            if (currentPage != TOTAL_PAGES)
                mAdapter.addLoadingFooter();
            else
                isLastPage = true;
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        try {
            if (savedInstanceState == null)
                return;

            LogUtils.LOGD(TAG, "onViewStateRestored(): ");
            mRepoList = savedInstanceState.getParcelableArrayList(LIST_REPOS);
            if (mRepoList != null && mRepoList.isEmpty())
                LogUtils.LOGD(TAG, "onViewStateRestored: mRepoList is empty");
            mAdapter.setRepoList(mRepoList);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRepoList != null && !mRepoList.isEmpty()) {
            outState.putParcelableArrayList(LIST_REPOS, (ArrayList<? extends Parcelable>) mRepoList);
        }
    }

    public void clearList() {
        mAdapter.setRepoList(Collections.EMPTY_LIST);
    }

    public void cancelLoadMore() {
        currentPage--;
        mAdapter.removeLoadingFooter();
        isLoading = false;
    }
}
