package com.mix.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.mixtest.R;
import com.mix.activity.DiaryContentActivity;
import com.mix.adapter.ListViewAdapter;
import com.mix.bean.Diary;
import com.mix.sqlite.DatabaseHelper;
import com.mix.utils.MyApplication;
import com.mix.view.ReFlashListView;
import com.mix.view.ReFlashListView.IReflashListener;

import android.support.v4.app.Fragment;
import android.R.integer;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class SecondFragment extends Fragment implements IReflashListener{
	
	List<Diary> diaryList;
	
	ReFlashListView mListView;
	
	private DatabaseHelper dbHelper;
	
	private List<Diary> newdiaryList;
	
	ListViewAdapter adapter;

	Diary diary;
	LayoutInflater inflater;
	ViewGroup container;
	int idNow;
	int id_afterdelete;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_view, container, false);
		this.inflater = inflater;
		this.container = container;
		diaryList = new ArrayList<Diary>();
		dbHelper = new DatabaseHelper(MyApplication.getContext(), "UserInformation.db", null, 2);
		dbHelper.getWritableDatabase();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query("DIARY", null, null, null, null, null, null);
		if(cursor.moveToLast()){
		//	idNow = cursor.getInt(cursor.getColumnIndex("id"));
			idNow = cursor.getCount();
			id_afterdelete = idNow;
			Log.i("tag", "id = " + idNow);
		}
		showList(diaryList, view);
/*		if(adapter == null){
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
				mListView = (ReFlashListView) view.findViewById(R.id.list_view);
				mListView.setInterface(this);
				adapter = new ListViewAdapter(getActivity().getApplicationContext(), diaryList);
				mListView.setAdapter(adapter); 
		} else {
			adapter.onDateChange(diaryList);
		}*/
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Diary diary = diaryList.get(position-1);//为什么是position-1呢
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
						newdiaryList = new ArrayList<Diary>();//这段没用的更新代码值得商榷
						for(Diary diary2 : diaryList){
							newdiaryList.add(diary2);
						}
						SQLiteDatabase db = dbHelper.getWritableDatabase();
						Diary diary = newdiaryList.get(position-1);
						db.delete("Diary", "diary_title = ?", new String[]{diary.titleForDiary});
						diaryList.clear();
						diaryList.addAll(newdiaryList);
						id_afterdelete = idNow - 1;
						adapter.onDateChange(diaryList);//需要重绘ListView
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
	
	private void showList(List<Diary> diaryList,View view){
		if(adapter == null){
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
					diaryList.add(0,diary);
				}while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
				mListView = (ReFlashListView) view.findViewById(R.id.list_view);
				mListView.setInterface(this);
				adapter = new ListViewAdapter(getActivity().getApplicationContext(), diaryList);
				mListView.setAdapter(adapter); 
		} else if(id_afterdelete < idNow){
			diaryList.clear();
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
					diaryList.add(0,diary);
				}while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
			adapter.onDateChange(diaryList);
		} else{
			dbHelper = new DatabaseHelper(MyApplication.getContext(), "UserInformation.db", null, 2);
			dbHelper.getWritableDatabase();
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			Cursor cursor = db.query("DIARY", null, null, null, null, null, null);
			if(cursor.moveToPosition(idNow)){
				do{
					diary = new Diary();
					diary.titleForDiary = cursor.getString(cursor.getColumnIndex("diary_title"));
					diary.secondtitleForDiary = cursor.getString(cursor.getColumnIndex("diary_secondtitle"));
					diary.contentForDiary = cursor.getString(cursor.getColumnIndex("diary_content"));
					byte[] in = cursor.getBlob(cursor.getColumnIndex("diary_picture"));
					Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
					diary.pictureForDiary = bmpout;
					diaryList.add(0,diary);
					idNow = cursor.getCount();
				}while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
			adapter.onDateChange(diaryList);
		}
	}
	
	@Override
	public void onReflash() {
		View view = inflater.inflate(R.layout.list_view, container, false);
		showList(diaryList, view);
		mListView.reflashComplete();
	}
}