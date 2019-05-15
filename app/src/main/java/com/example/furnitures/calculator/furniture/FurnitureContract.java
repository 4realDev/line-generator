package com.example.furnitures.calculator.furniture;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import java.util.List;

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
public interface FurnitureContract {

    interface ViewModel {

        //region Actions
        //void onUpClicked();
        void onFurnitureClicked(@NonNull FurnitureViewState furniture);
        //endregion

        //region Data
        @NonNull
        LiveData<List<FurnitureViewState>> getFurnitureList();
        //endregion

        //region NavigationEvents
        //@NonNull
        //LiveData<Void> getUpNavigationEvent();
        //endregion
    }

    //interface Navigator {
    //
    //    void onNavigateUpFromFurniture();
    //}
}
