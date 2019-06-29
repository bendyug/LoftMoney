package com.dbendyug.loftmoney;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.ListFragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class BudgetActivity extends AppCompatActivity {

    public static final String INCOME = "income";
    public static final String OUTCOME = "outcome";



    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BudgetViewPagerAdapter budgetViewPagerAdapter;
    List<BudgetFragment> budgetFragmentList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);



        budgetViewPagerAdapter = new BudgetViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        for (int i = 0; i < 2; i++) {
            budgetFragmentList.add(BudgetFragment.newInstance(R.color.outcome_price_color));
        }

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(budgetViewPagerAdapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == budgetFragmentList.size() - 1){
                    budgetFragmentList.add(BudgetFragment.newInstance(R.color.outcome_price_color));
                    budgetViewPagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.getTabAt(0).setText(R.string.outcome);
//        tabLayout.getTabAt(1).setText(R.string.income);

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_indicator_color));

    }

    class BudgetViewPagerAdapter extends FragmentPagerAdapter {



        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return "Tab " + position;
        }

        public BudgetViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
            }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            return budgetFragmentList.get(position);

//            switch (position){
//                case (0):
//                    budgetFragmentList.add(BudgetFragment.newInstance(R.color.outcome_price_color));
//                    return budgetFragmentList.get(position);
//                case (1):
//                    budgetFragmentList.add(BudgetFragment.newInstance(R.color.income_price_color));
//                    return budgetFragmentList.get(position);
//            }
//            return null;
        }


        @Override
        public int getCount() {
            return budgetFragmentList.size();
        }
    }
}
