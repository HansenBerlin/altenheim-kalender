package com.altenheim.calendar.interfaces;

import java.io.IOException;

public interface IGoogleAPIController 
{
    public String showOpeningHours(String locationSearchUserInput) throws IOException, InterruptedException;
    public int[] searchForDestinationDistance(String startAt, String destination) throws IOException, InterruptedException;    
}
