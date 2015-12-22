package com.mix.activity;

import com.example.mixtest.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DiaryContentActivity extends Activity{
	
	private TextView titleTextView;
	private ImageView pictureImageView;
	private TextView contentTextView;
	
	public static void actionStart(Context context,String title,String content,Bitmap picture){
		Intent intent = new Intent(context,DiaryContentActivity.class);
		intent.putExtra("diary_title", title);
		intent.putExtra("diary_content", content);
		intent.putExtra("diary_picture", picture);
		context.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary_content);
		titleTextView = (TextView) findViewById(R.id.id_content_title);
		pictureImageView = (ImageView) findViewById(R.id.id_content_picture);
		contentTextView = (TextView) findViewById(R.id.id_content_content);
		titleTextView.setText(getIntent().getStringExtra("diary_title"));
		pictureImageView.setImageBitmap((Bitmap) getIntent().getParcelableExtra("diary_picture"));
		contentTextView.setText(getIntent().getStringExtra("diary_content"));
	}
}
