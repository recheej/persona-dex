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
    private final int personaID;

    public PersonaFusionListPagerAdapter(FragmentManager fm, Context context, int personaID) {
        super(fm);
        this.context = context;
        this.personaID = personaID;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return FusionListFragment.newInstance(true, personaID);
            case 1:
                return FusionListFragment.newInstance(false, personaID);
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
