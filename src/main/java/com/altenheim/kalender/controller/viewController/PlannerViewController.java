package com.altenheim.kalender.controller.viewController;

public class PlannerViewController extends ResponsiveController {
    private CustomViewOverride customCalendar;

    public PlannerViewController(CustomViewOverride custumCalendar) {
        this.customCalendar = custumCalendar;
    }

    public void updateCustomCalendarView(CustomViewOverride calendarView) {
        if (childContainer.getChildren().contains(this.customCalendar)) {
            childContainer.getChildren().remove(this.customCalendar);
            this.customCalendar = calendarView;
        }
        childContainer.add(this.customCalendar, 0, 0, 1, 1);
    }

    public void changeContentPosition(double width, double height) {
        //
    }
    
}
