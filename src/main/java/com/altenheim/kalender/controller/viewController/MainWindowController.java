package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.IAnimationController;
import com.altenheim.kalender.interfaces.IViewRootsModel;
import com.altenheim.kalender.models.SettingsModel;
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
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import jfxtras.styles.jmetro.Style;

public class MainWindowController extends ResponsiveController
{
    private IViewRootsModel allViewsInformation;
    private IAnimationController animationController;  
    private CustomViewOverride customCalendar;  
    private Stage stage;
    private GuiUpdateController guiSetup;
    private Map<String, Pair<Button, Pane>> allButtonsWithBackgrounds;
    private boolean initilizationDone;
    private int currentView = 0;
    private int currentMenuWidth = 240;
    private int topMenuHeight = 100;
    private Button currentlyActive;
    private Background currentSecondaryColor;
    private SettingsModel settings;

    @FXML private Pane menuBtnPanePlanner, menuBtnPaneSmartSearch, menuBtnPaneSettings, menuBtnPaneMail, menuBtnPaneContacts, menuBtnPaneStats;
    @FXML private Button btnLogo, menuBtnPlanner, menuBtnSearch, menuBtnSettings, menuBtnContacts, menuBtnStats, menuBtnMail;     
    @FXML private Button btnAddAppointment, btnSwitchModes, btnSwitchLanguage, btnUser;
    @FXML private GridPane rootContainer, childContainer, topMenu;
    @FXML private AnchorPane viewsRoot;
    @FXML private ColumnConstraints columnLeftMenu;
    @FXML private RowConstraints topRow;
    @FXML private Text txtVersion, txtBreadcrumb;
    @FXML private VBox vboxLeftPane;   
    @FXML private HBox topButtonRow;


    public MainWindowController(Stage stage, IViewRootsModel allViewsInformation, GuiUpdateController guiSetup, CustomViewOverride customCalendar, SettingsModel settings)
    {
        this.stage = stage;
        this.allViewsInformation = allViewsInformation;
        this.guiSetup = guiSetup;  
        this.animationController = new AnimationController();
        this.customCalendar = customCalendar;
        this.settings = settings;
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
        if (button != currentlyActive) {
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
        
    }    
   
    
    @FXML
    private void switchMode(ActionEvent event) 
    {
        switchCssMode();
        if (event != null)
            updateViewOnButtonClicked(currentlyActive);   
    }
    
    public void switchCssMode() {
        if (settings.cssMode.equals("Light"))
        {
            setColorsForDarkAndLightMode(Style.DARK, StylePresets.DARK_MENU_BACKGROUND, StylePresets.DARK_MAIN_BACKGROUND,
                    StylePresets.DARK_PRIMARY, StylePresets.DARK_SECONDARY, StylePresets.DARK_SECONDARY_CSS,
                    StylePresets.DARK_APPLICATION_CSS_FILE, StylePresets.LIGHT_CALENDAR_CSS_FILE, StylePresets.DARK_CALENDAR_CSS_FILE);
            currentSecondaryColor = StylePresets.DARK_SECONDARY;
            settings.cssMode = "Dark";
            settings.writeSimpleProperties();
        }
        else
        {
            setColorsForDarkAndLightMode(Style.LIGHT, StylePresets.LIGHT_MENU_BACKGROUND, StylePresets.LIGHT_MAIN_BACKGROUND,
                    StylePresets.LIGHT_PRIMARY, StylePresets.LIGHT_SECONDARY, StylePresets.LIGHT_SECONDARY_CSS,
                    StylePresets.LIGHT_APPLICATION_CSS_FILE, StylePresets.DARK_CALENDAR_CSS_FILE, StylePresets.LIGHT_CALENDAR_CSS_FILE);
            currentSecondaryColor = StylePresets.LIGHT_SECONDARY;
            settings.cssMode = "Light";
            settings.writeSimpleProperties();
        }
        
    }


    private void setColorsForDarkAndLightMode(Style style, Background menu, Background background, 
        Background primary, Background secondary, String secondaryCSS, String appCssFile, String calCssFileOld, String calCssFileNew)
    {
        guiSetup.getJMetroStyle().setStyle(style);
        vboxLeftPane.setBackground(menu);
        viewsRoot.setBackground(background);
        topButtonRow.setBackground(primary);
        btnLogo.setStyle(secondaryCSS);
        customCalendar.updateCss(calCssFileOld, calCssFileNew);
        for (var view : allViewsInformation.getAllViews()) 
            view.setBackground(background);
        if (initilizationDone)
        {
            var applicationStyle = guiSetup.getJMetroStyle().getOverridingStylesheets();
            applicationStyle.clear();
            applicationStyle.add(0, appCssFile);

            
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
        ((PlannerViewController)allViewsInformation.getAllViewControllers()[0]).updateCustomCalendarView((CustomViewOverride) newView);
    }




    public void updateViewOnButtonClicked(Button pressed)
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
            changeContentPosition(stage.getWidth(), stage.getHeight());  
            updateWindowSize();                       
        };
        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);         
    }    

    private void updateWindowSize()
    {
        double width = stage.getWidth() - currentMenuWidth;
        double height = stage.getHeight() - topMenuHeight; 
     
        viewsRoot.setMinSize(width, height);
        topButtonRow.setMinWidth(width);
        allViewsInformation.getAllViews()[currentView].setMinSize(width, height);
        allViewsInformation.getAllViews()[currentView].setPrefSize(width, height);
        allViewsInformation.getAllViews()[currentView].setMaxSize(width, height);

        allViewsInformation.getAllViewControllers()[currentView].changeContentPosition(width, height);
    }


    final void changeContentPosition(double width, double height) 
    {
        if (width < 1000)
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
        if (height < 800) 
            topMenuHeight = 50;        
        else      
            topMenuHeight = 100;              

        columnLeftMenu.setMinWidth(currentMenuWidth);
        columnLeftMenu.setPrefWidth(currentMenuWidth);
        columnLeftMenu.setMaxWidth(currentMenuWidth);         
        topRow.setMinHeight(topMenuHeight);
        topRow.setMaxHeight(topMenuHeight);
        topRow.setPrefHeight(topMenuHeight);
    }   
}