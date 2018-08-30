package com.kartsev.dmitry.githubseeker.presenter.impl

import com.kartsev.dmitry.githubseeker.model.dto.GitHubSearchAnswerDTO
import com.kartsev.dmitry.githubseeker.model.impl.ModelImpl
import com.kartsev.dmitry.githubseeker.model.interfaces.IModel
import com.kartsev.dmitry.githubseeker.presenter.impl.mappers.RepoListMapper
import com.kartsev.dmitry.githubseeker.presenter.interfaces.IPresenter
import com.kartsev.dmitry.githubseeker.presenter.vo.RepositoryVO
import com.kartsev.dmitry.githubseeker.utils.LogUtils
import com.kartsev.dmitry.githubseeker.view.interfaces.IView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainPresenter() : IPresenter {
    private val searchModel: IModel
    private var mView: IView? = null
    private var repoListMapper: RepoListMapper

    private var callFirstTime: Call<GitHubSearchAnswerDTO>? = null
    private var callForMore: Call<GitHubSearchAnswerDTO>? = null

    init {
        this.searchModel = ModelImpl()
        this.repoListMapper = RepoListMapper()
        LogUtils.setAsAllowedTag(this.javaClass.simpleName)
    }

    override fun attachView(view: IView?) {
        mView = view!!
    }

    override fun detachView() {
        mView = null
    }

    override fun onSearchClick(query: String) {
        LogUtils.LOGD(this.javaClass.simpleName, "onSearchClick($query), $searchModel, $repoListMapper")

        callFirstTime = searchModel.getRepoList(query, 1)

        callFirstTime?.enqueue(object : Callback<GitHubSearchAnswerDTO> {
            override fun onResponse(call: Call<GitHubSearchAnswerDTO>, response: Response<GitHubSearchAnswerDTO>) {
                if (response.body() != null && response.body().totalCount > 0) {
                    LogUtils.LOGD(tag, "showing repo list ${response.body()}")
                            mView?.showList(repoListMapper.getRepoList(response.body().items), response.body().totalCount)
                        } else {
                            LogUtils.LOGD(tag, "list is empty")
                            mView?.showEmptyList()
                        }
            }

            override fun onFailure(call: Call<GitHubSearchAnswerDTO>, t: Throwable) {
                mView?.showError(t.message)
            }
        })
    }

    override fun onLoadMore(query: String?, page: Int) {
        callForMore = searchModel.getRepoList(query, page)

        callForMore?.enqueue(object : Callback<GitHubSearchAnswerDTO> {
            override fun onResponse(call: Call<GitHubSearchAnswerDTO>, response: Response<GitHubSearchAnswerDTO>) {
                if (response.body() != null && response.body().totalCount > 0) {
                    LogUtils.LOGD(tag, "showing repo list ${response.body()}")
                    mView?.addToList(repoListMapper.getRepoList(response.body().items))
                } else {
                    mView?.notCorrectServerAnswer()
                }
            }

            override fun onFailure(call: Call<GitHubSearchAnswerDTO>, t: Throwable) {
                mView?.showError(t.message)
            }
        })
    }

    override fun onRepoChosen(repo: RepositoryVO?) {
        mView?.showRepoDetails(repo)
    }

    override fun onCloseDetailsScreen() {
        mView?.closeDetailsScreen()
    }

    override fun onStop() {
        
    }

    companion object {
        val tag = this::class.java.simpleName
    }
}
