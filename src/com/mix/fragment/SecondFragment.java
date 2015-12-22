package com.mix.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.mixtest.R;
import com.mix.activity.DiaryContentActivity;
import com.mix.adapter.ListViewAdapter;
import com.mix.bean.Diary;
import com.mix.sqlite.DatabaseHelper;
import com.mix.utils.MyApplication;

import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class SecondFragment extends Fragment{
	
	private List<Diary> diaryList;
	
	private ListView mListView;
	
	private DatabaseHelper dbHelper;
	
	private List<Diary> newdiaryList;
	
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
				diary.contentForDiary = cursor.getString(cursor.getColumnIndex("diary_content"));
				byte[] in = cursor.getBlob(cursor.getColumnIndex("diary_picture"));
				Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
				diary.pictureForDiary = bmpout;
				diaryList.add(diary);
			}while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		
		final ListViewAdapter adapter = new ListViewAdapter(getActivity(), diaryList);
		mListView.setAdapter(adapter); 
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Diary diary = diaryList.get(position);
				DiaryContentActivity.actionStart(getActivity(), diary.titleForDiary, diary.contentForDiary, diary.pictureForDiary);
			}
		});
		
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
				dialog.setTitle("删除");
				dialog.setMessage("确认删除该游记？");
				dialog.setCancelable(false);
				dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						newdiaryList = new ArrayList<Diary>();
						for(Diary diary2 : diaryList){
							newdiaryList.add(diary2);
						}
						SQLiteDatabase db = dbHelper.getWritableDatabase();
						Diary diary = newdiaryList.get(position);
						db.delete("Diary", "diary_title = ?", new String[]{diary.titleForDiary});
						diaryList.clear();
						diaryList.addAll(newdiaryList);
						adapter.notifyDataSetChanged();
						db.close();
						Toast.makeText(MyApplication.getContext(), "删除成功!", Toast.LENGTH_SHORT).show();
					}
				});
				dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				dialog.show();
				
				return true;
			}
			
		});
		return view;
	}
}