package com.altenheim.kalender.implementations.controller.viewController;

import com.altenheim.kalender.interfaces.models.ViewRootsModel;
import com.altenheim.kalender.interfaces.models.SettingsModel;
import com.altenheim.kalender.interfaces.viewController.GuiUpdateController;
import com.altenheim.kalender.implementations.controller.models.SettingsModelImpl;
import com.altenheim.kalender.resourceClasses.*;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class GuiUpdateControllerImpl implements GuiUpdateController
{
    private JMetro jMetroStyle;
    private ViewRootsModel viewsInformation;
    private SettingsModel settings;

    public GuiUpdateControllerImpl(JMetro jMetroStyle, ViewRootsModel viewsInformation, SettingsModel settings)
    {
        this.jMetroStyle = jMetroStyle;
        this.viewsInformation = viewsInformation;
        this.settings = settings;
    }

    public JMetro getJMetroStyle()
    {
        return jMetroStyle;
    }

    public void init()
    {
        setupViews();
        initializeViews();
        updateChildContainers();
    }

    public void setupColorMode() 
    {
        jMetroStyle.getOverridingStylesheets().clear();
        if (SettingsModelImpl.isDarkmodeActive)
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

    public void registerCalendars()
    {
        ((PlannerViewController)viewsInformation.getAllViewControllers()[0]).registerButtonEvents();
    }

    private void setupViews()
    {
        for (int i = 0; i < FxmlFiles.ALL_FILES.length; i++)
        {
            var loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(FxmlFiles.ALL_FILES[i]));
            loader.setController(viewsInformation.getAllViewControllers()[i]);
            try
            {
                viewsInformation.addViewRootToList(i, loader.load());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
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