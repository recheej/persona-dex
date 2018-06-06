package com.persona5dex.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v14.preference.MultiSelectListPreference;
import android.support.v7.preference.PreferenceScreen;

import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.activities.BaseActivity;
import com.persona5dex.dagger.activity.ActivityContextModule;
import com.persona5dex.dagger.activity.LayoutModule;
import com.persona5dex.dagger.activity.ViewModelModule;
import com.persona5dex.dagger.activity.ViewModelRepositoryModule;
import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.uploaddatabase.UploadDatabaseDialogPreference;
import com.persona5dex.uploaddatabase.UploadDbPreferenceDialogFragment;
import com.persona5dex.viewmodels.SettingsViewModel;
import com.persona5dex.viewmodels.ViewModelFactory;

import javax.inject.Inject;

/**
 * Created by Rechee on 10/7/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    private BaseActivity activity;

    @Inject
    ViewModelFactory viewModelFactory;

    private SettingsViewModel viewModel;
    private PreferenceManager preferenceManager;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        preferenceManager = getPreferenceManager();

        PreferenceScreen preferenceScreen = preferenceManager.getPreferenceScreen();
        preferenceScreen.addPreference(new UploadDatabaseDialogPreference(this.getContext()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.activity = (BaseActivity) getActivity();

        Persona5Application.get(activity)
                .getComponent()
                .viewModelComponent(new AndroidViewModelRepositoryModule())
                .activityComponent(new LayoutModule(activity),
                        new ActivityContextModule(activity),
                        new ViewModelModule(),
                        new ViewModelRepositoryModule())
                .plus()
                .inject(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SettingsViewModel.class);

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
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment fragment;
        if (preference instanceof UploadDatabaseDialogPreference) {
            fragment = UploadDbPreferenceDialogFragment.Companion.newInstance(preference);
            fragment.setTargetFragment(this, 0);
            fragment.show(getFragmentManager(),
                    "android.support.v7.preference.PreferenceFragment.DIALOG");
        } else super.onDisplayPreferenceDialog(preference);
    }
}
