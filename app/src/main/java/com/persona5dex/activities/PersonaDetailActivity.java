package com.persona5dex.activities;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.persona5dex.ArcanaNameProvider;
import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.adapters.PersonaDetailFragmentPagerAdapter;
import com.persona5dex.dagger.application.Persona5ApplicationComponent;
import com.persona5dex.models.PersonaDetailInfo;
import com.persona5dex.repositories.PersonaDetailRepository;
import com.persona5dex.viewmodels.PersonaDetailInfoViewModel;
import com.persona5dex.viewmodels.PersonaDetailInfoViewModelFactory;

import javax.inject.Inject;

public class PersonaDetailActivity extends BaseActivity {

    @Inject
    Toolbar mainToolbar;

    @Inject
    PersonaDetailRepository repository;

    @Inject
    ArcanaNameProvider arcanaNameProvider;

    private PersonaDetailInfo detailPersona;
    private int personaID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona_detail);
        component.inject(this);

        this.personaID = getIntent().getIntExtra("persona_id", 1);

        final PersonaDetailInfoViewModelFactory factory = new PersonaDetailInfoViewModelFactory(repository, arcanaNameProvider, personaID);

        PersonaDetailInfoViewModel viewModel = new ViewModelProvider(this, factory).get(PersonaDetailInfoViewModel.class);
        viewModel.getDetailsForPersona().observe(this, new Observer<PersonaDetailInfo>() {
            @Override
            public void onChanged(@Nullable PersonaDetailInfo personaDetailInfo) {
                if(personaDetailInfo != null){
                    PersonaDetailActivity.this.detailPersona = personaDetailInfo;
                    setUpToolbar();
                }
            }
        });

        ViewPager viewPager = findViewById(R.id.view_pager);
        PersonaDetailFragmentPagerAdapter pagerAdapter = new PersonaDetailFragmentPagerAdapter(getSupportFragmentManager(), this, personaID);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
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
                //viewModel.storePersonaForFusion(detailPersona);

                Intent startDetailIntent = new Intent(PersonaDetailActivity.this, PersonaFusionActivity.class);
                startDetailIntent.putExtra("persona_id", personaID);
                startActivity(startDetailIntent);

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
