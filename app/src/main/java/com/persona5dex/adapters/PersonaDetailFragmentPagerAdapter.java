package com.persona5dex.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.persona5dex.R;
import com.persona5dex.fragments.PersonaDetailInfoFragment;
import com.persona5dex.fragments.PersonaElementsFragment;
import com.persona5dex.fragments.PersonaSkillsFragment;
import com.persona5dex.models.Persona;

/**
 * Created by Rechee on 7/30/2017.
 */

public class PersonaDetailFragmentPagerAdapter extends FragmentPagerAdapter {

    private final Context context;

    public PersonaDetailFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new PersonaDetailInfoFragment();
            case 1:
                return new PersonaElementsFragment();
            case 2:
                return new PersonaSkillsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getString(R.string.tab_name_info);
            case 1:
                return context.getString(R.string.tab_name_elements);
            case 2:
                return context.getString(R.string.tab_name_skills);
            default:
                return null;
        }
    }
}
