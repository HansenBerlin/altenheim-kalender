package com.altenheim.kalender.implementations.controller.viewController;


import com.altenheim.kalender.interfaces.models.ViewRootsModel;
import com.altenheim.kalender.interfaces.models.SettingsModel;
import com.altenheim.kalender.implementations.controller.models.SettingsModelImpl;
import com.altenheim.kalender.resourceClasses.StylePresets;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.MDL2IconFont;
import jfxtras.styles.jmetro.Style;

public class MainWindowController extends ResponsiveController 
{
    private final ViewRootsModel allViewsInformation;
    private final CustomViewOverride customCalendar;
    private Stage stage;
    private JMetro jMetro;
    private Map<String, Pair<Button, Pane>> allButtonsWithBackgrounds;
    private int currentView = 0;
    private int currentMenuWidth = 240;
    private int topMenuHeight = 100;
    private Button currentlyActive;
    private Background currentSecondaryColor;
    private final SettingsModel settings;

    @FXML private Pane menuBtnPanePlanner, menuBtnPaneSmartSearch, menuBtnPaneSettings, menuBtnPaneMail, menuBtnPaneContacts;
    @FXML private Button btnLogo, menuBtnPlanner, menuBtnSearch, menuBtnSettings, menuBtnContacts, menuBtnMail;
    @FXML private Button btnAddAppointment, btnSwitchModes;
    @FXML private GridPane rootContainer, childContainer, topMenu;
    @FXML private AnchorPane viewsRoot;
    @FXML private ColumnConstraints columnLeftMenu;
    @FXML private RowConstraints topRow;
    @FXML private Text txtVersion, txtBreadcrumb;
    @FXML private VBox vboxLeftPane;
    @FXML private HBox topButtonRow;

    public MainWindowController(ViewRootsModel allViewsInformation, CustomViewOverride customCalendar, SettingsModel settings)
    {
        this.allViewsInformation = allViewsInformation;
        this.customCalendar = customCalendar;
        this.settings = settings;
    }  
    
    public void initJFXObjects(Stage stage, JMetro jmetro)
    {
        this.stage = stage;
        this.jMetro = jmetro;
        bindWindowSize();
    }

    @FXML
    private void initialize() throws IOException 
    {
        ((PlannerViewController)allViewsInformation.getAllViewControllers()[0]).updateCustomCalendarView(customCalendar);
        viewsRoot.getChildren().addAll(allViewsInformation.getAllViews());
        setupMenuButtons();        
    }

    @FXML
    private void changeScene(MouseEvent event) 
    {
        var button = (Button) event.getSource();
        if (button != currentlyActive) 
        {
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

    public void switchCssMode() 
    {
        if (!SettingsModelImpl.isDarkmodeActive)
        {
            setColorsForDarkAndLightMode(Style.DARK, StylePresets.DARK_MENU_BACKGROUND,
                    StylePresets.DARK_MAIN_BACKGROUND, StylePresets.DARK_PRIMARY,
                    StylePresets.DARK_SECONDARY_CSS, StylePresets.DARK_APPLICATION_CSS_FILE);
            currentSecondaryColor = StylePresets.DARK_SECONDARY;
        } 
        else 
        {
            setColorsForDarkAndLightMode(Style.LIGHT, StylePresets.LIGHT_MENU_BACKGROUND,
                    StylePresets.LIGHT_MAIN_BACKGROUND, StylePresets.LIGHT_PRIMARY,
                    StylePresets.LIGHT_SECONDARY_CSS, StylePresets.LIGHT_APPLICATION_CSS_FILE);
            currentSecondaryColor = StylePresets.LIGHT_SECONDARY;
        }    
        SettingsModelImpl.isDarkmodeActive ^= true;
    }

    private void setColorsForDarkAndLightMode(Style style, Background menu, Background background,
                                              Background primary, String secondaryCSS, String appCssFile)
    {        
        jMetro.setStyle(style);
        vboxLeftPane.setBackground(menu);
        viewsRoot.setBackground(background);
        topButtonRow.setBackground(primary);
        btnLogo.setStyle(secondaryCSS);
        jMetro.getOverridingStylesheets().clear();
        jMetro.getOverridingStylesheets().add(appCssFile);             
        customCalendar.updateCss();
        settings.saveSettings(); 
        
        for (var view : allViewsInformation.getAllViews())
            view.setBackground(background);  
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
        Button[] buttonsList = { menuBtnPlanner, menuBtnSearch, menuBtnContacts, menuBtnMail, menuBtnSettings, btnAddAppointment, btnSwitchModes };
        Pane[] buttonBackgrounds = { menuBtnPanePlanner, menuBtnPaneSmartSearch, menuBtnPaneContacts, menuBtnPaneMail, menuBtnPaneSettings, null, null };
        allButtonsWithBackgrounds = createMainMenuButtons(buttonsList, buttonBackgrounds);
        currentlyActive = menuBtnPlanner;
        if (SettingsModelImpl.isDarkmodeActive)
            menuBtnPanePlanner.setBackground(StylePresets.LIGHT_SECONDARY);
        else
            menuBtnPanePlanner.setBackground(StylePresets.DARK_SECONDARY);
    }

    private void bindWindowSize() 
    {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
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

    protected void changeContentPosition(double width, double height) 
    {
        if (width < 1280) {
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

    public Map<String, Pair<Button, Pane>> createMainMenuButtons(Button[] buttons, Pane[] buttonBackgrounds) {
        setImages(buttons);

        String[] buttonCaptions = { "Planer", "Smart Search", "Kontakte", "Mailtemplates", "Einstellungen", "", "" };
        var buttonsMap = new Hashtable<String, Pair<Button, Pane>>();

        for (int i = 0; i < buttons.length; i++) 
        {
            buttons[i].setAccessibleText(String.format("%d", i));
            var buttonAndBackground = new Pair<Button, Pane>(buttons[i], buttonBackgrounds[i]);
            buttonsMap.put(buttonCaptions[i], buttonAndBackground);
        }

        return buttonsMap;
    }

    private void setImages(Button[] buttonsList) {
        var iconCal = new MDL2IconFont("\uE787");
        var iconSearch = new MDL2IconFont("\uE99A");
        var iconContacts = new MDL2IconFont("\uE779");
        var iconMail = new MDL2IconFont("\uE715");
        var iconSettings = new MDL2IconFont("\uE713");
        var iconPlus = new MDL2IconFont("\uE710");
        var iconMode = new MDL2IconFont("\uE793");        

        MDL2IconFont[] iconListMenuButtons = { iconCal, iconSearch, iconContacts, iconMail, iconSettings, iconPlus,
                iconMode };

        for (int i = 0; i < iconListMenuButtons.length; i++) 
        {
            iconListMenuButtons[i].setStyle("-fx-font-size:22");
            buttonsList[i].setGraphic(iconListMenuButtons[i]);
        }
    }
}