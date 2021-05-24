package com.oobiliuoo.parkingtong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{
    ViewPager2 viewPager;

    private LinearLayout llHome, llOrder,llMine;
    private ImageView ivHome, ivOrder,ivMine,ivCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPager();
        initTabView();
    }

    private void initTabView() {

        llHome = findViewById(R.id.id_tab_home);
        llHome.setOnClickListener(this);
        llOrder = findViewById(R.id.id_tab_order);
        llOrder.setOnClickListener(this);
        llMine = findViewById(R.id.id_tab_mine);
        llMine.setOnClickListener(this);

        ivHome = findViewById(R.id.id_iv_home);
        ivOrder = findViewById(R.id.id_iv_order);
        ivMine = findViewById(R.id.id_iv_mine);

        ivHome.setSelected(true);
        ivCurrent = ivHome;
    }

    private void initPager() {
        viewPager = findViewById(R.id.id_viewpager);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance("通信录"));
        fragments.add(HomeFragment.newInstance("发现"));
        fragments.add(HomeFragment.newInstance("我"));
        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),getLifecycle(),fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                changeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }

    private void changeTab(int position) {
        ivCurrent.setSelected(false);
        switch (position){
            case R.id.id_tab_home:
                viewPager.setCurrentItem(0);
            case 0:
                ivHome.setSelected(true);
                ivCurrent = ivHome;
                break;
            case R.id.id_tab_order:
                viewPager.setCurrentItem(1);
            case 1:
                ivOrder.setSelected(true);
                ivCurrent = ivOrder;
                break;
            case R.id.id_tab_mine:
                viewPager.setCurrentItem(2);
            case 2:
                ivMine.setSelected(true);
                ivCurrent = ivMine;
                break;

        }
    }

    @Override
    public void onClick(View v) {
        changeTab(v.getId());
    }

}