package com.myst3ry.moneytrackerxe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabs;
    private ViewPager pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabs = (TabLayout) findViewById(R.id.main_tabs);
        pages = (ViewPager) findViewById(R.id.main_pager);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!((LSApp) getApplication()).isLoggedIn()) {
            startActivity(new Intent(this, AuthActivity.class));
        } else {
            initUI();
        }
    }

    private void initUI() {
        if (pages.getAdapter() != null)
            return;

        pages.setAdapter(new MainPagerAdapter());
        tabs.setupWithViewPager(pages);
    }

    private class MainPagerAdapter extends FragmentPagerAdapter {

        private final String[] titles;
        private final String[] types = {Item.EXP_TYPE, Item.INC_TYPE};

        MainPagerAdapter() {
            super(getSupportFragmentManager());
            titles = getResources().getStringArray(R.array.main_titles);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                case 1:
                    Bundle args = new Bundle();
                    Fragment fragment = new ItemsFragment();
                    args.putString(ItemsFragment.ARG_TYPE, types[position]);
                    fragment.setArguments(args);
                    return fragment;
                case 2:
                    return new BalanceFragment();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }
}
