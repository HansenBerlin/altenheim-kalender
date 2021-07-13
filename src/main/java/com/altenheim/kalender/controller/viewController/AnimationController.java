package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.IAnimationController;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class AnimationController implements IAnimationController 
{
    public AnimationController()
    {
    }

    public void growAndShrinkContainer(Node box, boolean isActivated)
    {
        double x = 0;
        double y = 0;
        if (isActivated)
        {
            x = 1;
            y = 1;
        }

        var transition = new ScaleTransition(Duration.seconds(0.2), box);
        transition.setToX(x);
        transition.setToY(y);
        transition.play();
        box.setVisible(isActivated);
    }

    public void growAndShrinkCircle(Circle[] circles, Text[] headers, boolean isWindowSmall)
    {
        double r = 1;
        boolean isVisible = true;
        if (isWindowSmall)   
        {
            r = 0.2;
            isVisible = false;
        } 
        for (int i = 0; i < headers.length; i++) 
        {
            headers[i].setVisible(isVisible);
            var transition = new ScaleTransition(Duration.seconds(0.2), circles[i]);
            transition.setToX(r);
            transition.setToY(r);
            transition.play();            
        } 
    }       
}