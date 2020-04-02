package com.persona5dex.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.crashlytics.android.Crashlytics;
import com.persona5dex.R;
import com.persona5dex.repositories.PersonaElementsRepository;
import com.persona5dex.viewmodels.PersonaElementsViewModel;
import com.persona5dex.viewmodels.PersonaElementsViewModelFactory;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import static com.persona5dex.models.Enumerations.Element;
import static com.persona5dex.models.Enumerations.ElementEffect;

public class PersonaElementsFragment extends BaseFragment {
    PersonaElementsViewModel viewModel;

    private int personaID;

    @Inject
    PersonaElementsRepository repository;

    public PersonaElementsFragment() {
        super();
    }

    public static PersonaElementsFragment newInstance(int personaID) {
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

        getActivityComponent().inject(this);

        final PersonaElementsViewModelFactory factory = new PersonaElementsViewModelFactory(repository, personaID);
        viewModel = new ViewModelProvider(this, factory).get(PersonaElementsViewModel.class);
        viewModel.getElementsForPersona().observe(getViewLifecycleOwner(), elements -> {
            setElementView(elements, R.id.textViewPhysicalStat, Element.PHYSICAL);
            setElementView(elements, R.id.textViewGunStat, Element.GUN);
            setElementView(elements, R.id.textViewFireStat, Element.FIRE);
            setElementView(elements, R.id.textViewIceStat, Element.ICE);
            setElementView(elements, R.id.textViewElectricStat, Element.ELECTRIC);
            setElementView(elements, R.id.textViewWindStat, Element.WIND);
            setElementView(elements, R.id.textViewPsyStat, Element.PSYCHIC);
            setElementView(elements, R.id.textViewNuclearStat, Element.NUCLEAR);
            setElementView(elements, R.id.textViewBlessStat, Element.BLESS);
            setElementView(elements, R.id.textViewCurseStat, Element.CURSE);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_persona_elements, container, false);
        return baseView;
    }

    private void setElementView(Map<Element, ElementEffect> elementMap, int textViewID, Element element) {
        TextView elementStatView = baseView.findViewById(textViewID);
        String statText = "";

        try {
            ElementEffect elementEffect = Objects.requireNonNull(elementMap.get(element));
            switch(elementEffect) {
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
        } catch(NullPointerException e) {
            final String exceptionMessage = String.format(Locale.ROOT, "null for persona with id: %d. Element: %s", personaID, element);
            Crashlytics.logException(new RuntimeException(exceptionMessage, e));
        }
    }
}
