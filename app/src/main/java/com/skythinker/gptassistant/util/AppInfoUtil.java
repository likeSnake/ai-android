package com.skythinker.gptassistant.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.skythinker.gptassistant.entity.AppInfo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AppInfoUtil {
    private static ArrayList<AppInfo> appInfos;
    public static Drawable getAppIcon(Context context, String pkgName) {
        try {
            if (null != pkgName) {
                PackageManager pm = context.getPackageManager();
                ApplicationInfo info = pm.getApplicationInfo(pkgName, 0);
                return info.loadIcon(pm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAppName(Context context, String pkgName) {
        try {
            if (null != pkgName) {
                PackageManager pm = context.getPackageManager();
                ApplicationInfo info = pm.getApplicationInfo(pkgName, 0);
                return info.loadLabel(pm).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    // 初始化获取包名+应用名
    public static void initAppListAsync(Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (appInfos == null){
                    appInfos = new ArrayList<>();
                }else {
                    return;
                }
                PackageManager packageManager = context.getPackageManager();
                List<ApplicationInfo> apps = packageManager.getInstalledApplications(0);

                for (ApplicationInfo app : apps) {
                    // 排除系统应用
                    if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        continue;
                    }

                    String appName = app.loadLabel(packageManager).toString();
                    String packageName = app.packageName;
                    //Drawable icon = app.loadIcon(packageManager);
                    long appSize = 1;

                   /* try {
                        // 获取应用大小
                        PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
                        appSize = packageInfo.applicationInfo.sourceDir.length();
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }*/
                    System.out.println("应用程序名称：" + appName);
                    System.out.println("应用程序包名：" + packageName);
                    //System.out.println("应用程序大小：" + appSize);
                    //byte[] bytes = getDrawableByte(icon);

                    appInfos.add(new AppInfo(appName, packageName));
                }

                // Notify the listener with the app list
                //listener.onAppListFetched(appInfos);
            }
        }).start();
    }

    public static ArrayList<AppInfo> getAppInfos(){
            return appInfos;
    }

    public interface AppListListener {
        void onAppListFetched(ArrayList<AppInfo> appList);
    }
    public static byte[] convertDrawableToByteArray(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        return bitmapdata;
    }

    public static Bitmap byteArrayToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static Drawable getDrawable(byte[] bytes,Context context) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        BitmapDrawable bd = new BitmapDrawable(context.getResources(), bitmap);
        return bd;
    }


    public static byte[] getDrawableByte(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        // 创建一个字节数组输出流
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        //设置位图的压缩格式，质量为100%，并放入字节数组输出流中
        //选择Bitmap.CompressFormat.PNG，不然把byte[]转换成drawable图像会有黑色背景
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        // 将字节数组输出流转化为字节数组byte[]
        byte[] bitmapdata = stream.toByteArray();
        return bitmapdata;
    }
}
