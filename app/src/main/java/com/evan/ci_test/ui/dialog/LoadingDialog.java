package com.evan.ci_test.ui.dialog;

import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.evan.ci_test.R;
import com.evan.ci_test.databinding.DialogLoadingBinding;

/* Loading的Dialog */
public class LoadingDialog extends DialogFragment {

    private DialogLoadingBinding binding;

    public static LoadingDialog newInstance() {
        return new LoadingDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_TransparentWindow);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogLoadingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        aniVector();  /* 驅動 Vector 動畫 */
    }

    public void aniVector() {
        Drawable drawable = binding.loadProgressBar.getDrawable();

        if (drawable instanceof AnimatedVectorDrawable) {
            final AnimatedVectorDrawable avd = (AnimatedVectorDrawable) drawable;
            avd.start();
            avd.registerAnimationCallback(new Animatable2.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    avd.start();
                    if (getActivity() != null)
                        binding.loadTv.setText(getActivity().getString(R.string.text_timeout));
                }
            });
        }


    }

}