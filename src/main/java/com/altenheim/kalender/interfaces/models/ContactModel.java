package com.altenheim.kalender.interfaces.models;

import com.altenheim.kalender.models.ContactModelImpl;

import java.io.Serializable;
import java.util.List;

public interface ContactModel extends Serializable
{
    void registerButtonEvent();
    List<ContactModelImpl> getDataToSerialize();
    void rebuildObservableListFromSerializedData(List<ContactModelImpl> serialized);
}
