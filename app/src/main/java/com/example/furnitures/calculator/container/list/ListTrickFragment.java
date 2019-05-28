package com.example.furnitures.calculator.container.list;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.furnitures.R;
import com.example.furnitures.calculator.container.selection.ListTrickAdapter;
import com.example.furnitures.calculator.helper.SimpleItemTouchHelperCallback;
import com.example.furnitures.calculator.trick.FurnitureViewState;

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
public class ListTrickFragment
    extends Fragment {

    private RecyclerView recyclerView;
    private ListTrickContract.ViewModel viewModel;

    private SimpleItemTouchHelperCallback simpleItemTouchHelperCallback;
    private ItemTouchHelper itemTouchHelper;

    @NonNull
    public static ListTrickFragment newInstance() {
        return new ListTrickFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(ListTrickViewModel.class);

        simpleItemTouchHelperCallback = new SimpleItemTouchHelperCallback(this, viewModel);
        itemTouchHelper = new ItemTouchHelper(simpleItemTouchHelperCallback);

        // Use ItemTouchHelper (convenience class / bequemlichkeits Klasse)
        // Class that make our RecyclerView swipable
        // Pass SimpleCallBack to create the two Methods
        // SimpleCallback wants two parameters -> drag and drop direction and swipe direction
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_tricks, container, false);
        recyclerView = view.findViewById(R.id.fragment_select_tricks__recycler);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListTrickAdapter adapter = new ListTrickAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Observed alle veränderungen der Liste und gibt sie an den Adapter weiter
        // submitList löst automatisch notifyDataSetChanged aus
        viewModel.getSelectedItemsViewState().observe(getViewLifecycleOwner(), newData -> {
            if (newData != null) adapter.submitList(newData);
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void onItemMoved(Integer fromPosition, Integer toPosition){
        viewModel.onItemMove(fromPosition, toPosition);
    }

    public void onItemDelete(FurnitureViewState furnitureViewState){
        viewModel.onItemDelete(furnitureViewState);
    }

    public FurnitureViewState getFurnitureAt(Integer position){
        return viewModel.getFurnitureAt(position);
    }


}
