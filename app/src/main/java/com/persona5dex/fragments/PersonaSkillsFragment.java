package com.persona5dex.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.activities.SkillDetailActivity;
import com.persona5dex.dagger.activity.ActivityContextModule;
import com.persona5dex.dagger.activity.LayoutModule;
import com.persona5dex.dagger.activity.ViewModelRepositoryModule;
import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.models.PersonaDetailSkill;
import com.persona5dex.viewmodels.PersonaDetailSkillsViewModel;
import com.persona5dex.viewmodels.ViewModelFactory;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import static com.persona5dex.viewmodels.PersonaDetailSkillsViewModel.MAX_CONFIDANT_SKILL_VALUE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonaSkillsFragment extends BaseFragment {
    public static final String SKILL_ID = "skill_id";
    PersonaDetailSkillsViewModel viewModel;
    private int personaID;

    @Inject
    ViewModelFactory viewModelFactory;

    public PersonaSkillsFragment() {
        // Required empty public constructor
        super();
    }

    public static PersonaSkillsFragment newInstance(int personaID){
        PersonaSkillsFragment elementsFragment = new PersonaSkillsFragment();
        Bundle args = new Bundle();
        args.putInt("persona_id", personaID);
        elementsFragment.setArguments(args);
        return elementsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personaID = getArguments().getInt("persona_id", 1);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Persona5Application.get(activity).getComponent()
                .viewModelComponent(new AndroidViewModelRepositoryModule())
                .activityComponent(new LayoutModule(activity),
                        new ActivityContextModule(activity),
                        new ViewModelRepositoryModule())
                .inject(this);

        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(PersonaDetailSkillsViewModel.class);

        viewModel.getSkillsForPersona(personaID)
                .observe(getViewLifecycleOwner(), new Observer<List<PersonaDetailSkill>>() {
            @Override
            public void onChanged(@Nullable List<PersonaDetailSkill> personaDetailSkills) {
                ViewGroup skillsGrid = baseView.findViewById(R.id.skill_grid);

                LayoutInflater inflater = activity.getLayoutInflater();
                ViewGroup container = activity.findViewById(R.id.view_pager);

                for(final PersonaDetailSkill personaSkill: personaDetailSkills){
                    View personaSkillRow = inflater.inflate(R.layout.persona_skil_row, container, false);

                    TextView textViewSkillName = personaSkillRow.findViewById(R.id.textViewSkillName);
                    textViewSkillName.setText(personaSkill.name);

                    TextView textViewSkillLevel = personaSkillRow.findViewById(R.id.textViewSkillLevel);

                    if(personaSkill.levelRequired == 0){
                        textViewSkillLevel.setText("-");
                    }
                    else if(personaSkill.levelRequired == MAX_CONFIDANT_SKILL_VALUE) {
                        // max persona
                        textViewSkillLevel.setText(R.string.max_confidant);
                    }
                    else{
                        textViewSkillLevel.setText(String.format(Locale.getDefault(), "%d", personaSkill.levelRequired));
                    }

                    TextView textViewEffect = personaSkillRow.findViewById(R.id.textView_effect);
                    textViewEffect.setText(personaSkill.effect);

                    View horizontalDivider = inflater.inflate(R.layout.grid_divider_horizontal, container, false);

                    personaSkillRow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent personaDetailIntent = new Intent(getContext(), SkillDetailActivity.class);
                            personaDetailIntent.putExtra(SKILL_ID, personaSkill.skillID);
                            startActivity(personaDetailIntent);
                        }
                    });

                    skillsGrid.addView(personaSkillRow);
                    skillsGrid.addView(horizontalDivider);
                }

                skillsGrid.removeViewAt(skillsGrid.getChildCount() - 1); //remove hanging divider
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        baseView = inflater.inflate(R.layout.fragment_persona_skills, container, false);
        return baseView;
    }
}
