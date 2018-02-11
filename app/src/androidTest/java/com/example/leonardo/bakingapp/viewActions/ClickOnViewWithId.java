package com.example.leonardo.bakingapp.viewActions;

import android.support.annotation.IdRes;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;


public class ClickOnViewWithId implements ViewAction {

    @IdRes
    int id;

    public ClickOnViewWithId(int id) {
        this.id = id;
    }

    @Override
    public Matcher<View> getConstraints() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Click on subview with specific id";
    }

    @Override
    public void perform(UiController uiController, View view) {
        uiController.loopMainThreadUntilIdle();
        view.findViewById(id).performClick();
    }
}
