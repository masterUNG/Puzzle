package net.galliliu.jigsawpuzzle.presenter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by galliliu on 16/5/22.
 */
public class SystemScreenUtil {
    public static float getSystemScreenDensity(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }

    public static DisplayMetrics getSystemScreenSize(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }
}
