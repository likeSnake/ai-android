package com.skythinker.gptassistant.util;

import static com.skythinker.gptassistant.util.MyUtil.TOURIST;

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
        PreferencesUtil.setParam(TOURIST, user.getPhone_number());
        return true;
    }
}
