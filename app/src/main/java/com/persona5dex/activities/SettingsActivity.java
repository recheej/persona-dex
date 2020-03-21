package com.persona5dex.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.widget.Toolbar;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.view.View;
import android.widget.ProgressBar;

import com.persona5dex.R;
import com.persona5dex.extensions.WorkInfoStateUtils;
import com.persona5dex.fragments.SettingsFragment;
import com.persona5dex.jobs.PersonaJobCreator;
import com.persona5dex.services.FusionCalculatorJobService;

import javax.inject.Inject;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

/**
 * Created by Rechee on 10/7/2017.
 */

public class SettingsActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    Toolbar mainToolbar;

    @Inject
    ProgressBar fusionsProgressBar;

    @Inject
    PersonaJobCreator personaJobCreator;

    private View frameLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        component.inject(this);

        personaJobCreator.getStateForGenerateFusionJob().observe(this, state -> {
            if(WorkInfoStateUtils.isFinished(state)){
                showSettings();
            }
        });

        SettingsFragment settingsFragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, settingsFragment)
                .commit();

        setSupportActionBar(this.mainToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.action_settings);

        frameLayout = findViewById(R.id.container);

        hideSettings();
    }

    private void hideSettings() {
        fusionsProgressBar.setVisibility(ProgressBar.VISIBLE);
        frameLayout.setVisibility(View.INVISIBLE);
    }

    private void showSettings() {
        fusionsProgressBar.setVisibility(ProgressBar.INVISIBLE);
        frameLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        defaultSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        defaultSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_key_theme))) {
            final String nightModeValue = sharedPreferences.getString(key, String.valueOf(MODE_NIGHT_FOLLOW_SYSTEM));
            AppCompatDelegate.setDefaultNightMode(Integer.parseInt(nightModeValue));
        } else {
            personaJobCreator.scheduleGenerateFusionJob();
        }
    }
}
