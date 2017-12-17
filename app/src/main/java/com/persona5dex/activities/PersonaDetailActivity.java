package com.persona5dex.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.persona5dex.BuildConfig;
import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.adapters.PersonaDetailFragmentPagerAdapter;
import com.persona5dex.dagger.activity.ActivityComponent;
import com.persona5dex.dagger.activity.ActivityContextModule;
import com.persona5dex.dagger.LayoutModule;
import com.persona5dex.dagger.application.Persona5ApplicationComponent;
import com.persona5dex.dagger.activity.ViewModelModule;
import com.persona5dex.dagger.activity.ViewModelRepositoryModule;
import com.persona5dex.models.PersonaDetailInfo;
import com.persona5dex.viewmodels.PersonaDetailInfoViewModel;

import javax.inject.Inject;

public class PersonaDetailActivity extends BaseActivity {

    @Inject
    Toolbar mainToolbar;

    private PersonaDetailInfoViewModel viewModel;
    private PersonaDetailInfo detailPersona;
    private int personaID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona_detail);

        final Persona5ApplicationComponent applicationComponent = Persona5Application.get(this)
                .getComponent();

        ActivityComponent component = applicationComponent.plus(
                new LayoutModule(this),
                new ActivityContextModule(this),
                new ViewModelModule(),
                new ViewModelRepositoryModule()
        );
        component.inject(this);
        this.component = component;

        if(BuildConfig.ENABLE_CRASHLYTICS){
            //see how personas are being viewed in app
//            Answers.getInstance().logContentView(new ContentViewEvent()
//                    .putContentName("View Persona Detail")
//                    .putContentType("View Persona Screen")
//                    .putContentId(this.detailPersona.name)
//            );
        }

        this.personaID = getIntent().getIntExtra("persona_id", 1);

        viewModel = ViewModelProviders.of(this).get(PersonaDetailInfoViewModel.class);
        viewModel.init(applicationComponent, personaID);

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
