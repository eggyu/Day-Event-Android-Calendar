package com.mday.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements WeekPageAdapter.onClickView {

    private CustomViewPager viewPager;
    private List<CourseEvent>courseEventList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.view_pager_time);
        viewPager.setPagingEnabled(false);

        courseEventList=new ArrayList<>();

        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        startDate.set(Calendar.HOUR_OF_DAY, 7);
        startDate.set(Calendar.MINUTE, 0);
        endDate.set(Calendar.HOUR_OF_DAY, 9);
        endDate.set(Calendar.MINUTE, 0);

        courseEventList.add(new CourseEvent("Java",startDate.getTime(),endDate.getTime(),"Room 2202","D7"));


        //startDate.add(Calendar.DAY_OF_MONTH, 2);
        //endDate.add(Calendar.DAY_OF_MONTH, 2);

        startDate.set(Calendar.HOUR_OF_DAY, 11);
        startDate.set(Calendar.MINUTE, 0);
        endDate.set(Calendar.HOUR_OF_DAY, 13);
        endDate.set(Calendar.MINUTE, 0);
        courseEventList.add(new CourseEvent("Sports",startDate.getTime(),endDate.getTime(),"Room 2202","D7"));
        //startDate.add(Calendar.DAY_OF_MONTH, 2);
        //endDate.add(Calendar.DAY_OF_MONTH, 2);

        startDate.set(Calendar.HOUR_OF_DAY, 16);
        startDate.set(Calendar.MINUTE, 0);
        endDate.set(Calendar.HOUR_OF_DAY, 18);
        endDate.set(Calendar.MINUTE, 0);
        courseEventList.add(new CourseEvent("Tester",startDate.getTime(),endDate.getTime(),"Room 2202","D7"));

       // startDate.add(Calendar.DAY_OF_MONTH, 3);
        //endDate.add(Calendar.DAY_OF_MONTH, 3);

        startDate.set(Calendar.HOUR_OF_DAY, 20);
        startDate.set(Calendar.MINUTE, 0);
        endDate.set(Calendar.HOUR_OF_DAY, 22);
        endDate.set(Calendar.MINUTE, 0);

        courseEventList.add(new CourseEvent("C#",startDate.getTime(),endDate.getTime(),"Room 2202","D7"));

        initAdapter(courseEventList);


        moveToThisWeek();
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
        Toast.makeText(MainActivity.this,"Position"+poss,Toast.LENGTH_LONG).show();

    }
}
