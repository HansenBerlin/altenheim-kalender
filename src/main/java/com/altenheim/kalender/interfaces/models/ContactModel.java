package com.altenheim.kalender.interfaces.models;

import com.altenheim.kalender.implementations.controller.models.ContactModelImpl;

import java.io.Serializable;
import java.util.List;

public interface ContactModel extends Serializable
{
    List<ContactModelImpl> getDataToSerialize();
    void rebuildObservableListFromSerializedData(List<ContactModelImpl> serialized);
}
