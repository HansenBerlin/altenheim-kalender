package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.IViewRootsModel;
import com.altenheim.kalender.resourceClasses.StylePresets;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import jfxtras.styles.jmetro.Style;

public class MainWindowController extends ResponsiveController
{
    private Stage stage;
    private IViewRootsModel allViewsInformation;
    private GuiUpdateController guiSetup;
    private Map<String, Pair<Button, Pane>> allButtonsWithBackgrounds;
    private boolean initilizationDone;
    private int currentView = 0;
    private int currentMenuWidth = 240;
    private Button currentlyActive;
    private boolean darkModeActive = false;
    private Background currentSecondaryColor;

    @FXML private Pane menuBtnPanePlanner, menuBtnPaneSmartSearch, menuBtnPaneSettings, menuBtnPaneMail, menuBtnPaneContacts, menuBtnPaneStats;
    @FXML private Button btnLogo, menuBtnPlanner, menuBtnSearch, menuBtnSettings, menuBtnContacts, menuBtnStats, menuBtnMail;     
    @FXML private Button btnAddAppointment, btnSwitchModes, btnSwitchLanguage, btnUser;
    @FXML private GridPane rootContainer, childContainer, topMenu;
    @FXML private AnchorPane viewsRoot;
    @FXML private ColumnConstraints columnLeftMenu;
    @FXML private Text txtVersion, txtBreadcrumb;
    @FXML private VBox vboxLeftPane;   
    @FXML private HBox topButtonRow;


    public MainWindowController(Stage stage, IViewRootsModel allViewsInformation, GuiUpdateController guiSetup)
    {
        this.stage = stage;
        this.allViewsInformation = allViewsInformation;
        this.guiSetup = guiSetup;  
    }


    @FXML 
    private void initialize() throws IOException 
    {
        ((PlannerViewController)allViewsInformation.getAllViewControllers()[0]).updateCustomCalendarView(null);
        viewsRoot.getChildren().addAll(allViewsInformation.getAllViews());
        switchMode(null);
        setupMenuButtons();
        bindWindowSize();
        initilizationDone = true;
    }


    @FXML
    private void changeScene(MouseEvent event) throws IOException 
    {
        var button = (Button)event.getSource();
        int userChoice = Integer.parseInt(button.getAccessibleText()); 
        updateViewOnButtonClicked(button);        
        allViewsInformation.getAllViews()[userChoice].setDisable(false);
        allViewsInformation.getAllViews()[userChoice].setVisible(true);  
        allViewsInformation.getAllViews()[currentView].setDisable(true);
        allViewsInformation.getAllViews()[currentView].setVisible(false);
        currentView = userChoice;
        currentlyActive = button;
        updateWindowSize();
    }    
   
    
    @FXML
    private void switchMode(ActionEvent event) 
    {
        if (darkModeActive)
        {
            setColorsForDarkAndLightMode(Style.DARK, StylePresets.DARK_MENU_BACKGROUND, StylePresets.DARK_MAIN_BACKGROUND,
                    StylePresets.DARK_PRIMARY, StylePresets.DARK_SECONDARY, StylePresets.DARK_SECONDARY_CSS,
                    StylePresets.DARK_APPLICATION_CSS_FILE, StylePresets.DARK_CALENDAR_CSS_FILE);
            currentSecondaryColor = StylePresets.DARK_SECONDARY;
        }
        else
        {
            setColorsForDarkAndLightMode(Style.LIGHT, StylePresets.LIGHT_MENU_BACKGROUND, StylePresets.LIGHT_MAIN_BACKGROUND,
                    StylePresets.LIGHT_PRIMARY, StylePresets.LIGHT_SECONDARY, StylePresets.LIGHT_SECONDARY_CSS,
                    StylePresets.LIGHT_APPLICATION_CSS_FILE, StylePresets.LIGHT_CALENDAR_CSS_FILE);
            currentSecondaryColor = StylePresets.LIGHT_SECONDARY;
        }
        if (event != null)
            updateViewOnButtonClicked(currentlyActive);

        darkModeActive ^= true;
    } 


