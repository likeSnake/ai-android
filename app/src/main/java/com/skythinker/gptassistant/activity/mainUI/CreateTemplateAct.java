package com.skythinker.gptassistant.activity.mainUI;

import static com.skythinker.gptassistant.util.MyUtil.APP_LOGIN_URL;
import static com.skythinker.gptassistant.util.MyUtil.APP_USER_TEMPLATE_URL;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skythinker.gptassistant.R;
import com.skythinker.gptassistant.entity.base.BaseEntity;
import com.skythinker.gptassistant.entity.base.MyToken;
import com.skythinker.gptassistant.entity.copyWriter.TextTemplate;
import com.skythinker.gptassistant.util.HttpUtils;
import com.skythinker.gptassistant.util.MyToastUtil;
import com.skythinker.gptassistant.util.MyUtil;
import com.tencent.mmkv.MMKV;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CreateTemplateAct extends AppCompatActivity {
    private Unbinder unbinder;
    @BindView(R.id.team_userName)
    EditText team_userName;
    @BindView(R.id.template_hint)
    EditText template_hint;
    @BindView(R.id.template_prompting)
    EditText template_prompting;
    @BindView(R.id.teamSubmit)
    Button teamSubmit;
    @BindView(R.id.layoutTitle)
    TextView layoutTitle;
    private TextTemplate textTemplate;

    private int templateType = 0; // 0新建 1修改

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_template);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_blue_04));
        }

        unbinder = ButterKnife.bind(this);
        initData();
    }

    public void initData(){
        textTemplate = (TextTemplate)getIntent().getSerializableExtra("data");
        if (textTemplate != null){
            // 编辑已有模板
            templateType = 1;
            layoutTitle.setText(getString(R.string.editor_create_act_hint14));
            teamSubmit.setText(getString(R.string.editor_create_act_hint15));
            initTextTemplateView(textTemplate);
        }
    }

    public void initTextTemplateView(TextTemplate textTemplate){
        String title = textTemplate.getTitle();
        String content = textTemplate.getContent();
        String prompt = textTemplate.getPrompt();
        int permissionLevel = textTemplate.getPermissionLevel();

        team_userName.setText(title);
        template_hint.setText(content);
        template_prompting.setText(prompt);

    }
    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.teamSubmit})
    public void myListener(View view){
        switch (view.getId()){
            case R.id.teamSubmit:
                submit();
                break;
        }
    }

    public void submit(){
        String tempName = team_userName.getText().toString();
        String tempHint = template_hint.getText().toString();
        String tempPrompt = template_prompting.getText().toString();

        if (tempName.isEmpty()){
            MyToastUtil.showError("请输入模板名");
            return;
        }
        if (tempHint.isEmpty()){
            MyToastUtil.showError("请输入模板描述");
            return;
        }
        if (tempPrompt.isEmpty()){
            MyToastUtil.showError("请完成AI设定");
            return;
        }
        if (textTemplate == null)textTemplate = new TextTemplate();
        textTemplate.setTitle(tempName);
        textTemplate.setContent(tempHint);
        textTemplate.setPrompt(tempPrompt);

        String json = new Gson().toJson(textTemplate);
        HttpUtils.sendPostRequest(APP_USER_TEMPLATE_URL, json, new HttpUtils.HttpCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseEntity<Integer> baseEntity = new Gson().fromJson(result, new TypeToken<BaseEntity<Integer>>(){}.getType());
                if (baseEntity.code != MyUtil.HTTP_CODE_SUCCESSFUL){
                    MyToastUtil.showError(baseEntity.msg);
                }else {
                    MyToastUtil.showSuccessful(templateType == 0?"模板创建成功!":"模板已修改");
                    finish();
                }
            }

            @Override
            public void onFailure(Exception e) {
                MyToastUtil.showError(e.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
