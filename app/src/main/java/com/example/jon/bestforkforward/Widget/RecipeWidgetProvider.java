package com.example.jon.bestforkforward.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.jon.bestforkforward.R;
import com.example.jon.bestforkforward.UI.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    private static int mRecipeId;
    private Context mContext;
    private static final String GENERIC_KEY_STRING = "recipe_id";
    private static final String PREFS_WIDGET_KEY = "recipe_id_widget_memory";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        int height = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        RemoteViews views;
        if (width < 200) {
            views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        } else {
            if (height < 100) {
                views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider_long);
            } else {
                views = new RemoteViews(context.getPackageName(),R.layout.recipe_widget_provider_long_tall);
            }
            Intent intent = new Intent(context, RecipeWidgetService.class);
            intent.putExtra(GENERIC_KEY_STRING, mRecipeId);
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
        toInstructions.putExtra(GENERIC_KEY_STRING, mRecipeId);
        toInstructions.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, toInstructions, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_image, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_ingredients_listview);
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
        SharedPreferences prefs = mContext.getSharedPreferences(PREFS_WIDGET_KEY, Context.MODE_PRIVATE);
        if (!prefs.contains(GENERIC_KEY_STRING)) {
            mRecipeId = 1;
        } else {
            mRecipeId = prefs.getInt(GENERIC_KEY_STRING, 0) + 1;
        }
        if (mRecipeId > 4) {
            mRecipeId = 1;
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(GENERIC_KEY_STRING, mRecipeId);
        editor.apply();
    }
}

