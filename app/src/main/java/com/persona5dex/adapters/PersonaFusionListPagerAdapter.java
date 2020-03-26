package com.persona5dex.adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.persona5dex.R;

import java.util.Locale;

/**
 * Created by Rechee on 7/30/2017.
 */

public class PersonaFusionListPagerAdapter extends FragmentPagerAdapter {
    private static final int FRAGMENT_COUNT = 2;
    private final Context context;
    private final Fragment toFragment;
    private final Fragment fromFragment;
    private Fragment currentFragment;

    public PersonaFusionListPagerAdapter(FragmentManager fm, Context context, Fragment toFragment,
                                         Fragment fromFragment) {
        super(fm);
        this.context = context;
        this.toFragment = toFragment;
        this.fromFragment = fromFragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
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
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        currentFragment = (Fragment) object;
    }

    @NonNull
    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return context.getString(R.string.to);
            case 1:
                return context.getString(R.string.from);
        }

        throw new IllegalStateException("cannot get page title for position: " + position);
    }
}
