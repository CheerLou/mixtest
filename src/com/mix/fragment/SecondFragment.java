package com.mix.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.mixtest.R;
import com.mix.adapter.ListViewAdapter;
import com.mix.bean.Diary;
import com.mix.sqlite.DatabaseHelper;
import com.mix.utils.MyApplication;

import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ListView;

public class SecondFragment extends Fragment{
	
	private List<Diary> diaryList;
	
	private ListView mListView;
	
	private DatabaseHelper dbHelper;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_view, container, false);
		mListView = (ListView) view.findViewById(R.id.list_view);
		
		diaryList = new ArrayList<Diary>();
		Diary diary;
		dbHelper = new DatabaseHelper(MyApplication.getContext(), "UserInformation.db", null, 2);
		dbHelper.getWritableDatabase();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query("DIARY", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				diary = new Diary();
				diary.titleForDiary = cursor.getString(cursor.getColumnIndex("diary_title"));
				diary.secondtitleForDiary = cursor.getString(cursor.getColumnIndex("diary_secondtitle"));
				byte[] in = cursor.getBlob(cursor.getColumnIndex("diary_picture"));
				Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
				diary.pictureForDiary = bmpout;
				diaryList.add(diary);
			}while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		
		ListViewAdapter adapter = new ListViewAdapter(getActivity(), diaryList);
		mListView.setAdapter(adapter); 
		return view;
	}
}