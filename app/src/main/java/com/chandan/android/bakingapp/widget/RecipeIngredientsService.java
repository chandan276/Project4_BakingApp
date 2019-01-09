package com.chandan.android.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class RecipeIngredientsService extends IntentService {

    public static final String ACTION_UPDATE_INGREDIENTS_WIDGETS = "com.chandan.android.bakingapp.action.update_indredients_widgets";
    private static String ingredientsText = "";
    private static String recipeText = "";

    public RecipeIngredientsService() {
        super("RecipeIngredientsService");
    }

    public static void startActionUpdateIngredientsWidgets(Context context) {
        startActionUpdateIngredientsWidgets(context, RecipeIngredientsService.recipeText, RecipeIngredientsService.ingredientsText);
    }

    public static void startActionUpdateIngredientsWidgets(Context context, String recipeText, String ingredientsText) {
        RecipeIngredientsService.recipeText = recipeText;
        RecipeIngredientsService.ingredientsText = ingredientsText;

        Intent intent = new Intent(context, RecipeIngredientsService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENTS_WIDGETS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_INGREDIENTS_WIDGETS.equals(action)) {
                handleActionUpdateIngredientsWidgets();
            }
        }
    }

    private void handleActionUpdateIngredientsWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeIngredientWidget.class));
        //Now update all widgets
        RecipeIngredientWidget.updateIngredientsWidgets(this, appWidgetManager, RecipeIngredientsService.recipeText, RecipeIngredientsService.ingredientsText, appWidgetIds);
    }
}
