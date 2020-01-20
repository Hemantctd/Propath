package org.ctdworld.propath.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.DateTimeHelper;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ActivityEvent extends AppCompatActivity
{
    private final String TAG = ActivityEvent.class.getSimpleName();

    Context mContext;
    Toolbar mToolbar;
    TextView mToolbarTxtTitle;

    ImageView mImgPreviousMonth, mImgNextMonth;
    CompactCalendarView mCalender;
    TextView mTxtMonthName;

    DateTimeHelper mDateTimeHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

      //  Log.i(TAG,"Firebase token = "+ SessionHelper.getInstance(this).getFirebaseRegistrationToken());

        init();
        setToolbar();
        setListeners();
        setMonthName(mCalender.getFirstDayOfCurrentMonth()); // setting month name
        mCalender.setLocale(TimeZone.getDefault(), Locale.getDefault());  // setting time zone and locale





        // setting events for testing
        String strDate1 = "10-10-2018";
        String strDate2 = "20-10-2018";


      //  SimpleDateFormat dateParser = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date1 = mDateTimeHelper.getDateDayMonthYear(strDate1);
            Date date2 = mDateTimeHelper.getDateDayMonthYear(strDate2);

          /*  Log.i(TAG," date 1 = "+mDateTimeHelper.getDateStringDayMonthYear(date1));
            Log.i(TAG," date time = "+mDateTimeHelper.getMessageDateTime(date1));
            Log.i(TAG,"month year = "+mDateTimeHelper.getMonthNameWithYear(date1));
            Log.i(TAG,"month year = "+mDateTimeHelper.getMonthNameWithYear(strDate1));*/

            addEvent(date1);
            addEvent(date2);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setListeners()
    {
        // showing previous month
        mImgPreviousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCalender.scrollLeft();
            }
        });

        // showing next month
        mImgNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCalender.scrollRight();
            }
        });

        mCalender.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked)
            {
                Log.i(TAG,"dateClicked = "+dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth)
            {
                setMonthName(firstDayOfNewMonth);
            }
        });

    }

    private void init() {
        mContext = this;
        mDateTimeHelper = new DateTimeHelper();

        mToolbarTxtTitle = findViewById(R.id.toolbar_txt_title);
        mToolbar = findViewById(R.id.toolbar);

        mImgPreviousMonth = findViewById(R.id.event_img_previous_month);
        mImgNextMonth = findViewById(R.id.event_img_next_month);
        mTxtMonthName = findViewById(R.id.event_txt_month_name);
        mCalender = findViewById(R.id.event_calendar);
    }


    private void addEvent(Date date)
    {
        Event event = new Event(Color.BLUE,date.getTime());
        mCalender.addEvent(event);
    }


    private void setMonthName(Date date)
    {
       // SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
       // String strDate = dateFormat.format(date);
        mTxtMonthName.setText(mDateTimeHelper.getMonthNameWithYear(mCalender.getFirstDayOfCurrentMonth()));
    }

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        mToolbarTxtTitle.setText("Event");

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }

}
