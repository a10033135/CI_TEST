package com.evan.ci_test.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.evan.ci_test.R;
import com.evan.ci_test.databinding.DialogErrBinding;

/* Err時產生的Dialog */
public class ErrDialog extends DialogFragment implements View.OnClickListener {

    private DialogErrBinding binding;

    public static ErrDialog newInstance(String msg) {
        Bundle args = new Bundle();
        args.putString("msg", msg);
        ErrDialog fragment = new ErrDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_TransparentWindow);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogErrBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        String err = bundle.getString("msg");
        binding.btConfirm.setOnClickListener(this);

        if (getActivity() != null)
            binding.tvMsg.setText(err != null ? err : getActivity().getString(R.string.text_unknowError));

    }

    @Override
    public void onClick(View v) {
        dismissAllowingStateLoss();
    }
}
