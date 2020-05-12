package com.evan.ci_test.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.evan.ci_test.R;

public abstract class MyFragment extends Fragment {

    public Fragment mFragment = this;
    public Activity activity;

    protected void setTitle(String title) {
        getActivity().setTitle(title);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        setTitle(activity.getString(R.string.text_TitleDefault));
    }
}
