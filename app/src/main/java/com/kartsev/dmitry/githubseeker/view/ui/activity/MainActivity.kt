package com.kartsev.dmitry.githubseeker.view.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction.*
import android.view.View.GONE
import android.view.View.VISIBLE
import com.kartsev.dmitry.githubseeker.R
import com.kartsev.dmitry.githubseeker.presenter.impl.MainPresenter
import com.kartsev.dmitry.githubseeker.presenter.interfaces.IPresenter
import com.kartsev.dmitry.githubseeker.presenter.vo.RepositoryVO
import com.kartsev.dmitry.githubseeker.utils.HideKeyboard
import com.kartsev.dmitry.githubseeker.utils.LogUtils
import com.kartsev.dmitry.githubseeker.view.interfaces.IView
import com.kartsev.dmitry.githubseeker.view.ui.adapters.listeners.IItemClickListener
import com.kartsev.dmitry.githubseeker.view.ui.fragments.RepoDetailsFragment
import com.kartsev.dmitry.githubseeker.view.ui.fragments.RepoListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IView, IItemClickListener {
    private lateinit var mPresenter: IPresenter
    private lateinit var mFragmentManager: FragmentManager

    // fragments
    private var mRepoListFragment: RepoListFragment? = null
    private var mRepoDetailsFragment: RepoDetailsFragment? = null
    private var mCurrentFragment: Fragment? = null
    private val REPO_LIST = "REPO_LIST"
    private val REPO_DETAILS = "DETAILS_VIEW"
    private val BACK_STACK_OPERATION = "RepoBackStack"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPresenter = MainPresenter(this)
        mFragmentManager = supportFragmentManager
        if (savedInstanceState != null) {
            mRepoListFragment = supportFragmentManager.findFragmentByTag(REPO_LIST) as RepoListFragment
            mRepoDetailsFragment = supportFragmentManager.findFragmentByTag(REPO_DETAILS) as RepoDetailsFragment
        } else {
            if (mRepoListFragment == null)
                mRepoListFragment = RepoListFragment.newInstance()
            if (mRepoDetailsFragment == null)
                mRepoDetailsFragment = RepoDetailsFragment.newInstance()
            setupFragments()
        }
        LogUtils.setAsAllowedTag(this.javaClass.simpleName)

        initClickListeners()
    }

    private fun initClickListeners() {
        btnSearch.setOnClickListener {
            if (editSearchQuery.text.toString().isNotBlank() && editSearchQuery.text.toString().length > 2) {
                LogUtils.LOGD(this.javaClass.simpleName, "Click GO! (${editSearchQuery.text})")
                HideKeyboard.hideKeyboard(this)
                layoutProgress.visibility = VISIBLE
                textNothingFound.visibility = GONE
                mPresenter.onSearchClick(editSearchQuery.text.toString())
            } else
                showError(applicationContext.getString(R.string.error_search_string))
        }
    }

    override fun showList(repoList: MutableList<RepositoryVO>?) {
        LogUtils.LOGD(this.javaClass.simpleName, "$mRepoListFragment, Got repo list: $repoList")
        switchFragment(mRepoListFragment)
        layoutProgress.visibility = GONE
        mRepoListFragment!!.showRepoList(repoList)
    }

    override fun onStop() {
        super.onStop()
        mPresenter.onStop()
    }

    override fun showError(error: String?) {
        makeToast(error)
    }

    private fun makeToast(message: String?) {
        message?.let {
            Snackbar.make(
                    container,
                    it,
                    Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun showEmptyList() {
        LogUtils.LOGD(this.javaClass.simpleName, "Nothing got")
        switchFragment(mRepoListFragment)
        mRepoListFragment?.clearList()
        layoutProgress.visibility = GONE
        textNothingFound.visibility = VISIBLE
    }

    private fun setupFragments() {
        val fragmentTransaction = mFragmentManager
                .beginTransaction()
        if (fragmentManager.findFragmentByTag(REPO_LIST) == null)
            fragmentTransaction.add(R.id.container, mRepoListFragment, REPO_LIST)
        if (fragmentManager.findFragmentByTag(REPO_DETAILS) == null)
            fragmentTransaction.add(R.id.container, mRepoDetailsFragment, REPO_DETAILS)
        fragmentTransaction.addToBackStack(BACK_STACK_OPERATION)
        fragmentTransaction.hide(mRepoDetailsFragment)
        fragmentTransaction.commit()
        mCurrentFragment = mRepoListFragment
    }

    /**
     * Switching to required fragment from current
     * @param Fragment fragmentTo - fragment to switch
     */
    private fun switchFragment(fragmentTo: Fragment?) {
        LogUtils.LOGD(this::class.java.simpleName, "switchFragment(): $fragmentTo")

        if (fragmentTo != null) {
            val fragmentTransaction = mFragmentManager.beginTransaction()
                    .setTransition(TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(BACK_STACK_OPERATION)
            if (fragmentTo == mRepoListFragment)
                fragmentTransaction.hide(mRepoDetailsFragment)
            else
                fragmentTransaction.hide(mRepoListFragment)
            fragmentTransaction.show(fragmentTo)
            fragmentTransaction.commitAllowingStateLoss()

            mCurrentFragment = fragmentTo
        }
    }

    override fun onItemControlClicked(position: Int, repository: RepositoryVO?, conrol: Int) {
        if (repository != null) {
            LogUtils.LOGD(this::class.java.simpleName, "Clicked $repository")
            mPresenter.onRepoChosen(repository)
        } else {
            when (conrol) {
                1 -> mPresenter.onCloseDetailsScreen()
            }
        }
    }

    override fun showRepoDetails(repo: RepositoryVO?) {
        mRepoDetailsFragment?.setRepoToShow(repo!!)
        switchFragment(mRepoDetailsFragment)
    }

    override fun closeDetailsScreen() {
        switchFragment(mRepoListFragment)
    }

}
