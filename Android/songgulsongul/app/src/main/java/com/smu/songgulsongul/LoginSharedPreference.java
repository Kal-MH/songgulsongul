package com.smu.songgulsongul;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LoginSharedPreference {

    static final String PREF_LOGIN_ID = "login_id";
    static final String PREF_USER_ID = "user_id";
    static final String PREF_TOKEN = "device_token";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 계정 정보 저장
    public static void setLogin(Context ctx, int userId, String login_id) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(PREF_USER_ID, userId);
        editor.putString(PREF_LOGIN_ID, login_id);
        editor.commit();
    }

    public static void setLoginId(Context ctx, String login_id) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGIN_ID, login_id);
        editor.commit();
    }

    public static void setUserId(Context ctx, int userId) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(PREF_USER_ID, userId);
        editor.commit();
    }

    public static void setToken(Context ctx, String token) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_TOKEN, token);
        editor.commit();
    }


    public static void changeLoginId(Context ctx, String login_id) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_LOGIN_ID);
        editor.putString(PREF_LOGIN_ID, login_id);

        editor.commit();
    }

    // 저장된 정보 가져오기
    public static String getLoginId(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_LOGIN_ID, "");
    }

    public static int getUserId(Context ctx) {
        return getSharedPreferences(ctx).getInt(PREF_USER_ID, -1);
    }

    public static String getToken(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_TOKEN, "");
    }

    // 로그아웃
    public static void clearLogin(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_USER_ID);
        editor.remove(PREF_LOGIN_ID);
        editor.commit();
    }
}