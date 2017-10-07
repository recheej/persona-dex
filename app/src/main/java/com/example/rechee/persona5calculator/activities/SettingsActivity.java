package com.example.rechee.persona5calculator.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.example.rechee.persona5calculator.fragments.SettingsFragment;

/**
 * Created by Rechee on 10/7/2017.
 */

public class SettingsActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
