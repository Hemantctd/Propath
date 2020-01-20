package org.ctdworld.propath.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterEducationForAthlete extends FragmentStatePagerAdapter
{

    private List<Fragment> listFragment = new ArrayList<>();
    private List<String> listTitle = new ArrayList<>();

    public void addItem(Fragment fragment, String title)
    {
        listFragment.add(fragment);
        listTitle.add(title);

    }

    public AdapterEducationForAthlete(FragmentManager fm)
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


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return super.instantiateItem(container, position);
    }


}