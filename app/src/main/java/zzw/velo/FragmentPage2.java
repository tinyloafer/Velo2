package zzw.velo;

/**
 * Created by zzw on 2015/12/20.
 */

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import java.util.ArrayList;

import zzw.velo.R;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class FragmentPage2 extends Fragment {

    Resources resources;
    private ViewPager mPager;//ҳ������
    private ArrayList<Fragment> fragmentsList;
    private ImageView ivBottomLine;//����ͼƬ
    private TextView tvTabMonth, tvTabWeek,tvTabDay;

    private int currIndex = 0;//��ǰҳ�����
    private int bottomLineWidth;//����ͼƬ���
    private int offset = 0;//����ͼƬƫ����
    private int position_one;
    private int position_two;
    public final static int num = 3 ;
    Fragment page2_month;
    Fragment page2_week;
    Fragment page2_day;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2, null);
        resources = getResources();




        InitWidth(view);
        InitTextView(view);
        InitViewPager(view);
        TranslateAnimation animation = new TranslateAnimation(position_two,offset, 0, 0);
        animation.setFillAfter(true);
        animation.setDuration(300);
        ivBottomLine.startAnimation(animation);
        return view;




    }

    //��ʼ��ͷ��
    private void InitTextView(View parentView) {
        tvTabMonth = (TextView) parentView.findViewById(R.id.tv_month);
        tvTabWeek = (TextView) parentView.findViewById(R.id.tv_week);
        tvTabDay = (TextView) parentView.findViewById(R.id.tv_day);

        tvTabMonth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mPager.setCurrentItem(0);
            }
        });
        tvTabWeek.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mPager.setCurrentItem(1);
            }
        });
        tvTabDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mPager.setCurrentItem(2);
            }
        });

    }

    private void InitViewPager(View parentView) {


        mPager = (ViewPager) parentView.findViewById(R.id.vPager);
        fragmentsList = new ArrayList<Fragment>();
        page2_month = new FragmentPage2_Month();
        page2_week = new FragmentPage2_Week();
        page2_day = new FragmentPage2_Day();
        fragmentsList.add(page2_month);
        fragmentsList.add(page2_week);
        fragmentsList.add(page2_day);

        mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentsList));

        mPager.setOnPageChangeListener(new MyOnPageChangeListener());

        mPager.setCurrentItem(0);




    }

    //��ʼ������
    private void InitWidth(View parentView) {
        ivBottomLine = (ImageView) parentView.findViewById(R.id.iv_bottom_line);
        bottomLineWidth = ivBottomLine.getLayoutParams().width;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (int) ((screenW / num - bottomLineWidth) / 2);
        int avg = (int) (screenW / num);
        position_one = avg + offset - 8;
        position_two = 2 * avg + offset - 18;


    }


    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(position_one, offset, 0, 0);
                        tvTabWeek.setTextColor(0xFF000000);

                    }
                    if (currIndex == 2) {
                        animation = new TranslateAnimation(position_two, offset, 0, 0);
                        tvTabDay.setTextColor(0xFF000000);
                    }
                    tvTabMonth.setTextColor(0xFFFF9933);
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_one, 0, 0);
                        tvTabMonth.setTextColor(0xFF000000);
                    }
                    if (currIndex == 2) {
                        animation = new TranslateAnimation(position_two, position_one, 0, 0);
                        tvTabDay.setTextColor(0xFF000000);
                    }
                    tvTabWeek.setTextColor(0xFFFF9933);
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_two, 0, 0);
                        tvTabMonth.setTextColor(0xFF000000);
                    }
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(position_one, position_two, 0, 0);
                        tvTabWeek.setTextColor(0xFF000000);
                    }
                    tvTabDay.setTextColor(0xFFFF9933);
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(300);
            ivBottomLine.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }


}