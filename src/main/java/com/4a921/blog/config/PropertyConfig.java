package com.knowswift.myspringboot.config;

import com.knowswift.myspringboot.utils.MD5Utils;

/**
 * 全局变量。禁止在项目中使用未经定义的值，避免改动麻烦，以及其他人无法理解业务逻辑
 */
public class PropertyConfig {

    //老师端小程序
    public final static String MINI_ID_ONE = "";
    public final static String MINI_SECRET_ONE = "";

    //家长端小程序
    public final static String MINI_ID_TWO = "";
    public final static String MINI_SECRET_TWO = "";

    //腾讯云短信
    public final static Integer TECENT_MSG_APP_ID = 0;
    public final static String TECENT_MSG_APP_KEY = "";


    //公众号
    public final static String WECHAT_OFFICIAL_ACCOUNTS_APP_ID = "";
    public final static String WECHAT_OFFICIAL_ACCOUNTS_APP_SECRET = "";
    public final static String WECHAT_OFFICIAL_ACCOUNTS_TOKEN = "";//
    public final static String WECHAT_OFFICIAL_ACCOUNTS_ASE_KEY = "";//

    // 微信商户号
    public static final String MCH_ID = ""; //
    public static final String MCH_APP_KEY = "";


    //角色权限
    public final static String ROLE_ADMIN = "ROLE_ADMIN";  // 管理员
    public final static String ROLE_USER = "ROLE_USER";  // 用户1
    public final static String ROLE_CUSTOMER = "ROLE_CUSTOMER";   // 用户2
    public final static String ROLE_VISITOR = "ROLE_VISITOR"; // 游客
    public final static String[] ROLES = {ROLE_ADMIN, ROLE_USER, ROLE_CUSTOMER, ROLE_VISITOR};


    public final static String DEFAULT_PASSWORD = MD5Utils.md5Decode32WithSalt("88888888");


    public final static Integer TRUE = 1;
    public final static Integer FALSE = 0;
}

