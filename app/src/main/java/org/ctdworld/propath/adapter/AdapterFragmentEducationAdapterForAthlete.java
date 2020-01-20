package org.ctdworld.propath.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdapterFragmentEducationAdapterForAthlete extends FragmentStatePagerAdapter
{

    private List<Fragment> listFragment = new ArrayList<>();
    private List<String> listTitle = new ArrayList<>();

    public void addItem(Fragment fragment, String title)
    {
        listFragment.add(fragment);
        listTitle.add(title);
    }

    public AdapterFragmentEducationAdapterForAthlete(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        return listFragment.get(position);
    }


    @Override
    public int getCount() {
        return listFragment.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {

        return listTitle.get(position);
    }


}