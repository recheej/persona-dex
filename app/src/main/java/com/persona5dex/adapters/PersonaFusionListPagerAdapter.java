package com.persona5dex.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.persona5dex.R;
import com.persona5dex.fragments.FusionListFragment;

/**
 * Created by Rechee on 7/30/2017.
 */

public class PersonaFusionListPagerAdapter extends FragmentPagerAdapter {
    private static final int FRAGMENT_COUNT = 2;
    private final Context context;
    private final Fragment toFragment;
    private final Fragment fromFragment;

    public PersonaFusionListPagerAdapter(FragmentManager fm, Context context, Fragment toFragment,
                                         Fragment fromFragment) {
        super(fm);
        this.context = context;
        this.toFragment = toFragment;
        this.fromFragment = fromFragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return toFragment;
            case 1:
                return fromFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getString(R.string.to);
            case 1:
                return context.getString(R.string.from);
            default:
                return null;
        }
    }
}
