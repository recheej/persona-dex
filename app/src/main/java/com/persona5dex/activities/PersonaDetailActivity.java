package com.persona5dex.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdSize;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import javax.inject.Inject;

public class PersonaDetailActivity extends BaseActivity {

    @Inject
    Toolbar mainToolbar;

    @Inject
    PersonaDetailViewModel viewModel;
    private Persona detailPersona;

    private AdView adView;

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
        setUpToolbar();

        ViewPager viewPager = findViewById(R.id.view_pager);
        PersonaDetailFragmentPagerAdapter pagerAdapter = new PersonaDetailFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        initializeAd();
    }

    private void setUpToolbar(){
        this.mainToolbar.setTitle(this.detailPersona.name);
        this.mainToolbar.setSubtitle(String.format("Level: %d", this.detailPersona.level));

        setSupportActionBar(this.mainToolbar);
    }

    private void initializeAd() {
        MobileAds.initialize(this, getString(R.string.admob_app_id));

        adView = new AdView(this);

        RelativeLayout.LayoutParams layoutParams =  new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        adView.setLayoutParams(layoutParams);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.admob_main_unit_id));

        AdRequest adRequest = new AdRequest.Builder().build();

        RelativeLayout detailLayout = findViewById(R.id.detail_layout);
        detailLayout.addView(adView);

        adView.loadAd(adRequest);
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