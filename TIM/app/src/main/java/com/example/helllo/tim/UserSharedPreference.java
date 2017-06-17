//package com.example.helllo.tim;
//
//import android.content.SharedPreferences;
////import com.helllo.tim.application.SignalApplication;
//
//
// // @TODO : 싱글톤 sharedPreference 만들게 될 경우 구현
//
//public class UserSharedPreference {
//    private static final String TAG = UserSharedPreference.class.getSimpleName();
//    private static UserSharedPreference mInstance = null;
//    private SharedPreferences mSharedPreferencesInstance = null;
//    private SharedPreferences.Editor editor = null;
//
//    protected UserSharedPreference() {
//    }
//
//    public static UserSharedPreference getInstance() {
//        if (mInstance == null) {
//            mInstance = new UserSharedPreference();
//        }
//
//        mInstance.mSharedPreferencesInstance = SignalApplication.getInstance().getApplicationContext().getSharedPreferences("user_shared_preferences", 0);
//        mInstance.editor = mInstance.mSharedPreferencesInstance.edit();
//        return mInstance;
//    }
//
//    public int getId() {
//        return getInt("id");
//    }
//
//    public void setId(int id) {
//        putInt("id", id);
//    }
//
//    private int getInt(String key) {
//        return mSharedPreferencesInstance.getInt(key, 0);
//    }
//
//    private String getString(String key) {
//        return mSharedPreferencesInstance.getString(key, "");
//    }
//
//    private Boolean getBoolean(String key) {
//        return mSharedPreferencesInstance.getBoolean(key, false);
//    }
//
//    private void putInt(String key, int value) {
//        editor.putInt(key, value);
//        editor.commit();
//    }
//
//    private void putString(String key, String value) {
//        editor.putString(key, value);
//        editor.commit();
//    }
//
//    private void putBoolean(String key, Boolean value) {
//        editor.putBoolean(key, value);
//        editor.commit();
//    }
//
//}