package com.example.jon.bestforkforward.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.example.jon.bestforkforward.R;
import com.example.jon.bestforkforward.UI.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    private static int mRecipeId;
    private Context mContext;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        RemoteViews views;
        if (width < 200) {
            views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        } else {
            views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider_long);
            Intent intent = new Intent(context, RecipeWidgetService.class);
            intent.putExtra("recipe_id", mRecipeId);
            views.setRemoteAdapter(R.id.widget_ingredients_listview, intent);
        }
        switch (mRecipeId) {
            case 1:
                views.setImageViewResource(R.id.widget_image, R.drawable.nutella);
                break;
            case 2:
                views.setImageViewResource(R.id.widget_image, R.drawable.chocolatebrownie);
                break;
            case 3:
                views.setImageViewResource(R.id.widget_image, R.drawable.yellowcake);
                break;
            case 4:
                views.setImageViewResource(R.id.widget_image, R.drawable.cheesecake);
                break;
            default:
                views.setImageViewResource(R.id.widget_image, R.drawable.nutella);
        }

        Intent toInstructions = new Intent(context, MainActivity.class);
        toInstructions.putExtra("recipe_id", mRecipeId);
        toInstructions.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, toInstructions, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_image, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, null);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_ingredients_listview);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        mContext = context;
        getTodaysId();
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        updateAppWidget(context, appWidgetManager, appWidgetId);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    private void getTodaysId() {
        SharedPreferences prefs = mContext.getSharedPreferences("recipe_id_memory", Context.MODE_PRIVATE);
        if (!prefs.contains("recipe_id")) {
            mRecipeId = 1;
        } else {
            mRecipeId = prefs.getInt("recipe_id", 0) + 1;
        }
        if (mRecipeId > 4) {
            mRecipeId = 1;
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("recipe_id", mRecipeId);
        editor.apply();
    }
}

