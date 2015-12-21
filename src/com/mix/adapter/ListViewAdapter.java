package com.mix.adapter;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.example.mixtest.R;
import com.mix.bean.Diary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter{
	
	private List<Diary> mList;
	private LayoutInflater mInflater;

	
	public ListViewAdapter(Context context, List<Diary> data){
		mList = data;
		mInflater = LayoutInflater.from(context);
		
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_item, null);
			viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.id_list_pic);
			viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.id_list_title);
			viewHolder.tvSecondTitle = (TextView) convertView.findViewById(R.id.id_list_secondtitle);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.ivIcon.setImageBitmap(mList.get(position).pictureForDiary);
		viewHolder.tvTitle.setText(mList.get(position).titleForDiary);
		viewHolder.tvSecondTitle.setText(mList.get(position).secondtitleForDiary);
		return convertView;
	}
	
	class ViewHolder {
		public TextView tvTitle, tvSecondTitle;
		public ImageView ivIcon;
	}

}
