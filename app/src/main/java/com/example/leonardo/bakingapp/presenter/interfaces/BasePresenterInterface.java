package com.example.leonardo.bakingapp.presenter.interfaces;

import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.databinding.Observable;
import android.support.v7.widget.RecyclerView;

@BindingMethods(value = {
    @BindingMethod(type = RecyclerView.class, attribute = "android:adapter", method = "setAdapter")
})
public interface BasePresenterInterface extends Observable {
}
