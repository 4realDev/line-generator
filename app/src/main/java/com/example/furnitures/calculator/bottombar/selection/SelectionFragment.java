package com.example.furnitures.calculator.bottombar.selection;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.furnitures.R;
import com.example.furnitures.calculator.helper.ItemDecorationSpaceGrid;
import com.example.furnitures.calculator.trick.FurnitureViewState;
import org.jetbrains.annotations.NotNull;

public class SelectionFragment
    extends Fragment
    implements SelectionAdapter.FurnitureClickListener {

    private RecyclerView recyclerView;
    //private FurnitureContract.Navigator navigator;
    private FurnitureContract.ViewModel viewModel;

    @NonNull
    public static SelectionFragment newInstance() {
        return new SelectionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //AppAnalyzer.logEvent(AppAnalyzer.EVENT_SHOW_TRUCK_CALCULATOR_ITEMS);
        //setHasOptionsMenu(true);
        //navigator = new CalculatorNavigator(this);

        // wenn man this übergibt, übergibt man den Lifecycler des Fragments
        // ruft die Activity auch ViewModelProviders auf, so erstellt man zwei unterschiedliche Instanzen vom ViewModel
        // Damit beide die selbe Referenzieren, muss man bei beiden den selben Lifecylcer übergeben
        // -> Lifecycler von Acticity (da Activity länger lebt)
        viewModel = ViewModelProviders.of(getActivity()).get(SelectionViewModel.class);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_tricks, container, false);
        recyclerView = view.findViewById(R.id.fragment_select_tricks__recycler);
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
        SelectionAdapter adapter = new SelectionAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        ItemDecorationSpaceGrid spaceGrid = new ItemDecorationSpaceGrid(3,50, 125,true,0);
        recyclerView.addItemDecoration(spaceGrid);
        recyclerView.setAdapter(adapter);

        viewModel.getFurnitureList().observe(getViewLifecycleOwner(), newData -> {
            if (newData != null) adapter.submitList(newData);
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

    //region ListTrickAdapter.FurnitureClickListener
    @Override
    public void onFurnitureClicked(@NotNull FurnitureViewState furniture) {
        viewModel.onFurnitureClicked(furniture);
    }
    //endregionm

    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
