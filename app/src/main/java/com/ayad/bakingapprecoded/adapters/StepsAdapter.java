package com.ayad.bakingapprecoded.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ayad.bakingapprecoded.R;
import com.ayad.bakingapprecoded.models.Step;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    private List<Step> mSteps;
    private OnItemClickHandler mClickHandler;

    public StepsAdapter(List<Step> steps, OnItemClickHandler clickHandler) {
        mClickHandler = clickHandler;
        mSteps = steps;
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        final View view = inflater.inflate(R.layout.item_step, viewGroup, false);

        final StepsViewHolder viewHolder = new StepsViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickHandler.onItemClick(viewHolder.getAdapterPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder recipesViewHolder, int i) {
        Step step = mSteps.get(i);

        recipesViewHolder.stepTextView.setText(step.getShortDescription());

    }

    @Override
    public int getItemCount() {

        if (mSteps != null) {
            return mSteps.size();
        }
        return 0;
    }

    public interface OnItemClickHandler {
        void onItemClick(int position);
    }

    class StepsViewHolder extends RecyclerView.ViewHolder {

        TextView stepTextView;

        StepsViewHolder(@NonNull View itemView) {
            super(itemView);

            stepTextView = itemView.findViewById(R.id.tv_step_title);

        }
    }
}
