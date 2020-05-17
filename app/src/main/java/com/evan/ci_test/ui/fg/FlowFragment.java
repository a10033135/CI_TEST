package com.evan.ci_test.ui.fg;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evan.ci_test.R;
import com.evan.ci_test.ui.base.LoadMoreOnScrollListener;
import com.evan.ci_test.ui.base.MyFragment;
import com.evan.ci_test.utils.MyDialog;
import com.evan.ci_test.utils.NetWork;
import com.evan.ci_test.Common.PhotoDetail;
import com.evan.ci_test.ui.base.PicFlowAdapter;
import com.evan.ci_test.utils.Utils;
import com.evan.ci_test.databinding.FragmentFlowBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Thread.sleep;


/* 照片牆頁面 */
public class FlowFragment extends MyFragment {

    private FragmentFlowBinding binding;
    private List<PhotoDetail> photoDetails = new ArrayList<>();
    private PicFlowAdapter flowAdapter;

    @Override
    protected void setTitle(String title) {
        super.setTitle(activity.getString(R.string.text_flowTitle));
    }


    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFlowBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(); /* 重置頁面 */
        loadData();
    }

    private void initView() {
        flowAdapter = new PicFlowAdapter(photoDetails, getContext());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);

        binding.picflowRecy.setAdapter(flowAdapter);
        binding.picflowRecy.setLayoutManager(layoutManager);

        /* 分頁載入機制 */
        binding.picflowRecy.addOnScrollListener(new LoadMoreOnScrollListener(layoutManager) {
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
        });
    }

    private void loadData() {
        MyDialog.showLoadingPage(mFragment); /* 加載畫面 */

        if(!NetWork.networkConnected(activity))
            MyDialog.showErrDialog(this,"沒有網路");

        /* 開始下載資料 */
        NetWork.getApiService().getDetails().enqueue(new Callback<List<PhotoDetail>>() {
            @Override
            public void onResponse(Call<List<PhotoDetail>> call, Response<List<PhotoDetail>> response) {
                Log.e("TAG", "Response,String.size: " + response.body().size());
                MyDialog.closeLoadingPage();/* 關閉加載畫面 */

                if (response.body() == null)
                    MyDialog.showErrDialog(mFragment, activity.getString(R.string.text_errNullBody));

                photoDetails = response.body();
                flowAdapter.setPhotoDetails(subPhotoDetails(1)); /* 僅顯示 1~20 的資料  */
                flowAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<PhotoDetail>> call, Throwable t) {
                Utils.showToast(activity, getActivity().getString(R.string.text_loadDelay));
                loadData(); /* 失敗時，維持加載畫面，並再次嘗試下載資料 */
            }
        });

    }

    /* 判斷顯示的資料值 */
    private List<PhotoDetail> subPhotoDetails(int position) {
        int endPosi = position * 40;
        int startPosi = 0;
        return photoDetails.subList(startPosi, endPosi);
    }

}
