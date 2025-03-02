package com.skythinker.gptassistant.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.skythinker.gptassistant.R;
import com.skythinker.gptassistant.fragment.CreateFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    ViewPager2 viewPager;
    List<Fragment> fragmentList;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initData();
        setMyAdapter();
    }

    public void initData(){
        viewPager = findViewById(R.id.viewpager2bottom);
        bottomNavigationView = findViewById(R.id.bootomnav2);
        setupViewPagerWithBottomNav();

        fragmentList = new ArrayList<>();
        fragmentList.add(new CreateFragment());
        fragmentList.add(new CreateFragment());

        viewPager.setCurrentItem(1, false);
        bottomNavigationView.setSelectedItemId(R.id.create_item);
    }
    private void setupViewPagerWithBottomNav() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(MainActivity2.this);
        viewPager.setAdapter(adapter);

        // 禁止左右滑动 (可选)
        viewPager.setUserInputEnabled(false);
        // ViewPager2 页面切换监听
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // 更新 BottomNavigationView 选中项
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });

        // BottomNavigationView 点击监听
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home_item){
                    viewPager.setCurrentItem(0, false);
                    return true;
                }else if (item.getItemId() == R.id.create_item) {
                    viewPager.setCurrentItem(1, false);
                    return true;
                }

                return false;
            }
        });

        // 监听 ViewPager 页面切换，自动更新 BottomNavigationView 选中状态
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.home_item);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.create_item);
                        break;
                }
            }
        });
    }
    private void setMyAdapter() {

    }

    class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new CreateFragment();
                case 1:
                    return new CreateFragment();
                default:
                    return new CreateFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2; // Fragment 数量
        }
    }
}