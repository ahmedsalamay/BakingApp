package com.phantom.asalama.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.phantom.asalama.baking.R;
import com.phantom.asalama.baking.models.Baking;
import com.phantom.asalama.baking.screens.RecipeDetail.RecipeDetailActivity;
import com.phantom.asalama.baking.screens.Recipes.RecipesActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String recipeName) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);

        Intent homeScreenIntent = new Intent(context, RecipesActivity.class);
        PendingIntent homeScreenPendingIntent = PendingIntent.getActivity
                (context, 0, homeScreenIntent, 0);
        views.setOnClickPendingIntent(R.id.widget_container, homeScreenPendingIntent);

        Intent ingredientsIntent = new Intent(context, WidgetService.class);
        views.setRemoteAdapter(R.id.widget_ingredients_list, ingredientsIntent);

        Intent recipeDetailIntent = new Intent(context, RecipeDetailActivity.class);
        PendingIntent reipeDetailPendingIntent = PendingIntent.getActivity
                (context, 0, recipeDetailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_ingredients_list, reipeDetailPendingIntent);

        views.setTextViewText(R.id.widget_title, recipeName);
        views.setEmptyView(R.id.widget_ingredients_list, R.id.empty_state_ingredients);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateRecipeWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, String recipeName) {
        for (int widgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, widgetId, recipeName);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        String recipeName = "";
        String reipeJson = "";
        Baking reipeObj = new Baking();
        Gson gson = new Gson();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences.contains(context.getString(R.string.preferences_key))) {
            reipeJson = preferences.getString(context.getString(R.string.preferences_key), "");
            reipeObj = gson.fromJson(reipeJson, Baking.class);
            recipeName = reipeObj.getName();
        }

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeName);
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
}

