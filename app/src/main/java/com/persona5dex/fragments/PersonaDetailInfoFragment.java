package com.persona5dex.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.persona5dex.BuildConfig;
import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.dagger.application.Persona5ApplicationComponent;
import com.persona5dex.models.PersonaDetailInfo;
import com.persona5dex.models.room.Stats;
import com.persona5dex.viewmodels.PersonaDetailInfoViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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

        ProgressBar progressBar = baseView.findViewById(R.id.progress_bar_fusions);
        progressBar.setVisibility(View.VISIBLE);
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

                    if(personaDetailInfo.note != null && !personaDetailInfo.note.isEmpty()){
                        addNote(personaDetailInfo.note);
                    }
                    else if(personaDetailInfo.max){
                        PersonaDetailInfoFragment.this.addNote(getString(R.string.max_note));
                    }

                    PhotoView personaPictureView = baseView.findViewById(R.id.imageView_persona);
                    Picasso picasso = Picasso
                            .with(PersonaDetailInfoFragment.this.getContext());

                    //green = memory, blue = disk, red = network)

                    if(personaDetailInfo.imageUrl == null){
                        progressBar.setVisibility(View.GONE);
                    }
                    else{
                        picasso.setIndicatorsEnabled(BuildConfig.DEBUG);
                        picasso
                                .load(personaDetailInfo.imageUrl)
                                .placeholder(R.drawable.placeholder)
                                .fit()
                                .centerInside()
                                .into(personaPictureView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        progressBar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError() {
                                        progressBar.setVisibility(View.GONE);
                                        personaPictureView.setVisibility(View.GONE);
                                    }
                                });
                    }
                }
            }
        });

        viewModel.getShadowsForPersona().observe(this, shadowsString -> {


            if(shadowsString != null){
                ViewGroup shadowContainer = baseView.findViewById(R.id.container_shadow_name);
                shadowContainer.setVisibility(View.VISIBLE);

                TextView shadowsTextView = shadowContainer.findViewById(R.id.textView_shadow_name);
                shadowsTextView.setText(shadowsString);
            }
        });
    }

    private void addNote(String notToAdd) {
        View notesContainer = baseView.findViewById(R.id.container_note);
        notesContainer.setVisibility(View.VISIBLE);
        setTextViewText(baseView, R.id.textView_note, notToAdd);
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
