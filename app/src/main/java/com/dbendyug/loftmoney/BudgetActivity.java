package com.dbendyug.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;


public class BudgetActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BudgetViewPagerAdapter budgetViewPagerAdapter;
    private Window window;
    private FloatingActionButton openAddScreenButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        budgetViewPagerAdapter = new BudgetViewPagerAdapter(getSupportFragmentManager());

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(budgetViewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (BudgetFragment.actionMode != null) {
                    BudgetFragment.actionMode.finish();
                }
                if (position == 2) {
                    openAddScreenButton.hide();
                } else {
                    openAddScreenButton.show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        window = getWindow();

        tabLayout.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setText(R.string.expense);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText(R.string.income);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setText(R.string.balance);

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_indicator_color));

        openAddScreenButton = findViewById(R.id.fab_add_screen);
        openAddScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                for (Fragment fragment : fragmentManager.getFragments()) {
                    if (fragment.getUserVisibleHint()) {
                        fragment.startActivityForResult(new Intent(BudgetActivity.this, AddItemActivity.class), BudgetFragment.REQUEST_CODE);
                    }
                }
                overridePendingTransition(R.anim.anim_from_right, R.anim.anim_to_left);
            }
        });
    }

    public void setStatusBarColor(int colorId) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(colorId));
    }


    @Override
    public void onSupportActionModeStarted(@NonNull androidx.appcompat.view.ActionMode mode) {
        super.onSupportActionModeStarted(mode);

        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_background_item_selected_color));
        tabLayout.setBackgroundColor(getResources().getColor(R.color.toolbar_background_item_selected_color));
        setStatusBarColor(R.color.status_bar_background_item_selected_color);
        mode.setTitle(getResources().getString(R.string.items_selected_count) + ItemsAdapter.selectedItems.size());
        openAddScreenButton.hide();
    }

    @Override
    public void onSupportActionModeFinished(@NonNull androidx.appcompat.view.ActionMode mode) {
        super.onSupportActionModeFinished(mode);

        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setStatusBarColor(R.color.colorPrimaryDark);
        openAddScreenButton.show();
    }

    static class BudgetViewPagerAdapter extends FragmentPagerAdapter {

        public BudgetViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case (0):
                    return BudgetFragment.newInstance(FragmentType.expense);
                case (1):
                    return BudgetFragment.newInstance(FragmentType.income);
                case (2):
                    return BalanceFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
