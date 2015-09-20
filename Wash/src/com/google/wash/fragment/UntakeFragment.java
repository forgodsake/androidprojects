package com.google.wash.fragment;

import java.util.Calendar;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.google.wash.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class UntakeFragment extends Fragment{
    
	@ViewInject(R.id.ll_appointment)
	private LinearLayout appointment;
	@ViewInject(R.id.textView6)
	private TextView texttime;
	private View window;
	private DatePicker datePicker;
	private TimePicker time1,time2;
	private int width;
	private String date ,timestart, timeend;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_order_untake, container, false);
		ViewUtils.inject(this, view);
		window = getLayoutInflater(savedInstanceState).inflate(R.layout.date_picker, null); 
		width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
		return view;
	}
	
	@OnClick(R.id.ll_appointment)
	private void click(View v){
		final PopupWindow pop = new PopupWindow(window,width-30,LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new ColorDrawable(0xffffffff));
		pop.setOutsideTouchable(true);
		pop.showAtLocation(getView().findViewById(R.id.ll_appointment),Gravity.CENTER,0,0);
		window.findViewById(R.id.button_cancel).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    pop.dismiss();
			}
		});
		window.findViewById(R.id.button_confirm).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Calendar calendar = Calendar.getInstance();
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
			    final int day = calendar.get(Calendar.DAY_OF_MONTH);
			    final int weekday = calendar.get(Calendar.DAY_OF_WEEK);
			    int hour = calendar.get(Calendar.HOUR_OF_DAY);
			    int minute = calendar.get(Calendar.MINUTE);
				datePicker = (DatePicker) window.findViewById(R.id.datePicker1);
				datePicker.init(year, month, day, new OnDateChangedListener() {
					
					@Override
					public void onDateChanged(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						date= ""+year+"-"+(monthOfYear+1)+"-"+dayOfMonth+
								"(周"+getweek(weekday+dayOfMonth-day)+")";
						
					}
				});
				time1 = (TimePicker) window.findViewById(R.id.timePicker1);
				time1.setIs24HourView(true);
				time1.setOnTimeChangedListener(new OnTimeChangedListener() {
					
					@Override
					public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
						String min = minute>9?""+minute:"0"+minute;
						timestart = "\n"+hourOfDay+":"+min;
						
					}
				});
				time2 = (TimePicker) window.findViewById(R.id.timePicker2);
				time2.setIs24HourView(true);
				time2.setOnTimeChangedListener(new OnTimeChangedListener() {
					
					@Override
					public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
						String min = minute>9?""+minute:"0"+minute;
					    timeend = "-"+hourOfDay+":"+min;
						
					}
				});
				String min = minute>9?""+minute:"0"+minute;
				if (date==null&&timestart!=null&&timeend!=null) {
					texttime.setText(""+year+"-"+(month+1)+"-"+day+
							"(周"+getweek(weekday)+")"+timestart+timeend);
				}else if (date==null||timestart==null||timeend==null) {
					texttime.setText(""+year+"-"+(month+1)+"-"+day+
							"(周"+getweek(weekday)+")"+"\n"+hour+":"+min+"-"+(hour+1)+":"+min);
				}else if (date!=null&&timestart!=null&&timeend!=null) {
					texttime.setText(date+timestart+timeend);
				}	
				texttime.setTextColor(Color.parseColor("#ff4d0f"));
				pop.dismiss();
			}
		});
	}
	private String getweek(int i){
		switch (i) {
		case 1:
			return "一";
		case 2:
			return "二";
		case 3:
			return "三";
		case 4:
			return "四";
		case 5:
			return "五";
		case 6:
			return "六";
		case 7:
			return "日";
		default:
			break;
		}
		return "二";
	}
}
