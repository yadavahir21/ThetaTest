package com.raghu.thetapracticle.extra;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.widget.Toast;

import com.raghu.thetapracticle.AppMain;

import java.io.File;

public class Utils {
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    public static void showToastMassage(Context context, String msg){
        if (context != null && msg != null){
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }
    public static File getHiddenMediaDir() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            File dir = new File(AppMain.getInstance().getExternalFilesDir(null),".private_system_data");
            if (!dir.exists())
                dir.mkdirs();
            return dir;
        }
        return null;

    }
}
