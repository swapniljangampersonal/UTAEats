package utaeats.uta.mav.utaeats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManagement {
    // Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "UTAEats";

    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_PATH = "PATH";

    //key or ID for the user
    public static final String KEY_ID = "ID";

    //role for the user
    public static final String ROLE = "role";

    // Constructor
    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void clearsession(){
        editor.clear();
        editor.commit();
    }

    //Create login session
    public void createUserLoginSession(String name) {
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

//        // Storing email in pref
//        editor.putString(KEY_EMAIL, email);

        // commit changes
        editor.commit();
    }
    public void setProfilePath(String name) {


        // Storing path in pref
        editor.putString(KEY_PATH, name);

//        // Storing email in pref
//        editor.putString(KEY_EMAIL, email);

        // commit changes
        editor.commit();
    }

    //to set the key_id for the user to the session
    public void setKeyId(String ID){

        editor.putString(KEY_ID,ID);
        editor.commit();
    }

    public String getKeyId(){

        return pref.getString(KEY_ID,null);
    }

    //to set the role for the user to the session
    public void setRole(String role){

        editor.putString(ROLE,role);
    }

    public String getRole(){

        return pref.getString(ROLE,null);
    }

    public String getPath()
    {
        return pref.getString(KEY_PATH,null);
    }
    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     */
    public boolean checkLogin() {
        // Check login status
        if (!this.isUserLoggedIn()) {

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginUser.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return true;
        }
        return false;
    }

    public String getData()
    {
        return pref.getString(KEY_NAME,null);
    }
    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        //   user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, RegisterUser.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Staring Login Activity
        _context.startActivity(i);
    }


    // Check for login
    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}

