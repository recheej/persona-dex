package com.example.rechee.persona5calculator.activities;

import android.support.v7.app.AppCompatActivity;

import com.example.rechee.persona5calculator.dagger.ActivityComponent;

/**
 * Created by Rechee on 7/30/2017.
 */

public class BaseActivity extends AppCompatActivity {
    protected ActivityComponent component;

    public ActivityComponent getComponent(){
        return this.component;
    }
}
