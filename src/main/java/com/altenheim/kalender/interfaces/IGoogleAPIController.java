package com.altenheim.kalender.interfaces;

import java.io.IOException;

public interface IGoogleAPIController 
{
    public String getOpeningHours(String locationSearchUserInput) throws IOException, InterruptedException;
    public int[] searchForDestinationDistance(String startAt, String destination) throws IOException, InterruptedException;    
}
