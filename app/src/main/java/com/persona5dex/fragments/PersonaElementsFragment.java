package com.persona5dex.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.dagger.application.Persona5ApplicationComponent;
import com.persona5dex.viewmodels.PersonaElementsViewModel;

import java.util.HashMap;

import static com.persona5dex.models.Enumerations.Element;
import static com.persona5dex.models.Enumerations.ElementEffect;

public class PersonaElementsFragment extends BaseFragment {
    PersonaElementsViewModel viewModel;

    private int personaID;

    public PersonaElementsFragment() {
        super();
    }

    public static PersonaElementsFragment newInstance(int personaID){
        PersonaElementsFragment elementsFragment = new PersonaElementsFragment();
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

        viewModel = ViewModelProviders.of(this).get(PersonaElementsViewModel.class);
        viewModel.init(component, personaID);

        viewModel.getElementsForPersona(personaID).observe(this, new Observer<HashMap<Element, ElementEffect>>() {
            @Override
            public void onChanged(@Nullable HashMap<Element, ElementEffect> elements) {
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
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_persona_elements, container, false);
        return baseView;
    }

    private void setElementView(View parentView, int textViewID, ElementEffect effect){
        TextView elementStatView = parentView.findViewById(textViewID);
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
