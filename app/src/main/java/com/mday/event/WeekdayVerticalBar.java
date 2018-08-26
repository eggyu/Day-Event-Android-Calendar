package com.mday.event;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class WeekdayVerticalBar extends RelativeLayout implements View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {

    private int HOUR_HEIGHT;
    private int HOUR_HEIGHT_LINE;

    private Date mDate;
    private List<CourseEvent> mItems;
    private Time mCurrentTime;

    private static int mCellHeight = 0;
    private static final int HOUR_GAP = 1;

    WeekPageAdapter.onClickView onClickView;
    private Typeface helveticaRegular;

    private ScrollView scrollView;

    private Activity activity;
    private SimpleDateFormat sdf;
    ScaleGestureDetector mScaleDetector = new ScaleGestureDetector(getContext(), this);

    public WeekdayVerticalBar(Context context, Date date, List<CourseEvent> items, WeekPageAdapter.onClickView onClickView, ScrollView scrollView, Activity activity) {
        super(context);
        mDate = date;
        this.onClickView = onClickView;
        this.scrollView = scrollView;
        this.activity = activity;

//        if (items == null)
//            mItems = new ArrayList<CourseEvent>();
//        else
//            mItems = items;
        this.mItems = items;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
//
        helveticaRegular = UtilsFonts.helveticaRegular(context);

        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createDayLayout();
            }
        });


    }

    private void createDayLayout() {
        HOUR_HEIGHT = Helper.dipValue(getContext(), Constants.HOUR_HEIGHT);
        HOUR_HEIGHT_LINE = Helper.dipValue(getContext(), Constants.HOUR_HEIGHT_LINE);
        createHoursCell();
        createItems();

        moveScrollViewToCurrentTime();


    }

    public void moveScrollViewToCurrentTime() {
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, Math.round(calculateHeightCurrentTime()));
                //ObjectAnimator objectAnimator = ObjectAnimator.ofInt(scrollView, "scrollY", 0, 250).setDuration(duration);
                //objectAnimator.start();
            }
        }, 200);
    }

    private void createHoursCell() {

        LinearLayout verticalHourCell = new LinearLayout(getContext());
        verticalHourCell.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams bg1Params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, HOUR_HEIGHT_LINE / 2 - 1);

        LinearLayout.LayoutParams bg2Params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, HOUR_HEIGHT);

        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);

        LinearLayout.LayoutParams bg1LayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams lineLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lineLayoutParams2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);

        LinearLayout.LayoutParams bg2LayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, HOUR_HEIGHT);

        LinearLayout.LayoutParams hourHeightLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, HOUR_HEIGHT);
        LinearLayout.LayoutParams hourHeightLayoutParams2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, HOUR_HEIGHT / 2 - 1);


        //Drawable bgResource = getResources().getDrawable(R.drawable.bg_scheduler_repeat);

        for (int i = Constants.INITIAL_HOUR; i <= Constants.END_HOUR; i++) {

            LinearLayout bg1Layout = new LinearLayout(getContext());
            bg1Layout.setPadding(5, 0, 5, 0);
            View bg1 = new View(getContext());
            //bg1.setBackground(bgResource);

            bg1Layout.addView(bg1, bg1Params);


//
            // LinearLayout bg2Layout = new LinearLayout(getContext());
//            bg2Layout.setPadding(5, 0, 5, 0);
//            View bg2 = new View(getContext());
//            //bg2.setBackground(bgResource);
//            bg2Layout.addView(bg2, bg2Params);
            LinearLayout hourHeightLayout = new LinearLayout(getContext());
            hourHeightLayout.setOrientation(LinearLayout.VERTICAL);

            LinearLayout lineLayout = new LinearLayout(getContext());
            View lineView = new View(getContext());
            lineView.setBackgroundColor(Color.GRAY);
            lineParams.setMargins(50, 0, 0, 0);
            lineLayout.addView(lineView, lineParams);


            hourHeightLayout.addView(bg1Layout, bg1LayoutParams);
            hourHeightLayout.addView(lineLayout, lineLayoutParams);
            // hourHeightLayout.addView(bg2Layout, bg2LayoutParams);

//            if(i==Constants.END_HOUR){
//                verticalHourCell.addView(hourHeightLayout, hourHeightLayoutParams2);
//            }else {
            verticalHourCell.addView(hourHeightLayout, hourHeightLayoutParams);
            // }


        }

        View viewOne = LayoutInflater.from(getContext()).inflate(R.layout.current_time_layout, this, false);
        LayoutParams layoutParamsOne = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParamsOne.setMargins(0, Math.round(calculateHeightCurrentTime()), 0, 0);
        addView(viewOne, layoutParamsOne);

        addView(verticalHourCell, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private void createItems() {

//        Calendar startCalendar = GregorianCalendar.getInstance();
//        startCalendar.setTime(mDate);
//        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
//        startCalendar.set(Calendar.MINUTE, 0);
//        startCalendar.set(Calendar.SECOND, 0);
//        Date startDate = startCalendar.getTime();
//
//        Calendar endCalendar = GregorianCalendar.getInstance();
//        endCalendar.setTime(mDate);
//        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
//        endCalendar.set(Calendar.MINUTE, 59);
//        endCalendar.set(Calendar.SECOND, 59);
//        Date endDate = endCalendar.getTime();


//        View viewOne = LayoutInflater.from(getContext()).inflate(R.layout.current_time_layout, this, false);
//        LayoutParams layoutParamsOne = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        layoutParamsOne.setMargins(0, Math.round(calculateHeightCurrentTime()), 0, 0);
//        addView(viewOne, layoutParamsOne);

        final SimpleDateFormat output = new SimpleDateFormat("HH'h'mm");
        for (final CourseEvent item : mItems) {
//			if ((startDate.compareTo(item.getEndDate()) < 0 && startDate.compareTo(item.getStartDate()) >= 0)
//					|| (startDate.compareTo(item.getStartDate()) < 0 && endDate.compareTo(item.getStartDate()) > 0)) {
            View view = null;

            if (item.getEndDatetime().before(Calendar.getInstance().getTime())) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_current_time, this, false);
            } else {
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_courses_envent_4, this, false);
            }


            RelativeLayout relativeLayout = view.findViewById(R.id.ln);


            TextView tv_name_courses = view.findViewById(R.id.tv_name_courses);
            TextView tv_time_courses = view.findViewById(R.id.tv_time_courses);
            TextView tv_class_name_courses = view.findViewById(R.id.tv_class_name_courses);
            TextView tv_room_courses = view.findViewById(R.id.tv_room_courses);

            tv_name_courses.setTypeface(helveticaRegular);
            tv_time_courses.setTypeface(helveticaRegular);
            tv_class_name_courses.setTypeface(helveticaRegular);
            tv_room_courses.setTypeface(helveticaRegular);

            tv_name_courses.setText(item.getNameEvent());
            tv_time_courses.setText(output.format(item.getStartDatetime()) + "-" + output.format(item.getEndDatetime()));
            tv_class_name_courses.setText(item.getAddress());
            tv_room_courses.setText(item.getRoom());


            RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
