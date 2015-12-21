package com.mix.activity;

import com.example.mixtest.R;
import com.mix.sqlite.DatabaseHelper;
import com.mix.utils.MyApplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends Activity{
	
	private DatabaseHelper dbHelper;
	
	private EditText usernameEditText;
	private EditText passwordEditText;
	private Button signbButton;
	private CheckBox rememberCheckBox;
	private CheckBox autoCheckBox;
	private TextView registerTextView;
	
	protected SharedPreferences pref;
	protected SharedPreferences.Editor editor;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);
		//初始化控件
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		usernameEditText = (EditText) findViewById(R.id.id_sign_username);
		passwordEditText = (EditText) findViewById(R.id.id_sign_password);
		signbButton = (Button) findViewById(R.id.id_sign_in);
		rememberCheckBox = (CheckBox) findViewById(R.id.id_sign_remember_password);
		autoCheckBox = (CheckBox) findViewById(R.id.id_sign_auto);
		registerTextView = (TextView) findViewById(R.id.id_sign_tv);
		//记住密码的判断
		boolean isRemember = pref.getBoolean("remember_password", false);
		boolean isAuto = pref.getBoolean("auto", false);
		if(isAuto){
			//启动主界面
			Intent intent = new Intent(SignInActivity.this,MainActivity.class);//登录成功跳转到主界面
			startActivity(intent);
			finish();//销毁当前的登录界面，这样在主界面点击back不会回到登录界面
			Toast.makeText(MyApplication.getContext(), "登录成功！", Toast.LENGTH_SHORT).show();
		}
		if(isRemember){
			String username = pref.getString("username", "");
			String password = pref.getString("password", "");
			usernameEditText.setText(username);
			passwordEditText.setText(password);
			rememberCheckBox.setChecked(true);
		}
		//初始化SQLite
		dbHelper = new DatabaseHelper(this, "UserInformation.db", null, 2);
		dbHelper.getWritableDatabase();
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		//设置登录键的点击事件
		signbButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean isright = false;//标识符，用户名和密码是否正确
				String nameString = usernameEditText.getText().toString();
				String passwordString = passwordEditText.getText().toString();
				Cursor cursor = db.query("USER", null, null, null, null, null, null);
				if(cursor.moveToFirst()){
					do{
						String name = cursor.getString(cursor.getColumnIndex("username"));
						String password = cursor.getString(cursor.getColumnIndex("password"));
						if((name.equals(nameString)) && (password.equals(passwordString))){
							isright = true;
							break;
						}
					}while (cursor.moveToNext());
				}
				if(isright){
					editor = pref.edit();
					if(rememberCheckBox.isChecked()){
						//检查复选框是否被选中
						editor.putBoolean("remember_password", true);
						editor.putString("username", nameString);
						editor.putString("password", passwordString);
						if(autoCheckBox.isChecked()){
							editor.putBoolean("auto", true);
						}
					} else {
						editor.clear();
					}
					editor.commit();
					//输入错误后重新输入时需要保证cursor和db是未关闭状态
					cursor.close();
					db.close();
					//启动主界面
					Intent intent = new Intent(SignInActivity.this,MainActivity.class);//登录成功跳转到主界面
					startActivity(intent);
					finish();//销毁当前的登录界面，这样在主界面点击back不会回到登录界面
					Toast.makeText(MyApplication.getContext(), "登录成功！", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MyApplication.getContext(), "用户名或密码错误，请重新输入！", Toast.LENGTH_SHORT).show();
					usernameEditText.setText("");
					passwordEditText.setText("");
				}
				
			}
		});
		
		
		/*
		 * 给TextView部分文字设置点击跳转
		 */
		String text = "还没有账号？快去注册一下！";
		SpannableString spannableString = new SpannableString(text);
		spannableString.setSpan(new ClickableSpan() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
				startActivity(intent);
				
			}
		}, 6, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		registerTextView.setText(spannableString);
		registerTextView.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
