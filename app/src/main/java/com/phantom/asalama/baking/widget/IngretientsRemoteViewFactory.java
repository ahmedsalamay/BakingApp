package com.phantom.asalama.baking.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.phantom.asalama.baking.R;
import com.phantom.asalama.baking.models.Baking;

public class IngretientsRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context mContext;
    private final String RECIPE_DETAIL_EXTRA = "info";
    Baking mRecipe;

    public IngretientsRemoteViewFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        String reipeJson = "";
        Gson gson = new Gson();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        reipeJson = preferences.getString(mContext.getString(R.string.preferences_key), "");
        mRecipe = gson.fromJson(reipeJson, Baking.class);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mRecipe != null)
            return mRecipe.getIngredients().size();
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mRecipe == null)
            return null;
        if (mRecipe.getIngredients().size() == 0)
            return null;

        Double quantity = mRecipe.getIngredients().get(position).getQuantity();
        String measure = mRecipe.getIngredients().get(position).getMeasure();
        String ingredien = mRecipe.getIngredients().get(position).getIngredient();

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                R.layout.list_item_ingredients_widget);
        remoteViews.setTextViewText(R.id.quantity, quantity.toString());
        remoteViews.setTextViewText(R.id.measure, measure);
        remoteViews.setTextViewText(R.id.ingredient_name, ingredien);

        //Bundle args=new Bundle();
        //args.putParcelable(
        Intent fillInIntent = new Intent();
        //fillInIntent.putExtras(args);
        fillInIntent.putExtra(RECIPE_DETAIL_EXTRA, mRecipe);
        remoteViews.setOnClickFillInIntent(R.id.ingredients_list_item_container_widget, fillInIntent);
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
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
