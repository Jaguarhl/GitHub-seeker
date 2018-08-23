package com.kartsev.dmitry.githubseeker.view.ui.adapters.recycler

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.kartsev.dmitry.githubseeker.R

abstract class BaseAdapterPagination<T>(protected var mList: MutableList<T>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var loadingFooter: T? = null
    private var isLoadingAdded = false

    val isEmpty: Boolean
        get() = itemCount == 0

    fun getList(): List<T> {
        return mList
    }

    fun setList(list: MutableList<T>) {
        this.mList = list
        notifyDataSetChanged()
    }

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    override fun getItemCount(): Int {
        return mList.size
    }

    private fun getItem(position: Int): T {
        return mList[position]
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mList.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    /*
       Helpers
    */

    fun setLoadingFooter(loadingFooter: T) {
        this.loadingFooter = loadingFooter
    }

    fun add(item: T) {
        mList.add(item)
    }

    fun addAll(itemsList: List<T>) {
        for (item in itemsList) {
            add(item)
        }
        notifyDataSetChanged()
    }

    fun remove(item: T) {
        val position = mList.indexOf(item)
        if (position >= 0) {
            mList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        loadingFooter?.let { mList.add(it) }
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        if (mList.contains(loadingFooter)) {
            val position = mList.indexOf(loadingFooter)
            mList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    /**
     * Main list's content ViewHolder
     */
    abstract inner class ItemVH(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * ILoading ViewHolder
     */
    abstract inner class LoadingVH(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {

        /*
        Pagination values
     */
        val ITEM = 0
        val LOADING = 1
    }
}
