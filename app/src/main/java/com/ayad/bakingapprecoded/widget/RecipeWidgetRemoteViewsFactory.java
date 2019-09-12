package com.ayad.bakingapprecoded.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import com.ayad.bakingapprecoded.R;
import com.ayad.bakingapprecoded.models.Ingredient;
import com.ayad.bakingapprecoded.models.Recipe;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * RemoteViewsFactory serves the purpose of an adapter in the widget’s context.
 * An adapter is used to connect the collection items with the data set.
 */
public class RecipeWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Ingredient> mIngredientList;

    public RecipeWidgetRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        mIngredientList = new ArrayList<>();
    }

    @Override
    public void onDataSetChanged() {
        getRecipeIngredients(mContext);
    }

    @Override
    public void onDestroy() {
        mIngredientList.clear();
    }

    @Override
    public int getCount() {
        return mIngredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item_ingredient);

        // Bind the list items with the ingredient content
        Ingredient ingredient = mIngredientList.get(position);
        remoteViews.setTextViewText(R.id.tv_quantity_measure,
                ingredient.getQuantity() + " " + ingredient.getMeasure());
        remoteViews.setTextViewText(R.id.tv_ingredient, ingredient.getIngredient());

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void getRecipeIngredients(Context context) {
        SharedPreferences sp = context.getSharedPreferences("bakingapp", Context.MODE_PRIVATE);
        String lastVisitedRecipeString = sp.getString("recipe", "");
        if (!TextUtils.isEmpty(lastVisitedRecipeString)) {
            Gson gson = new Gson();
            Recipe lastVisitedRecipe = gson.fromJson(lastVisitedRecipeString, Recipe.class);
            mIngredientList = lastVisitedRecipe.getIngredients();
        }
    }
}
