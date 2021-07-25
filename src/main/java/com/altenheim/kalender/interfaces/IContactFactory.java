package com.altenheim.kalender.interfaces;

import java.io.Serializable;

public interface IContactFactory extends Serializable {
    void createRandomContactsList(int amount);

    void createContact();
}
