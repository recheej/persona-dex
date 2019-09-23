package com.persona5dex.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.persona5dex.Persona5Application;
import com.persona5dex.dagger.activity.ActivityComponent;
import com.persona5dex.dagger.activity.ActivityContextModule;
import com.persona5dex.dagger.activity.LayoutModule;
import com.persona5dex.dagger.activity.ViewModelModule;
import com.persona5dex.dagger.activity.ViewModelRepositoryModule;
import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Rechee on 7/30/2017.
 */

public class BaseActivity extends AppCompatActivity {
    protected ActivityComponent component;
    private CompositeDisposable compositeStoppableDisposable;
    private CompositeDisposable compositeDestroyableDisposable;

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
                        new ViewModelModule(),
                        new ViewModelRepositoryModule()
                );
        component.inject(this);
        this.component = component;

        compositeStoppableDisposable = new CompositeDisposable();
        compositeDestroyableDisposable = new CompositeDisposable();
    }

    protected void addStoppableDisposable(Disposable disposable) {
        compositeStoppableDisposable.add(disposable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeStoppableDisposable.dispose();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDestroyableDisposable.dispose();
    }

    protected void addDestroyableDisposable(Disposable disposable) {
        compositeDestroyableDisposable.dispose();
    }
}
