package com.kartsev.dmitry.githubseeker.view.ui.adapters.recycler

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kartsev.dmitry.githubseeker.R
import com.kartsev.dmitry.githubseeker.presenter.vo.RepositoryVO
import com.kartsev.dmitry.githubseeker.utils.LogUtils
import com.kartsev.dmitry.githubseeker.view.ui.adapters.listeners.IItemClickListener

class RepoListAdapter(private var repoList: List<RepositoryVO>?, private val clickListener: IItemClickListener) : RecyclerView.Adapter<RepoListAdapter.ViewHolder>() {

    init {
        LogUtils.setAsAllowedTag(this::class.java.simpleName)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.rview_repo_item,
                viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = repoList!![position]
        LogUtils.LOGD(this::class.java.simpleName, "onBindViewHolder(): $repo")

        holder.textRepoName?.text = repo.repoName
        holder.textRepoLanguage?.text = repo.repoLanguage
        var number = "№ ${position + 1}."
        holder.textResultNumber?.text = number
        holder.layoutRepoItem?.setOnClickListener { clickListener.onItemControlClicked(position, repo, 0) }
    }

    override fun getItemCount(): Int {
        return repoList!!.size
    }

    fun setRepoList(repoList: List<RepositoryVO>?) {
        if (repoList != null) {
            LogUtils.LOGD(this::class.java.simpleName, "Setting repoList ($repoList)")
            this.repoList = repoList
            this.notifyDataSetChanged()
        }
    }

    class ViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
        var textRepoName: TextView? = null
        var textRepoLanguage: TextView? = null
        var textResultNumber: TextView? = null
        var layoutRepoItem: ConstraintLayout? = null

        init {
            textRepoName = viewItem.findViewById(R.id.textRepoName)
            textRepoLanguage = viewItem.findViewById(R.id.textRepoLanguage)
            layoutRepoItem = viewItem.findViewById(R.id.layoutRepoItem)
            textResultNumber = viewItem.findViewById(R.id.textResultNumber)
        }
    }
}
