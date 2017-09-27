package util;

import android.util.Log;

/**
 * Created by HanyLuv on 2015-10-24.
 */
public class Logger {

    private static final String TAG = "[[[ CARTOON_LOG ]]]";
    private static final boolean loggerMode = true;

    public static final void d(String msg) {
        if (loggerMode) {
            Log.d(TAG, msg);
        }
    }
}
