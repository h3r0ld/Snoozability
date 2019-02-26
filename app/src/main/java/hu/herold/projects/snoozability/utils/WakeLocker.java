package hu.herold.projects.snoozability.utils;

import android.content.Context;
import android.os.PowerManager;
import android.view.WindowManager;

public abstract class WakeLocker {
    private static PowerManager.WakeLock wakeLock;

    public static void acquire(Context ctx) {
        if (wakeLock != null) {
            wakeLock.release();
        }

        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);

        wakeLock = pm.newWakeLock( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "snoozability:wakelock");

        wakeLock.acquire(150000);
    }

    public static void release() {
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
    }
}