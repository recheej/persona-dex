package com.example.rechee.persona5calculator.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.rechee.persona5calculator.Persona5Application;
import com.example.rechee.persona5calculator.PersonaUtilities;
import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.dagger.ActivityComponent;
import com.example.rechee.persona5calculator.dagger.ActivityContextModule;
import com.example.rechee.persona5calculator.dagger.LayoutModule;
import com.example.rechee.persona5calculator.dagger.ViewModelModule;
import com.example.rechee.persona5calculator.dagger.ViewModelRepositoryModule;
import com.example.rechee.persona5calculator.fragments.SettingsFragment;
import com.example.rechee.persona5calculator.services.FusionCalculatorJobService;
import com.example.rechee.persona5calculator.viewmodels.SettingsViewModel;

import javax.inject.Inject;

/**
 * Created by Rechee on 10/7/2017.
 */

public class SettingsActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    Toolbar mainToolbar;

    @Inject
    ProgressBar fusionsProgressBar;

    @Inject
    SettingsViewModel viewModel;

    private SharedPreferences defaultSharedPreferences;
    private View frameLayout;
    private SharedPreferences fusionSharedPreferences;
    private boolean resetService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActivityComponent component = Persona5Application.get(this).getComponent().plus(
                new LayoutModule(this),
                new ActivityContextModule(this),
                new ViewModelModule(),
                new ViewModelRepositoryModule()
        );
        component.inject(this);
        this.component = component;

        SettingsFragment settingsFragment = SettingsFragment.newInstance(viewModel.getDLCPersonaForSettings());
        getFragmentManager().beginTransaction()
                .replace(R.id.container, settingsFragment)
                .commit();

        setSupportActionBar(this.mainToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        fusionSharedPreferences = getSharedPreferences(PersonaUtilities.SHARED_PREF_FUSIONS,
                Context.MODE_PRIVATE);

        frameLayout = findViewById(R.id.container);

        registerCalculationFinishedReceiver();

        if(fusionSharedPreferences.contains("initialized") && !fusionSharedPreferences.contains("finished")){

            fusionsProgressBar.setVisibility(ProgressBar.VISIBLE);
            frameLayout.setVisibility(View.INVISIBLE);
        }
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
                SettingsActivity.this.resetService = false;
                FusionCalculatorJobService.enqueueWork(SettingsActivity.this,
                        new Intent(SettingsActivity.this, FusionCalculatorJobService.class));
            }

            fusionsProgressBar.setVisibility(ProgressBar.INVISIBLE);
            frameLayout.setVisibility(View.VISIBLE);
        }
    };

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
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(fusionSharedPreferences.contains("initialized") && !fusionSharedPreferences.contains("finished")){
            this.resetService = true;
        }
        else{
            FusionCalculatorJobService.enqueueWork(this, new Intent(this, FusionCalculatorJobService.class));
        }
    }
}
