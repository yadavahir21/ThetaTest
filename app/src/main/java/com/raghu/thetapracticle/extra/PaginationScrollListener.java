package com.raghu.thetapracticle.extra;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {
    private static final String TAG = PaginationScrollListener.class.getSimpleName();
    public static final int PAGE_START = 1;
    public static int PAGE_SIZE = 0;

    @NonNull
    private LinearLayoutManager layoutManager;


    public PaginationScrollListener(@NonNull LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        Log.d(TAG, "onScrolled: ");
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                loadMoreItems((totalItemCount));
            }
        }
    }

    protected abstract void loadMoreItems(int startPosition);

    public abstract boolean isLastPage();

    public abstract boolean isLoading();
}
