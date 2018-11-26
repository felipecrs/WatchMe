package com.readme.app.view.adapter;

import com.readme.app.view.fragment.BooksFragment;
import com.readme.app.view.fragment.MoviesFragment;
import com.readme.app.view.fragment.SeriesFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Books", "Series", "Movies" };

    public SimpleFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return BooksFragment.newInstance();
            case 1:
                return SeriesFragment.newInstance();
            case 2:
                return MoviesFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
