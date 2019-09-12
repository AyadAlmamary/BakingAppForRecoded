package com.ayad.bakingapprecoded.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.ayad.bakingapprecoded.R;
import com.ayad.bakingapprecoded.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {

    private List<Recipe> mRecipes;
    private OnItemClickHandler mClickHandler;

    public RecipesAdapter(List<Recipe> recipes, OnItemClickHandler clickHandler) {
        mClickHandler = clickHandler;
        mRecipes = recipes;
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_recipe, viewGroup, false);

        final RecipesViewHolder viewHolder = new RecipesViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickHandler.onItemClick(viewHolder.getAdapterPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder recipesViewHolder, int i) {
        Recipe recipe = mRecipes.get(i);

        recipesViewHolder.recipeName.setText(recipe.getName());

        String imageUrl = recipe.getImage();
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.get().load(imageUrl)
                    .into(recipesViewHolder.recipeImage);
        } else {
            recipesViewHolder.recipeImage.setImageResource(R.drawable.cupcake);
        }
    }

    @Override
    public int getItemCount() {

        if (mRecipes != null) {
            return mRecipes.size();
        }
        return 0;
    }

    public interface OnItemClickHandler {
        void onItemClick(int position);
    }

    public class RecipesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView_recipe)
        ImageView recipeImage;
        @BindView(R.id.textView_recipe)
        TextView recipeName;

        RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
