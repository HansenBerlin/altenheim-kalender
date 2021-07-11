package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.IAnimationController;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
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
    
}
