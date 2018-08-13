package com.example.jon.bestforkforward.Widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.jon.bestforkforward.DataHandling.Ingredient;
import com.example.jon.bestforkforward.DataHandling.Recipe;
import com.example.jon.bestforkforward.DataHandling.RecipeRepository;
import com.example.jon.bestforkforward.R;

import java.util.List;

public class RecipeWidgetService extends RemoteViewsService {

    private static final String GENERIC_KEY_STRING = "recipe_id";
    private long lastChange = 0;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        int id = intent.getExtras().getInt(GENERIC_KEY_STRING);
        Log.i("GET-FACTORY", ": "+ id);
        return new RecipeRemoteViewsFactory(this.getApplicationContext(), id);
    }

    class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

        Context mContext;
        List<Ingredient> mIngredients;
        RecipeRepository mRepo;
        int mRecipeId;

        public RecipeRemoteViewsFactory(Context context, int recipeId){
            mContext = context;
            Log.i("FACTO-CONST", ": "+ recipeId);
            mRecipeId = recipeId-1;
            Log.i("FACTO-CONST-MINUS", ": "+ mRecipeId);
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            mRepo = new RecipeRepository(mContext);
            Log.i("DATA-CHANGE-BEFORE", ": "+ mRecipeId);
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastChange > 1800000) {
                if (mRecipeId < 4) {
                    mRecipeId++;
                } else {
                    mRecipeId = 1;
                }
                lastChange = currentTime;
            }
            Log.i("DATA-CHANGE-AFTER", ": "+ mRecipeId);
            Recipe recipe = mRepo.getSingleWidgetRecipe(mRecipeId);
            mIngredients = recipe.getIngredients();
            Log.i("DATA-CHANGE-INGRED", ": "+ mIngredients);
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mIngredients == null){
                return 0;
            }else {
                return mIngredients.size();
            }
        }

        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.small_ingredients_list_item);

            Log.i("GET-VIEW-INGRED", ": "+ mIngredients);
            Log.i("GET-VIEW-ID", ": "+ mRecipeId);

            Float quantity = mIngredients.get(position).getQuantity();
            String quantityS;
            int floorQuantity = (int)Math.floor(quantity);
            if (floorQuantity == quantity){
                quantityS = String.valueOf(floorQuantity);
            }else {
                quantityS = String.valueOf(quantity);
            }

            String measure = " " + mIngredients.get(position).getMeasure() + " ";
            String description = mIngredients.get(position).getIngredient();
            views.setTextViewText(R.id.small_ingredients_textview, quantityS + measure + description);

            return views;
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
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
