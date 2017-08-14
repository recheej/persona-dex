package com.example.rechee.persona5calculator.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.dagger.FragmentComponent;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.viewmodels.PersonaDetailViewModel;

import java.util.HashMap;

import javax.inject.Inject;

import static com.example.rechee.persona5calculator.models.Enumerations.Element;
import static com.example.rechee.persona5calculator.models.Enumerations.ElementEffect;

public class PersonaElementsFragment extends BaseFragment {

    private Persona detailPersona;

    @Inject
    PersonaDetailViewModel viewModel;

    public PersonaElementsFragment() {
        super();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentComponent component = activityComponent.plus();
        component.inject(this);

        this.detailPersona = viewModel.getDetailPersona();

        HashMap<Element, ElementEffect> elements = detailPersona.getElements();

        setElementView(baseView, R.id.textViewPhysicalStat, elements.get(Element.PHYSICAL));
        setElementView(baseView, R.id.textViewGunStat, elements.get(Element.GUN));
        setElementView(baseView, R.id.textViewFireStat, elements.get(Element.FIRE));
        setElementView(baseView, R.id.textViewIceStat, elements.get(Element.ICE));
        setElementView(baseView, R.id.textViewElectricStat, elements.get(Element.ELECTRIC));
        setElementView(baseView, R.id.textViewWindStat, elements.get(Element.WIND));
        setElementView(baseView, R.id.textViewPsyStat, elements.get(Element.PSYCHIC));
        setElementView(baseView, R.id.textViewNuclearStat, elements.get(Element.NUCLEAR));
        setElementView(baseView, R.id.textViewBlessStat, elements.get(Element.BLESS));
        setElementView(baseView, R.id.textViewCurseStat, elements.get(Element.CURSE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_persona_elements, container, false);
        return baseView;
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
