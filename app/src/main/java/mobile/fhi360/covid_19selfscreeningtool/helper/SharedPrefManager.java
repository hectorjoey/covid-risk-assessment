package mobile.fhi360.covid_19selfscreeningtool.helper;

import android.content.Context;
import android.content.SharedPreferences;

import mobile.fhi360.covid_19selfscreeningtool.model.Users;

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedprefretrofit";

    private static final String KEY_USER_ID = "keyuserid";
    private static final String KEY_FIRST_NAME= "keyfirstname";
    private static final String KEY_USER_EMAIL = "keyuseremail";
    private static final String KEY_USER_GENDER = "keyusergender";

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(Users users) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, users.getId());
        editor.putString(KEY_USER_EMAIL, users.getEmail());
        editor.putString(KEY_FIRST_NAME, users.getFirstname());
//        editor.putString(KEY_USER_GENDER, user.getGender());
        editor.apply();
        return true;
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USER_EMAIL, null) != null)
            return true;
        return false;
    }

    public Users getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Users(
                sharedPreferences.getInt(KEY_USER_ID, 0),
sharedPreferences.getString(KEY_USER_GENDER, null),
//                sharedPreferences.getString(KEY_FIRST_NAME, null),
                sharedPreferences.getString(KEY_USER_EMAIL, null)
//                sharedPreferences.getString(KEY_USER_GENDER, null)
        );
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}
