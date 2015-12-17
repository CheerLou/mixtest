package com.mix.activity;

import com.example.mixtest.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageButton;

public class StartActivity extends Activity{
	
	private ImageButton adButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advertisement);
		adButton = (ImageButton) findViewById(R.id.button_ad);
		AlphaAnimation animation = new AlphaAnimation(0.3f,1.0f);
		animation.setDuration(3000);
		adButton.startAnimation(animation);
		adButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(StartActivity.this,AdActivity.class);
				startActivity(intent);
				finish();
				
			}
		});
		animation.setAnimationListener(new AnimationImpl());
	}
	
	private class AnimationImpl implements AnimationListener{

		@Override
		public void onAnimationEnd(Animation arg0) {
			skip();
			
		}

		@Override
		public void onAnimationRepeat(Animation arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStart(Animation arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private void skip(){
		Intent intent = new Intent(StartActivity.this,SignInActivity.class);
		startActivity(intent);
		finish();
	}
}
