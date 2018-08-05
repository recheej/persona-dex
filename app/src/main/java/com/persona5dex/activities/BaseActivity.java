package com.persona5dex.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.persona5dex.BuildConfig;
import com.persona5dex.Persona5Application;
import com.persona5dex.PersonaFileUtilities;
import com.persona5dex.dagger.activity.ActivityComponent;
import com.persona5dex.dagger.activity.ActivityContextModule;
import com.persona5dex.dagger.activity.LayoutModule;
import com.persona5dex.dagger.activity.ViewModelModule;
import com.persona5dex.dagger.activity.ViewModelRepositoryModule;
import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.update.UpdateConfig;

import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Rechee on 7/30/2017.
 */

public class BaseActivity extends AppCompatActivity {
    protected ActivityComponent component;
    private CompositeDisposable compositeDisposable;

    @Inject
    @Named("defaultSharedPreferences")
    SharedPreferences defaultSharedPreferences;

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
                        new ViewModelModule(),
                        new ViewModelRepositoryModule()
                );
        component.inject(this);
        this.component = component;

        compositeDisposable = new CompositeDisposable();
    }

    protected void addStoppableDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.dispose();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showUpgradeDialog();
    }

    private void showUpgradeDialog() {
        final String updateDialogKey = String.format("update_%d", BuildConfig.VERSION_CODE);
        final boolean shouldShowDialogMessage = defaultSharedPreferences.getBoolean(updateDialogKey, true);

        if(shouldShowDialogMessage) {
            final Resources resources = getResources();
            final String packageName = this.getPackageName();

            @RawRes int jsonConfigIdentifier = resources.getIdentifier(updateDialogKey, "raw", packageName);
            @StringRes int dialogMessage = resources.getIdentifier(updateDialogKey, "string", packageName);

            if(jsonConfigIdentifier != 0) {
                Disposable disposable = Single
                        .fromCallable(() -> getUpdateConfig(resources, jsonConfigIdentifier))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(updateConfig -> {
                            @StringRes int actionButtonTextRes = resources.getIdentifier(updateConfig.getActionButtonText(), "string", packageName);

                            switch(updateConfig.getActionType()) {
                                case "activity":
                                    AlertDialog.Builder builder = new AlertDialog.Builder(this)
                                            .setMessage(dialogMessage)
                                            .setPositiveButton(actionButtonTextRes, (dialogInterface, i) -> {
                                                try {
                                                    Intent activityIntent = new Intent(this, Class.forName(updateConfig.getActionValue()));
                                                    this.startActivity(activityIntent);
                                                } catch(ClassNotFoundException e) {
                                                    e.printStackTrace();
                                                }
                                            });

                                    AlertDialog dialog = builder.create();
                                    if(actionButtonTextRes != 0) {
                                        dialog.setOnShowListener(dialogInterface -> {
                                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setText(actionButtonTextRes);

                                        });
                                    }
                                    dialog.show();

                                    defaultSharedPreferences.edit().putBoolean(updateDialogKey, false).apply();
                                    break;
                            }
                        }, Crashlytics::logException);

                addStoppableDisposable(disposable);
            }
        }
    }

    private UpdateConfig getUpdateConfig(Resources resources, int jsonConfigIdentifier) {
        InputStream inputStream = resources.openRawResource(jsonConfigIdentifier);

        PersonaFileUtilities fileUtilities = new PersonaFileUtilities();
        return fileUtilities.parseJsonFile(inputStream, UpdateConfig.class);
    }
}
