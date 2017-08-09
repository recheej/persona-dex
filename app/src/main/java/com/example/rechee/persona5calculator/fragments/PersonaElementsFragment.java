package com.example.rechee.persona5calculator.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.activities.BaseActivity;
import com.example.rechee.persona5calculator.dagger.FragmentComponent;
import com.example.rechee.persona5calculator.models.Enumerations;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.viewmodels.PersonaDetailViewModel;

import java.util.HashMap;

import javax.inject.Inject;

import static com.example.rechee.persona5calculator.models.Enumerations.*;

public class PersonaElementsFragment extends Fragment {

    private Persona detailPersona;

    @Inject
    PersonaDetailViewModel viewModel;

    private BaseActivity activity;

    public PersonaElementsFragment() {
        super();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.activity = (BaseActivity) getActivity();
        FragmentComponent component = activity.getComponent().plus();
        component.inject(this);

        this.detailPersona = viewModel.getDetailPersona();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_persona_elements, container, false);
        HashMap<Element, ElementEffect> elements = detailPersona.getElements();

        setElementView(view, R.id.textViewPhysicalStat, elements.get(Element.PHYSICAL));
        setElementView(view, R.id.textViewGunStat, elements.get(Element.GUN));
        setElementView(view, R.id.textViewFireStat, elements.get(Element.FIRE));
        setElementView(view, R.id.textViewIceStat, elements.get(Element.ICE));
        setElementView(view, R.id.textViewElectricStat, elements.get(Element.ELECTRIC));
        setElementView(view, R.id.textViewWindStat, elements.get(Element.WIND));
        setElementView(view, R.id.textViewPsyStat, elements.get(Element.PSYCHIC));
        setElementView(view, R.id.textViewNuclearStat, elements.get(Element.NUCLEAR));
        setElementView(view, R.id.textViewBlessStat, elements.get(Element.BLESS));
        setElementView(view, R.id.textViewCurseStat, elements.get(Element.CURSE));

        return view;
    }

    private void setElementView(View parentView, int textViewID, ElementEffect effect){
        TextView elementStatView = (TextView) parentView.findViewById(textViewID);
        String statText = "";

        switch(effect){
            case WEAK:
                statText = getContext().getString(R.string.effect_weak);
                break;
            case RESIST:
                statText = getContext().getString(R.string.effect_resist);
                break;
            case NULL:
                statText = getContext().getString(R.string.effect_null);
                break;
            case REPEL:
                statText = getContext().getString(R.string.effect_repel);
                break;
            case DRAIN:
                statText = getContext().getString(R.string.effect_drain);
                break;
            case NO_EFFECT:
                statText = "-";
                break;
        }

        elementStatView.setText(statText);
    }
}
