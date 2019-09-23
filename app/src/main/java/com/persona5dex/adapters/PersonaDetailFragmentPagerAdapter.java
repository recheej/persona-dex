package com.persona5dex.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.persona5dex.R;
import com.persona5dex.fragments.PersonaDetailInfoFragment;
import com.persona5dex.fragments.PersonaElementsFragment;
import com.persona5dex.fragments.PersonaSkillsFragment;

/**
 * Created by Rechee on 7/30/2017.
 */

public class PersonaDetailFragmentPagerAdapter extends FragmentPagerAdapter {

    private final Context context;
    private final int personaID;

    public PersonaDetailFragmentPagerAdapter(FragmentManager fm, Context context, int personaID) {
        super(fm);
        this.context = context;
        this.personaID = personaID;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return PersonaDetailInfoFragment.newInstance(personaID);
            case 1:
                return PersonaElementsFragment.newInstance(personaID);
                //return new PersonaElementsFragment();
            case 2:
                return PersonaSkillsFragment.newInstance(personaID);
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
