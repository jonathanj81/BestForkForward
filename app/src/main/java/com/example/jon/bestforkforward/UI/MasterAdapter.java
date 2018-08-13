package com.example.jon.bestforkforward.UI;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
    private int mWidth;
    private LinearLayout lastClickedUp = null;
    private ImageView lastClickedDown = null;
    private boolean isLand = false;
    private Context mContext;

    private static final String GENERIC_KEY_STRING = "recipe_id";

    @NonNull
    @Override
    public MasterAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int divisor = 2;
        int subtractor = 32;
        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_card_view, parent,false);

        isLand = (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
        if (isLand){
            divisor = 4;
            subtractor = 16;
            TextView dessert = view.findViewById(R.id.dessert_name_textview);
            dessert.setTextAppearance(mContext,android.R.style.TextAppearance_DeviceDefault_Medium);
        }
        mWidth = screenWidth/divisor - subtractor;
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

            if (!(mContext.getResources().getConfiguration().smallestScreenWidthDp > 600)){
                lastClickedUp.getLayoutParams().height = mWidth - 108;
                lastClickedDown.setVisibility(View.GONE);
            } else {
                lastClickedDown.setVisibility(View.INVISIBLE);
            }

            Button ingredients = v.findViewById(R.id.ingredients_button);
            Button instructions = v.findViewById(R.id.instructions_button);

            final int containerID = isLand ? R.id.container_right : R.id.container;

            ingredients.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.stateID = getAdapterPosition()+1;
                    MainActivity.isInstructions = false;
                    AppCompatActivity activity = (AppCompatActivity)v.getContext();
                    IngredientsFragment ingredientsFragment = new IngredientsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(GENERIC_KEY_STRING, MainActivity.stateID);
                    ingredientsFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(containerID, ingredientsFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
            instructions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.stateID = getAdapterPosition()+1;
                    MainActivity.isInstructions = true;
                    AppCompatActivity activity = (AppCompatActivity)v.getContext();
                    StepsFragment stepsFragment = new StepsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(GENERIC_KEY_STRING, MainActivity.stateID);
                    stepsFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(containerID, stepsFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }
}
