package com.example.rechee.persona5calculator.activities;

import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.rechee.persona5calculator.Persona5Application;
import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.adapters.PersonaDetailFragmentPagerAdapter;
import com.example.rechee.persona5calculator.adapters.PersonaFusionListAdapter;
import com.example.rechee.persona5calculator.adapters.PersonaFusionListPagerAdapter;
import com.example.rechee.persona5calculator.dagger.ActivityComponent;
import com.example.rechee.persona5calculator.dagger.ActivityContextModule;
import com.example.rechee.persona5calculator.dagger.LayoutModule;
import com.example.rechee.persona5calculator.dagger.PersonaFileModule;
import com.example.rechee.persona5calculator.dagger.ViewModelModule;
import com.example.rechee.persona5calculator.dagger.ViewModelRepositoryModule;
import com.example.rechee.persona5calculator.viewmodels.PersonaDetailViewModel;

import javax.inject.Inject;
import javax.inject.Named;

public class PersonaFusionActivity extends BaseActivity {

    @Inject
    Toolbar mainToolbar;

    @Inject
    PersonaDetailViewModel viewModel;

    private RecyclerView recyclerView;
    private PersonaFusionListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona_fusion);

        ActivityComponent component = Persona5Application.get(this).getComponent().plus(
                new LayoutModule(this),
                new ActivityContextModule(this),
                new ViewModelModule(),
                new ViewModelRepositoryModule(),
                new PersonaFileModule(this)
        );
        component.inject(this);
        this.component = component;

        setUpToolbar();

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager_fusion);
        PersonaFusionListPagerAdapter pagerAdapter = new PersonaFusionListPagerAdapter(getSupportFragmentManager(), this, viewModel.getPersonaForFusion());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_fusions);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpToolbar(){
        this.mainToolbar.setTitle(String.format("Fusions for: %s", viewModel.getPersonaForFusion()));

        setSupportActionBar(this.mainToolbar);
    }
}
