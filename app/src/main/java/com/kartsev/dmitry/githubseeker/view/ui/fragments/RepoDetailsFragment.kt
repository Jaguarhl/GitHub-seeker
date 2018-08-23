package com.kartsev.dmitry.githubseeker.view.ui.fragments

import android.content.Context
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.kartsev.dmitry.githubseeker.R
import com.kartsev.dmitry.githubseeker.presenter.vo.RepositoryVO
import com.kartsev.dmitry.githubseeker.view.ui.adapters.listeners.IItemClickListener
import kotlinx.android.synthetic.main.fragment_repo_details.*

class RepoDetailsFragment: Fragment() {
    private lateinit var listener: IItemClickListener
    private var mRepository: RepositoryVO? = null
    private val DATA = "DATA"

    companion object {
        fun newInstance(): RepoDetailsFragment {
            return RepoDetailsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_repo_details, container, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initClickListeners()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is IItemClickListener) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + " must implement IItemClickListener")
        }
    }

    private fun initClickListeners() {
        btnReturn.setOnClickListener { listener.onItemControlClicked(0, null, 1) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mRepository != null)
            outState.putParcelable(DATA, mRepository)
    }

    override fun onViewStateRestored(@Nullable savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        try {
            if (savedInstanceState == null)
                return
            mRepository = savedInstanceState.getParcelable(DATA)
            if (mRepository != null) {
                setRepoToShow(mRepository!!)
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

    fun setRepoToShow(repository: RepositoryVO) {
        this.mRepository = repository
        textRepoName.text = repository.repoName
        textRepoLanguage.text = repository.repoLanguage
        textAuthor.text = repository.authorName
        Glide.with(this).load(repository.avatarUrl)
                .override(75, 75)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .centerCrop()
                .into(imageAuthorAva)
    }
}