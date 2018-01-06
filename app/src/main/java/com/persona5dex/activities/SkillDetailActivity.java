package com.persona5dex.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.dagger.activity.LayoutModule;
import com.persona5dex.dagger.activity.ActivityContextModule;
import com.persona5dex.dagger.activity.ViewModelModule;
import com.persona5dex.dagger.activity.ViewModelRepositoryModule;
import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.fragments.PersonaSkillsFragment;
import com.persona5dex.models.room.Skill;
import com.persona5dex.viewmodels.PersonaDetailSkillsViewModel;
import com.persona5dex.viewmodels.ViewModelFactory;

import javax.inject.Inject;

public class SkillDetailActivity extends BaseActivity {

    @Inject
    Toolbar mainToolbar;

    @Inject
    ViewModelFactory viewModelFactory;

    private PersonaDetailSkillsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_detail);

        Persona5Application.get(this).getComponent()
                .viewModelComponent(new AndroidViewModelRepositoryModule())
                .activityComponent(new LayoutModule(this),
                        new ActivityContextModule(this),
                        new ViewModelModule(),
                        new ViewModelRepositoryModule())
                .inject(this);

        mainToolbar.setTitle(R.string.persona_skill);
        setSupportActionBar(mainToolbar);

        final TextView skillNameTextView = findViewById(R.id.textView_skillName);
        final TextView effectTextView = findViewById(R.id.textView_effect);
        final TextView elementTextView = findViewById(R.id.textView_Element);
        final TextView noteTextView = findViewById(R.id.textView_Note);
        final TextView costTextView = findViewById(R.id.textView_Cost);



        Intent intent = getIntent();
        final int skillID = intent.getIntExtra(PersonaSkillsFragment.SKILL_ID, 1);

        this.viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(PersonaDetailSkillsViewModel.class);

        this.viewModel.setSkillID(skillID);
        this.viewModel.getSkill().observe(this, new Observer<Skill>() {
            @Override
            public void onChanged(@Nullable Skill skill) {

                String firstLetter = Character.toString(skill.element.charAt(0)).toUpperCase();
                skill.element = String.format("%s%s", firstLetter, skill.element.substring(1));

                skillNameTextView.setText(skill.name);
                effectTextView.setText(skill.effect);
                elementTextView.setText(skill.element);

                if(skill.note == null || skill.note.isEmpty()){
                    View labelNote = findViewById(R.id.textView_note_label);
                    View star = findViewById(R.id.textView_star);

                    star.setVisibility(View.GONE);
                    labelNote.setVisibility(View.GONE);
                    noteTextView.setVisibility(View.GONE);
                }
                else{
                    noteTextView.setText(skill.note);
                }

                costTextView.setText(Integer.toString(skill.cost));
            }
        });
    }
}
