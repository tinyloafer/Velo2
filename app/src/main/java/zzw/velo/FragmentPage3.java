package zzw.velo;

/**
 * Created by zzw on 2015/12/20.
 */
import java.io.IOException;
import java.util.Calendar;

import zzw.velo.R;
import zzw.velo.MainTabActivity.readData;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class FragmentPage3 extends Fragment{
    ToggleButton button1;
    ToggleButton button2;
    Button button_setclock;
    Calendar c=Calendar.getInstance();
    int mHour;
    int mMinute;
    int num=50;
    Handler handler;
    TextView myHour;
    TextView myMinute;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_3, null);
//		InitTmpView(view);
//		InitClockView(view);
//
        return view;
    }

    public void findBike(View v){

    }
//	public void InitTmpView(final View parentview)
//	{
//
//		ToggleButton button1=(ToggleButton)parentview.findViewById(R.id.COTmpButton);
//		Button buttontmp=(Button)parentview.findViewById(R.id.btTmp);
//		final EditText ettmp=(EditText)parentview.findViewById(R.id.exTmp);
//		LinearLayout ly1=(LinearLayout)parentview.findViewById(R.id.linearlayout1);
//		ettmp.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
//		ettmp.setText(String.valueOf(num));
//		button1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				// TODO �Զ���ɵķ������
//				if(isChecked)
//				{
//					Toast.makeText(parentview.getContext(), "ˮ�������ѿ���", Toast.LENGTH_SHORT).show();
//					compareData data=new compareData();
//					data.start();
//
//				/*	handler.post(new Runnable() {
//						@Override
//						public void run() {
//							while(true)
//							{
//								int tmp=Math.abs(Integer.valueOf(MainTabActivity.TEMPERATURE)-num);
//								if(tmp<=2)
//								{
//									Log.e("�¶�����","123");
//									showToast();
//									Log.e("�¶�����","12345");
//								}
//							}
//
//						}
//
//						private void showToast() {
//							// TODO �Զ���ɵķ������
//							Toast toast=Toast.makeText(parentview.getContext(), "���Ժ�ˮ��", Toast.LENGTH_SHORT);
//							toast.show();
//						}
//						});*/
//
//				}
//				else
//				{
//					Toast.makeText(parentview.getContext(), "ˮ�������ѹر�", Toast.LENGTH_SHORT).show();
//					Thread.interrupted();
//				}
//			}
//		});
//		buttontmp.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				ettmp.clearFocus();
//				num=Integer.parseInt(ettmp.getText().toString());
//
//				if(num<0)
//				{	num=0;
//					Toast.makeText(parentview.getContext(), "������һ������0������",Toast.LENGTH_SHORT).show();
//					ettmp.setText(String.valueOf(num));
//				}
//				if(num>100)
//				{
//					num=100;
//					Toast.makeText(parentview.getContext(), "������һ��С��100������",Toast.LENGTH_SHORT).show();
//					ettmp.setText(String.valueOf(num));
//				}
//
//
//				// TODO �Զ���ɵķ������
//
//			}
//
//		});
//
//
//	}
//
//	public void InitClockView(final View parentview)
//	{
//		ToggleButton button2=(ToggleButton)parentview.findViewById(R.id.COTimeButton);
//		final TextView myHour=(TextView)parentview.findViewById(R.id.tv_hour);
//		final TextView myMinute=(TextView)parentview.findViewById(R.id.tv_minute);
//		button2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
//		{
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				// TODO �Զ���ɵķ������
//				if(isChecked)
//				{
//					Toast.makeText(parentview.getContext(), "��ˮ�����ѿ���", Toast.LENGTH_SHORT).show();
//					c.setTimeInMillis(System.currentTimeMillis());
//					int hour=c.get(Calendar.HOUR_OF_DAY);
//					int minute=c.get(Calendar.MINUTE);
//					new TimePickerDialog(parentview.getContext(),new TimePickerDialog.OnTimeSetListener() {
//
//						@Override
//						public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//							// TODO �Զ���ɵķ������
//							c.setTimeInMillis(System.currentTimeMillis());
//                            c.set(Calendar.HOUR, hourOfDay);        //��������Сʱ��
//                            c.set(Calendar.MINUTE, minute);
//                            c.set(Calendar.SECOND, 0);
//                            c.set(Calendar.MILLISECOND, 0);
//                            Toast.makeText(parentview.getContext(), "��ѡ�����Ѻ�ˮʱ����:"+hourOfDay+"ʱ"+minute+"��", Toast.LENGTH_SHORT).show();
//                            myHour.setText(hourOfDay+" : ");
//                            myHour.setTextColor(0xff000000);
//                            myMinute.setText(minute+"");
//                            myMinute.setTextColor(0xff000000);
//                            Intent intent = new Intent(parentview.getContext(), AlarmReceiver.class);
//                            PendingIntent pendingIntent = PendingIntent.getBroadcast(parentview.getContext(), 0,intent, 0);
//                            AlarmManager am1=(AlarmManager)getActivity().getSystemService(parentview.getContext().ALARM_SERVICE);
//                            /* �������� */
//                            am1.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
//                            am1.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (10 * 1000),(24 * 60 * 60 * 1000), pendingIntent);
//
//						}
//
//					}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
//
//				}
//				else
//				{
//					Toast.makeText(parentview.getContext(), "��ˮ�����ѹر�", Toast.LENGTH_SHORT).show();
//					Intent intent = new Intent(parentview.getContext(), AlarmReceiver.class);
//				    PendingIntent pendingIntent = PendingIntent.getBroadcast(parentview.getContext(), 0, intent, 0);
//				    AlarmManager am = (AlarmManager)getActivity().getSystemService(parentview.getContext().ALARM_SERVICE);
//				    am.cancel(pendingIntent);
//				    myHour.setText("00 : ");
//				    myHour.setTextColor(0xffC0C0C0);
//				    myMinute.setText("00");
//				    myMinute.setTextColor(0xffC0C0C0);
//				}
//			}
//		});
//
//
//	}
//
//
//	class compareData extends Thread{
//		public void run(){
//			while(true)
//			{
//			//	Log.e("�¶�����","123");
//				int tmp=Math.abs(Integer.valueOf(MainTabActivity.TEMPERATURE)-num);
//				if(tmp<=1)
//				{
//
//					MediaPlayer mp=new MediaPlayer();
//					  mp.reset();
//					  try {
//						mp.setDataSource("sdcard/SmartCup/test2.mp3");
//					} catch (IllegalArgumentException e) {
//						// TODO �Զ���ɵ� catch ��
//						e.printStackTrace();
//					} catch (SecurityException e) {
//						// TODO �Զ���ɵ� catch ��
//						e.printStackTrace();
//					} catch (IllegalStateException e) {
//						// TODO �Զ���ɵ� catch ��
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO �Զ���ɵ� catch ��
//						e.printStackTrace();
//					}
//					  try {
//						mp.prepare();
//					} catch (IllegalStateException e) {
//						// TODO �Զ���ɵ� catch ��
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO �Զ���ɵ� catch ��
//						e.printStackTrace();
//					}
//					Log.e("�¶�����","12345");
//					mp.setVolume(1, 1);
//					  mp.start();
//					  break;
//				}
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO �Զ���ɵ� catch ��
//					e.printStackTrace();
//				}
//
//			}
//
//		}
//
//	}
//
}




