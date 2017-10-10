package com.persona5dex.fragments;

import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.persona5dex.R;
import com.persona5dex.activities.BaseActivity;

/**
 * Created by Rechee on 10/7/2017.
 */

public class SettingsFragment extends PreferenceFragment {

    public static final String ARG_NAMES = "names";
    public static final String ARG_VALUES = "values";
    private String[] dlcNames;
    private String[] dlcValues;
    private PreferenceManager preferenceManager;
    private BaseActivity activity;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        dlcNames = (String[]) getArguments().getCharSequenceArray(ARG_NAMES);
        dlcValues = (String[]) getArguments().getCharSequenceArray(ARG_VALUES);

        preferenceManager = getPreferenceManager();

        this.activity = (BaseActivity) getActivity();
        MultiSelectListPreference dlcPreference = (MultiSelectListPreference) preferenceManager.findPreference(activity.getString(R.string.pref_key_dlc));

        dlcPreference.setEntries(this.dlcNames);
        dlcPreference.setEntryValues(this.dlcValues);
    }

    public static SettingsFragment newInstance(String[][] dlcSettings){
        SettingsFragment settingsFragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putCharSequenceArray(ARG_NAMES, dlcSettings[0]);
        args.putCharSequenceArray(ARG_VALUES, dlcSettings[1]);
        settingsFragment.setArguments(args);
        return settingsFragment;
    }
}
