package com.mday.event;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;



@SuppressLint("ValidFragment")
public class WeekFragment extends Fragment {

    private Date mStartDate;
    private List<CourseEvent> mItems;

    private int widthCell;
    private int hourWidth;
    private int hourHeight;
    private int hourHeightLine;

    private WeekPageAdapter.onClickView onClickView;
    private ScrollView scrollView;

    private Activity activity;


    public WeekFragment() {
    }

    public WeekFragment(Date startDate, List<CourseEvent> items, WeekPageAdapter.onClickView onClickView, Activity activity) {
        mStartDate = startDate;
        this.onClickView = onClickView;
        this.activity = activity;

//        if (items == null)
//            mItems = new ArrayList<ListCourseEvent>();
//        else
//            mItems = items;
        this.mItems=items;


    }
    private static final float MIN_ZOOM = 1.0f;
    private static final float MAX_ZOOM = 4.0f;

    private float scale = 1.0f;
    private float lastScaleFactor = 0f;

    // Where the finger first  touches the screen
    private float startX = 0f;
    private float startY = 0f;

    // How much to translate the canvas
    private float dx = 0f;
    private float dy = 0f;
    private float prevDx = 0f;
    private float prevDy = 0f;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        widthCell = Helper.dipValue(getActivity().getBaseContext(), 48);
        hourWidth = Helper.dipValue(container.getContext(), Constants.HOUR_WIDTH);
        hourHeight = Helper.dipValue(container.getContext(), Constants.HOUR_HEIGHT);
        hourHeightLine = Helper.dipValue(container.getContext(), Constants.HOUR_HEIGHT_LINE);

        View weekdayView = inflater.inflate(R.layout.weekday, null);

        LinearLayout weekdayHeader = (LinearLayout) weekdayView.findViewById(R.id.weekdayHeader);
        final LinearLayout weekdayHorizontalBar = (LinearLayout) weekdayView.findViewById(R.id.weekdayHorizontalBar);

//        weekdayHorizontalBar.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction() & MotionEvent.ACTION_MASK) {
//                    case MotionEvent.ACTION_DOWN:
//                        Log.i(TAG, "DOWN");
//                        if (scale > MIN_ZOOM) {
//                            startX = event.getX() - prevDx;
//                            startY = event.getY() - prevDy;
//                        }
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//
//                        dx = event.getX() - startX;
//                        dy = event.getY() - startY;
//
//                        break;
//                    case MotionEvent.ACTION_POINTER_DOWN:
//
//                        break;
//                    case MotionEvent.ACTION_POINTER_UP:
//
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        Log.i(TAG, "UP");
//                        prevDx = dx;
//                        prevDy = dy;
//                        break;
//                }
//                if ( scale >= MIN_ZOOM) {
//                    Toast.makeText(getContext(),"zooom",Toast.LENGTH_LONG).show();
//                    hourHeight+=20;
//                   getParent().requestDisallowInterceptTouchEvent(true);
//                   float maxDx = (child().getWidth() - (child().getWidth() / scale)) / 2 * scale;
//                   float maxDy = (child().getHeight() - (child().getHeight() / scale)) / 2 * scale;
//                   dx = Math.min(Math.max(dx, -maxDx), maxDx);
//                   dy = Math.min(Math.max(dy, -maxDy), maxDy);
//                   Log.i(TAG, "Width: " + child().getWidth() + ", scale " + scale + ", dx " + dx + ", max " + maxDx);
//                   applyScaleAndTranslation();
//
//                }
//                return true;
//            }
//        });

        final ZoomableRelativeLayout zome = (ZoomableRelativeLayout) weekdayView.findViewById(R.id.zoom);
        zome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                zome.init(getContext());
                return false;
            }
        });

        scrollView = weekdayView.findViewById(R.id.scroll_view);


        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(mStartDate);


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createHoursRuler(weekdayHorizontalBar);
            }
        });


        LinearLayout verticalHourCel = new LinearLayout(weekdayHorizontalBar.getContext());
        //weekdayHeader.addView(verticalHourCel, 0, new RelativeLayout.LayoutParams(hourWidth, LayoutParams.MATCH_PARENT));

        //for (int i = 0; i < 7; i++) {
        //createHeader(inflater, weekdayHeader, calendar);
        createWeekdayBar(container.getContext(), calendar.getTime(), weekdayHorizontalBar);
        //calendar.add(Calendar.DAY_OF_YEAR, 1);
        //}

        return weekdayView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//		scrollView.post(new Runnable() {
