package com.phantom.asalama.baking.screens.RecipeDetail;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.phantom.asalama.baking.R;
import com.phantom.asalama.baking.models.Step;
import com.phantom.asalama.baking.util.Utility;

import java.util.ArrayList;

/**
 * An activity representing a single Step detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeDetailActivity}.
 */
public class StepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //hide the tool bar if we are on landscape to show video on full screen
        if (this.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE
                && !Utility.isTablet(this)) {
            if (getSupportActionBar() != null) {
                toolbar.setVisibility(View.GONE);
                appBarLayout.setVisibility(View.GONE);
            }
            hideSystemUI();
        }
        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            Step step = getIntent().getParcelableExtra(StepDetailFragment.ARG_ITEM_ID);
            ArrayList<Step> steps = getIntent().getParcelableArrayListExtra(Intent.EXTRA_CHANGED_COMPONENT_NAME_LIST);
            arguments.putParcelable(StepDetailFragment.ARG_ITEM_ID,
                    step);
            arguments.putParcelableArrayList(Intent.EXTRA_CHANGED_COMPONENT_NAME_LIST, steps);
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_container, fragment)
                    .commit();
        }
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

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                // Hide the nav bar and status bar
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

}
