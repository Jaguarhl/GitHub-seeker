package com.kartsev.dmitry.githubseeker.presenter.impl

import com.kartsev.dmitry.githubseeker.model.dto.GitHubSearchAnswerDTO
import com.kartsev.dmitry.githubseeker.model.dto.RepoItemDTO
import com.kartsev.dmitry.githubseeker.model.impl.ModelImpl
import com.kartsev.dmitry.githubseeker.model.interfaces.IModel
import com.kartsev.dmitry.githubseeker.presenter.interfaces.IPresenter
import com.kartsev.dmitry.githubseeker.presenter.vo.RepositoryVO
import com.kartsev.dmitry.githubseeker.utils.LogUtils
import com.kartsev.dmitry.githubseeker.view.interfaces.IView
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainPresenter(private val mView: IView) : IPresenter {
    private val searchModel: IModel
    private val currentScreen: Int // 0 - repo list, 1 - repo details

    init {
        this.searchModel = ModelImpl()
        this.currentScreen = 0
        LogUtils.setAsAllowedTag(this.javaClass.simpleName)
    }

    override fun onSearchClick(query: String) {
        LogUtils.LOGD(this.javaClass.simpleName, "onSearchClick($query)")

        val call: Call<GitHubSearchAnswerDTO>? = searchModel.getRepoList(query, 0)

        call?.enqueue(object : Callback<GitHubSearchAnswerDTO> {
            override fun onResponse(call: Call<GitHubSearchAnswerDTO>, response: Response<GitHubSearchAnswerDTO>) {
                if (response.body().totalCount > 0) {
                    LogUtils.LOGD(tag, "showing repo list ${response.body()}")
                            mView.showList(getRepoList(response.body().items), response.body().totalCount)
                        } else {
                            LogUtils.LOGD(tag, "list is empty")
                            mView.showEmptyList()
                        }
            }

            override fun onFailure(call: Call<GitHubSearchAnswerDTO>, t: Throwable) {

            }
        })
    }

    override fun onLoadMore(query: String?, page: Int) {
        val call: Call<GitHubSearchAnswerDTO>? = searchModel.getRepoList(query, 0)

        call?.enqueue(object : Callback<GitHubSearchAnswerDTO> {
            override fun onResponse(call: Call<GitHubSearchAnswerDTO>, response: Response<GitHubSearchAnswerDTO>) {
                if (response.body().totalCount > 0) {
                    LogUtils.LOGD(tag, "showing repo list ${response.body()}")
                    mView.showList(getRepoList(response.body().items), response.body().totalCount)
                } else {
                    LogUtils.LOGD(tag, "list is empty")
                    mView.showEmptyList()
                }
            }

            override fun onFailure(call: Call<GitHubSearchAnswerDTO>, t: Throwable) {

            }
        })
    }


    private fun getRepoList(items: List<RepoItemDTO>): MutableList<RepositoryVO>? = runBlocking {
        val job = async(CommonPool) {
            var repoList: MutableList<RepositoryVO> = ArrayList()
            items.mapTo(repoList) { it -> RepositoryVO(it.fullName, it.language, it.owner.login, it.owner.avatarUrl) }

            return@async repoList
        }
        job.await()
    }

    override fun onRepoChosen(repo: RepositoryVO?) {
        mView.showRepoDetails(repo)
    }

    override fun onCloseDetailsScreen() {
        mView.closeDetailsScreen()
    }

    override fun onStop() {
        
    }

    companion object {
        val tag = this::class.java.simpleName
    }
}
