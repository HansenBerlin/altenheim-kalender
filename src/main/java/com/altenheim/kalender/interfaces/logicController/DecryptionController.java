package com.altenheim.kalender.interfaces.logicController;

public interface DecryptionController
{
    String decrypt(String password, String salt, String cypherText);
}
