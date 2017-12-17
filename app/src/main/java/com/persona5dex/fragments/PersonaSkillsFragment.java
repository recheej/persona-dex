package com.persona5dex.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.dagger.application.Persona5ApplicationComponent;
import com.persona5dex.models.PersonaDetailSkill;
import com.persona5dex.viewmodels.PersonaDetailSkillsViewModel;

import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonaSkillsFragment extends BaseFragment {
    PersonaDetailSkillsViewModel viewModel;
    private int personaID;

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

        Persona5ApplicationComponent component = Persona5Application.get(activity).getComponent();

        viewModel = ViewModelProviders.of(this).get(PersonaDetailSkillsViewModel.class);
        viewModel.init(component, personaID);

        viewModel.getElementsForPersona(personaID).observe(this, new Observer<List<PersonaDetailSkill>>() {
            @Override
            public void onChanged(@Nullable List<PersonaDetailSkill> personaDetailSkills) {
                LinearLayout skillsGrid = baseView.findViewById(R.id.skill_grid);

                LayoutInflater inflater = activity.getLayoutInflater();
                ViewGroup container = activity.findViewById(R.id.view_pager);

                for(PersonaDetailSkill personaSkill: personaDetailSkills){
                    View personaSkillRow = inflater.inflate(R.layout.persona_skil_row, container, false);

                    TextView textViewSkillName = personaSkillRow.findViewById(R.id.textViewSkillName);
                    textViewSkillName.setText(personaSkill.name);

                    TextView textViewSkillLevel = personaSkillRow.findViewById(R.id.textViewSkillLevel);

                    if(personaSkill.levelRequired == 0){
                        textViewSkillLevel.setText("-");
                    }
                    else{
                        textViewSkillLevel.setText(String.format(Locale.getDefault(), "%d", personaSkill.levelRequired));
                    }

                    View horizontalDivider = inflater.inflate(R.layout.grid_divider_horizontal, container, false);

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
