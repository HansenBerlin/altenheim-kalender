package com.altenheim.kalender.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class MainWindowController 
{
    private Background primaryColor;
    private Background secondaryColor;
    private Background transparent;

    // Werden später noch gebraucht um das initiale Setup zu vereinfachen
    private List<Pane> allButtonBackgroundPanes;
    private List<MFXButton> allButtons;

    @FXML
    private Pane menuBtnPanePlanner, menuBtnPaneSmartSearch, menuBtnPaneSettings, 
        menuBtnPaneContacts, menuBtnPaneStats;

    @FXML
    private MFXButton menuBtnPlanner, menuBtnSearch, menuBtnSettings, menuBtnContacts, menuBtnStats;    

    @FXML
    private ImageView imgLogo, imgIconPlannerButton, imgIconSearchButton, imgIconSettingsButton, imgIconContactsButton, 
        imgIconStatsButton, imgIconAddAppointment, imgIconUser, imgIconLanguage;

    @FXML
    private AnchorPane anchorPaneMainView;

    @FXML
    void changeScene(MouseEvent event) throws IOException 
    {
        var button = (MFXButton)event.getSource();
        setPaneColor(button);
    }    

    @FXML 
    public void initialize() throws FileNotFoundException 
    {
        setColors();
        setButtonStates();
        setImages();                
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
        imgLogo.setImage(new Image(new FileInputStream(new File("src/main/java/resources/logo-mock.png"))));
        imgIconPlannerButton.setImage(new Image(new FileInputStream(new File("src/main/java/resources/calendar.png"))));
        imgIconSearchButton.setImage(new Image(new FileInputStream(new File("src/main/java/resources/calendar.png"))));
        imgIconContactsButton.setImage(new Image(new FileInputStream(new File("src/main/java/resources/calendar.png"))));
        imgIconSettingsButton.setImage(new Image(new FileInputStream(new File("src/main/java/resources/calendar.png"))));
        imgIconStatsButton.setImage(new Image(new FileInputStream(new File("src/main/java/resources/calendar.png"))));
        imgIconAddAppointment.setImage(new Image(new FileInputStream(new File("src/main/java/resources/plus.png"))));
        imgIconLanguage.setImage(new Image(new FileInputStream(new File("src/main/java/resources/language.png"))));
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
            loadNewNodeFromXML("plannerView.fxml");

        }        
        else if (button.equals(menuBtnSearch))
        {
            menuBtnPaneSmartSearch.setBackground(primaryColor);
            menuBtnSearch.setBackground(secondaryColor);
            loadNewNodeFromXML("searchView.fxml");
        }
    }

    private void loadNewNodeFromXML(String fileName) throws IOException
    {
        anchorPaneMainView.getChildren().clear();        
        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = new FileInputStream(new File("src/main/java/resources/" + fileName)); 
        var root = loader.load(fileInputStream);
        anchorPaneMainView.getChildren().add((Node)root);
    }    
}
