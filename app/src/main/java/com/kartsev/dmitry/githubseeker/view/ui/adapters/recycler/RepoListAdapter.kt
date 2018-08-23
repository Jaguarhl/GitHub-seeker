package com.kartsev.dmitry.githubseeker.view.ui.adapters.recycler

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.kartsev.dmitry.githubseeker.R
import com.kartsev.dmitry.githubseeker.presenter.vo.RepositoryVO
import com.kartsev.dmitry.githubseeker.utils.LogUtils
import com.kartsev.dmitry.githubseeker.view.listeners.IItemClickListener

class RepoListAdapter(private var repoList: List<RepositoryVO>?, private val clickListener: IItemClickListener):
        BaseAdapterPagination<RepositoryVO>(repoList as MutableList<RepositoryVO>) {
    init {
        LogUtils.setAsAllowedTag(this::class.java.simpleName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            ITEM -> {
                val viewItem = inflater.inflate(R.layout.rview_repo_item,
                        parent, false)
                viewHolder = ItemVH(viewItem)
            }
            LOADING -> {
                val viewLoading = inflater.inflate(R.layout.rview_item_progress,
                        parent, false)
                viewHolder = LoadingVH(viewLoading)
            }
        }
        return viewHolder!!
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repo = list[position]
        LogUtils.LOGD(this::class.java.simpleName, "onBindViewHolder(): $repo")

        if (repo == null)
            return

        when (getItemViewType(position)) {
            ITEM -> {
                val itemVH = holder as ItemVH
                itemVH.textRepoName?.text = repo.repoName
                itemVH.textRepoLanguage?.text = repo.repoLanguage
                var number = "№ ${position + 1}."
                itemVH.textResultNumber?.text = number
                itemVH.layoutRepoItem?.setOnClickListener { clickListener.onItemControlClicked(position, repo, 0) }
            }

            LOADING -> {
                val loadingVH = holder as LoadingVH
                loadingVH.progressLoadMore?.visibility = View.VISIBLE
            }
        }
    }

    fun setRepoList(repoList: List<RepositoryVO>?) {
        if (repoList != null) {
            setList(repoList)
        }
    }

    /**
     * Main list's content ViewHolder
     */
    private inner class ItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textRepoName: TextView? = null
        var textRepoLanguage: TextView? = null
        var textResultNumber: TextView? = null
        var layoutRepoItem: ConstraintLayout? = null

        init {
            textRepoName = itemView.findViewById(R.id.textRepoName)
            textRepoLanguage = itemView.findViewById(R.id.textRepoLanguage)
            layoutRepoItem = itemView.findViewById(R.id.layoutRepoItem)
            textResultNumber = itemView.findViewById(R.id.textResultNumber)
        }
    }

    /**
     * ILoading ViewHolder
     */
    private inner class LoadingVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var progressLoadMore: ProgressBar? = null

        init {
            progressLoadMore = itemView.findViewById(R.id.progressLoadMore)
        }
    }
}
