package org.ctdworld.propath.adapter;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.ctdworld.propath.activity.ActivityContact;
import org.ctdworld.propath.fragment.FragmentContact;
import org.ctdworld.propath.fragment.FragmentGroupChatInjuryManagement;

public class AdapterViewPagerContact extends FragmentStatePagerAdapter
{
    SparseArray<Fragment> registerFragments = new SparseArray<>();

    public AdapterViewPagerContact(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = new Fragment();
        switch (position)
        {
            case ActivityContact.FRAGMENT_CONTACT:
                fragment = new FragmentContact();
                break;
            case ActivityContact.FRAGMENT_GROUP:
                fragment = new FragmentGroupChatInjuryManagement();
                break;
        }

        return fragment;
    }


    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        String title = "";
        switch (position)
        {
            case ActivityContact.FRAGMENT_CONTACT:
                title = "Contacts";
                break;
            case ActivityContact.FRAGMENT_GROUP:
                title = "Groups";
                break;
        }

        return title;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
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
