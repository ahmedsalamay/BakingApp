package com.phantom.asalama.baking.screens.Recipes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.ybq.android.spinkit.SpinKitView;
import com.phantom.asalama.baking.BakingApplication;
import com.phantom.asalama.baking.R;
import com.phantom.asalama.baking.models.Baking;
import com.phantom.asalama.baking.util.BakingServices;
import com.phantom.asalama.baking.util.SimpleIdlingResource;
import com.phantom.asalama.baking.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class RecipesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Baking>> {

    //Init vars
    private RecyclerView mRecipesRecyclerView;
    private List<Baking> mBakingList;
    private BakingServices mBakingServices;
    private RecipesRecyclerViewAdapter mRecipesRecyclerViewAdapter;
    private LoaderManager mLoaderManager;
    private SpinKitView mLoadingIndecator;
    private Loader<List<Baking>> bakingLoader;

    @Nullable
    private SimpleIdlingResource mSimpleIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mSimpleIdlingResource == null) {
            mSimpleIdlingResource = new SimpleIdlingResource();
        }
        return mSimpleIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        //init vars
        mBakingList = new ArrayList<>();
        mBakingServices = ((BakingApplication) getApplication()).getmMovieService();
        mLoadingIndecator = findViewById(R.id.spin_kit);
        mRecipesRecyclerView = findViewById(R.id.recipes_rec_view);

        assert mRecipesRecyclerView != null;
        mRecipesRecyclerViewAdapter = new RecipesRecyclerViewAdapter(this, mBakingList);
        mRecipesRecyclerView.setAdapter(mRecipesRecyclerViewAdapter);

        //show 1 colm on recView(portrait) or 3 tablet
        if (!Utility.isTablet(this)) {
            mRecipesRecyclerView.setLayoutManager(new GridLayoutManager(
                    this, 1,
                    LinearLayoutManager.VERTICAL,
                    false));
        } else {
            mRecipesRecyclerView.setLayoutManager(new GridLayoutManager(
                    this, 3,
                    LinearLayoutManager.VERTICAL, false));
        }

        getIdlingResource();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mLoaderManager = getSupportLoaderManager();
        bakingLoader = mLoaderManager.getLoader(0);

        if (bakingLoader == null) {
            getSupportLoaderManager().initLoader(0, null, this);
        } else {
            getSupportLoaderManager().restartLoader(0, null, this);
        }
    }

    @NonNull
    @Override
    public Loader<List<Baking>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<List<Baking>>(this) {
            @Override
            protected void onStartLoading() {
                forceLoad();
                mLoadingIndecator.setVisibility(View.VISIBLE);
            }

            @Nullable
            @Override
            public List<Baking> loadInBackground() {
                Call<List<Baking>> bakingCall = mBakingServices.getBakingInfo();

                if (Utility.isConnectedOrConnecting(getContext())) {
                    Response<List<Baking>> response;

                    try {
                        response = bakingCall.execute();
                        if (response.isSuccessful()) {
                            return response.body();
                        } else {
                            Log.e("Response Msg", response.message());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Baking>> loader, List<Baking> data) {
        mBakingList = data;
        mRecipesRecyclerViewAdapter.setNewData(mBakingList);
        mRecipesRecyclerViewAdapter.notifyDataSetChanged();
        mLoadingIndecator.setVisibility(View.INVISIBLE);
        if (mSimpleIdlingResource != null) {
            mSimpleIdlingResource.setIdleState(true);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Baking>> loader) {

    }
}
