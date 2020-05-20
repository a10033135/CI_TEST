package com.evan.ci_test.ui.fg;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.evan.ci_test.Common.PhotoDetail;
import com.evan.ci_test.R;
import com.evan.ci_test.databinding.FragmentFlowBinding;
import com.evan.ci_test.ui.base.LoadMoreOnScrollListener;
import com.evan.ci_test.ui.base.MyFragment;
import com.evan.ci_test.ui.base.PicFlowAdapter;
import com.evan.ci_test.utils.MyDialog;
import com.evan.ci_test.utils.NetWork;
import com.evan.ci_test.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Thread.sleep;


/* 照片牆頁面 */
public class FlowFragment extends MyFragment implements SwipeRefreshLayout.OnRefreshListener {

    private boolean firstGetData; /* 是否為第一次加載畫面 */
    private List<PhotoDetail> photoDetails; /* 純文字資料 */
    private WeakHashMap<String, Bitmap> bitmaps; /* bitmap資料，以 weak 使 Recy 時可以順利啟動回收，避免內存泄露 */
    private LoadMoreOnScrollListener onScrollListener;
    private FragmentFlowBinding binding;
    private PicFlowAdapter flowAdapter;

    @Override
    protected void setTitle(String title) {
        super.setTitle(activity.getString(R.string.text_flowTitle));
    }


    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFlowBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        getData();
    }

    /* 資料初始化 */
    private void initData() {
        firstGetData = true;
        photoDetails = new ArrayList<>();
        bitmaps = Utils.getPrefBitmaps(activity);
    }

    /* 下載資料 */
    private void getData() {

        if (!NetWork.networkConnected(activity)) {
            MyDialog.showErrDialog(this, "沒有網路");
            return;
        }

        if (firstGetData) {
            MyDialog.showLoadingPage(mFragment); /* 加載畫面 */
        }

        /* 開始下載資料 */
        NetWork.getApiService().getDetails().enqueue(new Callback<List<PhotoDetail>>() {
            @Override
            public void onResponse(Call<List<PhotoDetail>> call, Response<List<PhotoDetail>> response) {
                Log.e("TAG", "Response,String.size: " + response.body().size());
                MyDialog.closeLoadingPage();/* 關閉加載畫面 */

                if (response.body() == null)
                    MyDialog.showErrDialog(mFragment, activity.getString(R.string.text_errNullBody));

                photoDetails = response.body();
                getCleanBitmaps(); /* 根據新得到的photoDetail，清除 bitmaps 裡的舊網址，並於 Adapter 內重新加載 */

                if (firstGetData) {
                    initView();
                    firstGetData = false;
                } else {
                    flowAdapter.setBitmaps(bitmaps)
                               .setPhotoDetails(subPhotoDetails(1)); /* 僅顯示 1~20 的資料  */
                    flowAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<PhotoDetail>> call, Throwable t) {
                Utils.showToast(activity, getActivity().getString(R.string.text_loadDelay));
                getData(); /* 失敗時，維持加載畫面，並再次嘗試下載資料 */
            }
        });
    }

    /* 初始化畫面 */
    private void initView() {

        /* 下拉式更新 */
        binding.flowSwipeRefresh.setOnRefreshListener(this);

        flowAdapter = new PicFlowAdapter(subPhotoDetails(1), bitmaps, getContext()); /* 初始空值 */
        binding.picflowRecy.setAdapter(flowAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        binding.picflowRecy.setLayoutManager(layoutManager);

        onScrollListener = new LoadMoreOnScrollListener((GridLayoutManager) binding.picflowRecy.getLayoutManager()) {
            @Override
            public void onLoadMore(final int currentPage) {
                if (currentPage > 1) { /* 避開判斷第一頁 */
                    MyDialog.showLoadingPage(mFragment);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sleep(800);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        flowAdapter.setPhotoDetails(subPhotoDetails(currentPage)); /* 更新使用的資料 */
                                        flowAdapter.notifyDataSetChanged(); /* 提醒重新加載 */
                                        MyDialog.closeLoadingPage();
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        };

        /* 分頁載入機制 */
        binding.picflowRecy.clearOnScrollListeners();
        binding.picflowRecy.addOnScrollListener(onScrollListener);
    }

    private void getCleanBitmaps() {
        int num = 0;
        WeakHashMap<String, Bitmap> newBitmaps = new WeakHashMap<>();

        for (String key : bitmaps.keySet()) {
            if (photoDetails.contains(key)) {
                newBitmaps.put(key, bitmaps.get(key));
                num++;
            }
        }

        bitmaps = newBitmaps;
        Log.e("cleanBitmaps", "共更新" + num + "筆Bitmaps");
    }

    /* 判斷顯示的資料值 */
    private List<PhotoDetail> subPhotoDetails(int position) {
        int endPosi = position * 40;
        int startPosi = 0;
        return photoDetails.subList(startPosi, endPosi);
    }

    @Override
    public void onRefresh() {
        initData();
        getData();
        Utils.showToast(activity, getString(R.string.text_load));
        onScrollListener.initListener();
        binding.flowSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.savePrefBitmaps(activity, bitmaps);
    }

}
