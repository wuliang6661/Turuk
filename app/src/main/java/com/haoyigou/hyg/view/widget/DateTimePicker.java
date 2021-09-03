package com.haoyigou.hyg.view.widget;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.haoyigou.hyg.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Administrator on 2016/9/13.
 */
public class DateTimePicker extends Activity {
    private DatePicker datePicker;
    private TimePicker timePicker;

          @Override
          protected void onCreate(Bundle savedInstanceState) {
                 super.onCreate(savedInstanceState);
                setContentView(R.layout.item_datepicker);

                 datePicker = (DatePicker) findViewById(R.id.dpPicker);
                // timePicker = (TimePicker) findViewById(R.id.tpPicker);

                 datePicker.init(2013, 8, 20, new DatePicker.OnDateChangedListener() {

                                 @Override
                      public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // 获取一个日历对象，并初始化为当前选中的时间
                                 Calendar calendar = Calendar.getInstance();
                              calendar.set(year, monthOfYear, dayOfMonth);
                               SimpleDateFormat format = new SimpleDateFormat(
                                                "yyyy年MM月dd日  HH:mm");
                                 Toast.makeText(getApplicationContext(), format.format(calendar.getTime()), Toast.LENGTH_SHORT)
                                         .show();
                             }
                     });

                timePicker.setIs24HourView(true);
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        Toast.makeText(getApplicationContext(), hourOfDay + "小时" + minute + "分钟",
                                Toast.LENGTH_SHORT).show();
                    }
                });

             }
}
