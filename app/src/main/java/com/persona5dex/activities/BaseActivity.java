package com.persona5dex.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.dagger.activity.ActivityComponent;
import com.persona5dex.dagger.activity.ActivityContextModule;
import com.persona5dex.dagger.activity.LayoutModule;
import com.persona5dex.dagger.activity.ViewModelRepositoryModule;
import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;

import javax.inject.Inject;
import javax.inject.Named;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

/**
 * Created by Rechee on 7/30/2017.
 */

public class BaseActivity extends AppCompatActivity {
    protected ActivityComponent component;

    @Inject
    @Named("defaultSharedPreferences")
    protected SharedPreferences defaultSharedPreferences;

    public ActivityComponent getComponent() {
        return this.component;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityComponent component = Persona5Application.get(this).getComponent()
                .viewModelComponent(new AndroidViewModelRepositoryModule())
                .activityComponent(
                        new LayoutModule(this),
                        new ActivityContextModule(this),
                        new ViewModelRepositoryModule()
                );
        component.inject(this);
        this.component = component;

        final String nightModeValue =
                defaultSharedPreferences.getString(getString(R.string.pref_key_theme), String.valueOf(MODE_NIGHT_FOLLOW_SYSTEM));
        AppCompatDelegate.setDefaultNightMode(Integer.parseInt(nightModeValue));
    }
}