//			@Override
//			public void run() {
//				scrollView.scrollTo(0,500);
//			}
//		});
//
//
//		scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//			@Override
//			public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//				Toast.makeText(getContext(),"Change"+scrollY+"==="+oldScrollY,Toast.LENGTH_LONG).show();
//			}
//		});
    }


    private void createHoursRuler(LinearLayout weekdayHorizontalBar) {
        LinearLayout verticalHourCell = new LinearLayout(weekdayHorizontalBar.getContext());
        verticalHourCell.setBackgroundColor(Color.WHITE);
        verticalHourCell.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, hourHeightLine);
        LinearLayout.LayoutParams hourHeightLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, hourHeight);


//		LinearLayout hourHeightLayout23 = new LinearLayout(weekdayHorizontalBar.getContext());
//		TextView textView2 = new TextView(weekdayHorizontalBar.getContext());
//		textView2.setTextColor(Color.GRAY);
//		textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
//		textView2.setText("2bc");
//		hourHeightLayout23.setGravity(Gravity.CENTER_HORIZONTAL);
//		hourHeightLayout23.addView(textView2, textViewParams);
//		verticalHourCell.addView(hourHeightLayout23, hourHeightLayoutParams);

        for (int i = Constants.INITIAL_HOUR; i <= Constants.END_HOUR; i++) {
            LinearLayout hourHeightLayout = new LinearLayout(weekdayHorizontalBar.getContext());
            hourHeightLayout.setBackgroundColor(Color.WHITE);
            TextView textView = new TextView(weekdayHorizontalBar.getContext());
            textView.setTextColor(Color.GRAY);
            textView.setText(String.format("%02d:00", i));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            //textView.setTypeface(UtilsFonts.helveticaRegular(getContext()));
            textView.setTextColor(Color.parseColor("#787993"));
            hourHeightLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            hourHeightLayout.addView(textView, textViewParams);

            verticalHourCell.addView(hourHeightLayout, hourHeightLayoutParams);
        }

        weekdayHorizontalBar.addView(verticalHourCell, 0, new RelativeLayout.LayoutParams(hourWidth, LayoutParams.MATCH_PARENT));


    }

    private void createHeader(LayoutInflater inflater, LinearLayout weekdayHeader, Calendar calDate) {

        View viewHeader = inflater.inflate(R.layout.weekday_header, null);

        TextView dayOfWeekTextView = (TextView) viewHeader.findViewById(R.id.dayOfWeek);
        dayOfWeekTextView.setText(Helper.getStringDayOfWeek(calDate.getTime()));

        TextView dayOfMonthTextView = (TextView) viewHeader.findViewById(R.id.dayOfMonth);
        dayOfMonthTextView.setText(Helper.getStringDayOfMonth(calDate.getTime()));

        if (DateUtils.isToday(calDate.getTime().getTime())) {
            dayOfMonthTextView.setBackgroundResource(R.drawable.background_oval);
            dayOfMonthTextView.setTextColor(Color.WHITE);
        }
        if (IsWeekend(calDate)) {
            dayOfWeekTextView.setTextColor(getResources().getColor(R.color.dark_gray));
            dayOfMonthTextView.setTextColor(getResources().getColor(R.color.dark_gray));
        }
        if (IsDifferentMonth(calDate)) {
            dayOfWeekTextView.setTextColor(getResources().getColor(R.color.light_gray));
            dayOfMonthTextView.setTextColor(getResources().getColor(R.color.light_gray));
        }

        weekdayHeader.addView(viewHeader);
    }

    private void createWeekdayBar(Context context, Date date, LinearLayout weekdayHorizontalBar) {
        WeekdayVerticalBar verticalBar = new WeekdayVerticalBar(context, date, mItems, onClickView, scrollView, activity);
        verticalBar.setBackgroundColor(Color.WHITE);
        weekdayHorizontalBar.addView(verticalBar, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private static boolean IsWeekend(Calendar calDate) {
        if (calDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
            return true;
        if (calDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            return true;
        return false;
    }

    private static boolean IsDifferentMonth(Calendar calDate) {
        Calendar today = Calendar.getInstance();
        if (calDate.get(Calendar.MONTH) != today.get(Calendar.MONTH))
            return true;
        return false;
    }
}