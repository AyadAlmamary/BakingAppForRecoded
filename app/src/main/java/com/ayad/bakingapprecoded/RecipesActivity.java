package com.ayad.bakingapprecoded;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.ayad.bakingapprecoded.adapters.RecipesAdapter;
import com.ayad.bakingapprecoded.models.Recipe;
import com.ayad.bakingapprecoded.utils.RetrofitClient;
import com.ayad.bakingapprecoded.utils.RetrofitInterface;
import com.ayad.bakingapprecoded.utils.SimpleIdlingResource;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesActivity extends AppCompatActivity implements RecipesAdapter.OnItemClickHandler {

    @BindView(R.id.recycler_recipes)
    RecyclerView mRecipesRecycler;
    private RecipesAdapter mRecipesAdapter;
    private List<Recipe> mRecipes;

    private ProgressBar mProgressBar;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        ButterKnife.bind(this);

        mRecipes = new ArrayList<>();

        getIdlingResource();

        setupRecyclerView();
        showRecipes(mIdlingResource);
    }

    public void setupRecyclerView() {
        mRecipesAdapter = new RecipesAdapter(mRecipes, this);
        mRecipesRecycler.setAdapter(mRecipesAdapter);

        RecyclerView.LayoutManager layoutManager;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            layoutManager = new GridLayoutManager(
                    RecipesActivity.this, 2, GridLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new LinearLayoutManager(this);
        }

        mRecipesRecycler.setLayoutManager(layoutManager);
    }

    private void showRecipes(final SimpleIdlingResource idlingResource) {
        // Show the progressbar while waiting for data to load
        mProgressBar = findViewById(R.id.progressbar);
        mProgressBar.setVisibility(View.VISIBLE);

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        RetrofitInterface service = RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class);
        Call<List<Recipe>> call = service.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                mProgressBar.setVisibility(View.GONE);

                List<Recipe> recipeList = response.body();
                if (recipeList != null) {
                    mRecipes.clear();
                    mRecipes.addAll(recipeList);
                    mRecipesAdapter.notifyDataSetChanged();

                    if (idlingResource != null) {
                        idlingResource.setIdleState(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Log.e("onFailure", t.getMessage());
                Toast.makeText(RecipesActivity.this, "Something went wrong!. Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        Recipe selectedRecipe = mRecipes.get(position);
        intent.putExtra("recipe", selectedRecipe);
        startActivity(intent);
    }
}
