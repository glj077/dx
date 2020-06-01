package com.example.dx_4g;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dx_4g.funclass.BaseActivity;
import com.example.dx_4g.funclass.httpopenException;
import com.example.dx_4g.funclass.myApplication;

public class Main4Activity extends BaseActivity implements View.OnClickListener {

    TextView mTextLeftDate;
    TextView mTextLeftWeek;

    TextView mTextRightDate;
    TextView mTextRightWeek;

    TextView mTextMinRange;
    TextView mTextMaxRange;

    ImageView mTilteImage;

    private String startQueryTime;
    private String endQueryTime;

    CalendarView mCalendarView;

    int dateselectsign=0;

    @Override
    protected int getLayoutId() {
        return R.layout.alendarview;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void initView() {


        mTextLeftDate = findViewById(R.id.tv_left_date);
        mTextLeftWeek = findViewById(R.id.tv_left_week);
        mTextRightDate = findViewById(R.id.tv_right_date);
        mTextRightWeek = findViewById(R.id.tv_right_week);

        mTextMinRange = findViewById(R.id.tv_min_range);
        mTextMaxRange = findViewById(R.id.tv_max_range);

        mCalendarView = findViewById(R.id.calendarView);
        mTilteImage=findViewById(R.id.alarm_title_rego);


        findViewById(R.id.tv_commit).setOnClickListener(this);


        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                String selectdate=(month+1)+"月"+dayOfMonth+"日";
                dateselectsign=dateselectsign+1;
                if (dateselectsign==1) {
                    mTextLeftDate.setText(selectdate);
                    mTextRightDate.setText(null);
                    endQueryTime=null;
                    startQueryTime=year+"-"+isDateLength(month+1,10)+"-"+isDateLength(dayOfMonth,10)+" 00:00:00";
                }else{
                    mTextRightDate.setText(selectdate);
                    dateselectsign=0;
                    endQueryTime=year+"-"+isDateLength(month+1,10)+"-"+isDateLength(dayOfMonth,10)+" 00:00:00";
                }
            }
        });

        mTilteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myApplication.getInstance().setQuerytime(null);
                finish();
            }
        });




    }

    @Override
    protected void initData() throws httpopenException {
        myApplication.getInstance().setQuerytime(null);
        endQueryTime=null;
        startQueryTime=null;

    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_commit){
            if(endQueryTime!=null){
                String queryTime="from="+startQueryTime+" & to="+endQueryTime;
                myApplication.getInstance().setQuerytime(queryTime);
                //输入法处理
                InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Activity.INPUT_METHOD_SERVICE);
                boolean isOpen=imm.isActive();
                if (isOpen){
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                finish();
            }else{
                Toast mytoast=Toast.makeText(getApplication(),"请选择结束日期",Toast.LENGTH_LONG);
                mytoast.setGravity(Gravity.CENTER,0,190);
                mytoast.show();
            }

        }

    }

    @Override
    protected void onResume() {
        myApplication.getInstance().setQuerytime(null);
        endQueryTime=null;
        startQueryTime=null;
        super.onResume();
    }

    private String isDateLength(int date,int datalength){
        String revalue;
        if(date<datalength){
            revalue="0"+date;
        }
        else{
            revalue=String.valueOf(date);
        }
        return  revalue;
    }
}
