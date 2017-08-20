package com.legalimpurity.ChattingSampleApplication.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public final class MyPreferences {

    public static final String PREFERENCES_KEY = "ChattingApp+_prefs";

    public static final String PROPERTY_USERNAME = "03fd5e7c19ccad6b8aafa7f29587f67e";

    public static String getString(Context context, String id) {
        final SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        String registrationId = prefs.getString(id, "");
        return registrationId;
    }

    public static void setString(Context context, String key, String val) {
        final SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, val);
        editor.commit();
    }

}
