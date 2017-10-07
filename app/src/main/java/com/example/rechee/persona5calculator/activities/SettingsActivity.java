package com.example.rechee.persona5calculator.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.example.rechee.persona5calculator.Persona5Application;
import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.dagger.ActivityComponent;
import com.example.rechee.persona5calculator.dagger.ActivityContextModule;
import com.example.rechee.persona5calculator.dagger.LayoutModule;
import com.example.rechee.persona5calculator.dagger.ViewModelModule;
import com.example.rechee.persona5calculator.dagger.ViewModelRepositoryModule;
import com.example.rechee.persona5calculator.fragments.SettingsFragment;

import javax.inject.Inject;

/**
 * Created by Rechee on 10/7/2017.
 */

public class SettingsActivity extends BaseActivity {

    @Inject
    Toolbar mainToolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, new SettingsFragment())
                .commit();

        ActivityComponent component = Persona5Application.get(this).getComponent().plus(
                new LayoutModule(this),
                new ActivityContextModule(this),
                new ViewModelModule(),
                new ViewModelRepositoryModule()
        );
        component.inject(this);
        this.component = component;

        setSupportActionBar(this.mainToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
