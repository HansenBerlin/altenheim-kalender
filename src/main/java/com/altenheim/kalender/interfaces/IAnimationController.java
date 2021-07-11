package com.altenheim.kalender.interfaces;

import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public interface IAnimationController 
{
    void growAndShrinkContainer(Node box, boolean isActivated);    
    void growAndShrinkCircle(Circle[] circles, Text[] headers, boolean isWindowSmall);    
}
