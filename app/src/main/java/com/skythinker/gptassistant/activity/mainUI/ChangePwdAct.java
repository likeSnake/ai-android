package com.skythinker.gptassistant.activity.mainUI;

import static com.skythinker.gptassistant.util.MyUtil.APP_UPDATE_PWD_URL;
import static com.skythinker.gptassistant.util.MyUtil.APP_USER_TEMPLATE_URL;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skythinker.gptassistant.R;
import com.skythinker.gptassistant.activity.LoginActivity;
import com.skythinker.gptassistant.entity.SignInInfo;
import com.skythinker.gptassistant.entity.base.BaseEntity;
import com.skythinker.gptassistant.util.HttpUtils;
import com.skythinker.gptassistant.util.MyToastUtil;
import com.skythinker.gptassistant.util.MyUtil;
import com.tencent.mmkv.MMKV;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * jcl
 * 2025/4/9
 */
public class ChangePwdAct extends AppCompatActivity {
    private Unbinder unbinder;

    private String newPwd;
    private String reNewPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_blue_04));
        }*/

        unbinder = ButterKnife.bind(this);
        initData();
    }

    public void initData(){

    }

    public void submit(){
        if (!newPwd.equals(reNewPwd)){
            MyToastUtil.showSuccessful("两次密码输入不一致!");
        }
        SignInInfo info = new SignInInfo();
        info.setPhone(phone);
        info.setCheckType(4); // 4--修改密码
        info.setPassword(pwd);// 原始密码
        info.setNewPassword(newPwd);// 新密码

        String json = new Gson().toJson(info);
        HttpUtils.sendPostRequest(APP_UPDATE_PWD_URL, json, new HttpUtils.HttpCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseEntity<Integer> baseEntity = new Gson().fromJson(result, new TypeToken<BaseEntity<Integer>>(){}.getType());
                if (baseEntity.code != MyUtil.HTTP_CODE_SUCCESSFUL){
                    MyToastUtil.showError(baseEntity.msg);
                }else {
                    MyToastUtil.showSuccessful("密码重置成功，请重新登录");
                    ClearInfo(ChangePwdAct.this);
                }
            }

            @Override
            public void onFailure(Exception e) {
                MyToastUtil.showError(e.getMessage());
            }
        });
    }
    public static void ClearInfo(Activity activity) {
        // 清空登录Token
        MMKV.defaultMMKV().encode(MyUtil.ACCESS_TOKEN, "");
        MMKV.defaultMMKV().encode(MyUtil.REFRESH_TOKEN,"");
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
