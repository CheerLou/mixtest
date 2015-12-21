package com.mix.sqlite;

import com.mix.utils.MyApplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper{
	
	public static final String CREATE_USER = "create table User ("
			+ "id integer primary key autoincrement, "
			+ "username text, "
			+ "password text)";
	
	public static final String CREATE_DIARY = "create table Diary ("
			+ "id integer primary key autoincrement, "
			+ "diary_title text, "
			+ "diary_secondtitle text, "
			+ "diary_content text, "
			+ "diary_picture blob)";
	
	private Context mcontext;

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		mcontext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_USER);
		db.execSQL(CREATE_DIARY);
		Toast.makeText(MyApplication.getContext(), "我不告诉你我搞了一个SQLite~", Toast.LENGTH_LONG);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
		case 1:
			db.execSQL(CREATE_DIARY);
			break;

		default:
			break;
		}
	}

}
