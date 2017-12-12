package com.persona5dex.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v14.preference.MultiSelectListPreference;

import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.activities.BaseActivity;
import com.persona5dex.dagger.Persona5ApplicationComponent;
import com.persona5dex.viewmodels.SettingsViewModel;

/**
 * Created by Rechee on 10/7/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String ARG_NAMES = "names";
    public static final String ARG_VALUES = "values";
    private String[] dlcNames;
    private String[] dlcValues;
    private BaseActivity activity;

    private SettingsViewModel viewModel;
    private PreferenceManager preferenceManager;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {


        setPreferencesFromResource(R.xml.preferences, rootKey);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.activity = (BaseActivity) getActivity();
        preferenceManager = getPreferenceManager();

        Persona5ApplicationComponent component = Persona5Application.get(activity).getComponent();

        viewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        viewModel.init(component);


        final MultiSelectListPreference dlcPreference = (MultiSelectListPreference) preferenceManager
                .findPreference(activity.getString(R.string.pref_key_dlc));

        viewModel.getDLCPersonaForSettings().observe(this, new Observer<String[][]>() {
            @Override
            public void onChanged(@Nullable String[][] dlcInfo) {
                if(dlcInfo == null){
                    dlcInfo = new String[0][0];
                }

                String[] dlcNames = dlcInfo[0];
                String[] dlcValues = dlcInfo[1];

                dlcPreference.setEntries(dlcNames);
                dlcPreference.setEntryValues(dlcValues);
            }
        });

//
//        addPreferencesFromResource(R.xml.preferences);
//        dlcNames = (String[]) getArguments().getCharSequenceArray(ARG_NAMES);
//        dlcValues = (String[]) getArguments().getCharSequenceArray(ARG_VALUES);


    }
//
//    public static SettingsFragment newInstance(String[][] dlcSettings){
//        SettingsFragment settingsFragment = new SettingsFragment();
//        Bundle args = new Bundle();
//        args.putCharSequenceArray(ARG_NAMES, dlcSettings[0]);
//        args.putCharSequenceArray(ARG_VALUES, dlcSettings[1]);
//        settingsFragment.setArguments(args);
//        return settingsFragment;
//    }
}
