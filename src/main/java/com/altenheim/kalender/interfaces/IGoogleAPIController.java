package com.altenheim.kalender.interfaces;

import java.io.IOException;

public interface IGoogleAPIController 
{
    String getOpeningHours(String locationSearchUserInput) throws IOException, InterruptedException;
    int[] searchForDestinationDistance(String startAt, String destination) throws IOException, InterruptedException;
}
