package com.chandan.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.chandan.android.bakingapp.R;
import com.chandan.android.bakingapp.activity.RecipeListActivity;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, String recipeText,
                                String ingredientsText,
                                int appWidgetId) {

        Intent intent = new Intent(context, RecipeListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredient_widget);
        views.setTextViewText(R.id.recipe_name_appwidget_text, recipeText);
        views.setTextViewText(R.id.ingredients_content_appwidget_text, ingredientsText);

        views.setOnClickPendingIntent(R.id.app_widget_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        RecipeIngredientsService.startActionUpdateIngredientsWidgets(context);
    }

    public static void updateIngredientsWidgets(Context context, AppWidgetManager appWidgetManager,
                                                String recipeText, String ingredientsText, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipeText, ingredientsText, appWidgetId);
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
}

