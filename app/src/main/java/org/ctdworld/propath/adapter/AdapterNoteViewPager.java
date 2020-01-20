package org.ctdworld.propath.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.ctdworld.propath.fragment.FragmentNotesCategory;
import org.ctdworld.propath.fragment.FragmentNotesAll;

public class AdapterNoteViewPager extends FragmentPagerAdapter {
    private int numOfTabs;

    public AdapterNoteViewPager(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }



    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                return new FragmentNotesAll();
            case 1:
                return new FragmentNotesCategory();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }



}
