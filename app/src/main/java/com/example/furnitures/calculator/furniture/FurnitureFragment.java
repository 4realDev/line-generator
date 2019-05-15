package com.example.furnitures.calculator.furniture;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.furnitures.R;
import org.jetbrains.annotations.NotNull;

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
public class FurnitureFragment
    extends Fragment
    implements FurnitureAdapter.FurnitureClickListener {

    private RecyclerView recyclerView;

    //private FurnitureContract.Navigator navigator;
    private FurnitureContract.ViewModel viewModel;

    @NonNull
    public static FurnitureFragment newInstance() {
        return new FurnitureFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //AppAnalyzer.logEvent(AppAnalyzer.EVENT_SHOW_TRUCK_CALCULATOR_ITEMS);
        //setHasOptionsMenu(true);
        //navigator = new CalculatorNavigator(this);
        viewModel = ViewModelProviders.of(this).get(FurnitureViewModel.class);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_furniture, container, false);
        recyclerView = view.findViewById(R.id.fragment_furniture__recycler);
        return view;
    }

    //@Override
    //protected boolean showHomeAsUp() {
        //return true;
    //}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setTitle(R.string.furniture_title);
        FurnitureAdapter adapter = new FurnitureAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(adapter);
        viewModel.getFurnitureList().observe(getViewLifecycleOwner(), data -> {
            if (data != null) adapter.setData(data);
        });

        //viewModel.getUpNavigationEvent().observe(this, nothing -> navigator.onNavigateUpFromFurniture());
    }

    //@Override
    //public boolean onOptionsItemSelected(MenuItem item) {
    //    switch (item.getItemId()) {
    //        case android.R.id.home:
    //            viewModel.onUpClicked();
    //            return true;
    //        default:
    //            return super.onOptionsItemSelected(item);
    //    }
    //}

    //region FurnitureAdapter.FurnitureClickListener
    @Override
    public void onFurnitureClicked(@NotNull FurnitureViewState furniture) {
        viewModel.onFurnitureClicked(furniture);
    }
    //endregionm
}
