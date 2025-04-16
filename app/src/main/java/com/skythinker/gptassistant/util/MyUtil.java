package com.skythinker.gptassistant.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.skythinker.gptassistant.R;
import com.skythinker.gptassistant.activity.RegisterActivity;

/**
 * 作者: jiang
 *
 * @date: 2024/10/17
 */
public class MyUtil {
    //public static final String APP_BASE_URL = "http://20.30.1.8:8089";
    //public static final String APP_BASE_URL = "https://ai.ling520.top";
    public static final String APP_BASE_URL = "http://192.168.31.206:8089";
    public static final boolean OPEN_APP_DEBUG = true;
    public static final String APP_LOGIN_URL = "/user/loginPassword";
    public static final String APP_USER_INFO_URL = "/user/getUserInfo";
    public static final String APP_USER_REGISTER_URL = "/user/addUser";
    public static final String APP_USER_REFRESH_TOKEN = "/user/refreshAccessToken";
    public static final String APP_USER_GET_TEMPLATE = "/user/getAllTemplate";
    public static final String APP_USER_GET_TEMPLATE_BY_ID = "/user/getTemplateByUserId";
    public static final String APP_APPINFO_URL = "/appInfo/getAppInfo";
    public static final String APP_USER_TEMPLATE_URL = "/user/createTemplate";
    public static final String APP_UPDATE_PWD_URL = "/user/updatePwd";
    public static final int HTTP_CODE_SUCCESSFUL = 1;
    public static final int HTTP_CODE_ERROR = -1;
    public static final int HTTP_CODE_TOKEN_OVERDUE = -3;
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String IS_LOGIN = "isLogin"; // 保存是否登录
    public static final String TOURIST = "TOURIST"; // 用户手机号
    public static final String API_MSG_SEND = "http://106.ihuyi.com/webservice/sms.php?method=Submit&account="+MyUtil.MSG_API_ID+"&password="+MyUtil.MSG_API_KEY;
    public static final String MSG_API_ID = "C87297789";
    public static final String MSG_API_KEY = "352a4dc676729706ebd7c1eaecfaaa8a";
    public static final String TEMP_TYPE1 = "请你充当一名论文编辑专家，在论文评审的角度去修改论文摘要部分，使其更加流畅，优美。下面是具体要求：\n" +
            "\n" +
            "能让读者快速获得文章的要点或精髓，让文章引人入胜；能让读者了解全文中的重要信息、分析和论点；帮助读者记住论文的要点\n" +
            "字数限制在300字以下\n" +
            "请你在摘要中明确指出您的模型和方法的创新点，强调您的贡献。\n" +
            "用简洁、明了的语言描述您的方法和结果，以便评审更容易理解论文";

    public static final String TEMP_TYPE2 = "你是一位资深写作专家，具有丰富的写作技巧与内容策划经验。你能帮助我完成各类文字创作任务，包括但不限于：\n" +
            "\n" +
            "学术写作（如论文、研究报告）\n" +
            "\n" +
            "营销文案（如广告、社交媒体文案）\n" +
            "\n" +
            "创意写作（如小说、散文、故事构思）\n" +
            "\n" +
            "职场写作（如简历、求职信、项目汇报）\n" +
            "请在创作时：\n" +
            "\n" +
            "分析我的目标读者和写作目的；\n" +
            "\n" +
            "明确内容结构，语言简洁有力；\n" +
            "\n" +
            "必要时提供多个写作风格和语气选择；\n" +
            "\n" +
            "主动提出优化建议或内容拓展方向。" ;
    public static void MyLog(Object s){
        System.out.println("----*--------*"+s);

    }

    private static AlertDialog dialog;
    public static void initLoading(Activity activity){
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_load, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    public static void showLoading(){
        dialog.show();
    }
    public static void hideLoading(){
        dialog.dismiss();
    }

}
