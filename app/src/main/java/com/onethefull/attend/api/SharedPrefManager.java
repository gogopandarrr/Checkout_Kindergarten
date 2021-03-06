package com.onethefull.attend.api;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String TAG = SharedPrefManager.class.getSimpleName();
    private static SharedPrefManager mInstance;
    private static SharedPreferences mSharedPrefs;
    private static SharedPreferences.Editor mEditor;
    private static final String SHARED_PREF_FILE_NAME = "appData";


    public static SharedPrefManager getInstance(Context context){
        if (mInstance == null)
            mInstance = new SharedPrefManager(context);

        return mInstance;

    }

    public SharedPrefManager(Context context){
        mSharedPrefs = context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPrefs.edit();
    }


    public void initialize(Context context){
        mSharedPrefs = context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPrefs.edit();
        mEditor.clear();
        mEditor.commit();

    }

    public void saveLoginData(String id){
        mEditor.putBoolean(Base.KEY_SAVE_LOGIN_DATA, true);
        mEditor.putString(Base.KEY_SAVE_LOGIN_ID, id);
        mEditor.commit();

    }

    public boolean getLoginBoolean(){
        return mSharedPrefs.getBoolean(Base.KEY_SAVE_LOGIN_DATA, false);
    }

    public String getLoginId(){
        String id = "";
        return mSharedPrefs.getString(Base.KEY_SAVE_LOGIN_ID, id);
    }


}
