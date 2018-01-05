package com.persona5dex.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.dagger.activity.ActivityComponent;
import com.persona5dex.dagger.activity.ActivityContextModule;
import com.persona5dex.dagger.LayoutModule;
import com.persona5dex.dagger.activity.ViewModelModule;
import com.persona5dex.dagger.activity.ViewModelRepositoryModule;
import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.fragments.SettingsFragment;
import com.persona5dex.services.FusionCalculatorJobService;

import javax.inject.Inject;

/**
 * Created by Rechee on 10/7/2017.
 */

public class SettingsActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    Toolbar mainToolbar;

    @Inject
    ProgressBar fusionsProgressBar;

    private SharedPreferences defaultSharedPreferences;
    private View frameLayout;
    private boolean resetService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActivityComponent component = Persona5Application.get(this).getComponent()
                .viewModelComponent(new AndroidViewModelRepositoryModule())
                .activityComponent(
                new LayoutModule(this),
                new ActivityContextModule(this),
                new ViewModelModule(),
                new ViewModelRepositoryModule()
        );
        component.inject(this);
        this.component = component;

        SettingsFragment settingsFragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, settingsFragment)
                .commit();

        setSupportActionBar(this.mainToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.action_settings);

        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        frameLayout = findViewById(R.id.container);

        FusionCalculatorJobService.enqueueWork(this, new Intent(this, FusionCalculatorJobService.class));

        fusionsProgressBar.setVisibility(ProgressBar.VISIBLE);
        frameLayout.setVisibility(View.INVISIBLE);
    }

    private void registerCalculationFinishedReceiver() {
        IntentFilter calculationFinishedIntentFilter = new IntentFilter(FusionCalculatorJobService.Constants.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, calculationFinishedIntentFilter);
    }

    // Define the callback for what to do when fusion calculation service is finished
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(SettingsActivity.this.resetService){
                //it's possible that the user changed the dlc values while the service is running so reset again
                SettingsActivity.this.resetService = false;
                final Intent work = new Intent(SettingsActivity.this,
                        FusionCalculatorJobService.class);
                work.putExtra("forceReset", true);

                FusionCalculatorJobService.enqueueWork(SettingsActivity.this,
                        work);
            }

            fusionsProgressBar.setVisibility(ProgressBar.INVISIBLE);
            frameLayout.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerCalculationFinishedReceiver();
        defaultSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        defaultSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        this.resetService = true;

        final Intent work = new Intent(this, FusionCalculatorJobService.class);
        work.putExtra("forceReset", true);
        FusionCalculatorJobService.enqueueWork(this, work);
    }
}
