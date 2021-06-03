package com.altenheim.kalender.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.MDL2IconFont;
import jfxtras.styles.jmetro.Style;

public class MainWindowController 
{
    private Background primaryColor, secondaryColor, transparent, dark, brown, grey;    
    private UpdateViewController viewUpdate;
    private SearchViewController searchViewController;
    private PlannerViewController plannerViewController;
    private Stage stage;
    private JMetro jMetro;
    private final String[] buttonCaptions = {"Planner", "Smart Search", "Stats", "Contacts", "Mailtemplates", "Settings"};
    private boolean darkModeActive = false;
    
    private List<Button> allMenuButtons;   
    
    public MainWindowController(Stage stage, JMetro jMetro)
    {
        this.stage = stage;
        this.jMetro = jMetro;
    }

    @FXML
    private Pane menuBtnPanePlanner, menuBtnPaneSmartSearch, menuBtnPaneSettings, 
        menuBtnPaneContacts, menuBtnPaneStats;

    @FXML
    private Button btnLogo, menuBtnPlanner, menuBtnSearch, menuBtnSettings, menuBtnContacts, menuBtnStats, menuBtnMail;  
    
    @FXML
    private Button btnAddAppointment, btnSwitchModes, btnSwitchLanguage, btnUser;       

    @FXML
    private GridPane rootContainer, childViewPlanner, childViewSearch, topMenu;

    @FXML
    private AnchorPane anchorPaneMainView;

    @FXML
    private ColumnConstraints columnLeftMenu;

    @FXML
    private Text txtVersion, txtBreadcrumb;    

    @FXML
    private VBox vboxLeftPane;
    
    @FXML
    private HBox topButtonRow;

    @FXML 
    public void initialize() throws IOException 
    {
        setColors();
        setButtonStates();
        allMenuButtons = new ArrayList<Button>();
        createButtonList();
        setImages();
        searchViewController = new SearchViewController(stage, anchorPaneMainView);     
        plannerViewController = new PlannerViewController(stage, anchorPaneMainView); 
        viewUpdate = new UpdateViewController(searchViewController, plannerViewController, childViewPlanner, childViewSearch); 
        initializeChildNodes(); 
        //plannerViewController.addCustomCalendarView();    
        initBackgrounds();
        bindWindowSize();   
    }

    @FXML
    void changeScene(MouseEvent event) throws IOException 
    {
        var button = (Button)event.getSource();
        setView(button);
    }  
    
    @FXML
    void switchLightAndDarkMode(ActionEvent event) throws FileNotFoundException 
    {
        if (darkModeActive)
        {
            jMetro.setStyle(Style.LIGHT);
            vboxLeftPane.setBackground(grey);
            childViewPlanner.setBackground(transparent);
            childViewSearch.setBackground(transparent);
            anchorPaneMainView.setBackground(transparent);
            darkModeActive = false;          
        }
        else
        {
            jMetro.setStyle(Style.DARK);        
            vboxLeftPane.setBackground(brown);
            childViewPlanner.setBackground(dark);
            childViewSearch.setBackground(dark);
            anchorPaneMainView.setBackground(dark);
            darkModeActive = true;          
        }
    }    

    private void initializeChildNodes() throws IOException
    {
        viewUpdate.setupNodes();   
        childViewPlanner = viewUpdate.getPlannerView();
        childViewSearch = viewUpdate.getSearchView();    
        anchorPaneMainView.getChildren().addAll(childViewPlanner, childViewSearch);
        childViewSearch.setDisable(true);
        childViewSearch.setVisible(false);
    }

    private void setColors()
    {
        primaryColor = new Background(new BackgroundFill(Color.web("#3fb4c6"), CornerRadii.EMPTY, Insets.EMPTY));
        secondaryColor = new Background(new BackgroundFill(Color.web("#1d2027"), CornerRadii.EMPTY, Insets.EMPTY));
        transparent = new Background(new BackgroundFill(Color.web("transparent"), CornerRadii.EMPTY, Insets.EMPTY)); 
        dark = new Background(new BackgroundFill(Color.web("#181818"), CornerRadii.EMPTY, Insets.EMPTY));   
        brown = new Background(new BackgroundFill(Color.web("#333333"), CornerRadii.EMPTY, Insets.EMPTY));    
        grey = new Background(new BackgroundFill(Color.web("#cdcdcd"), CornerRadii.EMPTY, Insets.EMPTY));  
    }

    private void setButtonStates()
    {
        menuBtnPanePlanner.setBackground(primaryColor);
        menuBtnPaneSmartSearch.setBackground(transparent);
        btnLogo.setBackground(primaryColor);
        btnLogo.setStyle("-fx-background-color:#3fb4c6");   

    }

