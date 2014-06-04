package com.casapp;

import data.objects.CommentCategorised;
import android.content.Context;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SeekBarListener implements OnSeekBarChangeListener{
	
	private Context context;  
	private View view;
	public SeekBarListener(Context context, View view){
		this.context=context;
		this.view = view;
	}

	@Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    	// TODO Auto-generated method stub
    	int progress = (seekBar.getProgress() == 0 ? 1 : seekBar.getProgress());
    	switch(seekBar.getId()) {
	    	case R.id.temperatureSlider:
	        	TextView temperatureText = (TextView) view.findViewById(R.id.temperatureValue);
	        	String[] temperature_array = context.getResources().getStringArray(R.array.temperature_array);
	        	int arrayTemperaturePos = CommentCategorised.calculateDiscreteClassification(progress, temperature_array.length, seekBar.getMax());
        		temperatureText.setText(temperature_array[arrayTemperaturePos - 1]);	
	    		break;
	    	case R.id.noiseSlider:
	        	TextView noiseText = (TextView) view.findViewById(R.id.noiseValue);
	        	String[] noise_array = context.getResources().getStringArray(R.array.noise_array);
	        	int arrayNoisePos = CommentCategorised.calculateDiscreteClassification(progress, noise_array.length, seekBar.getMax());
        		noiseText.setText(noise_array[arrayNoisePos - 1]);
	    		break;
	    	case R.id.crowdingSlider:
	        	TextView crowdingText = (TextView) view.findViewById(R.id.crowdingValue);
	        	String[] crowding_array = context.getResources().getStringArray(R.array.crowding_array);
	        	int arrayCrowdingPos = CommentCategorised.calculateDiscreteClassification(progress, crowding_array.length, seekBar.getMax());
        		crowdingText.setText(crowding_array[arrayCrowdingPos - 1]);	
	    		break;	    
	    	case R.id.seatingSlider:
	        	TextView seatingText = (TextView) view.findViewById(R.id.seatingValue);
	        	String[] seating_array = context.getResources().getStringArray(R.array.seating_array);
	        	int arraySeatingPos = CommentCategorised.calculateDiscreteClassification(progress, seating_array.length, seekBar.getMax());
        		seatingText.setText(seating_array[arraySeatingPos - 1]);	
	    		break;
	    	case R.id.cleanSlider:
	        	TextView cleanlinessText = (TextView) view.findViewById(R.id.cleanValue);
	        	String[] cleanliness_array = context.getResources().getStringArray(R.array.cleanliness_array);
	        	int arrayCleanlinessPos = CommentCategorised.calculateDiscreteClassification(progress, cleanliness_array.length, seekBar.getMax());
        		cleanlinessText.setText(cleanliness_array[arrayCleanlinessPos - 1]);	
	    		break;	    		
	    	case R.id.scenerySlider:
	        	TextView sceneryText = (TextView) view.findViewById(R.id.sceneryValue);
	        	String[] scenery_array = context.getResources().getStringArray(R.array.scenery_array);
	        	int arraySceneryPos = CommentCategorised.calculateDiscreteClassification(progress, scenery_array.length, seekBar.getMax());
        		sceneryText.setText(scenery_array[arraySceneryPos - 1]);	
	    		break;
	    	case R.id.securitySlider:
	        	TextView perceived_securityText = (TextView) view.findViewById(R.id.securityValue);
	        	String[] perceived_security_array = context.getResources().getStringArray(R.array.perceived_security_array);
	        	int arrayPerceived_SecurityPos = CommentCategorised.calculateDiscreteClassification(progress, perceived_security_array.length, seekBar.getMax());
        		perceived_securityText.setText(perceived_security_array[arrayPerceived_SecurityPos - 1]);	
	    		break;	   
	    	case R.id.courtesySlider:
	        	TextView courtesyText = (TextView) view.findViewById(R.id.courtesyValue);
	        	String[] courtesy_array = context.getResources().getStringArray(R.array.courtesy_array);
	        	int arrayCourtesyPos = CommentCategorised.calculateDiscreteClassification(progress, courtesy_array.length, seekBar.getMax());
	        	courtesyText.setText(courtesy_array[arrayCourtesyPos - 1]);
	    		break;
	    	case R.id.smoothnessSlider:
	        	TextView smoothnessText = (TextView) view.findViewById(R.id.smoothnessValue);
	        	String[] smoothness_array = context.getResources().getStringArray(R.array.smoothness_array);
	        	int arraySmoothnessPos = CommentCategorised.calculateDiscreteClassification(progress, smoothness_array.length, seekBar.getMax());
	        	smoothnessText.setText(smoothness_array[arraySmoothnessPos - 1]);
	    		break;	    
	    	case R.id.speedSlider:
	        	TextView speedText = (TextView) view.findViewById(R.id.speedValue);
	        	String[] speed_array = context.getResources().getStringArray(R.array.speed_array);
	        	int arraySpeedPos = CommentCategorised.calculateDiscreteClassification(progress, speed_array.length, seekBar.getMax());
	    		speedText.setText(speed_array[arraySpeedPos - 1]);	
	    		break;
	    	case R.id.progressSlider:
	        	TextView arrivedText = (TextView) view.findViewById(R.id.progressValue);
	        	String[] arrived_array = context.getResources().getStringArray(R.array.progress_array);
	        	int arrayArrivedPos = CommentCategorised.calculateDiscreteClassification(progress, arrived_array.length, seekBar.getMax());
	        	arrivedText.setText(arrived_array[arrayArrivedPos - 1]);
	    		break;
	    	case R.id.similarSlider:
	        	TextView similarText = (TextView) view.findViewById(R.id.similarValue);
	        	String[] similar_array = context.getResources().getStringArray(R.array.similar_array);
	        	int arraySimilarPos = CommentCategorised.calculateDiscreteClassification(progress, similar_array.length, seekBar.getMax());
	        	similarText.setText(similar_array[arraySimilarPos - 1]);
	    		break;	    
	    	default:
	    		break;
		
    	}
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    	// TODO Auto-generated method stub
    	int progress = (seekBar.getProgress() == 0 ? 1 : seekBar.getProgress());
    	switch(seekBar.getId()) {
	    	case R.id.temperatureSlider:
	        	seekBar.setSecondaryProgress(progress);
	        	TextView temperatureText = (TextView) view.findViewById(R.id.temperatureValue);
	        	String[] temperature_array = context.getResources().getStringArray(R.array.temperature_array);
	        	int arrayTemperaturePos = CommentCategorised.calculateDiscreteClassification(progress, temperature_array.length, seekBar.getMax());
	        	temperatureText.setText(temperature_array[arrayTemperaturePos - 1]);  		
	    		break;
	    	case R.id.noiseSlider:
	    		seekBar.setSecondaryProgress(progress);
	        	TextView noiseText = (TextView) view.findViewById(R.id.noiseValue);
	        	String[] noise_array = context.getResources().getStringArray(R.array.noise_array);
	        	int arrayNoisePos = CommentCategorised.calculateDiscreteClassification(progress, noise_array.length, seekBar.getMax());
        		noiseText.setText(noise_array[arrayNoisePos - 1]);
	    		break;	    		
	    	case R.id.crowdingSlider:
	        	seekBar.setSecondaryProgress(progress);
	        	TextView crowdingText = (TextView) view.findViewById(R.id.crowdingValue);
	        	String[] crowding_array = context.getResources().getStringArray(R.array.crowding_array);
	        	int arrayCrowdingPos = CommentCategorised.calculateDiscreteClassification(progress, crowding_array.length, seekBar.getMax());
	        	crowdingText.setText(crowding_array[arrayCrowdingPos - 1]);  		
	    		break;    		
	    	case R.id.seatingSlider:
	        	seekBar.setSecondaryProgress(progress);
	        	TextView seatingText = (TextView) view.findViewById(R.id.seatingValue);
	        	String[] seating_array = context.getResources().getStringArray(R.array.seating_array);
	        	int arraySeatingPos = CommentCategorised.calculateDiscreteClassification(progress, seating_array.length, seekBar.getMax());
	        	seatingText.setText(seating_array[arraySeatingPos - 1]);  		
	    		break;
	    	case R.id.cleanSlider:
	        	seekBar.setSecondaryProgress(progress);
	        	TextView cleanlinessText = (TextView) view.findViewById(R.id.cleanValue);
	        	String[] cleanliness_array = context.getResources().getStringArray(R.array.cleanliness_array);
	        	int arrayCleanlinessPos = CommentCategorised.calculateDiscreteClassification(progress, cleanliness_array.length, seekBar.getMax());
	        	cleanlinessText.setText(cleanliness_array[arrayCleanlinessPos - 1]);  		
	    		break;
	    	case R.id.scenerySlider:
	        	seekBar.setSecondaryProgress(progress);
	        	TextView sceneryText = (TextView) view.findViewById(R.id.sceneryValue);
	        	String[] scenery_array = context.getResources().getStringArray(R.array.scenery_array);
	        	int arraySceneryPos = CommentCategorised.calculateDiscreteClassification(progress, scenery_array.length, seekBar.getMax());
	        	sceneryText.setText(scenery_array[arraySceneryPos - 1]);  		
	    		break;
	    	case R.id.securitySlider:
	        	seekBar.setSecondaryProgress(progress);
	        	TextView perceived_securityText = (TextView) view.findViewById(R.id.securityValue);
	        	String[] perceived_security_array = context.getResources().getStringArray(R.array.perceived_security_array);
	        	int arrayPerceived_SecurityPos = CommentCategorised.calculateDiscreteClassification(progress, perceived_security_array.length, seekBar.getMax());
	        	perceived_securityText.setText(perceived_security_array[arrayPerceived_SecurityPos - 1]);  		
	    		break;
	    	case R.id.courtesySlider:
	        	seekBar.setSecondaryProgress(progress);
	        	TextView courtesyText = (TextView) view.findViewById(R.id.courtesyValue);
	        	String[] courtesy_array = view.getResources().getStringArray(R.array.courtesy_array);
	        	int arrayCourtesyPos = CommentCategorised.calculateDiscreteClassification(progress, courtesy_array.length, seekBar.getMax());
	        	courtesyText.setText(courtesy_array[arrayCourtesyPos - 1]);  		
	    		break;
	    	case R.id.smoothnessSlider:
	        	seekBar.setSecondaryProgress(progress);
	        	TextView smoothnessText = (TextView) view.findViewById(R.id.smoothnessValue);
	        	String[] smoothness_array = context.getResources().getStringArray(R.array.smoothness_array);
	        	int arraySmoothnessPos = CommentCategorised.calculateDiscreteClassification(progress, smoothness_array.length, seekBar.getMax());
	        	smoothnessText.setText(smoothness_array[arraySmoothnessPos - 1]);  		
	    		break; 	   
	    	case R.id.speedSlider:
	        	seekBar.setSecondaryProgress(progress);
	        	TextView speedText = (TextView) view.findViewById(R.id.speedValue);
	        	String[] speed_array = context.getResources().getStringArray(R.array.speed_array);
	        	int arraySpeedPos = CommentCategorised.calculateDiscreteClassification(progress, speed_array.length, seekBar.getMax());
	        	speedText.setText(speed_array[arraySpeedPos - 1]);  		
	    		break;
	    	case R.id.progressSlider:
	        	seekBar.setSecondaryProgress(progress);
	        	TextView arrivedText = (TextView) view.findViewById(R.id.progressValue);
	        	String[] arrived_array = context.getResources().getStringArray(R.array.progress_array);
	        	int arrayArrivedPos = CommentCategorised.calculateDiscreteClassification(progress, arrived_array.length, seekBar.getMax());
	        	arrivedText.setText(arrived_array[arrayArrivedPos - 1]);  		
	    		break;
	    	case R.id.similarSlider:
	        	seekBar.setSecondaryProgress(progress);
	        	TextView similarText = (TextView) view.findViewById(R.id.similarValue);
	        	String[] similar_array = context.getResources().getStringArray(R.array.similar_array);
	        	int arraySimilarPos = CommentCategorised.calculateDiscreteClassification(progress, similar_array.length, seekBar.getMax());
	        	similarText.setText(similar_array[arraySimilarPos - 1]);  		
	    		break;	  
	    	default:
	    		break;
		
		}    	
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
    		boolean fromUser) {
    	// TODO Auto-generated method stub
    	progress = (progress == 0 ? 1 : progress);
    	switch(seekBar.getId()) {
	    	case R.id.temperatureSlider:
	        	TextView temperatureText = (TextView) view.findViewById(R.id.temperatureValue);
	        	String[] temperature_array = context.getResources().getStringArray(R.array.temperature_array);
	        	int arrayTemperaturePos = CommentCategorised.calculateDiscreteClassification(progress, temperature_array.length, seekBar.getMax());
	        	temperatureText.setText(temperature_array[arrayTemperaturePos - 1]);
	    		break;
	    	case R.id.noiseSlider:
	        	TextView noiseText = (TextView) view.findViewById(R.id.noiseValue);
	        	String[] noise_array = context.getResources().getStringArray(R.array.noise_array);
	        	int arrayNoisePos = CommentCategorised.calculateDiscreteClassification(progress, noise_array.length, seekBar.getMax());
	        	noiseText.setText(noise_array[arrayNoisePos - 1]);
	    		break;
	    	case R.id.crowdingSlider:
	        	TextView crowdingText = (TextView) view.findViewById(R.id.crowdingValue);
	        	String[] crowding_array = context.getResources().getStringArray(R.array.crowding_array);
	        	int arrayCrowdingPos = CommentCategorised.calculateDiscreteClassification(progress, crowding_array.length, seekBar.getMax());
	        	crowdingText.setText(crowding_array[arrayCrowdingPos - 1]);
	    		break;	    		
	    	case R.id.seatingSlider:
	        	TextView seatingText = (TextView) view.findViewById(R.id.seatingValue);
	        	String[] seating_array = context.getResources().getStringArray(R.array.seating_array);
	        	int arraySeatingPos = CommentCategorised.calculateDiscreteClassification(progress, seating_array.length, seekBar.getMax());
	        	seatingText.setText(seating_array[arraySeatingPos - 1]);
	    		break;
	    	case R.id.cleanSlider:
	        	TextView cleanlinessText = (TextView) view.findViewById(R.id.cleanValue);
	        	String[] cleanliness_array = context.getResources().getStringArray(R.array.cleanliness_array);
	        	int arrayCleanlinessPos = CommentCategorised.calculateDiscreteClassification(progress, cleanliness_array.length, seekBar.getMax());
	        	cleanlinessText.setText(cleanliness_array[arrayCleanlinessPos - 1]);
	    		break;
	    	case R.id.scenerySlider:
	        	TextView sceneryText = (TextView) view.findViewById(R.id.sceneryValue);
	        	String[] scenery_array = context.getResources().getStringArray(R.array.scenery_array);
	        	int arraySceneryPos = CommentCategorised.calculateDiscreteClassification(progress, scenery_array.length, seekBar.getMax());
	        	sceneryText.setText(scenery_array[arraySceneryPos - 1]);
	    		break;
	    	case R.id.securitySlider:
	        	TextView perceived_securityText = (TextView) view.findViewById(R.id.securityValue);
	        	String[] perceived_security_array = context.getResources().getStringArray(R.array.perceived_security_array);
	        	int arrayPerceived_SecurityPos = CommentCategorised.calculateDiscreteClassification(progress, perceived_security_array.length, seekBar.getMax());
	        	perceived_securityText.setText(perceived_security_array[arrayPerceived_SecurityPos - 1]);
	    		break;	  
	    	case R.id.courtesySlider:
	        	TextView courtesyText = (TextView) view.findViewById(R.id.courtesyValue);
	        	String[] courtesy_array = context.getResources().getStringArray(R.array.courtesy_array);
	        	int arrayCourtesyPos = CommentCategorised.calculateDiscreteClassification(progress, courtesy_array.length, seekBar.getMax());
	        	courtesyText.setText(courtesy_array[arrayCourtesyPos - 1]);
	    		break;
	    	case R.id.smoothnessSlider:
	        	TextView smoothnessText = (TextView) view.findViewById(R.id.smoothnessValue);
	        	String[] smoothness_array = context.getResources().getStringArray(R.array.smoothness_array);
	        	int arraySmoothnessPos = CommentCategorised.calculateDiscreteClassification(progress, smoothness_array.length, seekBar.getMax());
	        	smoothnessText.setText(smoothness_array[arraySmoothnessPos - 1]);
	    		break;	
	    	case R.id.speedSlider:
	        	TextView speedText = (TextView) view.findViewById(R.id.speedValue);
	        	String[] speed_array = context.getResources().getStringArray(R.array.speed_array);
	        	int arraySpeedPos = CommentCategorised.calculateDiscreteClassification(progress, speed_array.length, seekBar.getMax());
	        	speedText.setText(speed_array[arraySpeedPos - 1]);
	    		break;
	    	case R.id.progressSlider:
	        	TextView arrivedText = (TextView) view.findViewById(R.id.progressValue);
	        	String[] arrived_array = context.getResources().getStringArray(R.array.progress_array);
	        	int arrayArrivedPos = CommentCategorised.calculateDiscreteClassification(progress, arrived_array.length, seekBar.getMax());
	        	arrivedText.setText(arrived_array[arrayArrivedPos - 1]);
	    		break;
	    	case R.id.similarSlider:
	        	TextView similarText = (TextView) view.findViewById(R.id.similarValue);
	        	String[] similar_array = context.getResources().getStringArray(R.array.similar_array);
	        	int arraySimilarPos = CommentCategorised.calculateDiscreteClassification(progress, similar_array.length, seekBar.getMax());
	        	similarText.setText(similar_array[arraySimilarPos - 1]);
	    		break;	  
	    	default:
	    		break;
    		
    	}
    }

}
