package zzw.velo;

/**
 * Created by zzw on 2015/12/20.
 */
import java.util.Timer;
import java.util.TimerTask;

import zzw.velo.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

public class FragmentPage1 extends Fragment{
    public TextView TV_wei;//TVˮ��
    public TextView TV_tem;//TVˮ��
    public static  DBHelper1 helper1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_1, null);
        TV_wei=(TextView)view.findViewById(R.id.tv_speed);
        TV_tem=(TextView)view.findViewById(R.id.tv_slope);
        new Thread(new Runnable() {
            public void run() {
                while(true)
                {
                    Log.e("�л�","1");

                    TV_wei.post(new Runnable() {
                        public void run() {
                            TV_wei.setText(MainTabActivity.WEIGHT);
                        }
                    });
                    TV_tem.post(new Runnable() {
                        public void run() {
                            TV_tem.setText(MainTabActivity.TEMPERATURE);
                        }
                    });
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // TODO �Զ���ɵ� catch ��
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return view;
    }
}

