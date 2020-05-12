package com.evan.ci_test.utils;

import android.util.Log;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.evan.ci_test.ui.dialog.ErrDialog;
import com.evan.ci_test.ui.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

public class MyDialog {
    private static DialogFragment dialog;
    private static List<DialogFragment> listDialog = new ArrayList<>();

    public static void showErrDialog(Fragment fragment, String msg) {
        close();
        dialog = ErrDialog.newInstance(msg);
        show(fragment, Common.REQ_DIALOG_DEFAULT, false);
    }

    public static void showLoadingPage(Fragment fg) {

        if (fg == null)
            return;

        for (DialogFragment dg : listDialog) {
            if (dg instanceof LoadingDialog) {
                return;
            }
        }

        /*新建視窗*/
        dialog = LoadingDialog.newInstance();
        show(fg, Common.REQ_DIALOG_DEFAULT, false);
    }

    public static void closeLoadingPage() {
        for (DialogFragment dg : listDialog) {
            if (dg instanceof LoadingDialog) {
                dg.dismissAllowingStateLoss();
                listDialog.remove(dg);
                return;
            }
        }
    }


    private static void show(Fragment fg, int reqCode, boolean cancelable) {

        if (fg == null)
            return;

        dialog.setTargetFragment(fg, reqCode);
        if (fg.getFragmentManager() == null)
            return;
        Log.d("MyDialog", "Show " + dialog.getClass().getSimpleName()
                + ", ReqCode: " + reqCode);
        dialog.show(fg.getFragmentManager(), "dialogFg");
        dialog.setCancelable(cancelable);
        listDialog.add(dialog);
        dialog = null;


    }


    private static void close() {

        try {
            for (DialogFragment dialog : listDialog) {

                if (dialog != null && dialog.getDialog() != null && !dialog.isRemoving()) {
                    dialog.dismissAllowingStateLoss();
                    Log.d("MyDialog", "關閉完成: " + dialog.getClass().getSimpleName());
                }
            }

            listDialog.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
