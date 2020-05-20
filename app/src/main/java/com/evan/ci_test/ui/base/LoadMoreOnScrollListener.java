package com.evan.ci_test.ui.base;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class LoadMoreOnScrollListener extends RecyclerView.OnScrollListener {

    private int currentPage = 1; /* 總共頁數 */
    private int previousTotal = 0; /* 上一個 total Item 個數 */
    private boolean loading = true; /* 是否加載畫面 */
    private GridLayoutManager manager;

    public void initListener(){
        currentPage = 1;
        previousTotal = 0;
        loading = true;
    }

    protected LoadMoreOnScrollListener(GridLayoutManager manager) {
        this.manager = manager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = manager.getChildCount(); /* 螢幕上可見的 item 個數 */
        int totalItemCount = manager.getItemCount(); /* item 總數 */
        int firstVisibleItem = manager.findFirstVisibleItemPosition(); /* 第一個可見 item，意思是已經被回收的 item 們，原則上 first + visible = total*/

        /* 如果可以讀取，且需加載的總數大於之前設定的總數，便轉為預備 Loading 的狀態 */
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        /* 會隨著拉滑加載，最終會變得 total - visible = first ，就等於是拉到最底了，就可以來個 Loading */
        if (!loading && totalItemCount <= visibleItemCount + firstVisibleItem) {
            currentPage++;
            onLoadMore(currentPage); /* 第幾頁時加載資訊 */
            loading = true;
        }
    }

    public abstract void onLoadMore(int currentPage);
}
