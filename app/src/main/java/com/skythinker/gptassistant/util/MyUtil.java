package com.skythinker.gptassistant.util;

/**
 * 作者: jiang
 *
 * @date: 2024/10/17
 */
public class MyUtil {
    //public static final String APP_BASE_URL = "http://20.30.1.8:8089";
    //public static final String APP_BASE_URL = "https://ai.ling520.top";
    public static final String APP_BASE_URL = "http://192.168.31.206:8089";
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

    public static void MyLog(Object s){
        System.out.println("----*--------*"+s);

    }


}
