package com.casapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import com.google.gson.reflect.TypeToken;

import data.objects.UserDefinitions;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends FragmentActivity {

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	
	
	private HashMap<String, Object> headers = new HashMap<String, Object>();	
	//private User userLogged = new User();
	private UserDefinitions userDefinitions = new UserDefinitions();
	private String authToken = null;
	private String dateLogin = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("CasApp");
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33B5E5")));

		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		
		findViewById(R.id.register_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
				startActivity(registerIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

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

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			String uriLogin = "login";
			String typeStr = "0";
			mAuthTask.execute(typeStr, uriLogin, mEmail, mPassword);
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

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
						//Login success
						Toast.makeText(getApplicationContext(), getString(R.string.login_successful), Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
				}
			}
		};   
	

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<String, String, String> {	    
	    @Override
	    protected String doInBackground(String... params) {
	    	String typeStr = params[0];
	    	int type = Integer.parseInt(typeStr);
	    	String ret = "";
	    	
	    	Log.d("LOGIN", "DOINBACKGROUND - type= " + typeStr);
	    	
			if (isOnline()) {
    			try {

    		    	Log.d("LOGIN", "entrou - type= " + typeStr);
    				switch(type) {
    					//LOGIN
 
    					case 0:
    						String uriLogin = params[1];
    						String usernameString = params[2];
    						String passwordString = params[3];
    						
    						Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    						Date dateLoginD = c.getTime();
    						SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault());
    						dateLogin= df.format(dateLoginD) + " GMT" ;
    						
    						//Generates the token
    						HashMap<String, Object> tokenParams = new HashMap<String, Object>();
    						tokenParams.put("username", usernameString);
    						tokenParams.put("dateLogin", dateLogin);
    						authToken = CasApp.calculateCheckSum(tokenParams, passwordString);
    						
    						//Creates the request header
    						headers.put("username", usernameString);
    						headers.put("dateLogin", dateLogin);
    						headers.put("authToken", authToken);   
    						//headers.put("Content-Type:", "application/json");
    						
    						Log.d("LOGIN", "Username: " + usernameString + " DateLogin: " + dateLogin + " AuthToken: " + authToken);
    						userDefinitions = CasApp.doGet(LoginActivity.this, uriLogin, headers, new TypeToken<UserDefinitions>() {}.getType());
    						Log.d("LOGIN", "DOINBACKGROUND - depois get");
    						//
    						handler.sendEmptyMessage(2);
    						//AVISAR QUE FEZ LOGIN
    						ret = "ok login";
    						break;
						default:
							break;
    				}
    			}
    			catch(Exception e) {
    				//ERRO
    				handler.sendEmptyMessage(0);
					userDefinitions = new UserDefinitions();
					return e.getMessage();
    			}
			}
			else {
				//OFFLINE
				Log.d("LOGIN", "ent - type= " + typeStr);
				handler.sendEmptyMessage(1);
	    		userDefinitions = new UserDefinitions();
	    		return "ok";					
			}
			Log.d("LOGIN", "DOINBACKGROUND - ret= " + ret);
			return ret;
	    }
	    
		   @Override
	      protected void onPostExecute(String result) {
				super.onPostExecute(result);
				Log.d("LOGIN", "UPLOADDATA POSTEXECUTE - result= " + result);				
				if(!result.startsWith("ok")) {
					Log.d("LOGIN", "UPLOADDATA POSTEXECUTE - Exception");
					Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
					Intent myIntent = new Intent(LoginActivity.this, LoginActivity.class);
	        		myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        		startActivity(myIntent);
	        		finish();
				}
				else if(result.contains("login")) {
					Log.d("LOGIN", "UPLOADDATA POSTEXECUTE - checkout");
					//Saves the information needed for the request headers in the shared preferences
			        SharedPreferences pref = getSharedPreferences(CasApp.PREFS_NAME,MODE_PRIVATE);
			        Editor edit = pref.edit();
			        edit.putString(CasApp.PREF_DATELOGIN, dateLogin).putString(CasApp.PREF_AUTHTOKEN, authToken);
			        MainNavActivity.setLoggedin(true);
			        
			        edit.putBoolean(CasApp.PREF_CHECKIN, userDefinitions.isCheckedIn()).commit();
			        edit.putString(CasApp.PREF_USERNAME, userDefinitions.getUsername());
			        edit.putString(CasApp.PREFS_NAME, userDefinitions.getName());
			        edit.putString(CasApp.PREF_PASSWORD, mPassword);
			        edit.putString(CasApp.PREF_POINTS, Integer.toString(userDefinitions.getTotalPoints()));
			        edit.putString(CasApp.PREF_FEEDBACK_POINTS, Integer.toString(userDefinitions.getFeedbackPoints()));
			        edit.commit();
			        Log.d("LOGIN", "LOGIN : after putting in prefs: " + authToken);
	        		
	        		Intent myIntent = new Intent(LoginActivity.this, MainNavActivity.class);
	        		myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        		startActivity(myIntent);
	        		finish();
				}
				else{
					mAuthTask = null;
					showProgress(false);
				}
	      }
		
		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
		
		private boolean isOnline() {
			ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			
			if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable()
					&& conMgr.getActiveNetworkInfo().isConnected())
				return true;
			else return false;
		}	
	}
}
