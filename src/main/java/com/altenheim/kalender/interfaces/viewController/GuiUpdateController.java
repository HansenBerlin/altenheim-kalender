package com.altenheim.kalender.interfaces.viewController;

import jfxtras.styles.jmetro.JMetro;

public interface GuiUpdateController
{
    JMetro getJMetroStyle();
    void init();
    void setupColorMode();
    void registerCalendars();
    void updateAdvancedFeaturesFields();
}
