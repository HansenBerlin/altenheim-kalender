package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.IViewRootsModel;
import com.altenheim.kalender.resourceClasses.FxmlFiles;
import com.altenheim.kalender.resourceClasses.StylePresets;
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
    private boolean isDarkmodeActive = true;

    public JMetro getJMetroStyle() { return jMetroStyle; }


    public GuiUpdateController(JMetro jMetroStyle, IViewRootsModel viewsInformation)
    {
        this.jMetroStyle = jMetroStyle;
        this.viewsInformation = viewsInformation;
    }


    public void init() throws IOException
    {       
        setupViews();
        initializeViews();
        updateChildContainers();
    }


    public void setupColorMode()
    {
        if (isDarkmodeActive)
        {
            jMetroStyle.setStyle(Style.LIGHT);
            jMetroStyle.getOverridingStylesheets().add(StylePresets.LIGHT_APPLICATION_CSS_FILE);
        }
        else
        {
            jMetroStyle.setStyle(Style.DARK);
            jMetroStyle.getOverridingStylesheets().add(StylePresets.DARK_APPLICATION_CSS_FILE);
        }
        isDarkmodeActive ^= true;
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


    public Map<String, Pair<Button, Pane>> createMainMenuButtons(Button[] buttons, Pane[] buttonBackgrounds) throws FileNotFoundException
    {
        setImages(buttons);

        String[] buttonCaptions = {"Planer", "Smart Search", "Kontakte", "Mailtemplates", "Einstellungen", "", ""};
        var buttonsMap = new Hashtable<String, Pair<Button, Pane>>();

        for (int i = 0; i < buttons.length; i++) 
        {   
            buttons[i].setAccessibleText(String.format("%d", i));
            var buttonAndBackground = new Pair<Button, Pane>(buttons[i], buttonBackgrounds[i]);
            buttonsMap.put(buttonCaptions[i], buttonAndBackground);
        }

        return buttonsMap;
    }


    private void setImages(Button[] buttonsList) throws FileNotFoundException
    {
        var iconCal = new MDL2IconFont("\uE787");
        var iconSearch = new MDL2IconFont("\uE99A");
        var iconContacts = new MDL2IconFont("\uE779");
        //var iconStats = new MDL2IconFont("\uE776");
        var iconMail = new MDL2IconFont("\uE715");
        var iconSettings = new MDL2IconFont("\uE713");
        var iconPlus = new MDL2IconFont("\uE710");
        var iconMode = new MDL2IconFont("\uE793");
        //var iconLanguage = new MDL2IconFont("\uE774");
        //var iconUser = new MDL2IconFont("\uE748");
        //var iconClosePane = new MDL2IconFont("\uE8A0");
        //var iconOpenPane= new MDL2IconFont("\uE89F");

        MDL2IconFont[] iconListMenuButtons = {iconCal, iconSearch, iconContacts, 
            iconMail, iconSettings, iconPlus, iconMode};
         

        for (int i = 0; i < iconListMenuButtons.length; i++) 
        {
            iconListMenuButtons[i].setStyle("-fx-font-size:22");   
            buttonsList[i].setGraphic(iconListMenuButtons[i]);         
        }        
    }      
}
