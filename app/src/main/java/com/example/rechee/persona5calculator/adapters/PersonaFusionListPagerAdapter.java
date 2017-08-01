package com.example.rechee.persona5calculator.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.fragments.FusionListFragment;

/**
 * Created by Rechee on 7/30/2017.
 */

public class PersonaFusionListPagerAdapter extends FragmentPagerAdapter {
    private final Context context;
    private final String personaName;

    public PersonaFusionListPagerAdapter(FragmentManager fm, Context context, String personaName) {
        super(fm);
        this.context = context;
        this.personaName = personaName;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return FusionListFragment.newInstance(true, personaName);
            case 1:
                return FusionListFragment.newInstance(false, personaName);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
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
