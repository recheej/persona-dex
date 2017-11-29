package com.persona5dex.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.dagger.Persona5ApplicationComponent;
import com.persona5dex.models.PersonaDetailInfo;
import com.persona5dex.models.room.Stats;
import com.persona5dex.viewmodels.PersonaDetailInfoViewModel;

/**
 * Created by Rechee on 7/24/2017.
 */

public class PersonaDetailInfoFragment extends BaseFragment {
    PersonaDetailInfoViewModel viewModel;
    private int personaID;

    public PersonaDetailInfoFragment() {
        super();
    }

    public static PersonaDetailInfoFragment newInstance(int personaID){
        PersonaDetailInfoFragment detailInfoFragment = new PersonaDetailInfoFragment();
        Bundle args = new Bundle();
        args.putInt("persona_id", personaID);
        detailInfoFragment.setArguments(args);
        return detailInfoFragment;
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
        viewModel = ViewModelProviders.of(this).get(PersonaDetailInfoViewModel.class);
        viewModel.init(component, personaID);

        viewModel.getDetailsForPersona().observe(this, new Observer<PersonaDetailInfo>() {
            @Override
            public void onChanged(@Nullable PersonaDetailInfo personaDetailInfo) {
                if(personaDetailInfo != null){
                    Stats personaStats = personaDetailInfo.stats;
                    setTextViewText(baseView, R.id.textViewStrengthStat, Integer.toString(personaStats.strength));
                    setTextViewText(baseView, R.id.textViewMagicStat, Integer.toString(personaStats.magic));
                    setTextViewText(baseView, R.id.textViewEnduranceStat, Integer.toString(personaStats.endurance));
                    setTextViewText(baseView, R.id.textViewAgilityStat, Integer.toString(personaStats.agility));
                    setTextViewText(baseView, R.id.textViewLuckStat, Integer.toString(personaStats.luck));

                    setTextViewText(baseView, R.id.textView_arcanaName, personaDetailInfo.arcanaName);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_detail_info, container, false);
        return baseView;
    }

    private void setTextViewText(View rootView, int textViewId, String text){
        TextView view = rootView.findViewById(textViewId);
        view.setText(text);
    }
}
