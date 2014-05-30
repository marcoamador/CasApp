package com.casapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import com.google.gson.reflect.TypeToken;

import data.objects.User;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends FragmentActivity {
	
	private UserRegisterTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mUsername;
	private String mPassword;
	private String mName;
	private String mPasswordConf;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private EditText mPasswordConfView;
	private EditText mNameView;
	private EditText mUsernameView;
	private View mRegisterFormView;
	private View mRegisterStatusView;
	private TextView mRegisterStatusMessageView;
	
	private User userToRegister = new User();
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		
		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("CasApp");
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33B5E5")));
		
		mRegisterFormView = findViewById(R.id.register_form);
		mRegisterStatusView = findViewById(R.id.register_status);
		mRegisterStatusMessageView = (TextView) findViewById(R.id.register_status_message);
		
		mEmailView = (EditText) findViewById(R.id.emailRegister);
		mNameView = (EditText) findViewById(R.id.nameRegister);
		mUsernameView = (EditText) findViewById(R.id.usernameRegister);
		mPasswordView = (EditText) findViewById(R.id.passwordRegister);
		mPasswordConfView = (EditText) findViewById(R.id.passwordConfirmation);
		
		findViewById(R.id.register_account_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptRegister();
					}
				});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    
	    MenuItem login = menu.findItem(R.id.action_login);
	    MenuItem logout = menu.findItem(R.id.action_logout);
	    MenuItem settings = menu.findItem(R.id.action_settings);
	    
	    if(MainNavActivity.isLoggedin()){
	    	login.setVisible(false);
	    }else{
	    	logout.setVisible(false);
	    	settings.setVisible(false);
	    	
	    }
	    
	    settings.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
				startActivity(intent);
				return true;
				
			}
		});
	    
	    login.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(loginIntent);
				return true;
			}
		});
	    return super.onCreateOptionsMenu(menu);
	}

	protected void attemptRegister() {
		
		 if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);
		mPasswordConfView.setError(null);
		mUsernameView.setError(null);
		mNameView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		mPasswordConf = mPasswordConfView.getText().toString();
		mName = mNameView.getText().toString();
		mUsername = mUsernameView.getText().toString();
		

		boolean cancel = false;
		View focusView = null;

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(mUsername)) {
			mUsernameView.setError(getString(R.string.error_field_required));
			focusView = mUsernameView;
			cancel = true;
		} else if (mUsername.length() < 4) {
			mUsernameView.setError(getString(R.string.error_username_short));
			focusView = mUsernameView;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(mName)) {
			mNameView.setError(getString(R.string.error_field_required));
			focusView = mNameView;
			cancel = true;
		} else if (mName.length() < 4) {
			mNameView.setError(getString(R.string.error_name_short));
			focusView = mNameView;
			cancel = true;
		}
		
		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}
		
		// Check for a valid password.
		if (TextUtils.isEmpty(mPasswordConf)) {
			mPasswordConfView.setError(getString(R.string.error_field_required));
			focusView = mPasswordConfView;
			cancel = true;
		} else if (mPasswordConf.length() < 4) {
			mPasswordConfView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordConfView;
			cancel = true;
		} else if (!mPasswordConf.equals(mPassword)){
			mPasswordConfView.setError(getString(R.string.error_invalid_confirmation));
			focusView = mPasswordConfView;
			cancel = true;
		}
	
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mRegisterStatusMessageView.setText(R.string.creating_account);
			showProgress(true);
			userToRegister = new User();
        	userToRegister.setName(mName);
        	userToRegister.setUsername(mUsername);
        	userToRegister.setPassword(mPassword);
        	userToRegister.setEmail(mEmail);
        	
			String uriRegister = "user";
			String typeStr = "0";  
			mAuthTask = new UserRegisterTask();
			mAuthTask.execute(typeStr, uriRegister);
		}
	}
	
	
	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mRegisterStatusView.setVisibility(View.VISIBLE);
			mRegisterStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mRegisterStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mRegisterFormView.setVisibility(View.VISIBLE);
			mRegisterFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mRegisterFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mRegisterStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	//---------- UI HANDLER -------------	
		//Provides the user with information in case of failure of the web service requests 
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch(msg.what) {
					case 0:
						//Exception
						break;
					case 1:
						Toast.makeText(getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
						break;
					case 2:
						//Registration success
						Toast.makeText(getApplicationContext(), getString(R.string.registration_successful), Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
				}
			}
		};
	
	public class UserRegisterTask extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
	        Log.d("LOGIN", "ONPREEXECUTE");
		};
		
		@Override
		protected String doInBackground(String... params) {
	    	String typeStr = params[0];
	    	int type = Integer.parseInt(typeStr);
	    	String ret = "";
	    	
	    	Log.d("REGISTRATION", "DOINBACKGROUND - type= " + typeStr);
	    	
			if (isOnline()) {
    			try {
    				switch(type) {
    					//Registration
    					case 0:
    						String uriRegistration = params[1];
    						//Sends the password encrypted in MD5
    						//String passwordMD5 = CasApp.calculateCheckSum(new HashMap<String, Object>(), userToRegister.getPassword());
    						//userToRegister.setPassword(passwordMD5);
    						
    						HashMap<String, Object> headers = new HashMap<String, Object>();
    						
    						Log.d("REGISTRATION", "DOINBACKGROUND - antes put");
    						CasApp.doPut(RegisterActivity.this, uriRegistration, headers, new TypeToken<User>() {}.getType(), userToRegister);
    						Log.d("REGISTRATION", "DOINBACKGROUND - depois put");
    						handler.sendEmptyMessage(2);
    						ret = "ok registration";
    						break;
						default:
							break;
    				}
    			}
    			catch(Exception e) {
					handler.sendEmptyMessage(0);
					return e.getMessage();
    			}
			}
			else {
	    		handler.sendEmptyMessage(1);
	    		return "ok";					
			}
			Log.d("REGISTRATION", "DOINBACKGROUND - ret= " + ret);
			return ret;
	    }
		
		

		protected void onPostExecute(final String result) {
			super.onPostExecute(result);
			mAuthTask = null;
			showProgress(false);
			

			//Checks if there was an exception
			if(!result.startsWith("ok")) {
				Log.d("REGISTRATION", "UPLOADDATA POSTEXECUTE - Exception");
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
			}
			else if(result.contains("registration")) {
				SharedPreferences pref = getSharedPreferences(CasApp.PREFS_NAME,MODE_PRIVATE);
				
				Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				Date dateLoginD = c.getTime();
				SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault());
				String dateLogin= df.format(dateLoginD) + " GMT" ;
				
				//Generates the token
				HashMap<String, Object> tokenParams = new HashMap<String, Object>();
				tokenParams.put("username", mUsername);
				tokenParams.put("dateLogin", dateLogin);
				String authToken = CasApp.calculateCheckSum(tokenParams, mPassword);
			
				Editor edit = pref.edit();
				edit.putString(CasApp.PREF_USERNAME, mUsername);
				edit.putString(CasApp.PREFS_NAME, mName);
		        edit.putString(CasApp.PREF_PASSWORD, mPassword);
		        edit.putString(CasApp.PREF_AUTHTOKEN,  authToken);
		        edit.putString(CasApp.PREF_POINTS, "0");
		        edit.putString(CasApp.PREF_FEEDBACK_POINTS, "0");
				
		        edit.putString(CasApp.PREF_DATELOGIN, dateLogin);
		        edit.commit();
		        Intent myIntent = new Intent(RegisterActivity.this, MainNavActivity.class);
        		myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		startActivity(myIntent);
		        
				MainNavActivity.setLoggedin(true);
		        finish();
			}
			
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}	
	
	
	private boolean isOnline() {
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected())
			return true;
		else return false;
	}	
}


