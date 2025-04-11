package com.skythinker.gptassistant.util;

import com.skythinker.gptassistant.activity.mainUI.HistoryAct;
import com.skythinker.gptassistant.entity.user.User;
import com.skythinker.gptassistant.thisInterFace.AppDatabase;

/**
 * jcl
 * 2025/4/11
 */
public class SignInUtil {
    public static boolean signInSuccess(User user){
        if (user == null){
            return false;
        }
        AppDatabase db = AppDatabase.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.qrCodeInfoDao().deleteAllUsers();
                db.qrCodeInfoDao().insert(user);

            }
        }).start();
        ZmosDao zmosDao = new ZmosDao();
        zmosDao.userSave(user);
        // 保存临时用户，防止不能上架
        PreferencesUtil.setParam(TestInfo.TOURIST, user.getPhone());
        // 保存当前账号信息
        return true;
    }
}
