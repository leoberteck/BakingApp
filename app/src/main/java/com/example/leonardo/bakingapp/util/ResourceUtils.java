package com.example.leonardo.bakingapp.util;


import android.content.Context;
import android.support.annotation.StringRes;

public final class ResourceUtils {

    private Context context;

    public ResourceUtils(Context context) {
        this.context = context;
    }

    public String getString(@StringRes int stringId){
        return context.getString(stringId);
    }
}