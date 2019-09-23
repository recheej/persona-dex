package com.persona5dex.adapters;

import android.content.Context;
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
    private int toListCount;
    private int fromListCount;

    public PersonaFusionListPagerAdapter(FragmentManager fm, Context context, Fragment toFragment,
                                         Fragment fromFragment) {
        super(fm);
        this.context = context;
        this.toFragment = toFragment;
        this.fromFragment = fromFragment;
    }

    public void setToListCount(int toListCount) {
        this.toListCount = toListCount;
    }

    public void setFromListCount(int fromListCount) {
        this.fromListCount = fromListCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
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
        switch (position) {
            case 0:
                String to = context.getString(R.string.to);

                if (toListCount == 0) {
                    return to;
                }

                return String.format(Locale.getDefault(), "%s (%d)", to, toListCount);
            case 1:
                String from = context.getString(R.string.from);
                if (fromListCount == 0) {
                    return from;
                }

                return String.format(Locale.getDefault(), "%s (%d)", from, fromListCount);
            default:
                return null;
        }
    }
}