    private void setImages() throws FileNotFoundException
    {
        var iconCal = new MDL2IconFont("\uE787");
        var iconSearch = new MDL2IconFont("\uE99A");
        var iconContacts = new MDL2IconFont("\uE779");
        var iconStats = new MDL2IconFont("\uE776");
        var iconMail = new MDL2IconFont("\uE715");
        var iconSettings = new MDL2IconFont("\uE713");
        var iconPlus = new MDL2IconFont("\uE710");
        var iconMode = new MDL2IconFont("\uE793");
        var iconLanguage = new MDL2IconFont("\uE774");
        var iconUser = new MDL2IconFont("\uE748");
        var iconClosePane = new MDL2IconFont("\uE8A0");
        var iconOpenPane= new MDL2IconFont("\uE89F");

        MDL2IconFont[] iconListMenuButtons = {iconCal, iconSearch, iconStats, iconContacts, iconMail, iconSettings };
        MDL2IconFont[] iconListTopButtons = {iconPlus, iconMode, iconLanguage, iconUser };
        Button[] topButtons = { btnAddAppointment, btnSwitchModes, btnSwitchLanguage, btnUser };   

        for (int i = 0; i < iconListMenuButtons.length; i++) 
        {
            iconListMenuButtons[i].setStyle("-fx-font-size:22");   
            allMenuButtons.get(i).setGraphic(iconListMenuButtons[i]);         
        }

        for (int i = 0; i < iconListTopButtons.length; i++) 
        {
            iconListTopButtons[i].setStyle("-fx-font-size:16");   
            topButtons[i].setGraphic(iconListTopButtons[i]);         
        }
    }

    private void initBackgrounds()
    {
        vboxLeftPane.setBackground(grey);
        childViewPlanner.setBackground(transparent);
        childViewSearch.setBackground(transparent);
        anchorPaneMainView.setBackground(transparent);
    }

    private void setView(Button button) throws IOException
    {
        menuBtnPanePlanner.setBackground(transparent);
        menuBtnPaneSmartSearch.setBackground(transparent);

        if (button.equals(menuBtnPlanner))    
        {     
            menuBtnPanePlanner.setBackground(primaryColor);
            childViewSearch.setDisable(true);
            childViewSearch.setVisible(false);
            childViewPlanner.setDisable(false);
            childViewPlanner.setVisible(true);
            txtBreadcrumb.setText("> Terminübersicht"); 
            
        }        
        else if (button.equals(menuBtnSearch))
        {
            menuBtnPaneSmartSearch.setBackground(primaryColor);
            childViewSearch.setDisable(false);
            childViewSearch.setVisible(true);
            childViewPlanner.setDisable(true);
            childViewPlanner.setVisible(false);
            txtBreadcrumb.setText("> Smarte Terminsuche");  
        }
    } 
    
    private void createButtonList()
    {
        allMenuButtons.add(menuBtnPlanner);
        allMenuButtons.add(menuBtnSearch);
        allMenuButtons.add(menuBtnStats);
        allMenuButtons.add(menuBtnContacts);
        allMenuButtons.add(menuBtnMail);
        allMenuButtons.add(menuBtnSettings);
    }
    
    private void bindWindowSize()
    {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
        {
            changeMenuAppearance();
            //searchViewController.changeContentPosition();
            //plannerViewController.changeContentPosition();
            anchorPaneMainView.setMinSize(stage.getWidth()-240, stage.getHeight());
            topButtonRow.setMinWidth(stage.getWidth()-240);

        };
        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);

        ChangeListener<Number> innerSizeListener = (observable, oldValue, newValue) ->
        {
            plannerViewController.changeSize();  
            searchViewController.changeSize();  
            
        };
        anchorPaneMainView.widthProperty().addListener(innerSizeListener);
        anchorPaneMainView.heightProperty().addListener(innerSizeListener);    
        
        
    }

    private void changeMenuAppearance()
    {
        int menuWidth;
        if (stage.getWidth() < 1000)
        {
            menuWidth = 70;   
            for (int i = 0; i < allMenuButtons.size(); i++) 
            {
                allMenuButtons.get(i).setText("");              
            }       
            btnLogo.setText("SP");  
            txtVersion.setText("v. 0.1.2");
        }
        else
        {
            menuWidth = 240;
            for (int i = 0; i < allMenuButtons.size(); i++)   
            {
                allMenuButtons.get(i).setText(buttonCaptions[i]);                
            }    
            btnLogo.setText("SMARTPLANNER");  
            txtVersion.setText("Version 0.1.2; HWR Gruppe C"); 
        }      

        columnLeftMenu.setMinWidth(menuWidth);
        columnLeftMenu.setPrefWidth(menuWidth);
        columnLeftMenu.setMaxWidth(menuWidth);
    }

    
}
