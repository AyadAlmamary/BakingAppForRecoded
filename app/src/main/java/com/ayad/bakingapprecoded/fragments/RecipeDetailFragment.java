package com.ayad.bakingapprecoded.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ayad.bakingapprecoded.R;
import com.ayad.bakingapprecoded.adapters.IngredientsAdapter;
import com.ayad.bakingapprecoded.adapters.StepsAdapter;
import com.ayad.bakingapprecoded.models.Recipe;
import com.ayad.bakingapprecoded.models.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment implements StepsAdapter.OnItemClickHandler {

    @BindView(R.id.rv_ingredients)
    RecyclerView mIngredientsRecyclerView;
    @BindView(R.id.rv_steps)
    RecyclerView mStepsRecyclerView;

    private Recipe mRecipe;
    private OnStepClickListener mCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);

        if (getArguments() != null) {
            mRecipe = (Recipe) getArguments().getSerializable("recipe");
            setUpTheRecyclers();
        }

        return rootView;
    }

    public void setUpTheRecyclers() {
        mIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mIngredientsRecyclerView.setAdapter(new IngredientsAdapter(mRecipe.getIngredients()));
        mStepsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mStepsRecyclerView.setAdapter(new StepsAdapter(mRecipe.getSteps(), this));
    }

    @Override
    public void onItemClick(int position) {
        mCallback.onStepClicked(mRecipe.getSteps().get(position));
    }

    public interface OnStepClickListener {
        void onStepClicked(Step step);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " Must implement OnStepClickListener");
        }
    }
}
