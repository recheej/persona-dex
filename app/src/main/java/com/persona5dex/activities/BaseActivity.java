package com.persona5dex.activities;

import android.support.v7.app.AppCompatActivity;

import com.persona5dex.dagger.activity.ActivityComponent;

/**
 * Created by Rechee on 7/30/2017.
 */

public class BaseActivity extends AppCompatActivity {
    protected ActivityComponent component;

    public ActivityComponent getComponent(){
        return this.component;
    }
}
