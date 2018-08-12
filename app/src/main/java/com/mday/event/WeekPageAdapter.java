package com.mday.event;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Calendar;
import java.util.List;

public class WeekPageAdapter extends FragmentPagerAdapter {

	private List<CourseEvent> mItems;
	private int numberOfPages;
	private onClickView onClickView;
	private Activity activity;
	public WeekPageAdapter(FragmentManager manager, List<CourseEvent> items, onClickView onClickView, Activity activity) {
		super(manager);
		mItems = items;
		this.onClickView=onClickView;
		this.activity=activity;
		numberOfPages = Helper.numberOfWeeksWithShift();
	}

	@Override
	public int getCount() {
		return numberOfPages;
	}

	@Override
	public Fragment getItem(int position) {

		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		int shift = Helper.shifMonth() * -1;
		startDate.add(Calendar.DAY_OF_YEAR, shift + position * 7);

		return new WeekFragment(startDate.getTime(), mItems,onClickView,activity);
	}

	public interface onClickView{
		 void clickView(int poss);
	}

}
