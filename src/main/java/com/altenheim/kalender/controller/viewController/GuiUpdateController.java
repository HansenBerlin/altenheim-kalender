package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.IViewRootsModel;
import com.altenheim.kalender.models.SettingsModel;
import com.altenheim.kalender.resourceClasses.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.MDL2IconFont;
import jfxtras.styles.jmetro.Style;

public class GuiUpdateController 
{
    private JMetro jMetroStyle;
    private IViewRootsModel viewsInformation;
    //private boolean isDarkmodeActive = true;
    private SettingsModel settings;

    public JMetro getJMetroStyle() 
    {
        return jMetroStyle;
    }

    public GuiUpdateController(JMetro jMetroStyle, IViewRootsModel viewsInformation, SettingsModel settings) 
    {
        this.jMetroStyle = jMetroStyle;
        this.viewsInformation = viewsInformation;
        this.settings = settings;
    }

    public void init() throws IOException 
    {
        setupViews();
        initializeViews();
        updateChildContainers();
    }

    public void setupColorMode() 
    {
        jMetroStyle.getOverridingStylesheets().clear();
        if (settings.isDarkmodeActive) 
        {
            jMetroStyle.setStyle(Style.LIGHT);
            jMetroStyle.getOverridingStylesheets().add(StylePresets.LIGHT_APPLICATION_CSS_FILE);
        } 
        else 
        {
            jMetroStyle.setStyle(Style.DARK);
            jMetroStyle.getOverridingStylesheets().add(StylePresets.DARK_APPLICATION_CSS_FILE);
        }
        viewsInformation.getMainWindowController().switchCssMode();
    }

    private void setupViews() throws IOException 
    {
        for (int i = 0; i < FxmlFiles.ALL_FILES.length; i++) 
        {
            var loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(FxmlFiles.ALL_FILES[i]));
            loader.setController(viewsInformation.getAllViewControllers()[i]);            
            viewsInformation.addViewRootToList(i, loader.load());
        }
    }

    public void registerCalendars()
    {
        ((PlannerViewController)viewsInformation.getAllViewControllers()[0]).registerButtonEvents();
    }

    private void initializeViews() 
    {
        for (var view : viewsInformation.getAllViews()) 
        {
            view.setDisable(true);
            view.setVisible(false);
        }
        viewsInformation.getAllViews()[0].setVisible(true);
        viewsInformation.getAllViews()[0].setDisable(false);
    }

    private void updateChildContainers() 
    {
        for (int i = 0; i < FxmlFiles.ALL_FILES.length; i++)
            viewsInformation.getAllViewControllers()[i].setChildContainer(viewsInformation.getAllViews()[i]);
    }        
}