//				LinearLayout linearLayout = new LinearLayout(getContext());
//				linearLayout.setOrientation(LinearLayout.VERTICAL);
//				linearLayout.setGravity(Gravity.CENTER);
//
//				if (Build.VERSION.SDK_INT < 16)
//					linearLayout.setBackgroundDrawable(getGradientDrawable());
//				else
//					linearLayout.setBackground(getGradientDrawable());
//
//				ImageView imageView = new ImageView(getContext());
//				TextView textView=new TextView(getContext());
//				textView.setText("DEMO");
//				imageView.setImageDrawable(item.getIcon());
//				linearLayout.addView(imageView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//				linearLayout.addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));


            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, Math.round(calculateHeight(item)));
            relativeLayout.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
            //layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            //relativeLayout.setLayoutParams(layoutParams);
            layoutParams.setMargins(80, calculateRow(item), 50, 0);


            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView text = v.findViewById(R.id.tv_time_courses);
                    String s = null;

                    s = output.format((item.getStartDatetime()) + " - " + output.format(item.getEndDatetime()));

                    if (s.equals(text.getText())) {
                        CourseEvent itemcureent = item;
                        int poss = mItems.indexOf(itemcureent);

                        onClickView.clickView(poss);

                    }


                }
            });

            addView(view, layoutParams);
        }


    }

    private float calculateHeight(CourseEvent item) {

        float sizeMinutes = (float) HOUR_HEIGHT / 60;
        int minutes = 0;
        minutes = (int) (((item.getEndDatetime()).getTime() / 60000) - ((item.getStartDatetime()).getTime() / 60000));

//        long diff = item.getEndDate().getTime() - item.getStartDate().getTime();
//
//        long diffSeconds = diff / 1000 % 60;
//        long diffMinutes = diff / (60 * 1000) % 60;
//        long diffHours = diff / (60 * 60 * 1000) % 24;
//        long diffDays = diff / (24 * 60 * 60 * 1000);
//
//        int minus= (int) (diffMinutes+diffHours*60);


        return minutes * sizeMinutes;
    }

    private float calculateHeightCurrentTime() {
        float sizeMinutes = (float) HOUR_HEIGHT / 60;

        Calendar startDateTime = Calendar.getInstance();


        startDateTime.set(Calendar.HOUR_OF_DAY, Constants.INITIAL_HOUR);
        startDateTime.set(Calendar.MINUTE, 0);
        Date currentTime = Calendar.getInstance().getTime();

        int minutes = (int) ((currentTime.getTime() / 60000) - (startDateTime.getTime().getTime() / 60000));

        long diff = currentTime.getTime() - startDateTime.getTime().getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        int minus = (int) (diffMinutes + diffHours * 60);


        return minutes * sizeMinutes;
    }

    private int calculateRowCurrent() {
        Date currentTime = Calendar.getInstance().getTime();

        int hourOfDay = currentTime.getHours();
        int minutesOfHour = currentTime.getMinutes();

        Log.d("", "calculateRowCurrent: " + hourOfDay);
        Log.d("", "calculateRowCurrent: " + minutesOfHour);

        Log.d("TAG", "calculateRow: " + ((hourOfDay - Constants.INITIAL_HOUR) * HOUR_HEIGHT) + (minutesOfHour * (HOUR_HEIGHT / 60) + HOUR_HEIGHT_LINE / 2));

        return ((hourOfDay - Constants.INITIAL_HOUR) * HOUR_HEIGHT) + (minutesOfHour * (HOUR_HEIGHT / 60) + HOUR_HEIGHT_LINE / 2);
    }

    private int calculateRow(CourseEvent item) {
        Calendar calendar = GregorianCalendar.getInstance();

        calendar.setTime(item.getStartDatetime());

        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        int minutesOfHour = calendar.get(Calendar.MINUTE);

        Log.d("TAG", "calculateRow:=== " + ((hourOfDay - Constants.INITIAL_HOUR) * HOUR_HEIGHT) + (minutesOfHour * (HOUR_HEIGHT / 60) + HOUR_HEIGHT_LINE / 2));

        return ((hourOfDay - Constants.INITIAL_HOUR) * HOUR_HEIGHT) + (minutesOfHour * (HOUR_HEIGHT / 60) + HOUR_HEIGHT_LINE / 2);
    }


    public LayerDrawable getGradientDrawable() {
        Drawable[] layers = new Drawable[2];

        ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
        shapeDrawable.getPaint().setColor(Color.GRAY);
        shapeDrawable.getPaint().setStyle(Style.STROKE);
        shapeDrawable.getPaint().setStrokeWidth(1);
        shapeDrawable.getPaint().setPathEffect(new CornerPathEffect(20.0f));
        shapeDrawable.getPaint().setAntiAlias(true);

        GradientDrawable gradientDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{
                Color.parseColor("#eeeeee"), Color.parseColor("#d7d7d7")});
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setGradientRadius(90.0f);
        gradientDrawable.setCornerRadius(20.0f);

        layers[1] = shapeDrawable;
        layers[0] = gradientDrawable;

        return new LayerDrawable(layers);
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mScaleDetector.onTouchEvent(event))
            return true;
        return super.onTouchEvent(event);

    }
}