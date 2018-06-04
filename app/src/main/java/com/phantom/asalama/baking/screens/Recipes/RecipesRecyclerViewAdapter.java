package com.phantom.asalama.baking.screens.Recipes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.phantom.asalama.baking.BakingApplication;
import com.phantom.asalama.baking.R;
import com.phantom.asalama.baking.models.Baking;
import com.phantom.asalama.baking.screens.RecipeDetail.RecipeDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipesRecyclerViewAdapter extends RecyclerView.Adapter<RecipesRecyclerViewAdapter.ViewHolder> {

    private final String RECIPE_DETAIL_EXTRA = "info";
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Baking baking = (Baking) v.getTag();
            Context context = v.getContext();
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra(RECIPE_DETAIL_EXTRA, baking);
            context.startActivity(intent);
        }
    };
    private List<Baking> mBakings;
    private RecipesActivity mParentActivirty;

    public RecipesRecyclerViewAdapter(RecipesActivity parent, List<Baking> bakings) {
        mBakings = bakings;
        mParentActivirty = parent;
    }

    @NonNull
    @Override
    public RecipesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recipes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesRecyclerViewAdapter.ViewHolder holder, int position) {
        Picasso picasso = ((BakingApplication) mParentActivirty.getApplication()).getmPicasso();

        holder.recipeName.setText(mBakings.get(position).getName());
        holder.recipeServing.setText(String.valueOf(mBakings.get(position).getServings()));

        if (mBakings.get(position).getImage().isEmpty() || mBakings.get(position).getImage() == null) {
            picasso
                    .load(R.drawable.recipe_default_image)
                    .centerCrop()
                    .fit()
                    .into(holder.recipeImage);
        } else {
            picasso
                    .load(mBakings.get(position).getImage())
                    .centerCrop()
                    .placeholder(R.drawable.recipe_default_image)
                    .error(R.drawable.recipe_default_image)
                    .fit()
                    .into(holder.recipeImage);
        }
        holder.recipeNameContainer.setTag(mBakings.get(position));
        holder.recipeNameContainer.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        if (mBakings != null)
            return mBakings.size();
        return 0;
    }

    public void setNewData(List<Baking> newData) {
        mBakings = newData;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView recipeName;
        final TextView recipeServing;
        final ImageView recipeImage;
        final CardView recipeNameContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipe_name);
            recipeServing = itemView.findViewById(R.id.serving_number);
            recipeImage = itemView.findViewById(R.id.image);
            recipeNameContainer = itemView.findViewById(R.id.recipe_name_container);
        }
    }
}
