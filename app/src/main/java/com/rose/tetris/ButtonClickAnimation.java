package com.rose.tetris;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class ButtonClickAnimation {
    public static void aniButtonClick(View view) {
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click);
        view.startAnimation(animation);
    }
}
