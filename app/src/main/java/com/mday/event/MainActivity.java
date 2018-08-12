package com.mday.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements WeekPageAdapter.onClickView {

    private CustomViewPager viewPager;
    private List<CourseEvent>courseEventList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager = findViewById(R.id.view_pager_time);

        moveToThisWeek();

        courseEventList=new ArrayList<>();




        initAdapter(courseEventList);


    }

    private void moveToThisWeek() {
        Calendar calendar = Calendar.getInstance();
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        int position = (int) (Helper.shifMonth() + thisDay) / 7;

        viewPager.setCurrentItem(position);
    }
    private void initAdapter(List<CourseEvent> list) {


        WeekPageAdapter adapter = new WeekPageAdapter(getSupportFragmentManager(), list, this, MainActivity.this);
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void clickView(int poss) {

    }
}
