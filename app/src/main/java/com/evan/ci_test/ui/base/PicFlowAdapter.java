package com.evan.ci_test.ui.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evan.ci_test.Common.PhotoDetail;
import com.evan.ci_test.R;
import com.evan.ci_test.databinding.RowPicflowBinding;
import com.evan.ci_test.utils.NetWork;
import com.evan.ci_test.utils.Utils;

import java.io.BufferedInputStream;
import java.util.List;
import java.util.WeakHashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* 照片牆的調適器 */
public class PicFlowAdapter extends RecyclerView.Adapter<PicFlowAdapter.GridHolder> {

    private List<PhotoDetail> photoDetails;
    private WeakHashMap<String, Bitmap> bitmaps; /* 避免下載失敗儲存空值，因此使用 HashMap */
    private Context context;


    public PicFlowAdapter(List<PhotoDetail> photoDetails, WeakHashMap<String, Bitmap> bitmaps, Context context) {
        this.photoDetails = photoDetails;
        this.context = context;
        this.bitmaps = bitmaps;
    }

    @NonNull
    @Override
    public GridHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowPicflowBinding binding = RowPicflowBinding.inflate(LayoutInflater.from(context), parent, false);
        return new GridHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final GridHolder holder, int position) {
        PhotoDetail photoDetail = photoDetails.get(position);
        holder.tvId.setText(photoDetail.getId());
        holder.tvTitle.setText(photoDetail.getTitle());

        /* 使下載好的圖片暫存於 List ，不因回收而需重新下載圖片 */
        if (bitmaps.containsKey(photoDetail.getThumbnailUrl())) {
            Bitmap bitmap = bitmaps.get(photoDetail.getThumbnailUrl());
            holder.image.setImageBitmap(bitmap);
        } else {
            holder.image.setImageDrawable(context.getDrawable(R.drawable.image_default));
            loadData(photoDetail.getThumbnailUrl(), holder);
        }
    }

    private void loadData(final String url, final GridHolder holder) {
        NetWork.getApiService().getImage(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() == null)
                    return;

                ResponseBody body = response.body();
                Bitmap bitmap = BitmapFactory.decodeStream(new BufferedInputStream(body.byteStream()));
                bitmaps.put(url, bitmap); /* 將下載好的圖片存入 List ，供未來使用 */
                holder.image.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loadData(url, holder);
                /* 若下載失敗，再重新執行下載 */
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoDetails.size();
    }

    public void setPhotoDetails(List<PhotoDetail> photoDetails) {
        this.photoDetails = photoDetails;
    }

    public PicFlowAdapter setBitmaps(WeakHashMap<String, Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
        return this;
    }

    public class GridHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvId;
        private ImageView image;

        private GridHolder(RowPicflowBinding binding) {
            super(binding.getRoot());
            image = binding.rowImage;
            tvId = binding.rowTvId;
            tvTitle = binding.rowTvTitle;
        }
    }
}
