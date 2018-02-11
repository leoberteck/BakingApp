package com.example.leonardo.bakingapp.viewAssertions;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class AdapterCountEquals implements ViewAssertion {

    private int expected;

    public AdapterCountEquals(int expected) {
        this.expected = expected;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        int count = 0;
        if(view instanceof ListView){
            count = ((ListView)view).getAdapter() != null ? ((ListView)view).getAdapter().getCount() : count;
        } else if(view instanceof RecyclerView){
            count = ((RecyclerView)view).getAdapter() != null ? ((RecyclerView)view).getAdapter().getItemCount() : count;
        }

        assertThat(count, is(expected));
    }
}
