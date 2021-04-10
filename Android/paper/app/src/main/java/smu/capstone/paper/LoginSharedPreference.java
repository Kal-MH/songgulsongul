package smu.capstone.paper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LoginSharedPreference {

    static final String PREF_LOGIN_ID = "user_id";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 계정 정보 저장
    public static void setLoginId(Context ctx, String userId) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGIN_ID, userId);
        editor.commit();
    }

    // 저장된 정보 가져오기
    public static String getLoginId(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_LOGIN_ID, "");
    }

    // 로그아웃
    public static void clearLoginId(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}