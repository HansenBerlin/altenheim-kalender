package com.altenheim.kalender.interfaces;

import com.altenheim.kalender.controller.*;
import javafx.scene.layout.GridPane;

public interface ViewRootsInterface 
{
    public GridPane[] getAllViews();
    public ResponsiveController[] getAllViewControllers();
    public void addViewRootToList(int atIndex, GridPane view);
}
