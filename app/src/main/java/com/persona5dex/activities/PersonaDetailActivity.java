package com.persona5dex.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.persona5dex.BuildConfig;
import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.adapters.PersonaDetailFragmentPagerAdapter;
import com.persona5dex.dagger.ActivityComponent;
import com.persona5dex.dagger.ActivityContextModule;
import com.persona5dex.dagger.LayoutModule;
import com.persona5dex.dagger.ViewModelModule;
import com.persona5dex.dagger.ViewModelRepositoryModule;
import com.persona5dex.models.Persona;
import com.persona5dex.viewmodels.PersonaDetailViewModel;

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
                new ViewModelRepositoryModule()
        );
        component.inject(this);
        this.component = component;

        this.detailPersona = viewModel.getDetailPersona();

        if(BuildConfig.ENABLE_CRASHLYTICS){
            //see how personas are being viewed in app
            Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentName("View Persona Detail")
                    .putContentType("View Persona Screen")
                    .putContentId(this.detailPersona.name)
            );
        }

        setUpToolbar();

        ViewPager viewPager = findViewById(R.id.view_pager);
        PersonaDetailFragmentPagerAdapter pagerAdapter = new PersonaDetailFragmentPagerAdapter(getSupportFragmentManager(), this);
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
                viewModel.storePersonaForFusion(detailPersona);

                Intent startDetailIntent = new Intent(PersonaDetailActivity.this, PersonaFusionActivity.class);
                startActivity(startDetailIntent);

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
