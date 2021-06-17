package com.altenheim.kalender.controller.logicController;

import javafx.util.Pair;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class SecureAesController
{
    public String encrypt(String password, String salt, String plainText)
    {
        try
        {
            var paramKeyPair = createSpecs(password, salt);
            var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, paramKeyPair.getValue(), paramKeyPair.getKey());
            return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String password, String salt, String cypherText)
    {
        try
        {
            var paramKeyPair = createSpecs(password, salt);
            var cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, paramKeyPair.getValue(), paramKeyPair.getKey());
            return new String(cipher.doFinal(Base64.getDecoder().decode(cypherText)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private Pair<IvParameterSpec, SecretKeySpec> createSpecs(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        var ivSpec = new IvParameterSpec(iv);
        var factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        var spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        var tmpSecret = factory.generateSecret(spec);
        var secretKey = new SecretKeySpec(tmpSecret.getEncoded(), "AES");

        return new Pair<>(ivSpec, secretKey);
    }
}