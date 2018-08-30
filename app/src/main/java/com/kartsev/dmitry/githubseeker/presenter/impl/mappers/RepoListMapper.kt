package com.kartsev.dmitry.githubseeker.presenter.impl.mappers

import com.kartsev.dmitry.githubseeker.model.dto.RepoItemDTO
import com.kartsev.dmitry.githubseeker.presenter.vo.RepositoryVO
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking


class RepoListMapper {

    fun getRepoList(items: List<RepoItemDTO>): MutableList<RepositoryVO>? = runBlocking {
        val job = async(CommonPool) {
            var repoList: MutableList<RepositoryVO> = ArrayList()
            items.mapTo(repoList) { it -> RepositoryVO(it.fullName, it.language, it.owner.login, it.owner.avatarUrl) }

            return@async repoList
        }
        job.await()
    }
}