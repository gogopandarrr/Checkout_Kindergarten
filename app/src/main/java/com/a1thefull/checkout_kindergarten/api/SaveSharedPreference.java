package com.a1thefull.checkout_kindergarten.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Set the Login Status
     * @param context
     * @param loggedIn
     */
    public static void setLoggedIn(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean("LOGIN", loggedIn);
        editor.apply();
    }

/*    public static void logout(Context context) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean("LOGIN", true);
        editor.commit();
    }*/

    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean("LOGIN", false);
    }
}
