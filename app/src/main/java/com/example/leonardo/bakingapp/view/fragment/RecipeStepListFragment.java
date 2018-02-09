package com.example.leonardo.bakingapp.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leonardo.bakingapp.R;
import com.example.leonardo.bakingapp.databinding.RecipeStepListFragmentBinding;
import com.example.leonardo.bakingapp.presenter.interfaces.RecipeStepListMVP;

public class RecipeStepListFragment extends Fragment implements RecipeStepListMVP.RecipeStepListFragmentInterface {

    public static RecipeStepListFragment newInstance(RecipeStepListMVP.RecipeStepListPresenterInterface presenter){
        RecipeStepListFragment fragment = new RecipeStepListFragment();
        RecipeStepListFragment.presenter = presenter;
        presenter.setFragment(fragment);
        return fragment;
    }

    private static RecipeStepListMVP.RecipeStepListPresenterInterface presenter;

    public RecipeStepListFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecipeStepListFragmentBinding binding = DataBindingUtil.inflate(
            LayoutInflater.from(getActivity())
            , R.layout.recipe_step_list_fragment
            , container
            , false
        );
        binding.setPresenter(presenter);
        View view = binding.getRoot();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_recipe_steps);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.loadAdapter();
    }
}
