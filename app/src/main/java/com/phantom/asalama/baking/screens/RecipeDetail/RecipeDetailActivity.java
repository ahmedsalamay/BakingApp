package com.phantom.asalama.baking.screens.RecipeDetail;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.phantom.asalama.baking.R;
import com.phantom.asalama.baking.models.Baking;
import com.phantom.asalama.baking.models.Ingredient;
import com.phantom.asalama.baking.models.Step;
import com.phantom.asalama.baking.widget.BakingWidgetProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    private final String RECIPE_DETAIL_EXTRA = "info";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Baking mBaking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            mBaking = getIntent().getParcelableExtra(RECIPE_DETAIL_EXTRA);
        } else {
            mBaking = savedInstanceState.getParcelable(RECIPE_DETAIL_EXTRA);
        }

        if (findViewById(R.id.step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            Bundle arguments = new Bundle();
            arguments.putParcelable(StepDetailFragment.ARG_ITEM_ID, mBaking.getSteps().get(0));
            arguments.putParcelableArrayList(Intent.EXTRA_CHANGED_COMPONENT_NAME_LIST, (ArrayList<? extends Parcelable>) mBaking.getSteps());
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, fragment)
                    .commit();
            mTwoPane = true;
        }
        View ingredientRecyclerView = findViewById(R.id.ingredients_list);
        assert ingredientRecyclerView != null;
        setupingredientRecyclerView((RecyclerView) ingredientRecyclerView);

        View StepsRecyclerView = findViewById(R.id.step_list);
        assert StepsRecyclerView != null;
        setupStepsRecyclerView((RecyclerView) StepsRecyclerView);
        setCurrentRecieOnWidget();

    }

    private void setupingredientRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new IngredentsRecyclerViewAdapter(mBaking.getIngredients()));
    }

    private void setupStepsRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new StepsRecyclerViewAdapter(this, mBaking.getSteps(), mTwoPane));
    }

    private void setCurrentRecieOnWidget() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String recipejson = (new Gson()).toJson(mBaking);
        editor.putString(this.getString(R.string.preferences_key), recipejson);
        editor.apply();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds
                (new ComponentName(this, BakingWidgetProvider.class));
        BakingWidgetProvider.updateRecipeWidget(this, appWidgetManager, appWidgetIds, mBaking.getName());
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_ingredients_list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_DETAIL_EXTRA, mBaking);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mBaking = savedInstanceState.getParcelable(RECIPE_DETAIL_EXTRA);
    }

    public static class IngredentsRecyclerViewAdapter
            extends RecyclerView.Adapter<IngredentsRecyclerViewAdapter.ViewHolder> {

        private final List<Ingredient> mValues;

        IngredentsRecyclerViewAdapter(List<Ingredient> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIngredient.setText(mValues.get(position).getIngredient());
            holder.mQuantity.setText(mValues.get(position).getQuantity().toString());
            holder.mMeasure.setText(mValues.get(position).getMeasure());
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIngredient;
            final TextView mQuantity;
            final TextView mMeasure;

            ViewHolder(View view) {
                super(view);
                mIngredient = view.findViewById(R.id.ingredient_name);
                mQuantity = view.findViewById(R.id.quantity);
                mMeasure = view.findViewById(R.id.measure);
            }
        }
    }

    public static class StepsRecyclerViewAdapter
            extends RecyclerView.Adapter<StepsRecyclerViewAdapter.ViewHolder> {

        private final RecipeDetailActivity mParentActivity;
        private final List<Step> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Step item = (Step) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(StepDetailFragment.ARG_ITEM_ID, item);
                    arguments.putParcelableArrayList(Intent.EXTRA_CHANGED_COMPONENT_NAME_LIST, (ArrayList<? extends Parcelable>) mValues);
                    StepDetailFragment fragment = new StepDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.step_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, StepDetailActivity.class);
                    intent.putExtra(StepDetailFragment.ARG_ITEM_ID, item);
                    intent.putParcelableArrayListExtra
                            (Intent.EXTRA_CHANGED_COMPONENT_NAME_LIST, (ArrayList<? extends Parcelable>) mValues);
                    context.startActivity(intent);
                }
            }
        };

        StepsRecyclerViewAdapter(RecipeDetailActivity parent,
                                 List<Step> items,
                                 boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mContentView.setText(mValues.get(position).getShortDescription());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mContentView = view.findViewById(R.id.step_description_txt);
            }
        }
    }

}
