package com.kartsev.dmitry.githubseeker.view.ui.adapters.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.kartsev.dmitry.githubseeker.utils.LogUtils;

import java.util.List;

public abstract class BaseAdapterPagination<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String LOG_TAG = "BaseAdapterPagination";
    protected List<T> list;
    private T loadingFooter;

    /*
        Pagination values
     */
    protected static final int ITEM = 0;
    protected static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    public BaseAdapterPagination(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public int getItemCount() {
        return list.size();
    }

    private T getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    /*
       Helpers
    */

    public void setLoadingFooter(T loadingFooter) {
        this.loadingFooter = loadingFooter;
    }

    public void add(T item) {
        list.add(item);
    }

    public void addAll(List<T> itemsList) {
        LogUtils.LOGD(LOG_TAG, "adding " + itemsList);
        for (T item: itemsList) {
            add(item);
        }
        notifyDataSetChanged();
    }

    public void remove(T item) {
        int position = list.indexOf(item);
        if (position >= 0) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        list.add(loadingFooter);
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        if (list.contains(loadingFooter)) {
            int position = list.indexOf(loadingFooter);
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * Main list's content ViewHolder
     */
    protected abstract class ItemVH extends RecyclerView.ViewHolder {

        public ItemVH(View itemView) {
            super(itemView);
        }
    }

    /**
     * ILoading ViewHolder
     */
    protected abstract class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }
}

