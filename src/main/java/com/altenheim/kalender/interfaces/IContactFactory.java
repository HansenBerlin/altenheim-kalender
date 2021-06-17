package com.altenheim.kalender.interfaces;

import java.io.Serializable;

public interface IContactFactory extends Serializable
{
    public void createRandomContactsList(int amount);
    public void createContact();
}
