package com.example.rechee.persona5calculator.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.rechee.persona5calculator.Persona5Application;
import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.adapters.PersonaDetailFragmentPagerAdapter;
import com.example.rechee.persona5calculator.dagger.ActivityComponent;
import com.example.rechee.persona5calculator.dagger.ActivityContextModule;
import com.example.rechee.persona5calculator.dagger.LayoutModule;
import com.example.rechee.persona5calculator.dagger.PersonaFileModule;
import com.example.rechee.persona5calculator.dagger.ViewModelModule;
import com.example.rechee.persona5calculator.dagger.ViewModelRepositoryModule;
import com.example.rechee.persona5calculator.fragments.PersonaDetailInfoFragment;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.viewmodels.PersonaDetailViewModel;

import javax.inject.Inject;

public class PersonaDetailActivity extends BaseActivity {

    @Inject
    Toolbar mainToolbar;

    @Inject
    PersonaDetailViewModel viewModel;
    private Persona detailPersona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona_detail);

        ActivityComponent component = Persona5Application.get(this).getComponent().plus(
                new LayoutModule(this),
                new ActivityContextModule(this),
                new ViewModelModule(),
                new ViewModelRepositoryModule(),
                new PersonaFileModule(this)
        );
        component.inject(this);
        this.component = component;

        this.detailPersona = viewModel.getDetailPersona();
        setUpToolbar();

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        PersonaDetailFragmentPagerAdapter pagerAdapter = new PersonaDetailFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpToolbar(){
        this.mainToolbar.setTitle(this.detailPersona.name);
        this.mainToolbar.setSubtitle(String.format("Level: %d", this.detailPersona.level));

        setSupportActionBar(this.mainToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.persona_detail_menu, menu);

        MenuItem fusionMenuItem = menu.findItem(R.id.menu_item_fusions);
        fusionMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                viewModel.storePersonaForFusion(detailPersona);

                Intent startDetailIntent = new Intent(PersonaDetailActivity.this, PersonaFusionActivity.class);
                startActivity(startDetailIntent);

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
