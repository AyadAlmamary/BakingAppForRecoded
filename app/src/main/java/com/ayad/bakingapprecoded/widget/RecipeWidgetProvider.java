package com.ayad.bakingapprecoded.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.RemoteViews;


import com.ayad.bakingapprecoded.R;
import com.ayad.bakingapprecoded.RecipesActivity;
import com.ayad.bakingapprecoded.models.Recipe;
import com.google.gson.Gson;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    private static Recipe cLastVisitedRecipe;
    private static String cRecipeName;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);

        // Send an intent to the service to populate the listView
        Intent intent = new Intent(context, RecipeWidgetRemoteViewsService.class);
        views.setRemoteAdapter(R.id.lv_ingredients, intent);

        // Create an intent to open the RecipesActivity when the widget is clicked
        Intent goToActivityIntent = new Intent(context, RecipesActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, goToActivityIntent, 0);
        views.setOnClickPendingIntent(R.id.tv_recipe_name, pendingIntent);

        // Get the name of the last visited recipe and set its name it to the title TextView
        cRecipeName = getRecipeName(context);
        views.setTextViewText(R.id.tv_recipe_name, cRecipeName);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    // Send a broadcast to update the widget
    public static void sendUpdateBroadcast(Context context) {
        Intent intent = new Intent(context, RecipeWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        context.sendBroadcast(intent);
    }

    // Called when the broadcast is received
    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            // refresh all widgets
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, RecipeWidgetProvider.class);
            manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(cn), R.id.lv_ingredients);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
            views.setTextViewText(R.id.tv_recipe_name, getRecipeName(context));
            manager.updateAppWidget(cn, views);
        }
        super.onReceive(context, intent);
    }

    private static String getRecipeName(Context context) {
        SharedPreferences sp = context.getSharedPreferences("bakingapp", Context.MODE_PRIVATE);
        String lastVisitedRecipeString = sp.getString("recipe", "");
        if (!TextUtils.isEmpty(lastVisitedRecipeString)) {
            Gson gson = new Gson();
            cLastVisitedRecipe = gson.fromJson(lastVisitedRecipeString, Recipe.class);
            return cLastVisitedRecipe.getName();
        } else {
            return "No Data Available";
        }
    }
}

