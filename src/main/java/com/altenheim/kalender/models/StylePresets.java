package com.altenheim.kalender.models;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;


public class StylePresets
{
    public static final Background DARK_PRIMARY = getPreset(1);
    public static final Background DARK_SECONDARY = getPreset(2);
    public static final Background DARK_MAIN_BACKGROUND = getPreset(3);
    public static final Background DARK_MENU_BACKGROUND = getPreset(4);
    public static final Background LIGHT_PRIMARY = getPreset(5);
    public static final Background LIGHT_SECONDARY = getPreset(6);
    public static final Background LIGHT_MAIN_BACKGROUND = getPreset(7);
    public static final Background LIGHT_MENU_BACKGROUND = getPreset(8);
    public static final Background TRANSPARENT = getPreset(9);

    private static Background getPreset(int presetNumber) 
    {
        String stylesheet = null;
        switch (presetNumber) 
        {
            case 1:
                stylesheet = "#4b337d";
                break;
            case 2:
                stylesheet = "#281b42";
                break;
            case 3:
                stylesheet = "#181818";
                break;
            case 4:
                stylesheet = "#333333";
                break;
            case 5:
                stylesheet = "#5ddd8a";
                break;
            case 6:       
                stylesheet = "#4fba74";
                break;
            case 7:
                stylesheet = "#ffffff";
                break;
            case 8:
                stylesheet = "#cccccc";
                break;
            case 9:
                stylesheet = "transparent";
                break;
            default:
                break;
        }
        return new Background(new BackgroundFill(Color.web(stylesheet), CornerRadii.EMPTY, Insets.EMPTY));
    }    
}
