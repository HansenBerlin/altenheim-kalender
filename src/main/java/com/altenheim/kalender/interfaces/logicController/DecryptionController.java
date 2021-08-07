package com.altenheim.kalender.interfaces.logicController;

import javafx.util.Pair;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public interface DecryptionController
{
    String decrypt(String password, String salt, String cypherText);
}