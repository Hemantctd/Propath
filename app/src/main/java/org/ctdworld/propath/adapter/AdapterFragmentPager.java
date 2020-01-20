package org.ctdworld.propath.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdapterFragmentPager extends FragmentStatePagerAdapter
{

    private List<Fragment> listFragment = new ArrayList<>();
    private List<String> listTitle = new ArrayList<>();
    private Context mContext;
    private SparseArray<Fragment> registerFragments = new SparseArray<>();

    public AdapterFragmentPager(FragmentManager fm)
    {
        super(fm);
    }



    public void addItem(Fragment fragment, String title)
    {
        listFragment.add(fragment);
        listTitle.add(title);
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



    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position)
    {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registerFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        registerFragments.remove(position);
        super.destroyItem(container, position, object);
    }


    // to get current fragment instance
    public Fragment getRegisteredFragment(int position)
    {
        return registerFragments.get(position);
    }


}
