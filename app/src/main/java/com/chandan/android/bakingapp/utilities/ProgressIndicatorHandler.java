package com.chandan.android.bakingapp.utilities;

import android.content.Context;

import com.kaopiz.kprogresshud.KProgressHUD;

public class ProgressIndicatorHandler {

    private static final int ANIMATION_SPEED = 2;
    private static final float DIMENSION = 0.5f;

    private static KProgressHUD progressIndicator;

    public static void showProgressIndicator(Context context, String titleLabel, String detailLabel, boolean isCancellable) {
        if (context == null) {
            return;
        }

        progressIndicator = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(isCancellable)
                .setAnimationSpeed(ANIMATION_SPEED)
                .setDimAmount(DIMENSION)
                .show();

        if (titleLabel != null && !titleLabel.equals("")) {
            progressIndicator.setLabel(titleLabel);
        }

        if (detailLabel != null && !detailLabel.equals("")) {
            progressIndicator.setDetailsLabel(detailLabel);
        }
    }

    public static void hideProgressIndicator() {
        progressIndicator.dismiss();
    }
}
