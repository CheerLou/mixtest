package com.mix.view;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.zip.Inflater;

import com.example.mixtest.R;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class ReFlashListView extends ListView implements OnScrollListener{
	View header;
	int headerHeight;
	int firstVisibleItem;
	int scrollState;
	boolean isRemark;//当前是否在listView顶端按下的
	int startY;
	int state;//当前的状态
	final int NONE = 0;
	final int PULL = 1;//下拉
	final int RELESE = 2;//释放
	final int REFLASHING = 3;//刷新
	IReflashListener iReflashListener;

	public ReFlashListView(Context context) {
		super(context);
		initView(context);
	}
	
	public ReFlashListView(Context context,AttributeSet attrs) {
		super(context, attrs);		
		initView(context);
	}
	
	public ReFlashListView(Context context,AttributeSet attrs,int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
	//	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		header = inflater.inflate(R.layout.header_layout, null);
		measureView(header);
		headerHeight = header.getMeasuredHeight();
		topPadding(-headerHeight);
		this.addHeaderView(header);
		this.setOnScrollListener(this);
	}
	
	/**
	 * 通知父布局所占的宽高
	 * @param view
	 */
	private void measureView(View view) {
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if(p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int height;
		int tempHeight = p.height;
		if(tempHeight > 0) {
			height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
		} else {
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		view.measure(width, height);
	}
	
	private void topPadding(int topPadding) {
		header.setPadding(header.getPaddingLeft(), topPadding, header.getPaddingRight(), header.getPaddingBottom());
		header.invalidate();
	}
	

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch(ev.getAction()) {
		case MotionEvent.ACTION_DOWN :
			if(firstVisibleItem == 0) {
				isRemark = true;
				startY = (int) ev.getY();
			}
			break;
		case MotionEvent.ACTION_MOVE :
			onMove(ev);
			break;
		case MotionEvent.ACTION_UP :
			if(state == RELESE) {
				state = REFLASHING;
				reflashViewByState();
				iReflashListener.onReflash();
			} else if(state == PULL) {
				state = NONE;
				isRemark = false;
				reflashViewByState();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}


	private void onMove(MotionEvent ev) {
		int tempY = (int) ev.getY();//当前位置
		int space = tempY - startY;//移动的距离
		int topPadding = space - headerHeight;
		switch(state) {
		case NONE :
			if(space > 0){
				state = PULL;
				reflashViewByState();
			}
			break;
		case PULL :
			topPadding(topPadding);
			if(space > headerHeight + 30 && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
				state = RELESE;
				reflashViewByState();
			}	
			break;
		case RELESE :
			topPadding(topPadding);
			if(space < headerHeight + 30) {
				state = PULL;
				reflashViewByState();
			} else if(space < 0) {
				state = NONE;
				isRemark = false;
				reflashViewByState();
			}
			break;
		}
	}
	

	private void reflashViewByState() {
		TextView tip = (TextView) header.findViewById(R.id.tip);
		ImageView arrow = (ImageView) header.findViewById(R.id.arrow);
		ProgressBar progress = (ProgressBar) header.findViewById(R.id.progress);
		RotateAnimation anim = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF,0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(500);
		anim.setFillAfter(true);
		RotateAnimation anim1 = new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF,0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim1.setDuration(500);
		anim1.setFillAfter(true);
		switch(state) {
		case NONE :
			arrow.clearAnimation();
			topPadding(-headerHeight);
			break;
		case PULL :
			arrow.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			tip.setText("下拉刷新");
			arrow.clearAnimation();
			arrow.setAnimation(anim);
			break;
		case RELESE :
			arrow.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			tip.setText("松开刷新");
			arrow.clearAnimation();
			arrow.setAnimation(anim1);
			break;
		case REFLASHING :
			topPadding(50);
			arrow.setVisibility(View.GONE);
			progress.setVisibility(View.VISIBLE);
			tip.setText("刷新中");
			arrow.clearAnimation();
			break;
		}
	}
	
	public void reflashComplete() {
		state = NONE;
		isRemark = false;
		reflashViewByState();
		TextView lastupdatetime = (TextView) header.findViewById(R.id.lastupdate_time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		String time = format.format(date);
		lastupdatetime.setText(time);
	}
	
	public void setInterface(IReflashListener iReflashListener) {
		this.iReflashListener = iReflashListener;
	}
	
	/**
	 * 刷新数据接口
	 * @author me
	 *
	 */
	public interface IReflashListener {
		public void onReflash();
	}
}
