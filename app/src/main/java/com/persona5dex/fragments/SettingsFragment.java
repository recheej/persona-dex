package com.persona5dex.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.ThemeUtil;
import com.persona5dex.activities.BaseActivity;
import com.persona5dex.dagger.activity.ActivityContextModule;
import com.persona5dex.dagger.activity.LayoutModule;
import com.persona5dex.dagger.activity.ViewModelModule;
import com.persona5dex.dagger.activity.ViewModelRepositoryModule;
import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;
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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);
        //todo: use theme attribute
        view.setBackgroundColor(ThemeUtil.getThemeAttributeColor(requireContext(), R.attr.pageBackground));
        return view;
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

        final MultiSelectListPreference dlcPreference = preferenceManager
                .findPreference(activity.getString(R.string.pref_key_dlc));

        final ListPreference preference = preferenceManager.findPreference(activity.getString(R.string.pref_key_theme));
        preference.setOnPreferenceChangeListener((preference1, newValue) -> {
            return true;
        });

        viewModel.getDLCPersonaForSettings().observe(getViewLifecycleOwner(), new Observer<String[][]>() {
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
}
