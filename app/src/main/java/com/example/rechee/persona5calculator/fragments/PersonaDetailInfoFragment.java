package com.example.rechee.persona5calculator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.activities.PersonaFusionActivity;
import com.example.rechee.persona5calculator.dagger.FragmentComponent;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.viewmodels.PersonaDetailViewModel;

import javax.inject.Inject;

/**
 * Created by Rechee on 7/24/2017.
 */

public class PersonaDetailInfoFragment extends BaseFragment {
    private Persona detailPersona;

    @Inject
    PersonaDetailViewModel viewModel;

    public PersonaDetailInfoFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentComponent component = activity.getComponent().plus();
        component.inject(this);
        this.detailPersona = viewModel.getDetailPersona();

        Persona.Stats personaStats = detailPersona.getStats();
        setTextViewText(baseView, R.id.textViewStrengthStat, Integer.toString(personaStats.STRENGTH));
        setTextViewText(baseView, R.id.textViewMagicStat, Integer.toString(personaStats.MAGIC));
        setTextViewText(baseView, R.id.textViewEnduranceStat, Integer.toString(personaStats.ENDURANCE));
        setTextViewText(baseView, R.id.textViewAgilityStat, Integer.toString(personaStats.AGILITY));
        setTextViewText(baseView, R.id.textViewLuckStat, Integer.toString(personaStats.LUCK));

        setTextViewText(baseView, R.id.textView_arcanaName, detailPersona.arcanaName);

        baseView.findViewById(R.id.fusions_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.storePersonaForFusion(PersonaDetailInfoFragment.this.detailPersona);

                Intent startDetailIntent = new Intent(PersonaDetailInfoFragment.this.activity, PersonaFusionActivity.class);
                PersonaDetailInfoFragment.this.activity.startActivity(startDetailIntent);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.detail_info, container, false);
        return baseView;
    }

    private void setTextViewText(View rootView, int textViewId, String text){
        TextView view = (TextView) rootView.findViewById(textViewId);
        view.setText(text);
    }
}
