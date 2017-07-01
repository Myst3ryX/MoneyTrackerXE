package com.myst3ry.moneytrackerxe;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TabLayout tabs = (TabLayout) findViewById(R.id.main_tabs);
        final ViewPager pages = (ViewPager) findViewById(R.id.main_pager);

        pages.setAdapter(new MainPagerAdapter());
        tabs.setupWithViewPager(pages);

        getSupportActionBar().setElevation(0);
    }

    private class MainPagerAdapter extends FragmentPagerAdapter {

        private final String[] titles;

        MainPagerAdapter() {
            super(getSupportFragmentManager());
            titles = getResources().getStringArray(R.array.main_titles);
        }

        @Override
        public Fragment getItem(int position) {

            Bundle args = new Bundle();

            switch (position) {
                case 0:
                    args.putString(ItemsFragment.ARG_TYPE, Item.EXP_TYPE);
                    break;
                case 1:
                    args.putString(ItemsFragment.ARG_TYPE, Item.INC_TYPE);
                    break;
                case 2:
                    return new BalanceFragment();
            }

            Fragment fragment = new ItemsFragment();
            fragment.setArguments(args);

            return fragment;
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