    private void setColorsForDarkAndLightMode(Style style, Background menu, Background background, 
        Background primary, Background secondary, String secondaryCSS, String appCssFile, String calCssFile)
    {
        guiSetup.getJMetroStyle().setStyle(style);
        vboxLeftPane.setBackground(menu);
        viewsRoot.setBackground(background);
        topButtonRow.setBackground(primary);
        btnLogo.setStyle(secondaryCSS);
        for (var view : allViewsInformation.getAllViews()) 
            view.setBackground(background);
        if (initilizationDone)
        {
            var applicationStyle = guiSetup.getJMetroStyle().getOverridingStylesheets();
            applicationStyle.clear();
            applicationStyle.add(0, appCssFile);

            //updateCalendarStyle(guiSetup.getCalendarView(), calCssFile);
        }
    }

    private void updateCalendarStyle (CalendarView calendarView, String cssFile)
    {
        var newView = new CalendarView();
        newView.getStylesheets().add(cssFile);
        var newCalendarSource = new CalendarSource();
        var oldCalendarSources = calendarView.getCalendarSources();
        for (var source: oldCalendarSources)
        {
            newCalendarSource.getCalendars().addAll(source.getCalendars());
        }
        ((PlannerViewController)allViewsInformation.getAllViewControllers()[0]).updateCustomCalendarView(newView);
    }




    private void updateViewOnButtonClicked(Button pressed)
    {   
        for (String buttonName : allButtonsWithBackgrounds.keySet())
        {
            var buttonPair = allButtonsWithBackgrounds.get(buttonName);
            if (buttonPair.getKey().equals(currentlyActive) && !currentlyActive.equals(pressed))
                buttonPair.getValue().setBackground(StylePresets.TRANSPARENT);
            else if (buttonPair.getKey().equals(pressed))
            {
                buttonPair.getValue().setBackground(currentSecondaryColor);
                txtBreadcrumb.setText(buttonName);
            }
        }
    }


    private void setupMenuButtons() throws FileNotFoundException
    {
        Button[] buttonsList = { menuBtnPlanner, menuBtnSearch, menuBtnStats, menuBtnContacts, menuBtnMail,
            menuBtnSettings, btnAddAppointment, btnSwitchModes, btnSwitchLanguage, btnUser };
        Pane[] buttonBackgrounds = { menuBtnPanePlanner, menuBtnPaneSmartSearch, menuBtnPaneStats,
            menuBtnPaneContacts, menuBtnPaneMail, menuBtnPaneSettings, null, null, null, null };
        allButtonsWithBackgrounds = guiSetup.createMainMenuButtons(buttonsList, buttonBackgrounds);
        currentlyActive = menuBtnPlanner;
        menuBtnPanePlanner.setBackground(currentSecondaryColor);
    }
    
    
    private void bindWindowSize()
    {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
        {
            changeContentPosition();  
            updateWindowSize();            
        };
        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);         
    }    

    private void updateWindowSize()
    {
        double setWidth = stage.getWidth() - currentMenuWidth;
        double setHeight = stage.getHeight();          
        viewsRoot.setMinSize(setWidth, setHeight);
        topButtonRow.setMinWidth(setWidth);
        allViewsInformation.getAllViews()[currentView].setMinSize(setWidth, setHeight);
        allViewsInformation.getAllViewControllers()[currentView].changeContentPosition();
    }


    final void changeContentPosition() 
    {
        if (stage.getWidth() < 1000)
        {
            currentMenuWidth = 70;
            for (String buttonName : allButtonsWithBackgrounds.keySet())             
                allButtonsWithBackgrounds.get(buttonName).getKey().setText(""); 
            btnLogo.setText("SP");  
            txtVersion.setText("v. 0.1.2");
        }
        else
        {
            currentMenuWidth = 240;
            for (String buttonName : allButtonsWithBackgrounds.keySet()) 
                allButtonsWithBackgrounds.get(buttonName).getKey().setText(buttonName);             
            btnLogo.setText("SMARTPLANNER");  
            txtVersion.setText("Version 0.1.2; HWR Gruppe C"); 
        }      

        columnLeftMenu.setMinWidth(currentMenuWidth);
        columnLeftMenu.setPrefWidth(currentMenuWidth);
        columnLeftMenu.setMaxWidth(currentMenuWidth);        
    }   
}