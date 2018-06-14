package com.persona5dex.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.iid.FirebaseInstanceId;
import com.persona5dex.BuildConfig;
import com.persona5dex.R;
import com.persona5dex.activities.PersonaDetailActivity;
import com.persona5dex.models.PersonaEdgeDisplay;
import com.persona5dex.uploaddatabase.UploadDatabaseJobService;
import com.persona5dex.viewmodels.PersonaFusionListViewModel;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

/**
 * Created by Rechee on 7/30/2017.
 */

public class PersonaFusionListAdapter extends RecyclerView.Adapter<PersonaFusionListAdapter.ViewHolder> {

    private final RecyclerView recyclerView;

    private final List<PersonaEdgeDisplay> edges;
    private PersonaEdgeDisplay selectedPersonaEdge;
    private int selectedPosition = -1;


    class ViewHolder extends RecyclerView.ViewHolder implements ExpandableLayout.OnExpansionUpdateListener {
        private final ExpandableLayout expandableLayout;
        private TextView textViewPersonaNameOne;
        private TextView textViewPersonaNameTwo;
        private ImageView expandImage;

        private TextView textViewPersonaOneDetail;
        private TextView textViewPersonaTwoDetail;

        private PersonaEdgeDisplay personaEdge;

        ViewHolder(final View itemView) {
            super(itemView);

            this.expandImage = itemView.findViewById(R.id.expand_icon);

            this.expandableLayout = itemView.findViewById(R.id.expandable_fusion);
            this.expandableLayout.setInterpolator(new OvershootInterpolator());
            this.expandableLayout.setOnExpansionUpdateListener(this);
        }

        private void goToPersonaDetail(int personaID){
            Intent startDetailIntent = new Intent(itemView.getContext(), PersonaDetailActivity.class);
            startDetailIntent.putExtra("persona_id", personaID);
            itemView.getContext().startActivity(startDetailIntent);
        }

        ExpandableLayout expandableLayout() {
            return this.expandableLayout;
        }

        PersonaEdgeDisplay personaEdge() {
            return this.personaEdge;
        }

        ImageView expandImage() {
            return this.expandImage;
        }

        void bind(final PersonaEdgeDisplay edge){

            this.textViewPersonaNameOne = itemView.findViewById(R.id.textView_personaNameOne);
            this.textViewPersonaNameTwo = itemView.findViewById(R.id.textView_personaNameTwo);
            this.textViewPersonaOneDetail = itemView.findViewById(R.id.persona_one_detail);
            this.textViewPersonaTwoDetail = itemView.findViewById(R.id.persona_two_detail);

            this.textViewPersonaNameOne.setText(edge.leftPersonaName);
            this.textViewPersonaNameTwo.setText(edge.rightPersonaName);

            if(BuildConfig.ENABLE_CRASHLYTICS && edge.leftPersonaID == edge.rightPersonaID) {
                Crashlytics.log("Personas have same ID. Instance ID: " + FirebaseInstanceId.getInstance().getId());

                FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(recyclerView.getContext()));

                Job job = dispatcher.newJobBuilder()
                        .setService(UploadDatabaseJobService.class) // the JobService that will be called
                        .setTag(UploadDatabaseJobService.JOB_TAG)        // uniquely identifies the job
                        .build();

                dispatcher.mustSchedule(job);
            }

            this.textViewPersonaOneDetail.setOnClickListener(v -> goToPersonaDetail(edge.leftPersonaID));

            this.textViewPersonaTwoDetail.setOnClickListener(v -> goToPersonaDetail(edge.rightPersonaID));

            String detailsFor = itemView.getContext().getString(R.string.details_for);

            this.textViewPersonaOneDetail.setText(detailsFor + ": " + edge.leftPersonaName);
            this.textViewPersonaTwoDetail.setText(detailsFor + ": " + edge.rightPersonaName);

            personaEdge = edge;
            expandableLayout.collapse(false);
        }

        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
            int scrollPosition = getAdapterPosition();
            if(scrollPosition == RecyclerView.NO_POSITION){
                scrollPosition = 0;
            }

            recyclerView.smoothScrollToPosition(scrollPosition);
        }
    }

    public PersonaFusionListAdapter(List<PersonaEdgeDisplay> edges, RecyclerView recyclerView){
        this.edges = edges;
        this.recyclerView = recyclerView;
    }

    @Override
    public PersonaFusionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_personafusion, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PersonaFusionListAdapter.ViewHolder holder, final int position) {
        final int edgePosition = holder.getAdapterPosition() == RecyclerView.NO_POSITION ? 0 : holder.getAdapterPosition();
        holder.bind(edges.get(edgePosition));

        holder.itemView.setOnClickListener(v -> {
            //view clicked let's expand

            ViewHolder selectedViewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedPosition);
            if(selectedViewHolder != null){
                selectedViewHolder.expandableLayout().collapse();
                selectedViewHolder.expandImage().setImageResource(R.drawable.ic_expand_more_white_24dp);
            }

            if(holder.personaEdge() == selectedPersonaEdge){
                selectedPersonaEdge = null;
                selectedPosition = -1;
                holder.expandImage().setImageResource(R.drawable.ic_expand_more_white_24dp);
            }
            else{
                holder.expandableLayout().expand();
                selectedPersonaEdge = edges.get(edgePosition);
                selectedPosition = edgePosition;
                holder.expandImage().setImageResource(R.drawable.ic_expand_less_white_24dp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.edges.size();
    }
}
