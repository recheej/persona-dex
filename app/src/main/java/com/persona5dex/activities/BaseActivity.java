package com.persona5dex.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.dagger.activity.ActivityComponent;

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

        ActivityComponent component = buildComponent();
        component.inject(this);
        this.component = component;

        final String nightModeValue =
                defaultSharedPreferences.getString(getString(R.string.pref_key_theme), String.valueOf(MODE_NIGHT_FOLLOW_SYSTEM));
        AppCompatDelegate.setDefaultNightMode(Integer.parseInt(nightModeValue));
    }

    protected ActivityComponent buildComponent() {
        return Persona5Application.get(this).getComponent().activityComponent().activityContext(this).build();
    }
}
