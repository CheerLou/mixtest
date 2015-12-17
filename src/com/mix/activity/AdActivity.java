package com.mix.activity;

import com.example.mixtest.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class AdActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advertisementshow);
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(AdActivity.this,SignInActivity.class);
		startActivity(intent);
		finish();
	}
}
