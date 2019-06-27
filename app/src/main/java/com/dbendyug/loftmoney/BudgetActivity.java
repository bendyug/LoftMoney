package com.dbendyug.loftmoney;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

public class BudgetActivity extends AppCompatActivity {

    public static final String INCOME = "income";
    public static final String OUTCOME = "outcome";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BudgetViewPagerAdapter budgetViewPagerAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        budgetViewPagerAdapter = new BudgetViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(budgetViewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(R.string.outcome);
        tabLayout.getTabAt(1).setText(R.string.income);

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_indicator_color));

    }

    static class BudgetViewPagerAdapter extends FragmentPagerAdapter {

        public BudgetViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case (0):
                    return BudgetFragment.newInstance(R.color.outcome_price_color);
                case (1):
                    return BudgetFragment.newInstance(R.color.income_price_color);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
