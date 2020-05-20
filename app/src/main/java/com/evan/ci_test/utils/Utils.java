package com.evan.ci_test.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.WeakHashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.evan.ci_test.utils.Common.PREFERENCES_NAME;

public class Utils {

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static WeakHashMap<String, Bitmap> getPrefBitmaps(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        String data = pref.getString("bitmaps", null);
        if (data == null) {
            Log.e("getPrefBitmaps", "bitmaps為空");
            return new WeakHashMap<>();
        }

        Type type = new TypeToken<WeakHashMap<String, Bitmap>>() {}.getType();
        WeakHashMap<String, Bitmap> bitmaps = new Gson().fromJson(data, type);
        Log.e("getPrefBitmaps", "共得取" + bitmaps.size() + "筆資料");
        return bitmaps;
    }

    public static void savePrefBitmaps(Activity activity, WeakHashMap<String, Bitmap> bitmaps) {
        SharedPreferences pref = activity.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        pref.edit().putString("bitmaps", new Gson().toJson(bitmaps)).apply();
        Log.e("savePrefBitmaps", "共儲存" + bitmaps.size() + "筆");
    }
}
