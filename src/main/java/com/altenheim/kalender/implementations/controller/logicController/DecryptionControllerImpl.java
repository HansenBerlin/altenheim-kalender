package com.altenheim.kalender.implementations.controller.logicController;

import com.altenheim.kalender.interfaces.logicController.DecryptionController;
import javafx.util.Pair;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class DecryptionControllerImpl implements DecryptionController
{

    public String decrypt(String password, String salt, String cypherText) 
    {
        String decrypted = "";
        try 
        {
            var paramKeyPair = createSpecs(password, salt);
            var cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, paramKeyPair.getValue(), paramKeyPair.getKey());
            decrypted = new String(cipher.doFinal(Base64.getDecoder().decode(cypherText)));
        } 
        catch (BadPaddingException |
                NoSuchAlgorithmException |
                InvalidKeySpecException |
                NoSuchPaddingException |
                IllegalBlockSizeException |
                InvalidAlgorithmParameterException |
                InvalidKeyException e)
        {
            System.err.println(e.getMessage());
            return "";
        }
        finally
        {
            return  decrypted;
        }
    }

    private Pair<IvParameterSpec, SecretKeySpec> createSpecs(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException 
    {
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        var ivSpec = new IvParameterSpec(iv);
        var factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        var spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        var tmpSecret = factory.generateSecret(spec);
        var secretKey = new SecretKeySpec(tmpSecret.getEncoded(), "AES");

        return new Pair<>(ivSpec, secretKey);
    }
    
}
