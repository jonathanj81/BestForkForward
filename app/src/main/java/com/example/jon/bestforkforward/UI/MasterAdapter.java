package com.example.jon.bestforkforward.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jon.bestforkforward.DataHandling.Recipe;
import com.example.jon.bestforkforward.R;

import java.util.List;

public class MasterAdapter extends RecyclerView.Adapter<MasterAdapter.MasterAdapterViewHolder> {

    private List<Recipe> mRecipes;
    private Context mContext;

    @NonNull
    @Override
    public MasterAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_card_view, parent,false);
        return new MasterAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MasterAdapterViewHolder holder, int position) {
        holder.mName.setText(mRecipes.get(position).getName());
        holder.mIngredient.setText(mRecipes.get(position).getIngredients().get(0).getIngredient());
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) return 0;
        else return mRecipes.size();
    }

    public void setRecipes(List<Recipe> recipes){
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    public class MasterAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private TextView mIngredient;

        public MasterAdapterViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.card_recipe_name_textview);
            mIngredient = itemView.findViewById(R.id.card_recipe_ingredient1_textview);
        }
    }
}
