package com.casapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.google.gson.reflect.TypeToken;

import android.app.Dialog;
import android.content.Context;
import data.objects.CommentCategorised;
import data.objects.CommentWritten;
import data.objects.JourneyPath;
import data.objects.Stop;
import data.objects.TemplateComment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebStorage.Origin;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class CommentFragment extends Fragment implements LocationListener{
	
		ListView checkInResultList;
		CheckInResultsAdapter checkInResultAdapter;
		ArrayList<JourneyPath> checkInResults = new ArrayList<JourneyPath>();
		View view;
		String checkInOrigin = "";
		String checkInDestination = "";
		public static int selectedRow = -1;
		String written = "";
		private String latitude = "";
		private String longitude = "";	
		private CommentWritten comment;
		private CommentCategorised commentCat;
		Dialog dialog;
	    // newInstance constructor for creating fragment with arguments
	    public static CommentFragment newInstance() {
	    	CommentFragment commentFragment = new CommentFragment();
	        Bundle args = new Bundle();
	        commentFragment.setArguments(args);
	        return commentFragment;
	    }

	    // Store instance variables based on arguments passed
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	    }

	    // Inflate the view for the fragment based on layout XML
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    	SharedPreferences pref = getActivity().getSharedPreferences(CasApp.PREFS_NAME,Context.MODE_PRIVATE);
	        latitude = pref.getString(CasApp.PREF_LATITUDE, "0.0");
	        longitude = pref.getString(CasApp.PREF_LONGITUDE, "0.0");
	        checkInOrigin = pref.getString(CasApp.PREF_CHECKIN_ORIGIN, "");
	        checkInDestination = pref.getString(CasApp.PREF_CHECKIN_DESTINATION, "");
	        
	        
	        view = inflater.inflate(R.layout.comment_fragment, container, false);
	        
	        LinearLayout notLoggedInFrame = (LinearLayout) view.findViewById(R.id.notLoggedInComment);
	        final LinearLayout notCheckedInFrame = (LinearLayout) view.findViewById(R.id.notCheckedInComment);
	        final LinearLayout noCheckInResultsFrame = (LinearLayout) view.findViewById(R.id.noCheckInResultsFrame);
	        final LinearLayout checkInResultsFrame = (LinearLayout) view.findViewById(R.id.checkinResultsFrame);
	        final LinearLayout commentFrame = (LinearLayout) view.findViewById(R.id.commentMenuFrame);
	        
	        final Button commentLoginButton = (Button) view.findViewById(R.id.CommentLoginButton);
	        final Button checkInSwitch = (Button) view.findViewById(R.id.addCheckinManualSwitch);
	        final Button goBackButton = (Button) view.findViewById(R.id.goBackButtonCheckIn);
	        final Button checkInButton = (Button) view.findViewById(R.id.checkInButton);
	        
	        Button atmosphere = (Button) view.findViewById(R.id.atmosphereCommentButton);
	        Button vehicle = (Button) view.findViewById(R.id.vehicleCommentButton);
	        Button driver = (Button) view.findViewById(R.id.driverCommentButton);
	        Button writtenCommentSubmit = (Button) view.findViewById(R.id.writtenCommentSubmit);
	        final TextView writtenCommentField = (TextView) view.findViewById(R.id.writtenCommentField);
	        TextView commentOrigin = (TextView) view.findViewById(R.id.commentMenuOrigin);
			TextView commentDestination = (TextView) view.findViewById(R.id.commentMenuDestination);
	        
			///////////////////////////////////////////
			//
	        //ATMOSPHERE
	        atmosphere.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog = new Dialog(getActivity());
					dialog.setContentView(R.layout.atmosphere_dialog);
					dialog.setTitle("Atmosphere");
					dialog.getWindow().setLayout(700, 800);
					
					Button atmosphereDismiss = (Button) dialog.getWindow().getDecorView().findViewById(R.id.AtmosphereCommentDismissButton);
					atmosphereDismiss.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							
						}
					});
					
					//temperature
					final SeekBar temperatureSlider = (SeekBar) dialog.getWindow().getDecorView().findViewById(R.id.temperatureSlider);
					TextView temperatureValue = (TextView) dialog.getWindow().getDecorView().findViewById(R.id.temperatureValue);
					Button temperatureSubmit = (Button) dialog.getWindow().getDecorView().findViewById(R.id.temperatureSubmitButton);
					temperatureSlider.setOnSeekBarChangeListener(new SeekBarListener(getActivity(), dialog.getWindow().getDecorView()));
					int temperatureProgress = temperatureSlider.getProgress();
					String[] temperature_array = getResources().getStringArray(R.array.temperature_array);
			    	int arrayTemperaturePos = (temperatureProgress * temperature_array.length) / temperatureSlider.getMax();
			    	temperatureValue.setText(temperature_array[arrayTemperaturePos]);
					
			    	temperatureSubmit.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							int commentProgress = (temperatureSlider.getProgress() == 0 ? 1 : temperatureSlider.getProgress());
			            	commentCat = new CommentCategorised();
			            	commentCat.setClassification(commentProgress);
			            	commentCat.setDate();
			            	String[] temperature_array = getResources().getStringArray(R.array.temperature_array);
			            	commentCat.setDiscreteClassification(CommentCategorised.calculateDiscreteClassification(commentProgress, temperature_array.length, temperatureSlider.getMax()));
			            	commentCat.setIdTypeCategorisedComment(TemplateComment.Temperature.ordinal() + 1);
			            	
			            	SharedPreferences pref = getActivity().getSharedPreferences(CasApp.PREFS_NAME,Context.MODE_PRIVATE);
			                latitude = pref.getString(CasApp.PREF_LATITUDE, "0.0");
			                longitude = pref.getString(CasApp.PREF_LONGITUDE, "0.0");
			            	commentCat.setLatitude(latitude);
			            	commentCat.setLongitude(longitude);
			            	
			            	
			       			//Upload Data
			    			CategoryCommentTask commentTask = new CategoryCommentTask();
			    			commentTask.execute(commentCat);
							
						}
					});
			    	
			    	
					
					//noise
					final SeekBar noiseSlider = (SeekBar) dialog.getWindow().getDecorView().findViewById(R.id.noiseSlider);
					TextView noiseValue = (TextView) dialog.getWindow().getDecorView().findViewById(R.id.noiseValue);
					
					Button noiseSubmit = (Button) dialog.getWindow().getDecorView().findViewById(R.id.noiseSubmitButton);
					noiseSlider.setOnSeekBarChangeListener(new SeekBarListener(getActivity(), dialog.getWindow().getDecorView()));
					int noiseProgress = noiseSlider.getProgress();
					String[] noise_array = getResources().getStringArray(R.array.noise_array);
			    	int arrayNoisePos = (noiseProgress * noise_array.length) / noiseSlider.getMax();
			    	noiseValue.setText(noise_array[arrayNoisePos]);
					
			    	noiseSubmit.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							int commentProgress = (noiseSlider.getProgress() == 0 ? 1 : noiseSlider.getProgress());
			            	commentCat = new CommentCategorised();
			            	commentCat.setClassification(commentProgress);
			            	commentCat.setDate();
			            	String[] noise_array = getResources().getStringArray(R.array.noise_array);
			            	commentCat.setDiscreteClassification(CommentCategorised.calculateDiscreteClassification(commentProgress, noise_array.length, noiseSlider.getMax()));
			            	commentCat.setIdTypeCategorisedComment(TemplateComment.Noise.ordinal() + 1);
			            	
			            	SharedPreferences pref = getActivity().getSharedPreferences(CasApp.PREFS_NAME,Context.MODE_PRIVATE);
			                latitude = pref.getString(CasApp.PREF_LATITUDE, "0.0");
			                longitude = pref.getString(CasApp.PREF_LONGITUDE, "0.0");
			            	commentCat.setLatitude(latitude);
			            	commentCat.setLongitude(longitude);
			            	
			            	
			       			//Upload Data
			    			CategoryCommentTask commentTask = new CategoryCommentTask();
			    			commentTask.execute(commentCat);
							
						}
					});
			    	
			    	
			    	//Crowding
			    	
			    	final SeekBar crowdingSlider = (SeekBar) dialog.getWindow().getDecorView().findViewById(R.id.crowdingSlider);
					TextView crowdingValue = (TextView) dialog.getWindow().getDecorView().findViewById(R.id.crowdingValue);
					
					Button crowdingSubmit = (Button) dialog.getWindow().getDecorView().findViewById(R.id.crowdingSubmitButton);
					crowdingSlider.setOnSeekBarChangeListener(new SeekBarListener(getActivity(), dialog.getWindow().getDecorView()));
					int crowdingProgress = crowdingSlider.getProgress();
					String[] crowding_array = getResources().getStringArray(R.array.crowding_array);
			    	int arrayCrowdingPos = (crowdingProgress * crowding_array.length) / crowdingSlider.getMax();
			    	crowdingValue.setText(crowding_array[arrayCrowdingPos]);
					
			    	crowdingSubmit.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							int commentProgress = (crowdingSlider.getProgress() == 0 ? 1 : crowdingSlider.getProgress());
			            	commentCat = new CommentCategorised();
			            	commentCat.setClassification(commentProgress);
			            	commentCat.setDate();
			            	String[] crowding_array = getResources().getStringArray(R.array.crowding_array);
			            	commentCat.setDiscreteClassification(CommentCategorised.calculateDiscreteClassification(commentProgress, crowding_array.length, crowdingSlider.getMax()));
			            	commentCat.setIdTypeCategorisedComment(TemplateComment.Crowding.ordinal() + 1);
			            	
			            	SharedPreferences pref = getActivity().getSharedPreferences(CasApp.PREFS_NAME,Context.MODE_PRIVATE);
			                latitude = pref.getString(CasApp.PREF_LATITUDE, "0.0");
			                longitude = pref.getString(CasApp.PREF_LONGITUDE, "0.0");
			            	commentCat.setLatitude(latitude);
			            	commentCat.setLongitude(longitude);
			            	
			            	
			       			//Upload Data
			    			CategoryCommentTask commentTask = new CategoryCommentTask();
			    			commentTask.execute(commentCat);
							
						}
					});
			    	
			    	//Seating
			    	
			    	final SeekBar seatingSlider = (SeekBar) dialog.getWindow().getDecorView().findViewById(R.id.seatingSlider);
					TextView seatingValue = (TextView) dialog.getWindow().getDecorView().findViewById(R.id.seatingValue);
					
					Button seatingSubmit = (Button) dialog.getWindow().getDecorView().findViewById(R.id.seatingSubmitButton);
					seatingSlider.setOnSeekBarChangeListener(new SeekBarListener(getActivity(), dialog.getWindow().getDecorView()));
					int seatingProgress = seatingSlider.getProgress();
					String[] seating_array = getResources().getStringArray(R.array.seating_array);
			    	int arraySeatingPos = (seatingProgress * seating_array.length) / seatingSlider.getMax();
			    	seatingValue.setText(seating_array[arraySeatingPos]);
					
			    	seatingSubmit.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							int commentProgress = (seatingSlider.getProgress() == 0 ? 1 : seatingSlider.getProgress());
			            	commentCat = new CommentCategorised();
			            	commentCat.setClassification(commentProgress);
			            	commentCat.setDate();
			            	String[] seating_array = getResources().getStringArray(R.array.seating_array);
			            	commentCat.setDiscreteClassification(CommentCategorised.calculateDiscreteClassification(commentProgress, seating_array.length, seatingSlider.getMax()));
			            	commentCat.setIdTypeCategorisedComment(TemplateComment.Seating.ordinal() + 1);
			            	
			            	SharedPreferences pref = getActivity().getSharedPreferences(CasApp.PREFS_NAME,Context.MODE_PRIVATE);
			                latitude = pref.getString(CasApp.PREF_LATITUDE, "0.0");
			                longitude = pref.getString(CasApp.PREF_LONGITUDE, "0.0");
			            	commentCat.setLatitude(latitude);
			            	commentCat.setLongitude(longitude);
			            	
			            	
			       			//Upload Data
			    			CategoryCommentTask commentTask = new CategoryCommentTask();
			    			commentTask.execute(commentCat);
							
						}
					});
			    	
			    	//Clean
			    	

			    	final SeekBar cleanSlider = (SeekBar) dialog.getWindow().getDecorView().findViewById(R.id.cleanSlider);
					TextView cleanValue = (TextView) dialog.getWindow().getDecorView().findViewById(R.id.cleanValue);
					
					Button cleanSubmit = (Button) dialog.getWindow().getDecorView().findViewById(R.id.cleanSubmitButton);
					cleanSlider.setOnSeekBarChangeListener(new SeekBarListener(getActivity(), dialog.getWindow().getDecorView()));
					int cleanProgress = cleanSlider.getProgress();
					String[] clean_array = getResources().getStringArray(R.array.cleanliness_array);
			    	int arrayCleanPos = (cleanProgress * clean_array.length) / cleanSlider.getMax();
			    	cleanValue.setText(clean_array[arrayCleanPos]);
					
			    	cleanSubmit.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							int commentProgress = (cleanSlider.getProgress() == 0 ? 1 : cleanSlider.getProgress());
			            	commentCat = new CommentCategorised();
			            	commentCat.setClassification(commentProgress);
			            	commentCat.setDate();
			            	String[] clean_array = getResources().getStringArray(R.array.cleanliness_array);
			            	commentCat.setDiscreteClassification(CommentCategorised.calculateDiscreteClassification(commentProgress, clean_array.length, cleanSlider.getMax()));
			            	commentCat.setIdTypeCategorisedComment(TemplateComment.Cleanliness.ordinal() + 1);
			            	
			            	SharedPreferences pref = getActivity().getSharedPreferences(CasApp.PREFS_NAME,Context.MODE_PRIVATE);
			                latitude = pref.getString(CasApp.PREF_LATITUDE, "0.0");
			                longitude = pref.getString(CasApp.PREF_LONGITUDE, "0.0");
			            	commentCat.setLatitude(latitude);
			            	commentCat.setLongitude(longitude);
			            	
			            	
			       			//Upload Data
			    			CategoryCommentTask commentTask = new CategoryCommentTask();
			    			commentTask.execute(commentCat);
							
						}
					});
			    	
			    	//Scenery

			    	final SeekBar scenerySlider = (SeekBar) dialog.getWindow().getDecorView().findViewById(R.id.scenerySlider);
					TextView sceneryValue = (TextView) dialog.getWindow().getDecorView().findViewById(R.id.sceneryValue);
					
					Button scenerySubmit = (Button) dialog.getWindow().getDecorView().findViewById(R.id.scenerySubmitButton);
					scenerySlider.setOnSeekBarChangeListener(new SeekBarListener(getActivity(), dialog.getWindow().getDecorView()));
					int sceneryProgress = scenerySlider.getProgress();
					String[] scenery_array = getResources().getStringArray(R.array.scenery_array);
			    	int arraySceneryPos = (sceneryProgress * scenery_array.length) / scenerySlider.getMax();
			    	sceneryValue.setText(scenery_array[arraySceneryPos]);
					
			    	scenerySubmit.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							int commentProgress = (scenerySlider.getProgress() == 0 ? 1 : scenerySlider.getProgress());
			            	commentCat = new CommentCategorised();
			            	commentCat.setClassification(commentProgress);
			            	commentCat.setDate();
			            	String[] scenery_array = getResources().getStringArray(R.array.scenery_array);
			            	commentCat.setDiscreteClassification(CommentCategorised.calculateDiscreteClassification(commentProgress, scenery_array.length, scenerySlider.getMax()));
			            	commentCat.setIdTypeCategorisedComment(TemplateComment.Scenery.ordinal() + 1);
			            	
			            	SharedPreferences pref = getActivity().getSharedPreferences(CasApp.PREFS_NAME,Context.MODE_PRIVATE);
			                latitude = pref.getString(CasApp.PREF_LATITUDE, "0.0");
			                longitude = pref.getString(CasApp.PREF_LONGITUDE, "0.0");
			            	commentCat.setLatitude(latitude);
			            	commentCat.setLongitude(longitude);
			            	
			            	
			       			//Upload Data
			    			CategoryCommentTask commentTask = new CategoryCommentTask();
			    			commentTask.execute(commentCat);
							
						}
					});
			    	
			    	//Security

			    	final SeekBar securitySlider = (SeekBar) dialog.getWindow().getDecorView().findViewById(R.id.securitySlider);
					TextView securityValue = (TextView) dialog.getWindow().getDecorView().findViewById(R.id.securityValue);
					
					Button securitySubmit = (Button) dialog.getWindow().getDecorView().findViewById(R.id.securitySubmitButton);
					securitySlider.setOnSeekBarChangeListener(new SeekBarListener(getActivity(), dialog.getWindow().getDecorView()));
					int securityProgress = securitySlider.getProgress();
					String[] security_array = getResources().getStringArray(R.array.perceived_security_array);
			    	int arraySecurityPos = (securityProgress * security_array.length) / securitySlider.getMax();
			    	securityValue.setText(security_array[arraySecurityPos]);
					
			    	securitySubmit.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							int commentProgress = (securitySlider.getProgress() == 0 ? 1 : securitySlider.getProgress());
			            	commentCat = new CommentCategorised();
			            	commentCat.setClassification(commentProgress);
			            	commentCat.setDate();
			            	String[] security_array = getResources().getStringArray(R.array.perceived_security_array);
			            	commentCat.setDiscreteClassification(CommentCategorised.calculateDiscreteClassification(commentProgress, security_array.length, securitySlider.getMax()));
			            	commentCat.setIdTypeCategorisedComment(TemplateComment.Perceived_Security.ordinal() + 1);
			            	
			            	SharedPreferences pref = getActivity().getSharedPreferences(CasApp.PREFS_NAME,Context.MODE_PRIVATE);
			                latitude = pref.getString(CasApp.PREF_LATITUDE, "0.0");
			                longitude = pref.getString(CasApp.PREF_LONGITUDE, "0.0");
			            	commentCat.setLatitude(latitude);
			            	commentCat.setLongitude(longitude);
			            	
			            	
			       			//Upload Data
			    			CategoryCommentTask commentTask = new CategoryCommentTask();
			    			commentTask.execute(commentCat);
							
						}
					});
					dialog.show();
					
				}
			});
			///////////////////////////////////////////7
			//
	        //VEHICLE
	        vehicle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog = new Dialog(getActivity());
					dialog.setContentView(R.layout.vehicle_dialog);
					dialog.setTitle("Vehicle");
					dialog.getWindow().setLayout(700, 800);
					
					Button vehicleDismiss = (Button) dialog.getWindow().getDecorView().findViewById(R.id.VehicleCommentDismissButton);
					vehicleDismiss.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							
						}
					});
					dialog.show();
					
					//Speed
					final SeekBar speedSlider = (SeekBar) dialog.getWindow().getDecorView().findViewById(R.id.speedSlider);
					TextView speedValue = (TextView) dialog.getWindow().getDecorView().findViewById(R.id.speedValue);
					Button speedSubmit = (Button) dialog.getWindow().getDecorView().findViewById(R.id.speedSubmitButton);
					speedSlider.setOnSeekBarChangeListener(new SeekBarListener(getActivity(), dialog.getWindow().getDecorView()));
					int speedProgress = speedSlider.getProgress();
					String[] speed_array = getResources().getStringArray(R.array.speed_array);
			    	int arraySpeedPos = (speedProgress * speed_array.length) / speedSlider.getMax();
			    	speedValue.setText(speed_array[arraySpeedPos]);
					
			    	speedSubmit.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							int commentProgress = (speedSlider.getProgress() == 0 ? 1 : speedSlider.getProgress());
			            	commentCat = new CommentCategorised();
			            	commentCat.setClassification(commentProgress);
			            	commentCat.setDate();
			            	String[] speed_array = getResources().getStringArray(R.array.speed_array);
			            	commentCat.setDiscreteClassification(CommentCategorised.calculateDiscreteClassification(commentProgress, speed_array.length, speedSlider.getMax()));
			            	commentCat.setIdTypeCategorisedComment(TemplateComment.Speed.ordinal() + 1);
			            	
			            	SharedPreferences pref = getActivity().getSharedPreferences(CasApp.PREFS_NAME,Context.MODE_PRIVATE);
			                latitude = pref.getString(CasApp.PREF_LATITUDE, "0.0");
			                longitude = pref.getString(CasApp.PREF_LONGITUDE, "0.0");
			            	commentCat.setLatitude(latitude);
			            	commentCat.setLongitude(longitude);
			            	
			            	
			       			//Upload Data
			    			CategoryCommentTask commentTask = new CategoryCommentTask();
			    			commentTask.execute(commentCat);
							
						}
					});
					
					
					//Delay
			    	
			    	final SeekBar progressSlider = (SeekBar) dialog.getWindow().getDecorView().findViewById(R.id.progressSlider);
					TextView progressValue = (TextView) dialog.getWindow().getDecorView().findViewById(R.id.progressValue);
					Button progressSubmit = (Button) dialog.getWindow().getDecorView().findViewById(R.id.progressSubmitButton);
					progressSlider.setOnSeekBarChangeListener(new SeekBarListener(getActivity(), dialog.getWindow().getDecorView()));
					int progressProgress = progressSlider.getProgress();
					String[] progress_array = getResources().getStringArray(R.array.progress_array);
			    	int arrayProgressPos = (progressProgress * progress_array.length) / progressSlider.getMax();
			    	progressValue.setText(progress_array[arrayProgressPos]);
					
			    	progressSubmit.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							int commentProgress = (progressSlider.getProgress() == 0 ? 1 : progressSlider.getProgress());
			            	commentCat = new CommentCategorised();
			            	commentCat.setClassification(commentProgress);
			            	commentCat.setDate();
			            	String[] progress_array = getResources().getStringArray(R.array.progress_array);
			            	commentCat.setDiscreteClassification(CommentCategorised.calculateDiscreteClassification(commentProgress, progress_array.length, progressSlider.getMax()));
			            	commentCat.setIdTypeCategorisedComment(TemplateComment.Progress.ordinal() + 1);
			            	
			            	SharedPreferences pref = getActivity().getSharedPreferences(CasApp.PREFS_NAME,Context.MODE_PRIVATE);
			                latitude = pref.getString(CasApp.PREF_LATITUDE, "0.0");
			                longitude = pref.getString(CasApp.PREF_LONGITUDE, "0.0");
			            	commentCat.setLatitude(latitude);
			            	commentCat.setLongitude(longitude);
			            	
			            	
			       			//Upload Data
			    			CategoryCommentTask commentTask = new CategoryCommentTask();
			    			commentTask.execute(commentCat);
							
						}
					});
					
					//Similar
			    	final SeekBar similarSlider = (SeekBar) dialog.getWindow().getDecorView().findViewById(R.id.similarSlider);
					TextView similarValue = (TextView) dialog.getWindow().getDecorView().findViewById(R.id.similarValue);
					Button similarSubmit = (Button) dialog.getWindow().getDecorView().findViewById(R.id.similarSubmitButton);
					similarSlider.setOnSeekBarChangeListener(new SeekBarListener(getActivity(), dialog.getWindow().getDecorView()));
					int similarProgress = similarSlider.getProgress();
					String[] similar_array = getResources().getStringArray(R.array.similar_array);
			    	int arraySimilarPos = (similarProgress * similar_array.length) / similarSlider.getMax();
			    	similarValue.setText(similar_array[arraySimilarPos]);
					
			    	similarSubmit.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							int commentProgress = (similarSlider.getProgress() == 0 ? 1 : similarSlider.getProgress());
			            	commentCat = new CommentCategorised();
			            	commentCat.setClassification(commentProgress);
			            	commentCat.setDate();
			            	String[] similar_array = getResources().getStringArray(R.array.similar_array);
			            	commentCat.setDiscreteClassification(CommentCategorised.calculateDiscreteClassification(commentProgress, similar_array.length, similarSlider.getMax()));
			            	commentCat.setIdTypeCategorisedComment(TemplateComment.Similar.ordinal() + 1);
			            	
			            	SharedPreferences pref = getActivity().getSharedPreferences(CasApp.PREFS_NAME,Context.MODE_PRIVATE);
			                latitude = pref.getString(CasApp.PREF_LATITUDE, "0.0");
			                longitude = pref.getString(CasApp.PREF_LONGITUDE, "0.0");
			            	commentCat.setLatitude(latitude);
			            	commentCat.setLongitude(longitude);
			            	
			            	
			       			//Upload Data
			    			CategoryCommentTask commentTask = new CategoryCommentTask();
			    			commentTask.execute(commentCat);
							
						}
					});
					
					
					
					
					
				}
			});
	        ///////////////////////////////////////////7
	        //
	        //DRIVER
	        driver.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog = new Dialog(getActivity());
					dialog.setContentView(R.layout.driver_dialog);
					dialog.setTitle("Driver");
					dialog.getWindow().setLayout(700, 800);
					
					Button driverDismiss = (Button) dialog.getWindow().getDecorView().findViewById(R.id.DriverCommentDismissButton);
					driverDismiss.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							
						}
					});
					
					dialog.show();
					
					//Courtesy
					final SeekBar courtesySlider = (SeekBar) dialog.getWindow().getDecorView().findViewById(R.id.courtesySlider);
					TextView courtesyValue = (TextView) dialog.getWindow().getDecorView().findViewById(R.id.courtesyValue);
					Button courtesySubmit = (Button) dialog.getWindow().getDecorView().findViewById(R.id.courtesySubmitButton);
					courtesySlider.setOnSeekBarChangeListener(new SeekBarListener(getActivity(), dialog.getWindow().getDecorView()));
					int courtesyProgress = courtesySlider.getProgress();
					String[] courtesy_array = getResources().getStringArray(R.array.courtesy_array);
			    	int arrayCourtesyPos = (courtesyProgress * courtesy_array.length) / courtesySlider.getMax();
			    	courtesyValue.setText(courtesy_array[arrayCourtesyPos]);
					
			    	courtesySubmit.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							int commentProgress = (courtesySlider.getProgress() == 0 ? 1 : courtesySlider.getProgress());
			            	commentCat = new CommentCategorised();
			            	commentCat.setClassification(commentProgress);
			            	commentCat.setDate();
			            	String[] courtesy_array = getResources().getStringArray(R.array.courtesy_array);
			            	commentCat.setDiscreteClassification(CommentCategorised.calculateDiscreteClassification(commentProgress, courtesy_array.length, courtesySlider.getMax()));
			            	commentCat.setIdTypeCategorisedComment(TemplateComment.Courtesy.ordinal() + 1);
			            	
			            	SharedPreferences pref = getActivity().getSharedPreferences(CasApp.PREFS_NAME,Context.MODE_PRIVATE);
			                latitude = pref.getString(CasApp.PREF_LATITUDE, "0.0");
			                longitude = pref.getString(CasApp.PREF_LONGITUDE, "0.0");
			            	commentCat.setLatitude(latitude);
			            	commentCat.setLongitude(longitude);
			            	
			            	
			       			//Upload Data
			    			CategoryCommentTask commentTask = new CategoryCommentTask();
			    			commentTask.execute(commentCat);
							
						}
					});
					
					//Smoothness
					final SeekBar smoothnessSlider = (SeekBar) dialog.getWindow().getDecorView().findViewById(R.id.smoothnessSlider);
					TextView smoothnessValue = (TextView) dialog.getWindow().getDecorView().findViewById(R.id.smoothnessValue);
					Button smoothnessSubmit = (Button) dialog.getWindow().getDecorView().findViewById(R.id.smoothnessSubmitButton);
					smoothnessSlider.setOnSeekBarChangeListener(new SeekBarListener(getActivity(), dialog.getWindow().getDecorView()));
					int smoothnessProgress = smoothnessSlider.getProgress();
					String[] smoothness_array = getResources().getStringArray(R.array.smoothness_array);
			    	int arraySmoothnessPos = (smoothnessProgress * smoothness_array.length) / smoothnessSlider.getMax();
			    	smoothnessValue.setText(smoothness_array[arraySmoothnessPos]);
					
			    	smoothnessSubmit.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							int commentProgress = (smoothnessSlider.getProgress() == 0 ? 1 : smoothnessSlider.getProgress());
			            	commentCat = new CommentCategorised();
			            	commentCat.setClassification(commentProgress);
			            	commentCat.setDate();
			            	String[] smoothness_array = getResources().getStringArray(R.array.smoothness_array);
			            	commentCat.setDiscreteClassification(CommentCategorised.calculateDiscreteClassification(commentProgress, smoothness_array.length, smoothnessSlider.getMax()));
			            	commentCat.setIdTypeCategorisedComment(TemplateComment.Smoothness.ordinal() + 1);
			            	
			            	SharedPreferences pref = getActivity().getSharedPreferences(CasApp.PREFS_NAME,Context.MODE_PRIVATE);
			                latitude = pref.getString(CasApp.PREF_LATITUDE, "0.0");
			                longitude = pref.getString(CasApp.PREF_LONGITUDE, "0.0");
			            	commentCat.setLatitude(latitude);
			            	commentCat.setLongitude(longitude);
			            	
			            	
			       			//Upload Data
			    			CategoryCommentTask commentTask = new CategoryCommentTask();
			    			commentTask.execute(commentCat);
							
						}
					});
					
				}
			});
	        
	        
	        writtenCommentSubmit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {	        		
	            	String comment_string = writtenCommentField.getText().toString();
	            	comment = new CommentWritten();
	            	comment.setType(0);
	            	comment.setComment(comment_string);
	            	comment.setDate();
	            	
	            	comment.setLatitude(latitude);
	            	comment.setLongitude(longitude);
	            	
	            	if(comment_string.length() >= 3 && comment_string.length() <= 140) {
	           			//Upload Data
	        			CommentTask commentTask = new CommentTask();
	        			commentTask.execute(comment);
	            	}else if(comment_string.length() < 3){
	            		Toast.makeText(getActivity(), "The comment is too short. Please enter between 2-140 characters.", Toast.LENGTH_SHORT).show();
	            	}else if(comment_string.length() > 140){
	            		Toast.makeText(getActivity(), "The comment is too long. Please enter between 2-140 characters.", Toast.LENGTH_SHORT).show();
	            	}
					
				}
			});
	        
	        
	        final AutoCompleteTextView originCheckIn = (AutoCompleteTextView) view.findViewById(R.id.addCheckinManualOrigin);
	        final AutoCompleteTextView destinationCheckIn = (AutoCompleteTextView) view.findViewById(R.id.addCheckinManualDestination);
	        
	        final StopAutoCompleteAdapter originStopAdapter = new StopAutoCompleteAdapter(this.getActivity(), R.layout.autocomplete_list_item, R.id.item);
	        final StopAutoCompleteAdapter destinationStopAdapter = new StopAutoCompleteAdapter(getActivity(), R.layout.autocomplete_list_item, R.id.item);
	        originCheckIn.setAdapter(originStopAdapter);
	        destinationCheckIn.setAdapter(destinationStopAdapter);
	        
	        notCheckedInFrame.setVisibility(View.GONE);
	        commentFrame.setVisibility(View.GONE);
	        notLoggedInFrame.setVisibility(View.GONE);
	        noCheckInResultsFrame.setVisibility(View.GONE);
	        checkInResultsFrame.setVisibility(View.GONE);
	        
	        checkInResultList = (ListView) view.findViewById(R.id.checkInResultslist);
	        checkInResultAdapter = new CheckInResultsAdapter(getActivity(), R.layout.custom_listview_item_search_results, checkInResults);
	        checkInResultList.setAdapter(checkInResultAdapter);
	        
	        checkInResultList.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position, long arg) {
					checkInResultAdapter.setSelectedRow(position);
					checkInResultList.invalidateViews();
					view.refreshDrawableState();
				}
			});
	        
	        
	        
	        if(!MainNavActivity.isLoggedin()){
	        	notLoggedInFrame.setVisibility(View.VISIBLE);
	        	notCheckedInFrame.setVisibility(View.GONE);
	        	commentFrame.setVisibility(View.GONE);
	        }else if(MainNavActivity.isLoggedin() && !MainNavActivity.isCheckedIn()){
	        	notLoggedInFrame.setVisibility(View.GONE);
	        	notCheckedInFrame.setVisibility(View.VISIBLE);
	        	commentFrame.setVisibility(View.GONE);
	        }else if(MainNavActivity.isCheckedIn()){
	        	if(!checkInOrigin.equals(""))
	        		commentOrigin.setText(checkInOrigin);
	        	if(!checkInDestination.equals(""))
	        		commentDestination.setText(checkInDestination);
	        	commentFrame.setVisibility(View.VISIBLE);
	        	notLoggedInFrame.setVisibility(View.GONE);
	        	notCheckedInFrame.setVisibility(View.GONE);
	        }
	        
	        
	        goBackButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					noCheckInResultsFrame.setVisibility(View.GONE);
					notCheckedInFrame.setVisibility(View.VISIBLE);
					
				}
			});
	        
	        
	        commentLoginButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getActivity(), LoginActivity.class);
					startActivity(i);
					return;
				}
			});
	        
	        checkInSwitch.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String origin = originCheckIn.getText().toString();
					String destination = destinationCheckIn.getText().toString();
					originCheckIn.setText(destination);
					destinationCheckIn.setText(origin);
				}
			});
	        
	        checkInButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(selectedRow >= 0){
						String uriCheckIn = "journey/checkin";
	        			String typeStr = "1";
	        			SearchCheckInTask checkinTask = new SearchCheckInTask();
	        			checkinTask.execute(typeStr, uriCheckIn);
					}
					else {
	        			Toast.makeText(getActivity(), R.string.no_checkin_options_selected, Toast.LENGTH_SHORT).show();
	        		}
					
				}
			});
	        
	        Button checkInSearchButton = (Button) view.findViewById(R.id.AddManualCheckinButton);
	        checkInSearchButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					boolean readyStart = false;
					boolean readyEnd = false;
					
					if(originCheckIn.getText().toString().length() > 0 && destinationCheckIn.getText().toString().length() > 0 && originStopAdapter != null && destinationStopAdapter != null) {
						
						String originString = originCheckIn.getText().toString();
						String destinationString = destinationCheckIn.getText().toString();

						Stop origin = new Stop();
						origin.setName(originString);
						Stop destination = new Stop();
						destination.setName(destinationString);

						if(originStopAdapter.getItem(origin) == null && originStopAdapter.getCount() > 0) {
							origin = new Stop(originStopAdapter.getItem(0));
							originCheckIn.setText(origin.toString());
							readyStart = true;
						}
						else if(originStopAdapter.getItem(origin) != null) {
							origin = new Stop(originStopAdapter.getItem(origin));
							originCheckIn.setText(origin.toString());
							readyStart = true;
						}

						if(destinationStopAdapter.getItem(destination) == null && destinationStopAdapter.getCount() > 0) {
							destinationCheckIn.setText(destinationStopAdapter.getItem(0).toString());
							destination = new Stop(destinationStopAdapter.getItem(0));
							readyEnd = true;
						}
						else if(destinationStopAdapter.getItem(destination) != null) {
							destination = new Stop(destinationStopAdapter.getItem(destination));
							destinationCheckIn.setText(destination.toString());
							readyEnd = true;
						}
						
					}else if(originCheckIn.getText().toString().length() > 0 && originStopAdapter != null) {
						String originString = originCheckIn.getText().toString();
						
						Stop st = new Stop();
						st.setName(originString);
						
						if(originStopAdapter.getItem(st) == null && originStopAdapter.getCount() > 0) {
							st = new Stop(originStopAdapter.getItem(0));
							originCheckIn.setText(st.toString());
							readyStart = true;
						}
						else if(originStopAdapter.getItem(st) != null) {
							st = new Stop(originStopAdapter.getItem(st));
							originCheckIn.setText(st.toString());
							readyStart = true;
						}
					}else if(destinationCheckIn.getText().toString().length() > 0 && destinationStopAdapter != null) {
						String destinationString = destinationCheckIn.getText().toString();
						
						Stop st = new Stop();
						st.setName(destinationString);
						
						if(originStopAdapter.getItem(st) == null && destinationStopAdapter.getCount() > 0) {
							st = new Stop(destinationStopAdapter.getItem(0));
							destinationCheckIn.setText(st.toString());
							readyEnd = true;
						}
						else if(destinationStopAdapter.getItem(st) != null) {
							st = new Stop(destinationStopAdapter.getItem(st));
							destinationCheckIn.setText(st.toString());
							readyEnd = true;
						}
					}
					
					String uriCheckIn="";
					String typeStr = "0";
					if(readyStart == true && readyEnd == true) {
						uriCheckIn = "journey/search?origin=" + originCheckIn.getText().toString().replace(" ", "%20") + "&destination=" + destinationCheckIn.getText().toString().replace(" ", "%20");
					}else if(readyStart == true){
						uriCheckIn = "journey/lines?stop=" + originCheckIn.getText().toString().replace(" ", "%20");
					}else if(readyEnd == true){
						uriCheckIn = "journey/lines?stop=" + destinationCheckIn.getText().toString().replace(" ", "%20");
					}else{
						noCheckInResultsFrame.setVisibility(View.VISIBLE);
						notCheckedInFrame.setVisibility(View.GONE);
						return;
					}

					notCheckedInFrame.setVisibility(View.GONE);
					checkInResultsFrame.setVisibility(View.VISIBLE);

					SearchCheckInTask searchCheckIn = new SearchCheckInTask();
					searchCheckIn.execute(typeStr, uriCheckIn);	
					Log.d("PLANJOURNEY", "pathsResults " + MainNavActivity.getFeedResults().size());
				}
			});
	        return view;
	    }
	    
	    //---------- UI HANDLER -------------	
	    //Provides the user with information in case of failure of the web service requests 
	    final Handler handler = new Handler() {
	    	public void handleMessage(Message msg) {
	    		//dialog.dismiss();
	    		//0 - does not do anything
	    		//1 - no internet connection
	    		//2 - ok
	    		switch(msg.what) {
	    		case 0:
	    			break;
	    		case 1:
	    			Toast.makeText(getActivity(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
	    			break;
	    		case 2:
					Toast.makeText(getActivity(), getString(R.string.successful_written_comment), Toast.LENGTH_SHORT).show();
					break;
	    		default:
	    			break;
	    		}
	    	}
	    };	
	    
	     
	  		
	    //---------- UPLOAD DATA -------------
	    class SearchCheckInTask extends AsyncTask<String, String, String> {

	    	@Override
	    	protected void onPreExecute() {
	    		super.onPreExecute();
	    		LinearLayout linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgressCheckIn);
	    		linlaHeaderProgress.setVisibility(View.VISIBLE);
	    	}


	    	@Override
	    	protected String doInBackground(String... params) {
	    		String typeStr = params[0];
	    		int type = Integer.parseInt(typeStr);
	    		String ret = "";

	    		Log.d("CHECKIN", "DOINBACKGROUND - type= " + typeStr);

	    		if (isOnline()) {
	    			try {
	    				switch(type) {
	    				case 0:
	    					String uriGetLines= params[1];
	    					checkInResults = (ArrayList<JourneyPath>) CasApp.doGet(getActivity(), uriGetLines, MainNavActivity.headers, new TypeToken<ArrayList<JourneyPath>>() {}.getType());
	    					ret = "ok getlines";


	    					break; 	
	    					
	    				case 1:
	    					String uriCheckIn = params[1];
	    					Log.d("CHECKIN", "CHECKIN BUTTON: selectedLine=" + selectedRow);
    		    			
    		    			JourneyPath jp = (JourneyPath)(checkInResultAdapter.getItem(selectedRow));
		            		Calendar c = Calendar.getInstance();
		            		Date dateCheckIn = c.getTime();
		            		jp.setStartTime(dateCheckIn);
		            		checkInOrigin = jp.getOrigin().getName();
		            		checkInDestination = jp.getDestination().getName();
		            		CasApp.doPut(getActivity(), uriCheckIn, MainNavActivity.headers, new TypeToken<JourneyPath>() {}.getType(), jp);
		    				ret = "ok checkin";
    						handler.sendEmptyMessage(3);
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
	    		Log.d("CHECKIN", "DOINBACKGROUND - ret= " + ret);
	    		return ret;
	    	}

	    	@Override
	    	protected void onPostExecute(String result) {
	    		super.onPostExecute(result);
	    		Log.d("CHECKIN", "POSTEXECUTE - result= " + result);	

	    		//Checks if there was an exception
	    		if(!result.startsWith("ok")) {
	    			Log.d("CHECKIN", "POSTEXECUTE - Exception");
	    			Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
	    		}
	    		else if(result.contains("getlines")) {
	    			Log.d("CHECKIN", "POSTEXECUTE - planjourney");
	    			
	    		    final LinearLayout noCheckInResultsFrame = (LinearLayout) view.findViewById(R.id.noCheckInResultsFrame);
	    		    final LinearLayout checkInResultsFrame = (LinearLayout) view.findViewById(R.id.checkinResultsFrame);
	    			
	    			LinearLayout linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgressCheckIn);
		    		linlaHeaderProgress.setVisibility(View.VISIBLE);
	    			if(checkInResults.size() > 0){
	    				linlaHeaderProgress.setVisibility(View.GONE);
	    				checkInResultAdapter.updateList(checkInResults);
	    			}else{
	    				checkInResultsFrame.setVisibility(View.GONE);
	    				linlaHeaderProgress.setVisibility(View.GONE);
	    				noCheckInResultsFrame.setVisibility(View.VISIBLE);

	    			}

	    		}
	    		else if(result.contains("checkin")) {
					SharedPreferences pref = getActivity().getSharedPreferences(CasApp.PREFS_NAME, Context.MODE_PRIVATE);
					pref.edit().putBoolean(CasApp.PREF_CHECKIN, true).commit();
					pref.edit().putString(CasApp.PREF_CHECKIN_ORIGIN, checkInOrigin).commit();
					pref.edit().putString(CasApp.PREF_CHECKIN_DESTINATION, checkInDestination).commit();
					MainNavActivity.setCheckedIn(true);
					
					LinearLayout linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgressCheckIn);
		    		final LinearLayout checkInResultsFrame = (LinearLayout) view.findViewById(R.id.checkinResultsFrame);
					LinearLayout commentMenu = (LinearLayout) view.findViewById(R.id.commentMenuFrame);
					TextView commentOrigin = (TextView) view.findViewById(R.id.commentMenuOrigin);
					TextView commentDestination = (TextView) view.findViewById(R.id.commentMenuDestination);
					
					
					linlaHeaderProgress.setVisibility(View.GONE);
					checkInResultsFrame.setVisibility(View.GONE);
					
					commentOrigin.setText(checkInOrigin);
					commentDestination.setText(checkInDestination);
					
					commentMenu.setVisibility(View.VISIBLE);
					
					getActivity().invalidateOptionsMenu();
					/*Intent intent = new Intent(getActivity(),MainNavActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
					startActivity(intent);
					getActivity().finish();*/
				}
	    	}
	    }	
	    
	    class CommentTask extends AsyncTask<CommentWritten, String, String> {

		    @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        Toast.makeText(getActivity(), "Sending your comment...", Toast.LENGTH_SHORT).show();
		    }

		    
		    @Override
		    protected String doInBackground(CommentWritten... params) {
		    	CommentWritten comment = params[0];
		    	String uri = "comment/writtencomment";
		    	
				if (isOnline()) {
					try {            	
						CasApp.doPut(getActivity(), uri, MainNavActivity.headers, new TypeToken<CommentWritten>() {}.getType(), comment);
					}
	    			catch(Exception e) {
						handler.sendEmptyMessage(0);
						return e.getMessage();
	    			}
	    			handler.sendEmptyMessage(2);
	    			
				}
				else {
		    		handler.sendEmptyMessage(1);
		    		return "ok";					
				}
				return "ok";
		    }
		    
			   @Override
		      protected void onPostExecute(String result) {
				   super.onPostExecute(result);
				   //Checks if there was an exception
				   if(!result.equals("ok")) {
					   Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
				   }
		      }
		}	
	    
	    
	    
	  //---------- UPLOAD DATA -------------
		class CategoryCommentTask extends AsyncTask<CommentCategorised, String, String> {

		    @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        Toast.makeText(getActivity(), "Sending your comment...", Toast.LENGTH_SHORT).show();
		    }

		    
		    @Override
		    protected String doInBackground(CommentCategorised... params) {
		    	CommentCategorised comment = params[0];
		    	String uri = "comment/categorisedcomment";
		    	
				if (isOnline()) {
					try {            	
						CasApp.doPut(getActivity(), uri, MainNavActivity.headers, new TypeToken<CommentCategorised>() {}.getType(), comment);
					}
	    			catch(Exception e) {
						handler.sendEmptyMessage(0);
						return e.getMessage();
	    			}
	    			handler.sendEmptyMessage(2);
	    			
				}
				else {
		    		handler.sendEmptyMessage(1);
		    		return "ok";					
				}
				return "ok";
		    }
		    
			   @Override
		      protected void onPostExecute(String result) {
				   super.onPostExecute(result);
				   //Checks if there was an exception
				   if(!result.equals("ok")) {
					   Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
				   }
		      }
		}	
		
		protected boolean isBetterLocation(Location location, Location currentBestLocation) {
		    if (currentBestLocation == null) {
		        // A new location is always better than no location
		        return true;
		    }

		    // Check whether the new location fix is newer or older
		    long timeDelta = location.getTime() - currentBestLocation.getTime();
		    boolean isSignificantlyNewer = timeDelta > MainNavActivity.TWO_MINUTES;
		    boolean isSignificantlyOlder = timeDelta < - MainNavActivity.TWO_MINUTES;
		    boolean isNewer = timeDelta > 0;

		    // If it's been more than two minutes since the current location, use the new location
		    // because the user has likely moved
		    if (isSignificantlyNewer) {
		        return true;
		    // If the new location is more than two minutes older, it must be worse
		    } else if (isSignificantlyOlder) {
		        return false;
		    }

		    // Check whether the new location fix is more or less accurate
		    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		    boolean isLessAccurate = accuracyDelta > 0;
		    boolean isMoreAccurate = accuracyDelta < 0;
		    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		    // Check if the old and new location are from the same provider
		    boolean isFromSameProvider = isSameProvider(location.getProvider(),
		            currentBestLocation.getProvider());

		    // Determine location quality using a combination of timeliness and accuracy
		    if (isMoreAccurate) {
		        return true;
		    } else if (isNewer && !isLessAccurate) {
		        return true;
		    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
		        return true;
		    }
		    return false;
		}
		
		/** Checks whether two providers are the same */
		private boolean isSameProvider(String provider1, String provider2) {
		    if (provider1 == null) {
		      return provider2 == null;
		    }
		    return provider1.equals(provider2);
		}
	    
	    
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			if(MainNavActivity.lastLocation == null) {
				MainNavActivity.lastLocation = location;
				SharedPreferences pref = getActivity().getSharedPreferences(CasApp.PREFS_NAME,Context.MODE_PRIVATE);
				pref.edit().putString(CasApp.PREF_LATITUDE, Double.toString(location.getLatitude())).
				putString(CasApp.PREF_LONGITUDE, Double.toString(location.getLongitude())).commit();	
				return;
			}
			else if(isBetterLocation(location, MainNavActivity.lastLocation)) {
				MainNavActivity.lastLocation = location;
				SharedPreferences pref = getActivity().getSharedPreferences(CasApp.PREFS_NAME,Context.MODE_PRIVATE);
				pref.edit().putString(CasApp.PREF_LATITUDE, Double.toString(location.getLatitude())).
				putString(CasApp.PREF_LONGITUDE, Double.toString(location.getLongitude())).commit();	
				return;			
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	  		
	  	//---------- IS ONLINE -------------
		//Checks if there is internet connection
		private boolean isOnline() {		
			ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			
			if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable()
					&& conMgr.getActiveNetworkInfo().isConnected())
				return true;
			else return false;
		}	
}