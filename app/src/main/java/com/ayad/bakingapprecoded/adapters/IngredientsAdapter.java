package com.ayad.bakingapprecoded.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ayad.bakingapprecoded.R;
import com.ayad.bakingapprecoded.models.Ingredient;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {

    private List<Ingredient> mIngredients;

    public IngredientsAdapter(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_ingredient, viewGroup, false);

        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder recipesViewHolder, int i) {
        Ingredient ingredient = mIngredients.get(i);

        recipesViewHolder.ingredientTextview.setText(ingredient.getIngredient());
        recipesViewHolder.measureTextView.setText(ingredient.getQuantity() + " " + ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {

        if (mIngredients != null) {
            return mIngredients.size();
        }
        return 0;
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder {

        TextView measureTextView;
        TextView ingredientTextview;

        IngredientsViewHolder(@NonNull View itemView) {
            super(itemView);

            measureTextView = itemView.findViewById(R.id.tv_quantity_measure);
            ingredientTextview = itemView.findViewById(R.id.tv_ingredient);
        }
    }
}
