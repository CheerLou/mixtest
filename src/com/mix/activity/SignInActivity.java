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
		//��ʼ���ؼ�
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		usernameEditText = (EditText) findViewById(R.id.id_sign_username);
		passwordEditText = (EditText) findViewById(R.id.id_sign_password);
		signbButton = (Button) findViewById(R.id.id_sign_in);
		rememberCheckBox = (CheckBox) findViewById(R.id.id_sign_remember_password);
		autoCheckBox = (CheckBox) findViewById(R.id.id_sign_auto);
		registerTextView = (TextView) findViewById(R.id.id_sign_tv);
		//��ס������ж�
		boolean isRemember = pref.getBoolean("remember_password", false);
		boolean isAuto = pref.getBoolean("auto", false);
		if(isAuto){
			//����������
			Intent intent = new Intent(SignInActivity.this,MainActivity.class);//��¼�ɹ���ת��������
			startActivity(intent);
			finish();//���ٵ�ǰ�ĵ�¼���棬��������������back����ص���¼����
			Toast.makeText(MyApplication.getContext(), "��¼�ɹ���", Toast.LENGTH_SHORT).show();
		}
		if(isRemember){
			String username = pref.getString("username", "");
			String password = pref.getString("password", "");
			usernameEditText.setText(username);
			passwordEditText.setText(password);
			rememberCheckBox.setChecked(true);
		}
		//��ʼ��SQLite
		dbHelper = new DatabaseHelper(this, "UserInformation.db", null, 2);
		dbHelper.getWritableDatabase();
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		//���õ�¼���ĵ���¼�
		signbButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean isright = false;//��ʶ�����û����������Ƿ���ȷ
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
						//��鸴ѡ���Ƿ�ѡ��
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
					//����������������ʱ��Ҫ��֤cursor��db��δ�ر�״̬
					cursor.close();
					db.close();
					//����������
					Intent intent = new Intent(SignInActivity.this,MainActivity.class);//��¼�ɹ���ת��������
					startActivity(intent);
					finish();//���ٵ�ǰ�ĵ�¼���棬��������������back����ص���¼����
					Toast.makeText(MyApplication.getContext(), "��¼�ɹ���", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MyApplication.getContext(), "�û���������������������룡", Toast.LENGTH_SHORT).show();
					usernameEditText.setText("");
					passwordEditText.setText("");
				}
				
			}
		});
		
		
		/*
		 * ��TextView�����������õ����ת
		 */
		String text = "��û���˺ţ���ȥע��һ�£�";
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
