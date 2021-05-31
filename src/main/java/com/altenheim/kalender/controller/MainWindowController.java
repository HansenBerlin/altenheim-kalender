package com.altenheim.kalender.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainWindowController 
{
    private Background primaryColor;
    private Background secondaryColor;
    private Background transparent;
    private UpdateViewController viewUpdate;
    private SearchViewController searchViewController;
    private PlannerViewController plannerViewController;
    private Stage stage;
    private String[] buttonCaptions = {"Planner", "Smart Search", "Settings", "Contacts", "Stats", "SMART PLANNER"};
    
    private List<MFXButton> allButtons;   
    
    public MainWindowController(Stage stage)
    {
        this.stage = stage;
    }

    @FXML
    private Pane menuBtnPanePlanner, menuBtnPaneSmartSearch, menuBtnPaneSettings, 
        menuBtnPaneContacts, menuBtnPaneStats;

    @FXML
    private MFXButton btnLogo, menuBtnPlanner, menuBtnSearch, menuBtnSettings, menuBtnContacts, menuBtnStats;    

    @FXML
    private ImageView imgIconPlannerButton, imgIconSearchButton, imgIconSettingsButton, imgIconContactsButton, 
        imgIconStatsButton, imgIconAddAppointment, imgIconUser, imgIconLanguage, imgIconDarkMode;    

    @FXML
    private GridPane rootContainer, childViewPlanner, childViewSearch;

    @FXML
    private AnchorPane anchorPaneMainView;

    @FXML
    private ColumnConstraints columnLeftMenu;

    @FXML
    private Text txtVersion, txtBreadcrumb;    

    @FXML
    void changeScene(MouseEvent event) throws IOException 
    {
        var button = (MFXButton)event.getSource();
        setPaneColor(button);
    }    

    @FXML 
    public void initialize() throws IOException 
    {
        setColors();
        setButtonStates();
        allButtons = new ArrayList<MFXButton>();
        createButtonList();
        setImages();
        searchViewController = new SearchViewController(stage);     
        plannerViewController = new PlannerViewController(stage);     
        viewUpdate = new UpdateViewController(searchViewController, plannerViewController, childViewPlanner, childViewSearch); 
        initializeChildNodes(); 
        bindWindowSize();   
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
    }

    private void setButtonStates()
    {
        menuBtnPanePlanner.setBackground(primaryColor);
        menuBtnPlanner.setBackground(secondaryColor); 
        menuBtnPaneSmartSearch.setBackground(transparent);
        menuBtnSearch.setBackground(transparent);
    }

    private void setImages() throws FileNotFoundException
    {
        imgIconPlannerButton.setImage(new Image(new FileInputStream(new File("src/main/java/resources/calendar.png"))));
        imgIconSearchButton.setImage(new Image(new FileInputStream(new File("src/main/java/resources/loupe.png"))));
        imgIconContactsButton.setImage(new Image(new FileInputStream(new File("src/main/java/resources/calendar.png"))));
        imgIconSettingsButton.setImage(new Image(new FileInputStream(new File("src/main/java/resources/calendar.png"))));
        imgIconStatsButton.setImage(new Image(new FileInputStream(new File("src/main/java/resources/calendar.png"))));
        imgIconAddAppointment.setImage(new Image(new FileInputStream(new File("src/main/java/resources/plus.png"))));
        imgIconLanguage.setImage(new Image(new FileInputStream(new File("src/main/java/resources/language.png"))));
        imgIconDarkMode.setImage(new Image(new FileInputStream(new File("src/main/java/resources/darkMode.png"))));
        imgIconUser.setImage(new Image(new FileInputStream(new File("src/main/java/resources/user.png"))));
    }

    private void setPaneColor(MFXButton button) throws IOException
    {
        menuBtnPanePlanner.setBackground(transparent);
        menuBtnPlanner.setBackground(transparent);
        menuBtnPaneSmartSearch.setBackground(transparent);
        menuBtnSearch.setBackground(transparent);

        if (button.equals(menuBtnPlanner))    
        {
            menuBtnPanePlanner.setBackground(primaryColor);
            menuBtnPlanner.setBackground(secondaryColor);
            childViewSearch.setDisable(true);
            childViewSearch.setVisible(false);
            childViewPlanner.setDisable(false);
            childViewPlanner.setVisible(true);
            txtBreadcrumb.setText("> Terminübersicht");  
        }        
        else if (button.equals(menuBtnSearch))
        {
            menuBtnPaneSmartSearch.setBackground(primaryColor);
            menuBtnSearch.setBackground(secondaryColor);
            childViewSearch.setDisable(false);
            childViewSearch.setVisible(true);
            childViewPlanner.setDisable(true);
            childViewPlanner.setVisible(false);
            txtBreadcrumb.setText("> Smarte Terminsuche");  

        }
    } 
    
    private void createButtonList()
    {
        allButtons.add(menuBtnPlanner);
        allButtons.add(menuBtnSearch);
        allButtons.add(menuBtnSettings);
        allButtons.add(menuBtnContacts);
        allButtons.add(menuBtnStats);
        allButtons.add(btnLogo);
    }
    
    private void bindWindowSize()
    {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
        {
            changeMenuAppearance();
            searchViewController.changeContentPosition();
            plannerViewController.changeContentPosition();
        };
        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);
    }

    private void changeMenuAppearance()
    {
        int menuWidth;
        if (stage.getWidth() < 1000)
        {
            menuWidth = 70;   
            for (int i = 0; i < allButtons.size(); i++) 
            {
                allButtons.get(i).setText("");              
            }       
            btnLogo.setText("SP");  
            txtVersion.setText("v. 0.1.2");
        }
        else
        {
            menuWidth = 240;
            for (int i = 0; i < allButtons.size(); i++)   
            {
                allButtons.get(i).setText(buttonCaptions[i]);                
            }    
            txtVersion.setText("Version 0.1.2; HWR Gruppe C"); 
        }      

        columnLeftMenu.setMinWidth(menuWidth);
        columnLeftMenu.setPrefWidth(menuWidth);
        columnLeftMenu.setMaxWidth(menuWidth);
    }

    
}
