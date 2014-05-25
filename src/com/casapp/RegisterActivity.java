package com.casapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.EditText;
import android.widget.TextView;

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
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
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
		// TODO Auto-generated method stub
		
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
			mRegisterStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserRegisterTask();
			mAuthTask.execute((Void) null);
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
	
	public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}

			// TODO: register the new account here.
			return true;
		}

		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				finish();
				MainNavActivity.setLoggedin(true);
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}	
	
}


