package com.evan.ci_test.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class Utils {

    public static void showToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }


}
