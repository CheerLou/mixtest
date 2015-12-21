package com.mix.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.example.mixtest.R;
import com.mix.sqlite.DatabaseHelper;
import com.mix.utils.MyApplication;

import android.support.v4.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ThirdFragment extends Fragment{
	

	private EditText titleEditText;
	private EditText secondtitleEditText;
	private EditText contentEditText;
	private Button addButton;
	private Button saveButton;
	private ImageView picImageView;
	
	private Uri imageUri;

    private String imgName = "";
	
	public static final int PHOTO_WITH_DATA = 1;
	
	private DatabaseHelper dbHelper;
	
	private Bitmap bmp;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =  inflater.inflate(R.layout.add_view, container, false);
		titleEditText = (EditText) view.findViewById(R.id.id_add_title1);//这里为什么要加view.呢= =
		secondtitleEditText = (EditText) view.findViewById(R.id.id_add_title2);
		contentEditText = (EditText) view.findViewById(R.id.id_add_content);
		addButton = (Button) view.findViewById(R.id.id_add_button_addpic);
		saveButton = (Button) view.findViewById(R.id.id_add_button_save);
		picImageView = (ImageView) view.findViewById(R.id.id_add_showpic);
		addButton.setOnClickListener(new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, PHOTO_WITH_DATA);
				
			}
		});
		
		dbHelper = new DatabaseHelper(MyApplication.getContext(), "UserInformation.db", null, 2);
		dbHelper.getWritableDatabase();
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ContentValues values = new ContentValues();
				String title1 = titleEditText.getText().toString();
				String title2 = secondtitleEditText.getText().toString();
				String content = contentEditText.getText().toString();
				values.put("diary_title", title1);
				values.put("diary_secondtitle", title2);
				values.put("diary_content", content);
				
				ByteArrayOutputStream os = new ByteArrayOutputStream();   
				bmp.compress(Bitmap.CompressFormat.PNG, 100, os);    
				values.put("diary_picture", os.toByteArray());  
				db.insert("Diary", null, values);
				values.clear();
				Toast.makeText(MyApplication.getContext(),"上传成功！", Toast.LENGTH_LONG).show();
				db.close();
				
			}
		});

		return view;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == getActivity().RESULT_OK){
		switch(requestCode){
		case PHOTO_WITH_DATA:
			ContentResolver resolver = getActivity().getContentResolver();
			Uri originalUri = data.getData();
			try{
				Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
				imgName = createPhotoFileName();
				savePicture(imgName,photo);
				if(photo != null){
					picImageView.setImageBitmap(photo);
					bmp = photo;
				}
				Toast.makeText(MyApplication.getContext(), "已保存本应用的file文件夹系", Toast.LENGTH_LONG).show();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	private void savePicture(String fileName, Bitmap bitmap) {
		FileOutputStream fos = null;
		try {
			fos = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				if(null != fos){
					fos.close();
					fos = null;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
	}

	private String createPhotoFileName() {
		String fileName = "";
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		fileName = dateFormat.format(date);
		return fileName;
	}
	
}