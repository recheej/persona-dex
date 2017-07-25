package com.example.rechee.persona5calculator.activities;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.example.rechee.persona5calculator.Persona5Application;
import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.dagger.ActivityComponent;
import com.example.rechee.persona5calculator.dagger.ActivityContextModule;
import com.example.rechee.persona5calculator.dagger.DaggerActivityComponent;
import com.example.rechee.persona5calculator.dagger.DaggerViewModelComponent;
import com.example.rechee.persona5calculator.dagger.LayoutModule;
import com.example.rechee.persona5calculator.dagger.PersonaFileModule;
import com.example.rechee.persona5calculator.dagger.ViewModelComponent;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.viewmodels.PersonaDetailViewModel;

import javax.inject.Inject;

public class PersonaDetailActivity extends AppCompatActivity {

    @Inject
    Toolbar mainToolbar;

    @Inject
    PersonaDetailViewModel viewModel;
    private Persona detailPersona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona_detail);

        ViewModelComponent viewModelComponent = DaggerViewModelComponent.builder()
                .personaFileModule(new PersonaFileModule(this))
                .persona5ApplicationComponent(Persona5Application.get(this).getComponent())
                .build();

        ActivityComponent component = DaggerActivityComponent.builder()
                .layoutModule(new LayoutModule(this))
                .activityContextModule(new ActivityContextModule(this))
                .viewModelComponent(viewModelComponent)
                .build();
        component.inject(this);


        this.detailPersona = viewModel.getDetailPersona();
        setUpToolbar();
    }

    private void setUpToolbar(){
        this.mainToolbar.setTitle(this.detailPersona.name);
        this.mainToolbar.setSubtitle(String.format("Level: %d", this.detailPersona.level));

        setSupportActionBar(this.mainToolbar);
    }
}
