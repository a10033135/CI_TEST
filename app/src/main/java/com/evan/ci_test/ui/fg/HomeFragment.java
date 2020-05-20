package com.evan.ci_test.ui.fg;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evan.ci_test.R;
import com.evan.ci_test.databinding.FragmentHomeBinding;
import com.evan.ci_test.ui.base.MyFragment;
import com.evan.ci_test.utils.MyDialog;
import com.evan.ci_test.utils.NetWork;

/* 首頁 */
public class HomeFragment extends MyFragment {

    private FragmentHomeBinding binding;

    @Override
    protected void setTitle(String title) {
        super.setTitle(activity.getString(R.string.text_homeTitle));
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.homeBtAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!NetWork.networkConnected(activity)) {
                    MyDialog.showErrDialog(mFragment, activity.getString(R.string.text_noInternet));
                } else {
                    Navigation.findNavController(v).navigate(R.id.action_hostFragment_to_flowFragment2);
                }

            }
        });
    }
}

