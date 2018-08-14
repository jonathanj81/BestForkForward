package com.example.jon.bestforkforward.UI;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jon.bestforkforward.DataHandling.Ingredient;
import com.example.jon.bestforkforward.R;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterViewHolder> {

    private List<Ingredient> mIngredients;

    @NonNull
    @Override
    public IngredientsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_card_view, parent, false);
        return new IngredientsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapterViewHolder holder, int position) {
        Float quantity = mIngredients.get(position).getQuantity();
        int floorQuantity = (int)Math.floor(quantity);
        if (floorQuantity == quantity){
            holder.mQuantity.setText(String.valueOf(floorQuantity));
        }else {
            holder.mQuantity.setText(String.valueOf(quantity));
        }
        holder.mMeasure.setText(mIngredients.get(position).getMeasure());
        holder.mDescription.setText(mIngredients.get(position).getIngredient());
    }

    @Override
    public int getItemCount() {
        if (mIngredients == null) return 0;
        else return mIngredients.size();
    }

    public void setIngredients(List<Ingredient> ingredients){
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    public class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView mQuantity;
        private TextView mMeasure;
        private TextView mDescription;

        public IngredientsAdapterViewHolder(View itemView) {
            super(itemView);
            mQuantity = itemView.findViewById(R.id.ingredient_quantity);
            mMeasure = itemView.findViewById(R.id.ingredient_measure);
            mDescription = itemView.findViewById(R.id.ingredient_description);
        }
    }
}
