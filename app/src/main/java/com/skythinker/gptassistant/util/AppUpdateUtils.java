package com.skythinker.gptassistant.util;

import static com.skythinker.gptassistant.util.MyUtil.APP_BASE_URL;
import static com.skythinker.gptassistant.util.MyUtil.APP_USER_REFRESH_TOKEN;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skythinker.gptassistant.entity.AllAppInfos;
import com.skythinker.gptassistant.entity.AppInfo;
import com.skythinker.gptassistant.entity.base.BaseEntity;

/**
 * 作者: jiang
 *
 * @date: 2024/11/8
 */
public class AppUpdateUtils {

    public boolean getCheckUpdate(Activity activity){
        HttpUtils.sendGetRequest(APP_BASE_URL+APP_USER_REFRESH_TOKEN, new HttpUtils.HttpCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseEntity<AppInfo> baseEntity = new Gson().fromJson(result, new TypeToken<BaseEntity<AppInfo>>() {
                }.getType());
                if (baseEntity.code != MyUtil.HTTP_CODE_SUCCESSFUL) {
                    if (baseEntity.code == MyUtil.HTTP_CODE_TOKEN_OVERDUE) {

                    } else {
                        MyToastUtil.showError(baseEntity.msg);
                    }
                }else {
                    AppInfo appInfo = baseEntity.getData();
                    if (appInfo == null){
                        MyUtil.MyLog("版本更新信息获取失败");
                    }else {
                        String versionName = appInfo.getVersionName();
                        int versionCode = appInfo.getVersionCode();
                        int verCode = AppInfoUtil.getVerCode(activity);
                        if (versionCode > verCode){
                            // 需要更新
                        }

                    }
                }
            }

            @Override
            public void onFailure (Exception e){
                MyToastUtil.showError(e.getMessage());
                // 去登录
            }
        });

        return false;
    }
}
