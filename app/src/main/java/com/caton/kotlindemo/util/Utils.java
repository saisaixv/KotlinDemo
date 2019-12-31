package com.caton.kotlindemo.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static Class getClassByString(String name) throws ClassNotFoundException {
        Class baseInfo = Class.forName(name);
        return baseInfo;
    }

    public static boolean checkPackageNameIsExist(Context context, String pkgName) throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        return packageInfo != null;
    }

    public static boolean serviceIsRunning(Context context, String serviceName) {
        if (TextUtils.isEmpty(serviceName)) {
            return false;
        }
        ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runServiceList = mActivityManager
                .getRunningServices(100);
        for (int i = 0; i < runServiceList.size(); i++) {
            if (runServiceList.get(i).service.getClassName().toString().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }
}
