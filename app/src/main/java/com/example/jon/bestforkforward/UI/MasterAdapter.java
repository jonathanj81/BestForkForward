package com.example.jon.bestforkforward.UI;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jon.bestforkforward.DataHandling.Recipe;
import com.example.jon.bestforkforward.R;

import java.util.List;

public class MasterAdapter extends RecyclerView.Adapter<MasterAdapter.MasterAdapterViewHolder> {

    private List<Recipe> mRecipes;
    private Context mContext;
    private int mWidth;
    private LinearLayout lastClickedUp = null;
    private ImageView lastClickedDown = null;

    @NonNull
    @Override
    public MasterAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_card_view, parent,false);
        mWidth = mContext.getResources().getDisplayMetrics().widthPixels/2 - 32;
        view.setLayoutParams(new RecyclerView.LayoutParams(mWidth, mWidth + 128));
        return new MasterAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MasterAdapterViewHolder holder, int position) {
        holder.mName.setText(mRecipes.get(position).getName());
        holder.mServings.setText(String.valueOf(mRecipes.get(position).getServings()));
        holder.mPreview.getLayoutParams().height = mWidth - 108;
        switch (position){
            case 0:
                holder.mPreview.setImageResource(R.drawable.nutella);
                break;
            case 1:
                holder.mPreview.setImageResource(R.drawable.chocolatebrownie);
                break;
            case 2:
                holder.mPreview.setImageResource(R.drawable.yellowcake);
                break;
            case 3:
                holder.mPreview.setImageResource(R.drawable.cheesecake);
                break;
        }
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

    public class MasterAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mName;
        private TextView mServings;
        private ImageView mPreview;

        public MasterAdapterViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.dessert_name_textview);
            mServings = itemView.findViewById(R.id.dessert_servings_textview);
            mPreview = itemView.findViewById(R.id.dessert_preview_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (lastClickedUp != null){
                lastClickedUp.setVisibility(View.GONE);
                lastClickedDown.setVisibility(View.VISIBLE);
            }
            lastClickedUp = v.findViewById(R.id.card_second_layout);
            lastClickedDown = v.findViewById(R.id.dessert_preview_image);
            lastClickedUp.setVisibility(View.VISIBLE);
            lastClickedDown.setVisibility(View.GONE);

            lastClickedUp.getLayoutParams().height = mWidth - 108;

            Button ingredients = v.findViewById(R.id.ingredients_button);
            Button instructions = v.findViewById(R.id.instructions_button);

            ingredients.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity)v.getContext();
                    IngredientsFragment ingredientsFragment = new IngredientsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("recipe_id", getAdapterPosition()+1);
                    ingredientsFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, ingredientsFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
            instructions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity)v.getContext();
                    StepsFragment stepsFragment = new StepsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("recipe_id", getAdapterPosition()+1);
                    stepsFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, stepsFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }
}
