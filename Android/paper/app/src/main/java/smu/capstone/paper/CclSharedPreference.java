package smu.capstone.paper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CclSharedPreference {
    static final String PREF_CCL = "ccl";


    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 정보 저장
    public static void setPrefCcl(Context ctx, String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    // 저장된 정보 가져오기
    public static boolean getPrefCcl(Context ctx, String key) {
        SharedPreferences prefs = getSharedPreferences(ctx);
        boolean value = prefs.getBoolean(key, false);
        return value;
    }

}
