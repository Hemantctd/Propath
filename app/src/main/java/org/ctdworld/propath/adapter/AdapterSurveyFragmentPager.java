package org.ctdworld.propath.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdapterSurveyFragmentPager extends FragmentStatePagerAdapter
{

    private List<Fragment> listFragment = new ArrayList<>();
    private List<String> listTitle = new ArrayList<>();
    Context mContext;

    public void addItem(Fragment fragment, String title)
    {
        listFragment.add(fragment);
        listTitle.add(title);
    }

    public AdapterSurveyFragmentPager(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        if (listFragment != null)
            return listFragment.get(position);
        else
            return null;
    }


    @Override
    public int getCount() {
        if (listFragment != null)
            return listFragment.size();
        else
            return 0;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        if (listTitle != null)
            return listTitle.get(position);
        else
            return null;
    }


}
