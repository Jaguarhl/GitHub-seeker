package com.kartsev.dmitry.githubseeker.view.ui.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kartsev.dmitry.githubseeker.R
import com.kartsev.dmitry.githubseeker.presenter.vo.RepositoryVO
import com.kartsev.dmitry.githubseeker.view.ui.adapters.listeners.IItemClickListener
import com.kartsev.dmitry.githubseeker.view.ui.adapters.recycler.RepoListAdapter
import android.os.Parcelable
import android.support.annotation.Nullable
import android.support.v7.widget.RecyclerView
import com.kartsev.dmitry.githubseeker.utils.LogUtils



class RepoListFragment: Fragment(), IItemClickListener {
    private lateinit var mAdapter: RepoListAdapter
    private var mRepoList: List<RepositoryVO>? = emptyList()
    private lateinit var recyclerRepoList: RecyclerView
    private lateinit var listener: IItemClickListener

    private val LIST_REPOS = "RepoList"

    companion object {
        fun newInstance(): RepoListFragment {
            return RepoListFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.setAsAllowedTag(this::class.java.simpleName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_repo_list, container, false)
        recyclerRepoList = view.findViewById(R.id.recyclerRepoList)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is IItemClickListener) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + " must implement IItemClickListener")
        }
    }

    private fun initRecyclerView() {
        LogUtils.LOGD(this::class.java.simpleName, "Recycler init")
        mAdapter = RepoListAdapter(ArrayList(), this)
        recyclerRepoList.layoutManager = LinearLayoutManager(context)
        recyclerRepoList.adapter = mAdapter
    }

    override fun onItemControlClicked(position: Int, repository: RepositoryVO?, conrol: Int) {
        if (repository != null) {
            listener.onItemControlClicked(position, repository, conrol)
        }
    }

    fun showRepoList(repoList: MutableList<RepositoryVO>?) {
        if (repoList != null) {
            LogUtils.LOGD(this::class.java.simpleName, "showRepoList(): $repoList")
            mRepoList = repoList
            recyclerRepoList.scrollToPosition(0)
            mAdapter.setRepoList(repoList)
        }
    }

    override fun onViewStateRestored(@Nullable savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        try {
            if (savedInstanceState == null)
                return

            LogUtils.LOGD(this::class.java.simpleName, "onViewStateRestored(): ")
            mRepoList = savedInstanceState.getParcelableArrayList(LIST_REPOS)
            if (mRepoList != null && (mRepoList as java.util.ArrayList<RepositoryVO>).isEmpty())
                LogUtils.LOGD(this::class.java.simpleName, "onViewStateRestored: mRepoList is empty")
            mAdapter.setRepoList(mRepoList as java.util.ArrayList<RepositoryVO>?)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mRepoList != null && mRepoList?.isNotEmpty()!!) {
            outState.putParcelableArrayList(LIST_REPOS, mRepoList as ArrayList<out Parcelable>)
        }
    }

    fun clearList() {
        mAdapter.setRepoList(emptyList())
    }
}