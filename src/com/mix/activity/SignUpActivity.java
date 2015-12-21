package com.mix.activity;

import com.example.mixtest.R;
import com.mix.sqlite.DatabaseHelper;
import com.mix.utils.MyApplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends Activity{
	
	private DatabaseHelper dbHelper;
	
	private EditText usernameEditText;
	private EditText passwordEditText;
	private Button registerButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up);
		usernameEditText = (EditText) findViewById(R.id.id_register_username);
		passwordEditText = (EditText) findViewById(R.id.id_register_password);
		registerButton = (Button) findViewById(R.id.id_register_button);
		dbHelper = new DatabaseHelper(this, "UserInformation.db", null, 2);
		//判断用户名是否被占用，占用则使用TextView提示用户更改
		dbHelper.getWritableDatabase();
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		registerButton.setOnClickListener(new OnClickListener() {
			//退出，重新进入后输入的信息不见了
			@Override
			public void onClick(View v) {
				boolean ifRegister = false;
				String nameString = usernameEditText.getText().toString();
				String passwordString = passwordEditText.getText().toString();
				Cursor cursor = db.query("USER", null, null, null, null, null, null);
				if(cursor.moveToFirst()){
					do{
						String name = cursor.getString(cursor.getColumnIndex("username"));
						if(name.equals(nameString)){
							ifRegister = true;
							break;
						}
					}while (cursor.moveToNext());
				}
				cursor.close();
				if(ifRegister){
					Toast.makeText(MyApplication.getContext(),"用户名已被注册，请重新输入！", Toast.LENGTH_LONG).show();
					usernameEditText.setText("");
					passwordEditText.setText("");
				} else {
					ContentValues values = new ContentValues();
					values.put("username", nameString);
					values.put("password", passwordString);
					db.insert("User", null, values);
					values.clear();
					Toast.makeText(MyApplication.getContext(),"注册成功！", Toast.LENGTH_LONG).show();
					db.close();
					finish();
				  }
			/*	ContentValues values = new ContentValues();
				values.put("username", nameString);
				values.put("password", passwordString);
				db.insert("User", null, values);
				values.clear();*/
			}
		});
	}

}
