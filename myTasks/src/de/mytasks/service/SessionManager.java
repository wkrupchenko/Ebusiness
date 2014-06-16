package de.mytasks.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.mytasks.activities.LogonActivity;
import de.mytasks.activities.TasklistActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "myTasksPref";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";

	// User name (make variable public to access from outside)
	public static final String KEY_NAME = "username";

	 
	public static final String KEY_PASS = "pass";

	// User Id (make variable public to access from outside)
	public static final String KEY_ID = "userId";

	// Constructor
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	/**
	 * Create login session
	 * */
	// public void createLoginSession(String name, String email){
	public void createLoginSession(String id, String name, String password) {
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		
		// Storing name in pref
		editor.putString(KEY_NAME, name);

		// Storing email in pref
		editor.putString(KEY_PASS, password);

		// Storing id in pref
		editor.putString(KEY_ID, id);

		// commit changes
		editor.commit();
		
		System.out.println(pref.getString(KEY_NAME, null));
		System.out.println(pref.getString(KEY_PASS, null));
		System.out.println(pref.getString(KEY_ID, null));
	}

	/**
	 * Get stored session data
	 * */
	// public HashMap<String, String> getUserDetails(){
	public List<String> getUserDetails() {
		// HashMap<String, String> user = new HashMap<String, String>();
		List<String> user = new ArrayList<String>();
		// user name
		// user.put(KEY_NAME, pref.getString(KEY_NAME, null));
		user.add(pref.getString(KEY_NAME, null));

		// user email id
		user.add(pref.getString(KEY_PASS, null));

		// user id
		user.add(pref.getString(KEY_ID, null));

		// return user
		return user;
	}

	/**
	 * Check login method will check user login status If false it will redirect
	 * user to login page Else won't do anything
	 * */
	public void checkLogin() {
		// Check login status
		if (!this.isLoggedIn()) {
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, LogonActivity.class);
			
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						
			// Closing all the Activities
			//Intent.FLAG_ACTIVITY_NEW_TASK |
			//i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

			// Add new Flag to start new Activity
			//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			_context.startActivity(i);
		}
		
	}

	/**
	 * Clear session details
	 * */
	public void logoutUser() {
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();
		
		/*
		 
		Intent i = new Intent(_context, LogonActivity.class);
		 
		
		
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

	 
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

	 
		_context.startActivity(i);
		
		*/
	}

	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}
	
	public void isLoggedOut() {
		editor.putBoolean(IS_LOGIN, false);
		editor.commit();
	}
}